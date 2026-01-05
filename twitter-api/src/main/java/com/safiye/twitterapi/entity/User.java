package com.safiye.twitterapi.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name="users" ,schema = "twitter")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
@ToString
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @NotNull
    @NotBlank
    @NotEmpty
    @Column(name = "first_name")
    private String firstName;

    @Size(max = 50)
    @NotNull
    @NotBlank
    @NotEmpty
    @Column(name = "last_name")
    private String lastName;

    @Size(max = 30)
    @NotBlank
    @Column(unique = true,name = "username")
    private String username;

    @Size(max = 100)
    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @Size(max = 255)
    @NotBlank
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            schema = "twitter",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name ="role_id" )
    )
    private Set<Role> roles=new HashSet<>();

   @OneToMany(mappedBy = "user")
    private List<Tweet> tweets;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<Like> likes;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }
    private String uuid = UUID.randomUUID().toString();
}
