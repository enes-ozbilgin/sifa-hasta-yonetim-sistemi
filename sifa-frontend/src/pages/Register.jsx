import { useState } from 'react';
import api from '../services/api';
import { useNavigate, Link } from 'react-router-dom';

function Register() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState('PATIENT'); // Varsayılan rol HASTA
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            // Backend'deki /api/auth/register ucuna istek atıyoruz
            await api.post('/auth/register', { username, password, role });
            alert('Kayıt başarılı! Şimdi giriş yapabilirsiniz.');
            navigate('/login'); // Kayıt olunca login ekranına at
        } catch (error) {
            alert('Kayıt başarısız! Bu kullanıcı adı alınmış olabilir.');
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-100">
            <div className="bg-white p-8 rounded-xl shadow-lg w-96">
                <div className="text-center mb-6">
                    <h2 className="text-2xl font-bold text-gray-800">Şifa Polikliniği</h2>
                    <p className="text-gray-500 text-sm mt-1">Sisteme Yeni Kayıt</p>
                </div>

                <form onSubmit={handleRegister} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Kullanıcı Adı</label>
                        <input 
                            type="text" 
                            required
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none"
                            placeholder="Örn: emin_hasta"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Şifre</label>
                        <input 
                            type="password" 
                            required
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none"
                            placeholder="••••••••"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">Rolünüz</label>
                        <select 
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none bg-white"
                            value={role}
                            onChange={(e) => setRole(e.target.value)}
                        >
                            <option value="PATIENT">Hasta</option>
                            <option value="DOCTOR">Doktor</option>
                            <option value="CASHIER">Veznedar</option>
                        </select>
                    </div>

                    <button 
                        type="submit" 
                        className="w-full bg-green-600 text-white font-bold py-2 px-4 rounded-lg hover:bg-green-700 transition duration-200 mt-4"
                    >
                        Kayıt Ol
                    </button>
                </form>

                <div className="mt-6 text-center text-sm text-gray-600">
                    Zaten hesabınız var mı?{' '}
                    <Link to="/login" className="text-blue-600 font-semibold hover:underline">
                        Giriş Yapın
                    </Link>
                </div>
            </div>
        </div>
    );
}

export default Register;