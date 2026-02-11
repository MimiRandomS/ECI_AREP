// === CONFIGURACIÓN ===
const API_BASE = "https://taller6-back-arep.duckdns.org/arep";

document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");
    if (token) {
        showProfile();
    }

    document.getElementById("register-form").addEventListener("submit", registerUser);
    document.getElementById("login-form").addEventListener("submit", loginUser);
    document.getElementById("logout-btn").addEventListener("click", logoutUser);
});

// === REGISTRO ===
async function registerUser(e) {
    e.preventDefault();
    const user = {
        name: document.getElementById("reg-name").value,
        email: document.getElementById("reg-email").value,
        password: document.getElementById("reg-pass").value
    };

    try {
        const res = await fetch(`${API_BASE}/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(user)
        });

        if (res.ok) {
            alert("Usuario registrado con éxito.");
            document.getElementById("register-form").reset();
        } else {
            alert("Error al registrar usuario.");
        }
    } catch (err) {
        console.error("Error:", err);
    }
}

// === LOGIN ===
async function loginUser(e) {
    e.preventDefault();
    const credentials = {
        email: document.getElementById("log-email").value,
        password: document.getElementById("log-pass").value
    };

    try {
        const res = await fetch(`${API_BASE}/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(credentials)
        });

        if (res.ok) {
            const data = await res.json();
            localStorage.setItem("token", data.token);
            showProfile();
        } else {
            alert("Credenciales incorrectas");
        }
    } catch (err) {
        console.error("Error:", err);
    }
}

// === PERFIL ===
async function showProfile() {
    const token = localStorage.getItem("token");
    if (!token) return;

    try {
        const res = await fetch(`${API_BASE}/profile`, {
            headers: { Authorization: `Bearer ${token}` }
        });

        if (res.ok) {
            const user = await res.json();
            document.querySelector(".forms-container").style.display = "none";
            const profile = document.getElementById("profile-section");
            profile.style.display = "block";
            document.getElementById("profile-info").innerHTML = `
                <p><b>Nombre:</b> ${user.name}</p>
                <p><b>Email:</b> ${user.email}</p>
                <p><b>ID:</b> ${user.id}</p>
            `;
        } else {
            alert("Sesión expirada. Vuelve a iniciar sesión.");
            logoutUser();
        }
    } catch (err) {
        console.error("Error:", err);
    }
}

// === LOGOUT ===
function logoutUser() {
    localStorage.removeItem("token");
    document.querySelector(".forms-container").style.display = "flex";
    document.getElementById("profile-section").style.display = "none";
}
