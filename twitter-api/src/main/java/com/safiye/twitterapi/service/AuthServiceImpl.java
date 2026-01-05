package com.safiye.twitterapi.service;

import com.safiye.twitterapi.dto.request.LoginRequestDto;
import com.safiye.twitterapi.dto.request.RegisterRequestDto;
import com.safiye.twitterapi.dto.response.LoginResponseDto;
import com.safiye.twitterapi.dto.response.RegisterResponseDto;
import com.safiye.twitterapi.entity.Role;
import com.safiye.twitterapi.entity.User;
import com.safiye.twitterapi.exceptions.TwitterException;
import com.safiye.twitterapi.exceptions.UserAlreadyRegisteredException;
import com.safiye.twitterapi.exceptions.UserNotFoundException;
import com.safiye.twitterapi.repository.RoleRepository;
import com.safiye.twitterapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    private final RoleRepository roleRepository;

    @Override
    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) {
        if (userRepository.findByEmail(registerRequestDto.email()).isPresent()) {
            throw new UserAlreadyRegisteredException("Bu email zaten kayıtlı!");
        }
        if (userRepository.findByUsername(registerRequestDto.username()).isPresent()) {
            throw new TwitterException("Bu kullanıcı adı zaten alınmış!", HttpStatus.BAD_REQUEST);
        }
        String encodedPassword=passwordEncoder.encode(registerRequestDto.password());

        Role userRole=roleRepository.getRoleByAuthority("ROLE_USER");
        if (userRole == null) {
            throw new TwitterException("Sistem rolü bulunamadı!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User user = new User();
       user.setFirstName(registerRequestDto.firstName());
        user.setLastName(registerRequestDto.lastName());
        user.setUsername(registerRequestDto.username());
        user.setEmail(registerRequestDto.email());
        user.setPassword(encodedPassword);
        user.setRoles(Set.of(userRole));

        userRepository.save(user);

        return new RegisterResponseDto(registerRequestDto.email(),"Kullanıcı başarıyla kaydedildi.");
    }
    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUsername(loginRequestDto.username())
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı!"));

        if (!passwordEncoder.matches(loginRequestDto.password(), user.getPassword())) {
            throw new TwitterException("Hatalı şifre!", HttpStatus.UNAUTHORIZED);
        }

        Set<String> roles = user.getRoles().stream()
                .map(Role::getAuthority)
                .collect(Collectors.toSet());

        return new LoginResponseDto(
                user.getId(),
                user.getUsername(),
                "Giriş başarıyla tamamlandı.",
                roles
        );
    }
}
