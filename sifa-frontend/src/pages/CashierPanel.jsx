import { useState, useEffect } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';

function CashierPanel() {
  // Ödeme State'leri
  const [appointments, setAppointments] = useState([]);
  const [selectedAppId, setSelectedAppId] = useState('');
  const [tcNo, setTcNo] = useState(''); // YENİ EKLENDİ: TC Kimlik No State'i
  const [amount, setAmount] = useState('');
  const [discount, setDiscount] = useState(0);
  const [paymentMethod, setPaymentMethod] = useState('CREDIT_CARD'); 
  
  // Randevu State'leri
  const [doctors, setDoctors] = useState([]);
  const [patientId, setPatientId] = useState('');
  const [selectedDoctorId, setSelectedDoctorId] = useState('');
  const [date, setDate] = useState('');
  const [timeBlock, setTimeBlock] = useState(''); 
  const navigate = useNavigate();

  useEffect(() => {
    fetchAppointments();
    fetchDoctors();
  }, []);

  const fetchAppointments = async () => {
    try {
      const response = await api.get('/appointments'); 
      
      // İsteğe Bağlı Harika Bir İş Kuralı (Business Logic): 
      // Veznedar sadece muayenesi bitmiş (COMPLETED) randevuların ödemesini alsın!
      const completedAppointments = response.data.filter(app => app.status === 'COMPLETED');
      
      setAppointments(completedAppointments);
    } catch (error) { 
      console.error("Randevular çekilirken hata oluştu.");
    }
  };

  const fetchDoctors = async () => {
    try {
      const response = await api.get('/users/doctors'); 
      setDoctors(response.data);
    } catch (error) { }
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
      alert('Ödeme başarıyla alındı!');
      
      // Formu temizle
      setSelectedAppId('');
      setTcNo('');
      setAmount('');
      setDiscount(0);

      // YENİ EKLENDİ: Ödeme sonrası listeyi yenile ki ödenen randevu listeden düşsün!
      fetchAppointments();

    } catch (error) { 
      alert('Hata: Ödeme zaten yapılmış olabilir.'); 
    }
  };

  const handleBookAppointment = async (e) => {
    e.preventDefault();
    try {
      const dateTimeString = `${date}T${timeBlock}:00`;
      
      await api.post('/appointments', {
        patientId: parseInt(patientId),
        doctorId: parseInt(selectedDoctorId),
        dateTime: dateTimeString,
        status: 'SCHEDULED'
      });
      alert('Randevu başarıyla verildi!');
      fetchAppointments(); // Listeyi yenile
    } catch (error) {
      alert('Hata: ' + (error.response?.data?.message || 'Saat dolu olabilir!'));
    }
  };

  // YENİ KURAL: Dış sistem (Backend SSK API) üzerinden indirim hesaplama
  const applyTcDiscount = async () => {
    if(!amount || !selectedAppId || !tcNo) {
      alert("Lütfen TC No, Randevu ve Muayene Ücreti alanlarını doldurun.");
      return;
    }
    
    try {
      const response = await api.get(`/payments/calculate?appointmentId=${selectedAppId}&tcNo=${tcNo}&baseFee=${amount}`);
      
      setDiscount(response.data.discountAmount);
      alert(`SSK Sorgusu Başarılı! İndirim Tutarı: ${response.data.discountAmount} ₺`);
    } catch (error) {
      alert("SSK sistemine ulaşılamadı veya hata oluştu.");
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <div className="flex justify-between items-center bg-white p-6 rounded-lg shadow-md mb-8 border-t-4 border-green-500">
        <h1 className="text-2xl font-bold">🏢 Görevli / Vezne Paneli</h1>
        <button onClick={() => { localStorage.removeItem('token'); navigate('/login'); }} className="bg-red-500 text-white px-4 py-2 rounded shadow transition hover:bg-red-600">Çıkış</button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        
        {/* SOL: GÖREVLİ RANDEVU VERME FORMU */}
        <div className="bg-white rounded-lg shadow-md p-6 h-fit">
          <h2 className="text-xl font-semibold mb-4 border-b pb-2 text-blue-600">Yeni Randevu Ver</h2>
          <form onSubmit={handleBookAppointment} className="space-y-4">
            <input type="number" placeholder="Hasta ID" required className="w-full p-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 outline-none" onChange={(e) => setPatientId(e.target.value)} />
            
            <select required className="w-full p-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 outline-none bg-white" onChange={(e) => setSelectedDoctorId(e.target.value)}>
              <option value="">Klinik / Doktor Seçin...</option>
              {doctors.map(doc => (
                <option key={doc.id} value={doc.id}>Uzm. Dr. {doc.username}</option>
              ))}
            </select>

            <input type="date" required className="w-full p-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 outline-none" onChange={(e) => setDate(e.target.value)} />
            
            <select required className="w-full p-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 outline-none bg-white" onChange={(e) => setTimeBlock(e.target.value)}>
              <option value="">Saat (30 Dk Bloklar)...</option>
              <option value="09:00">09:00</option>
              <option value="09:30">09:30</option>
              <option value="10:00">10:00</option>
              <option value="10:30">10:30</option>
              <option value="11:00">11:00</option>
            </select>

            <button type="submit" className="w-full bg-blue-600 text-white font-bold p-3 rounded shadow transition hover:bg-blue-700 mt-2">Randevuyu Kaydet</button>
          </form>
        </div>

        {/* SAĞ: ÖDEME ALMA FORMU */}
        <div className="bg-white rounded-lg shadow-md p-6 h-fit">
          <h2 className="text-xl font-semibold mb-4 border-b pb-2 text-green-600">Muayene Ödemesi Al</h2>
          <form onSubmit={handlePayment} className="space-y-4">
            <select required className="w-full p-2 border border-gray-300 rounded focus:ring-2 focus:ring-green-500 outline-none bg-white" onChange={(e) => setSelectedAppId(e.target.value)}>
              <option value="">Randevu Seçin...</option>
              {appointments.map(app => ( <option key={app.id} value={app.id}>Randevu #{app.id} - Durum: {app.status}</option> ))}
            </select>
            
            {/* YENİ EKLENDİ: TC Kimlik Form Alanı */}
            <div>
              <input 
                type="text" 
                maxLength="11"
                className="w-full p-2 border border-gray-300 rounded focus:ring-2 focus:ring-green-500 outline-none"
                placeholder="11 Haneli Hasta TC Kimlik No"
                value={tcNo}
                onChange={(e) => setTcNo(e.target.value)}
              />
            </div>

            <div className="flex gap-2">
              <input type="number" placeholder="Muayene Ücreti (₺)" required className="w-full p-2 border border-gray-300 rounded focus:ring-2 focus:ring-green-500 outline-none" value={amount} onChange={(e) => setAmount(e.target.value)} />
              <button type="button" onClick={applyTcDiscount} className="bg-purple-600 hover:bg-purple-700 transition text-white text-sm font-semibold px-4 rounded whitespace-nowrap shadow">TC İndirimi Sorgula</button>
            </div>

            <input type="number" value={discount} readOnly className="w-full p-2 border border-gray-200 rounded bg-gray-50 text-gray-500" placeholder="İndirim Tutarı" />

            <select className="w-full p-2 border border-gray-300 rounded focus:ring-2 focus:ring-green-500 outline-none bg-white" onChange={(e) => setPaymentMethod(e.target.value)}>
              <option value="CREDIT_CARD">Kredi Kartı</option>
              <option value="CASH">Nakit</option>
            </select>

            <div className="bg-gray-50 p-4 text-center border rounded-lg mt-4 shadow-inner">
              <p className="text-sm text-gray-500 mb-1">Tahsil Edilecek Net Tutar</p>
              <p className="font-bold text-3xl text-green-600">{(amount - discount) > 0 ? (amount - discount) : 0} ₺</p>
            </div>
            <button type="submit" className="w-full bg-green-600 text-white font-bold p-3 rounded shadow transition hover:bg-green-700 mt-2">Ödemeyi Tahsil Et</button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default CashierPanel;