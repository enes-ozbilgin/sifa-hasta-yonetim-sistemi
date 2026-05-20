import { useState, useEffect } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';

function PatientPanel() {
  const [appointments, setAppointments] = useState([]);
  const [doctorId, setDoctorId] = useState('');
  const [dateTime, setDateTime] = useState('');
  const navigate = useNavigate();

  // Sayfa yüklendiğinde hastanın randevularını getir
  useEffect(() => {
    fetchAppointments();
  }, []);

  const fetchAppointments = async () => {
    try {
      // Şimdilik test amaçlı Hasta ID'sini 1 olarak sabit veriyoruz. 
      // İleride bunu token içinden veya giriş yapan kullanıcıdan dinamik alacağız.
      const response = await api.get('/appointments/patient/1');
      setAppointments(response.data);
    } catch (error) {
      console.error("Randevular çekilirken hata oluştu", error);
    }
  };

  const handleBookAppointment = async (e) => {
    e.preventDefault();
    try {
      // Backend'in bizden beklediği Appointment JSON formatı
      await api.post('/appointments', {
        patientId: 1, // Şimdilik test için sabit
        doctorId: parseInt(doctorId),
        dateTime: dateTime + ":00", // Backend LocalDateTime beklediği için saniye (:00) ekliyoruz
        status: 'SCHEDULED'
      });
      
      alert('Randevunuz başarıyla oluşturuldu!');
      setDoctorId('');
      setDateTime('');
      fetchAppointments(); // Listeyi otomatik yenile
    } catch (error) {
      alert('Randevu alınamadı! Seçilen saatte doktor dolu olabilir veya geçmiş bir tarih seçtiniz.');
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      {/* Navbar */}
      <div className="flex justify-between items-center bg-white p-6 rounded-lg shadow-md mb-8">
        <h1 className="text-2xl font-bold text-green-600">👤 Hasta Randevu Paneli</h1>
        <button 
          onClick={handleLogout}
          className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded transition"
        >
          Çıkış Yap
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Sol Taraf: Yeni Randevu Alma Formu */}
        <div className="bg-white rounded-lg shadow-md p-6 lg:col-span-1 h-fit">
          <h2 className="text-xl font-semibold mb-4 border-b pb-2">Yeni Randevu Al</h2>
          <form onSubmit={handleBookAppointment} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Doktor Seçimi</label>
              <select 
                required
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 outline-none"
                value={doctorId}
                onChange={(e) => setDoctorId(e.target.value)}
              >
                <option value="">Lütfen doktor seçin...</option>
                {/* Şimdilik doktorları elle (hardcode) ekledik, ileride veritabanından çekilebilir */}
                <option value="100">Dr. Sena (Dahiliye)</option>
                <option value="101">Dr. Ahmet (KBB)</option>
                <option value="102">Dr. Ayşe (Göz)</option>
              </select>
            </div>
            
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Tarih ve Saat</label>
              <input 
                type="datetime-local" 
                required
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 outline-none"
                value={dateTime}
                onChange={(e) => setDateTime(e.target.value)}
              />
            </div>

            <button 
              type="submit" 
              className="w-full bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded-lg transition"
            >
              Randevuyu Onayla
            </button>
          </form>
        </div>

        {/* Sağ Taraf: Geçmiş ve Aktif Randevular Listesi */}
        <div className="bg-white rounded-lg shadow-md p-6 lg:col-span-2">
          <h2 className="text-xl font-semibold mb-4 border-b pb-2">Randevularım</h2>
          
          {appointments.length === 0 ? (
            <p className="text-gray-500 italic text-center py-8">Henüz alınmış bir randevunuz bulunmuyor.</p>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full bg-white">
                <thead className="bg-gray-50 text-gray-600">
                  <tr>
                    <th className="py-3 px-4 text-left font-medium">Randevu ID</th>
                    <th className="py-3 px-4 text-left font-medium">Doktor ID</th>
                    <th className="py-3 px-4 text-left font-medium">Tarih & Saat</th>
                    <th className="py-3 px-4 text-left font-medium">Durum</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200">
                  {appointments.map((app) => (
                    <tr key={app.id} className="hover:bg-gray-50 transition">
                      <td className="py-3 px-4">#{app.id}</td>
                      <td className="py-3 px-4">Dr. {app.doctorId}</td>
                      <td className="py-3 px-4">{new Date(app.dateTime).toLocaleString('tr-TR')}</td>
                      <td className="py-3 px-4">
                        <span className={`px-3 py-1 text-xs font-bold rounded-full ${
                          app.status === 'COMPLETED' ? 'bg-blue-100 text-blue-800' : 
                          app.status === 'CANCELED' ? 'bg-red-100 text-red-800' : 
                          'bg-green-100 text-green-800'
                        }`}>
                          {app.status === 'SCHEDULED' ? 'PLANLANDI' : app.status}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default PatientPanel;