import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api', // Spring Boot adresimiz
});

// Her istek (Request) atılmadan önce araya gir ve Token'ı ekle
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token'); // Token'ı tarayıcıdan al
        if (token) {
            config.headers.Authorization = `Bearer ${token}`; // Backend'in beklediği format
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default api;