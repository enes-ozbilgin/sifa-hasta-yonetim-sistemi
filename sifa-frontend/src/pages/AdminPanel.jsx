import { useState, useEffect } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';

function AdminPanel() {
  const navigate = useNavigate();
  
  // State'ler
  const [stats, setStats] = useState({ doctorCount: 0, cashierCount: 0, patientCount: 0, monthlyRevenue: 0 });
  const [users, setUsers] = useState([]);
  const [settings, setSettings] = useState({ appointmentSlotMinutes: 30, discountRate: 0.20 });

  useEffect(() => {
    fetchDashboardData();
    fetchUsers();
    fetchSettings();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const res = await api.get('/admin/reports');
      setStats(res.data);
    } catch (err) { console.error("Raporlar çekilemedi."); }
  };

  const fetchUsers = async () => {
    try {
      const res = await api.get('/admin/users');
      setUsers(res.data);
    } catch (err) { console.error("Kullanıcılar çekilemedi."); }
  };

  const fetchSettings = async () => {
    try {
      const res = await api.get('/admin/settings');
      setSettings(res.data);
    } catch (err) { console.error("Ayarlar çekilemedi."); }
  };

  const handleDeleteUser = async (userId) => {
    if (window.confirm("Bu kullanıcıyı kalıcı olarak silmek istediğinize emin misiniz?")) {
      try {
        await api.delete(`/admin/users/${userId}`);
        alert("Kullanıcı başarıyla silindi.");
        fetchUsers(); // Listeyi yenile
        fetchDashboardData(); // İstatistikleri yenile
      } catch (err) {
        alert("Silme hatası! Kullanıcının aktif randevuları olabilir.");
      }
    }
  };

  const handleSaveSettings = async (e) => {
    e.preventDefault();
    try {
      await api.post('/admin/settings', settings);
      alert("Sistem ayarları başarıyla güncellendi!");
    } catch (err) { alert("Ayarlar kaydedilemedi."); }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    navigate('/login');
  };

  return (
    <div className="min-h-screen bg-gray-900 p-8 text-gray-200">
      
      {/* Üst Bar */}
      <div className="flex justify-between items-center bg-gray-800 p-6 rounded-lg shadow-xl mb-8 border-t-4 border-yellow-500">
        <h1 className="text-2xl font-bold text-white">👑 Sistem Yöneticisi Paneli</h1>
        <button onClick={handleLogout} className="bg-red-600 hover:bg-red-700 text-white px-6 py-2 rounded transition">
          Çıkış Yap
        </button>
      </div>

      {/* 1. GENEL RAPORLAR */}
      <h2 className="text-xl font-bold text-white mb-4 border-b border-gray-700 pb-2">📊 Genel Raporlar</h2>
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-12">
        <div className="bg-gray-800 p-6 rounded-lg border-l-4 border-green-500 shadow-md">
          <p className="text-gray-400 text-sm">Bu Ayki Net Gelir</p>
          <p className="text-3xl font-bold text-white">{stats.monthlyRevenue} ₺</p>
        </div>
        <div className="bg-gray-800 p-6 rounded-lg border-l-4 border-blue-500 shadow-md">
          <p className="text-gray-400 text-sm">Toplam Doktor</p>
          <p className="text-3xl font-bold text-white">{stats.doctorCount}</p>
        </div>
        <div className="bg-gray-800 p-6 rounded-lg border-l-4 border-purple-500 shadow-md">
          <p className="text-gray-400 text-sm">Toplam Veznedar</p>
          <p className="text-3xl font-bold text-white">{stats.cashierCount}</p>
        </div>
        <div className="bg-gray-800 p-6 rounded-lg border-l-4 border-yellow-500 shadow-md">
          <p className="text-gray-400 text-sm">Toplam Hasta</p>
          <p className="text-3xl font-bold text-white">{stats.patientCount}</p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        
        {/* 2. SİSTEM AYARLARI */}
        <div className="bg-gray-800 p-6 rounded-lg shadow-md border border-gray-700 h-fit">
          <h2 className="text-xl font-bold text-white mb-6 border-b border-gray-700 pb-2">⚙️ Sistem Ayarları</h2>
          <form onSubmit={handleSaveSettings} className="space-y-4">
            <div>
              <label className="block text-sm text-gray-400 mb-1">Randevu Blok Aralığı (Dakika)</label>
              <input 
                type="number" 
                className="w-full p-2 bg-gray-700 border border-gray-600 rounded text-white outline-none focus:border-yellow-500"
                value={settings.appointmentSlotMinutes}
                onChange={(e) => setSettings({...settings, appointmentSlotMinutes: parseInt(e.target.value)})}
              />
            </div>
            <div>
              <label className="block text-sm text-gray-400 mb-1">TC SSK İndirim Oranı (Örn: 0.20 = %20)</label>
              <input 
                type="number" 
                step="0.01"
                className="w-full p-2 bg-gray-700 border border-gray-600 rounded text-white outline-none focus:border-yellow-500"
                value={settings.discountRate}
                onChange={(e) => setSettings({...settings, discountRate: parseFloat(e.target.value)})}
              />
            </div>
            <button type="submit" className="w-full bg-yellow-600 hover:bg-yellow-700 text-white font-bold py-2 rounded transition">
              Ayarları Kaydet
            </button>
          </form>
        </div>

        {/* 3. KULLANICI YÖNETİMİ */}
        <div className="bg-gray-800 p-6 rounded-lg shadow-md border border-gray-700">
          <h2 className="text-xl font-bold text-white mb-6 border-b border-gray-700 pb-2">👥 Kullanıcı Yönetimi</h2>
          <div className="max-h-[300px] overflow-y-auto pr-2">
            <table className="w-full text-left text-sm">
              <thead>
                <tr className="text-gray-400 border-b border-gray-700">
                  <th className="pb-2">ID</th>
                  <th className="pb-2">Kullanıcı Adı</th>
                  <th className="pb-2">Rolü</th>
                  <th className="pb-2 text-right">İşlem</th>
                </tr>
              </thead>
              <tbody>
                {users.map(user => (
                  <tr key={user.id} className="border-b border-gray-700 hover:bg-gray-750 transition">
                    <td className="py-3">#{user.id}</td>
                    <td className="py-3 font-semibold">{user.username}</td>
                    <td className="py-3">
                      <span className="bg-gray-700 px-2 py-1 rounded text-xs text-gray-300">{user.role}</span>
                    </td>
                    <td className="py-3 text-right">
                      {user.username !== 'admin' && ( // Admin kendini silemesin
                        <button 
                          onClick={() => handleDeleteUser(user.id)}
                          className="bg-red-900 text-red-300 hover:bg-red-800 hover:text-white px-3 py-1 rounded transition text-xs"
                        >
                          Sil
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

    </div>
  );
}

export default AdminPanel;