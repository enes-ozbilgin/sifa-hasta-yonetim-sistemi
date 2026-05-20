import { useState, useEffect } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';

function CashierPanel() {
  const [appointments, setAppointments] = useState([]);
  const [selectedAppId, setSelectedAppId] = useState('');
  const [amount, setAmount] = useState('');
  const [discount, setDiscount] = useState('0');
  const [paymentMethod, setPaymentMethod] = useState('CREDIT_CARD');
  const navigate = useNavigate();

  // Sayfa yüklendiğinde tüm randevuları getir (Veznedar herkesi görmeli)
  useEffect(() => {
    fetchAppointments();
  }, []);

  const fetchAppointments = async () => {
    try {
      // Not: Backend'de tüm randevuları getiren bir uç nokta (GET /api/appointments) 
      // olduğunu varsayarak istek atıyoruz. Eğer yoksa geçici olarak hasta ID'si ile çekebiliriz.
      const response = await api.get('/appointments/patient/1'); 
      setAppointments(response.data);
    } catch (error) {
      console.error("Randevular çekilirken hata oluştu", error);
    }
  };

  const handlePayment = async (e) => {
    e.preventDefault();
    try {
      await api.post('/payments', {
        appointmentId: parseInt(selectedAppId),
        amount: parseFloat(amount),
        discount: parseFloat(discount),
        paymentMethod: paymentMethod
      });
      
      alert('Ödeme başarıyla alındı ve sisteme kaydedildi!');
      setSelectedAppId('');
      setAmount('');
      setDiscount('0');
      // İsteğe bağlı: Listeyi yenile
    } catch (error) {
      alert('Ödeme alınamadı! Bu randevu için zaten ödeme yapılmış olabilir.');
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  // Ödenecek net tutarı hesaplayan küçük bir yardımcı fonksiyon
  const calculateTotal = () => {
    const netAmount = parseFloat(amount || 0) - parseFloat(discount || 0);
    return netAmount > 0 ? netAmount : 0;
  };

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      {/* Navbar */}
      <div className="flex justify-between items-center bg-white p-6 rounded-lg shadow-md mb-8 border-t-4 border-green-500">
        <h1 className="text-2xl font-bold text-gray-800">💰 Vezne ve Ödeme Paneli</h1>
        <button 
          onClick={handleLogout}
          className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded transition shadow"
        >
          Çıkış Yap
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        
        {/* Sol Taraf: Ödeme Alma Formu */}
        <div className="bg-white rounded-lg shadow-md p-6 lg:col-span-1 h-fit">
          <h2 className="text-xl font-semibold mb-4 border-b pb-2 text-gray-700">Yeni Ödeme Al</h2>
          <form onSubmit={handlePayment} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Randevu Seçin</label>
              <select 
                required
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 outline-none"
                value={selectedAppId}
                onChange={(e) => setSelectedAppId(e.target.value)}
              >
                <option value="">Lütfen randevu seçin...</option>
                {appointments.map(app => (
                  <option key={app.id} value={app.id}>
                    Randevu #{app.id} - Durum: {app.status}
                  </option>
                ))}
              </select>
            </div>
            
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Muayene Ücreti (₺)</label>
              <input 
                type="number" 
                required
                min="0"
                step="0.01"
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 outline-none"
                placeholder="Örn: 1500"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Sigorta / İndirim (₺)</label>
              <input 
                type="number" 
                min="0"
                step="0.01"
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 outline-none"
                value={discount}
                onChange={(e) => setDiscount(e.target.value)}
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Ödeme Yöntemi</label>
              <select 
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 outline-none bg-white"
                value={paymentMethod}
                onChange={(e) => setPaymentMethod(e.target.value)}
              >
                <option value="CREDIT_CARD">Kredi Kartı</option>
                <option value="CASH">Nakit</option>
                <option value="BANK_TRANSFER">Havale / EFT</option>
              </select>
            </div>

            <div className="bg-gray-50 p-4 rounded-lg border mt-4">
              <p className="text-sm text-gray-600">Ödenecek Net Tutar:</p>
              <p className="text-2xl font-bold text-green-600">{calculateTotal()} ₺</p>
            </div>

            <button 
              type="submit" 
              className="w-full bg-green-600 hover:bg-green-700 text-white font-bold py-3 px-4 rounded-lg transition shadow-md mt-2"
            >
              Ödemeyi Tamamla
            </button>
          </form>
        </div>

        {/* Sağ Taraf: Sistemdeki Randevular Listesi */}
        <div className="bg-white rounded-lg shadow-md p-6 lg:col-span-2">
          <h2 className="text-xl font-semibold mb-4 border-b pb-2 text-gray-700">Sistemdeki Randevular</h2>
          
          {appointments.length === 0 ? (
            <p className="text-gray-500 italic text-center py-8">Sistemde hiç randevu bulunmuyor.</p>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full bg-white">
                <thead className="bg-gray-100 text-gray-600">
                  <tr>
                    <th className="py-3 px-4 text-left font-medium">Randevu ID</th>
                    <th className="py-3 px-4 text-left font-medium">Tarih</th>
                    <th className="py-3 px-4 text-left font-medium">Doktor ID</th>
                    <th className="py-3 px-4 text-left font-medium">Durum</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200">
                  {appointments.map((app) => (
                    <tr key={app.id} className="hover:bg-gray-50 transition">
                      <td className="py-3 px-4 font-semibold">#{app.id}</td>
                      <td className="py-3 px-4 text-sm text-gray-600">{new Date(app.dateTime).toLocaleString('tr-TR')}</td>
                      <td className="py-3 px-4 text-sm">Dr. {app.doctorId}</td>
                      <td className="py-3 px-4">
                        <span className={`px-2 py-1 text-xs font-bold rounded-full ${
                          app.status === 'COMPLETED' ? 'bg-blue-100 text-blue-800' : 
                          'bg-yellow-100 text-yellow-800'
                        }`}>
                          {app.status}
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

export default CashierPanel;