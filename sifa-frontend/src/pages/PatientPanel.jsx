import { useState, useEffect } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';

function PatientPanel() {
  const [appointments, setAppointments] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchAppointments();
  }, []);

  const fetchAppointments = async () => {
    try {
      const response = await api.get('/appointments/patient/1'); // İleride dinamik olacak
      setAppointments(response.data);
    } catch (error) {
      console.error("Randevular çekilirken hata oluştu", error);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 p-8">
      <div className="flex justify-between items-center bg-white p-6 rounded-lg shadow-md mb-8">
        <h1 className="text-2xl font-bold text-blue-600">👤 Hasta Randevu Takip Paneli</h1>
        <button onClick={() => { localStorage.removeItem('token'); navigate('/login'); }} className="bg-red-500 text-white px-4 py-2 rounded">Çıkış Yap</button>
      </div>

      <div className="bg-white rounded-lg shadow-md p-6">
        <h2 className="text-xl font-semibold mb-4 border-b pb-2">Geçmiş ve Gelecek Randevularım</h2>
        {/* Burada daha önce yazdığımız tablo kısmı aynen duracak, kod kalabalığı olmasın diye kısalttım. */}
        {appointments.map(app => (
            <div key={app.id} className="p-3 border-b">Randevu #{app.id} - Tarih: {new Date(app.dateTime).toLocaleString('tr-TR')} - Durum: {app.status}</div>
        ))}
      </div>
    </div>
  );
}

export default PatientPanel;