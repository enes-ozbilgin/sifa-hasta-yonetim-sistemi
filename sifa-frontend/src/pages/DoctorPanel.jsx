import { useState, useEffect } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';

function DoctorPanel() {
  const [appointments, setAppointments] = useState([]);
  const [selectedApp, setSelectedApp] = useState(null); // Muayene edilen aktif randevu
  const [diagnosis, setDiagnosis] = useState('');
  const [treatment, setTreatment] = useState('');
  const [notes, setNotes] = useState('');
  const navigate = useNavigate();

  // Login olurken localStorage'a kaydettiğimiz Doktor ID'si
  const doctorId = localStorage.getItem('userId');

  // Sayfa açıldığında SADECE BU DOKTORA AİT randevuları çek
  useEffect(() => {
    if (doctorId) {
      fetchAppointments();
    }
  }, [doctorId]);

  const fetchAppointments = async () => {
    try {
      // GÜNCELLEME: Sadece giriş yapan doktorun randevuları geliyor
      const response = await api.get(`/appointments/doctor/${doctorId}`); 
      setAppointments(response.data);
    } catch (error) {
      console.error("Randevular yüklenemedi", error);
    }
  };

  const handleSaveExamination = async (e) => {
    e.preventDefault();
    try {
      // Sena'nın muayene servisine POST isteği atıyoruz
      await api.post('/examinations', {
        appointmentId: selectedApp.id,
        diagnosis: diagnosis,
        treatment: treatment,
        notes: notes
      });

      alert(`Muayene başarıyla kaydedildi! Randevu #${selectedApp.id} tamamlandı.`);
      
      // Formu temizle ve listeyi yenile
      setSelectedApp(null);
      setDiagnosis('');
      setTreatment('');
      setNotes('');
      fetchAppointments();
      
    } catch (error) {
      alert("Muayene kaydedilirken bir hata oluştu. Randevu ID'sini kontrol edin.");
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userId'); // ID'yi de temizle
    navigate('/login');
  };

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      {/* Üst Bilgi Çubuğu */}
      <div className="flex justify-between items-center bg-white p-6 rounded-2xl shadow-sm mb-8 border-l-8 border-blue-600">
        <div>
          <h1 className="text-2xl font-bold text-gray-800">🩺 Doktor Muayene Ekranı</h1>
          <p className="text-sm text-gray-500">Hoş geldiniz, bugün muayene bekleyen hastalarınız aşağıda listelenmiştir.</p>
        </div>
        <button 
          onClick={handleLogout}
          className="bg-red-50 hover:bg-red-100 text-red-600 font-semibold px-6 py-2 rounded-xl border border-red-200 transition-all"
        >
          Oturumu Kapat
        </button>
      </div>

      <div className="grid grid-cols-1 xl:grid-cols-2 gap-8">
        
        {/* SOL: Randevu Listesi */}
        <div className="bg-white rounded-2xl shadow-sm p-6 overflow-hidden">
          <h2 className="text-lg font-bold text-gray-700 mb-6 flex items-center">
            <span className="bg-blue-100 text-blue-600 p-2 rounded-lg mr-3">📅</span>
            Günlük Randevu Listesi
          </h2>
          
          <div className="space-y-4 max-h-[500px] overflow-y-auto pr-2">
            {appointments.length === 0 ? (
              <div className="text-center py-10 text-gray-400 italic">Şu an bekleyen randevu yok.</div>
            ) : (
              appointments.map((app) => (
                <div 
                  key={app.id} 
                  className={`p-5 rounded-2xl border-2 transition-all cursor-pointer ${
                    selectedApp?.id === app.id ? 'border-blue-500 bg-blue-50' : 'border-gray-100 hover:border-blue-200'
                  }`}
                  onClick={() => app.status === 'SCHEDULED' && setSelectedApp(app)}
                >
                  <div className="flex justify-between items-start">
                    <div>
                      <h3 className="font-bold text-gray-800">Hasta ID: #{app.patientId}</h3>
                      <p className="text-sm text-gray-600 mt-1">{new Date(app.dateTime).toLocaleString('tr-TR')}</p>
                    </div>
                    <span className={`px-3 py-1 text-xs font-black rounded-full ${
                      app.status === 'COMPLETED' ? 'bg-green-100 text-green-700' : 'bg-orange-100 text-orange-700'
                    }`}>
                      {app.status === 'SCHEDULED' ? 'BEKLİYOR' : 'TAMAMLANDI'}
                    </span>
                  </div>
                  {app.status === 'SCHEDULED' && (
                    <button className="mt-3 text-sm font-bold text-blue-600 hover:underline">
                      Muayeneyi Başlat →
                    </button>
                  )}
                </div>
              ))
            )}
          </div>
        </div>

        {/* SAĞ: Muayene Giriş Formu */}
        <div className="bg-white rounded-2xl shadow-sm p-6">
          <h2 className="text-lg font-bold text-gray-700 mb-6 flex items-center">
            <span className="bg-green-100 text-green-600 p-2 rounded-lg mr-3">📝</span>
            Aktif Muayene Detayları
          </h2>

          {!selectedApp ? (
            <div className="flex flex-col items-center justify-center h-64 text-gray-400 border-2 border-dashed border-gray-200 rounded-2xl">
              <p>Lütfen muayene etmek için soldan bir randevu seçin.</p>
            </div>
          ) : (
            <form onSubmit={handleSaveExamination} className="space-y-5 animate-in fade-in duration-500">
              <div className="bg-blue-50 p-4 rounded-xl border border-blue-100">
                <p className="text-sm text-blue-800 font-bold">Seçili Randevu: #{selectedApp.id}</p>
              </div>

              <div>
                <label className="block text-sm font-bold text-gray-700 mb-2">Tanı / Teşhis</label>
                <input 
                  type="text" 
                  required
                  placeholder="Örn: Akut Sinüzit, Mevsimsel Alerji..."
                  className="w-full px-4 py-3 border border-gray-200 rounded-xl focus:ring-4 focus:ring-blue-100 focus:border-blue-500 outline-none transition-all"
                  value={diagnosis}
                  onChange={(e) => setDiagnosis(e.target.value)}
                />
              </div>

              <div>
                <label className="block text-sm font-bold text-gray-700 mb-2">Uygulanan Tedavi</label>
                <input 
                  type="text" 
                  required
                  placeholder="Örn: Antibiyotik tedavisi, İstirahat..."
                  className="w-full px-4 py-3 border border-gray-200 rounded-xl focus:ring-4 focus:ring-blue-100 focus:border-blue-500 outline-none transition-all"
                  value={treatment}
                  onChange={(e) => setTreatment(e.target.value)}
                />
              </div>

              <div>
                <label className="block text-sm font-bold text-gray-700 mb-2">Doktor Notları ve Tavsiyeler</label>
                <textarea 
                  rows="4"
                  placeholder="Hastaya özel uyarılar, takip süreci..."
                  className="w-full px-4 py-3 border border-gray-200 rounded-xl focus:ring-4 focus:ring-blue-100 focus:border-blue-500 outline-none transition-all"
                  value={notes}
                  onChange={(e) => setNotes(e.target.value)}
                ></textarea>
              </div>

              <button 
                type="submit" 
                className="w-full bg-blue-600 hover:bg-blue-700 text-white font-black py-4 rounded-xl shadow-lg shadow-blue-200 transition-all transform hover:-translate-y-1"
              >
                MUAYENEYİ KAYDET VE BİTİR
              </button>
            </form>
          )}
        </div>
      </div>
    </div>
  );
}

export default DoctorPanel;