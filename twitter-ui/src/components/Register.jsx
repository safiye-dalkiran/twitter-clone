import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';

export default function Register() {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        firstName: '',
        lastName: ''
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            const res = await fetch('http://localhost:3000/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData)
            });

            if (!res.ok) {
                const errorData = await res.json().catch(() => ({ message: 'Kayıt başarısız!' }));
                setError(errorData.message || 'Kayıt başarısız!');
                return;
            }

            alert('Kayıt başarılı! Giriş yapabilirsiniz.');
            navigate('/login');
        } catch (err) {
            setError('Sunucuya bağlanılamadı.');
            console.error('Kayıt hatası:', err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{ padding: '50px', textAlign: 'center' }}>
            <h2 style={{ color: '#1DA1F2', marginBottom: '30px' }}>Yeni Hesap Oluştur</h2>
            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '15px', maxWidth: '350px', margin: '0 auto' }}>
                <input
                    placeholder="Adınız"
                    value={formData.firstName}
                    onChange={e => setFormData({ ...formData, firstName: e.target.value })}
                    required
                    style={{ padding: '10px', borderRadius: '10px', border: '1px solid #ddd' }}
                />
                <input
                    placeholder="Soyadınız"
                    value={formData.lastName}
                    onChange={e => setFormData({ ...formData, lastName: e.target.value })}
                    required
                    style={{ padding: '10px', borderRadius: '10px', border: '1px solid #ddd' }}
                />
                <input
                    placeholder="Kullanıcı Adı"
                    value={formData.username}
                    onChange={e => setFormData({ ...formData, username: e.target.value })}
                    required
                    style={{ padding: '10px', borderRadius: '10px', border: '1px solid #ddd' }}
                />
                <input
                    type="email"
                    placeholder="Email"
                    value={formData.email}
                    onChange={e => setFormData({ ...formData, email: e.target.value })}
                    required
                    style={{ padding: '10px', borderRadius: '10px', border: '1px solid #ddd' }}
                />
                <input
                    type="password"
                    placeholder="Şifre"
                    value={formData.password}
                    onChange={e => setFormData({ ...formData, password: e.target.value })}
                    required
                    minLength={6}
                    style={{ padding: '10px', borderRadius: '10px', border: '1px solid #ddd' }}
                />

                {error && (
                    <div style={{ color: 'red', fontSize: '14px', marginTop: '-10px' }}>
                        {error}
                    </div>
                )}

                <button
                    type="submit"
                    disabled={loading}
                    style={{
                        backgroundColor: loading ? '#ccc' : '#1DA1F2',
                        color: 'white',
                        border: 'none',
                        padding: '12px',
                        borderRadius: '20px',
                        cursor: loading ? 'not-allowed' : 'pointer',
                        fontWeight: 'bold',
                        fontSize: '16px'
                    }}
                >
                    {loading ? 'Kaydediliyor...' : 'Kayıt Ol'}
                </button>
            </form>
            <p style={{ marginTop: '20px', color: '#657786' }}>
                Zaten hesabın var mı? <Link to="/login" style={{ color: '#1DA1F2', textDecoration: 'none' }}>Giriş Yap</Link>
            </p>
        </div>
    );
}