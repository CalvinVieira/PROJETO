const API_BASE = (window.location.hostname.includes('localhost') || window.location.hostname==='127.0.0.1') ? 'http://localhost:8080/api' : 'https://SEU-BACKEND.onrender.com/api';
const getUser = () => JSON.parse(localStorage.getItem('stratec_user') || 'null');
const setUser = (u) => localStorage.setItem('stratec_user', JSON.stringify(u));
const logout = () => { localStorage.removeItem('stratec_user'); location.href='login.html'; };
const protect = () => { if (!getUser()) location.href = 'login.html'; };
window.addEventListener('load',()=>{const l=document.createElement('div');l.className='loading-screen';l.id='loadingScreen';l.innerHTML="<img src='assets/logo-branca.png'><div class='spinner'></div><div style='margin-top:12px'>Carregando Stratec TI...</div>";document.body.appendChild(l);setTimeout(()=>l.remove(),600);});