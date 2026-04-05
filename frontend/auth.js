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

window.setUser = setUser;
window.getUser = getUser;
window.clearUser = clearUser;