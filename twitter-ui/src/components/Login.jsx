import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

export default function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            const res = await fetch("http://localhost:3000/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, password }),
            });

            if (!res.ok) {
                alert("Giriş hatalı!");
                return;
            }

            const data = await res.json();

            if (data.id === undefined || data.id === null) {
                console.error("Login response içinde id yok:", data);
                alert("Backend login response'unda id yok. (Frontend userId ile çalışıyor)");
                return;
            }

            localStorage.removeItem("userUuid");
            localStorage.setItem("userId", String(data.id));
            localStorage.setItem("username", data.username || username);

            navigate("/home");
        } catch (err) {
            console.error("Ağ Hatası:", err);
            alert("Sunucuya bağlanılamadı.");
        }
    };

    return (
        <div style={{ padding: "50px", textAlign: "center" }}>
            <h2 style={{ color: "#1DA1F2", marginBottom: "30px" }}>Twitter'a Giriş Yap</h2>

            <form
                onSubmit={handleLogin}
                style={{
                    display: "flex",
                    flexDirection: "column",
                    gap: "15px",
                    maxWidth: "350px",
                    margin: "0 auto",
                }}
            >
                <input
                    placeholder="Kullanıcı Adı"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                    style={{ padding: "10px", borderRadius: "10px", border: "1px solid #ddd" }}
                />
                <input
                    type="password"
                    placeholder="Şifre"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                    style={{ padding: "10px", borderRadius: "10px", border: "1px solid #ddd" }}
                />

                <button
                    type="submit"
                    style={{
                        backgroundColor: "#1DA1F2",
                        color: "white",
                        border: "none",
                        padding: "12px",
                        borderRadius: "20px",
                        cursor: "pointer",
                        fontWeight: "bold",
                        fontSize: "16px",
                    }}
                >
                    Giriş Yap
                </button>
            </form>

            <p style={{ marginTop: "20px", color: "#657786" }}>
                Hesabın yok mu? <Link to="/register" style={{ color: "#1DA1F2", textDecoration: "none" }}>Kayıt Ol</Link>
            </p>
        </div>
    );
}
