const API_BASE =
  window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1"
    ? "http://localhost:8080/api"
    : "https://stratec-ti-backend.onrender.com/api";

function setUser(user) {
  localStorage.setItem("stratec_user", JSON.stringify(user));
}

function getUser() {
  const raw = localStorage.getItem("stratec_user");
  return raw ? JSON.parse(raw) : null;
}

function clearUser() {
  localStorage.removeItem("stratec_user");
}

function protect() {
  const publicPages = ['index.html', 'login.html', 'cadastro.html', '404.html', ''];
  const path = window.location.pathname.split('/').pop();
  if (!publicPages.includes(path) && !getUser()) {
    window.location.href = 'login.html';
  }
}

function logout() {
  clearUser();
  window.location.href = 'index.html';
}

window.API_BASE = API_BASE;
window.setUser = setUser;
window.getUser = getUser;
window.clearUser = clearUser;
window.protect = protect;
window.logout = logout;
