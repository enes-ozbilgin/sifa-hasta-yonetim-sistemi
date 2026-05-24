import { useNavigate } from 'react-router-dom';

function AdminPanel() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userId'); // ID'yi de temizliyoruz
    navigate('/login');
  };

  // Butonlar için geçici (Placeholder) fonksiyonlar
  const handleUserManagement = () => {
    alert("Kullanıcı Yönetimi modülü yapım aşamasındadır. Yakında eklenecek!");
    // İleride burası: navigate('/admin/users'); olacak
  };

  const handleSettings = () => {
    alert("Sistem Ayarları modülü yapım aşamasındadır.");
    // İleride burası: navigate('/admin/settings'); olacak
  };

  const handleReports = () => {
    alert("Recep'in Raporlama modülü buraya entegre edilecek!");
    // İleride burası: navigate('/admin/reports'); olacak
  };

  return (
    <div className="min-h-screen bg-gray-900 p-8">
      <div className="flex justify-between items-center bg-gray-800 p-6 rounded-lg shadow-xl mb-8 border-t-4 border-yellow-500">
        <h1 className="text-2xl font-bold text-white">👑 Sistem Yöneticisi (Admin) Paneli</h1>
        <button 
          onClick={handleLogout}
          className="bg-red-600 hover:bg-red-700 text-white px-6 py-2 rounded shadow transition"
        >
          Sistemden Çıkış Yap
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
        
        {/* Kullanıcı Yönetimi */}
        <div className="bg-gray-800 p-6 rounded-lg shadow-lg text-center border border-gray-700 flex flex-col justify-between">
          <div>
            <div className="text-5xl mb-4">👥</div>
            <h2 className="text-xl font-bold text-white mb-2">Kullanıcı Yönetimi</h2>
            <p className="text-gray-400 text-sm mb-4">Sistemdeki doktor, veznedar ve hastaları yönetin.</p>
          </div>
          <button 
            onClick={handleUserManagement}
            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 rounded transition shadow-md active:scale-95"
          >
            Yönet
          </button>
        </div>

        {/* Sistem Ayarları */}
        <div className="bg-gray-800 p-6 rounded-lg shadow-lg text-center border border-gray-700 flex flex-col justify-between">
          <div>
            <div className="text-5xl mb-4">⚙️</div>
            <h2 className="text-xl font-bold text-white mb-2">Sistem Ayarları</h2>
            <p className="text-gray-400 text-sm mb-4">Poliklinik kuralları, indirim oranları ve çalışma saatleri.</p>
          </div>
          <button 
            onClick={handleSettings}
            className="w-full bg-yellow-600 hover:bg-yellow-700 text-white font-bold py-3 rounded transition shadow-md active:scale-95"
          >
            Ayarlar
          </button>
        </div>

        {/* Genel Raporlar */}
        <div className="bg-gray-800 p-6 rounded-lg shadow-lg text-center border border-gray-700 flex flex-col justify-between">
          <div>
            <div className="text-5xl mb-4">📊</div>
            <h2 className="text-xl font-bold text-white mb-2">Genel Raporlar</h2>
            <p className="text-gray-400 text-sm mb-4">Sistemdeki tüm mali veriler ve randevu istatistikleri.</p>
          </div>
          <button 
            onClick={handleReports}
            className="w-full bg-green-600 hover:bg-green-700 text-white font-bold py-3 rounded transition shadow-md active:scale-95"
          >
            Raporları Gör
          </button>
        </div>

      </div>
    </div>
  );
}

export default AdminPanel;