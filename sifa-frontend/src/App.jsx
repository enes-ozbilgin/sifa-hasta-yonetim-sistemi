import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { jwtDecode } from "jwt-decode";
import Login from './pages/Login';
import Register from './pages/Register';
import DoctorPanel from './pages/DoctorPanel';
import PatientPanel from './pages/PatientPanel';
import CashierPanel from './pages/CashierPanel';
import AdminPanel from './pages/AdminPanel';

// Gümrük Memuru Bileşenimiz: Token yoksa veya rol yetmiyorsa Login'e şutlar
const ProtectedRoute = ({ children, allowedRoles }) => {
  const token = localStorage.getItem('token');
  
  if (!token) {
    return <Navigate to="/login" replace />;
  }

  try {
    const decodedToken = jwtDecode(token);
    // Spring Security rolleri "ROLE_DOCTOR" şeklinde kaydeder, bunu kontrol ediyoruz
    const userRole = decodedToken.role || decodedToken.authorities?.[0]?.authority; 
    
    // Eğer kullanıcının rolü, bu sayfanın izin verilen rolleri arasında yoksa
    if (allowedRoles && !allowedRoles.includes(userRole)) {
       alert("Bu sayfaya erişim yetkiniz yok!");
       return <Navigate to="/login" replace />;
    }
    
    return children;
  } catch (error) {
    localStorage.removeItem('token');
    return <Navigate to="/login" replace />;
  }
};

function App() {
  return (
    <Router>
      <Routes>
        {/* Herkese Açık Sayfalar */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/" element={<Navigate to="/login" replace />} />

        {/* Sadece HASTA Rolüne Açık Sayfalar (Senin Modülün) */}
        <Route path="/patient" element={
          <ProtectedRoute allowedRoles={['ROLE_PATIENT']}>
            <PatientPanel />
          </ProtectedRoute>
        } />

        {/* Sadece DOKTOR Rolüne Açık Sayfalar (Sena'nın Modülü) */}
        <Route path="/doctor" element={
          <ProtectedRoute allowedRoles={['ROLE_DOCTOR']}>
            <DoctorPanel />
          </ProtectedRoute>
        } />

        {/* Sadece VEZNE Rolüne Açık Sayfalar (Meryem'in Modülü) */}
        <Route path="/cashier" element={
          <ProtectedRoute allowedRoles={['ROLE_CASHIER']}>
            <CashierPanel />
          </ProtectedRoute>
        } />

        {/* Sadece ADMIN Rolüne Açık Sayfalar (Enes'in Modülü) */}
        <Route path="/admin" element={
          <ProtectedRoute allowedRoles={['ROLE_ADMIN']}>
            <AdminPanel />
          </ProtectedRoute>
          } />
      </Routes>
    </Router>
  );
}

export default App;