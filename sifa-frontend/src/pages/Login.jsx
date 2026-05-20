import { useState } from 'react';
import api from '../services/api';
import { useNavigate, Link } from 'react-router-dom';
import { jwtDecode } from "jwt-decode";

function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await api.post('/auth/login', { username, password });
            const token = response.data.token;
            
            // Token'ı kaydet
            localStorage.setItem('token', token);
            
            // Token'ı çöz ve rolü bul
            const decodedToken = jwtDecode(token);
            const userRole = decodedToken.role || decodedToken.authorities?.[0]?.authority;

            // Role göre doğru panele yönlendir
            if (userRole === 'ROLE_DOCTOR') {
                navigate('/doctor');
            } else if (userRole === 'ROLE_CASHIER') {
                navigate('/cashier');
            } else {
                navigate('/patient'); // Varsayılan olarak Hasta paneline at
            }
            
        } catch (error) {
            alert('Giriş başarısız. Kullanıcı adı veya şifre hatalı!');
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-100">
            <div className="bg-white p-8 rounded-xl shadow-lg w-96 border-t-4 border-blue-600">
                <div className="text-center mb-8">
                    <h2 className="text-3xl font-extrabold text-gray-800">Şifa Polikliniği</h2>
                    <p className="text-gray-500 text-sm mt-2">Sisteme Giriş Yapın</p>
                </div>

                <form onSubmit={handleLogin} className="space-y-5">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Kullanıcı Adı</label>
                        <input 
                            type="text" 
                            required
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none transition"
                            placeholder="Kullanıcı adınızı girin"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Şifre</label>
                        <input 
                            type="password" 
                            required
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none transition"
                            placeholder="••••••••"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                    
                    <button 
                        type="submit" 
                        className="w-full bg-blue-600 text-white font-bold py-2.5 px-4 rounded-lg hover:bg-blue-700 transition duration-200 shadow-md"
                    >
                        Giriş Yap
                    </button>
                </form>

                <div className="mt-6 text-center text-sm text-gray-600">
                    Hesabınız yok mu?{' '}
                    <Link to="/register" className="text-green-600 font-semibold hover:underline">
                        Hemen Kayıt Olun
                    </Link>
                </div>
            </div>
        </div>
    );
}

export default Login;