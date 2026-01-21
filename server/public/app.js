const API_URL = '/status';

// DOM Elements
const currentLocationEl = document.getElementById('currentLocation');
const parkedByEl = document.getElementById('parkedBy');
const lastUpdatedEl = document.getElementById('lastUpdated');
const newLocationInput = document.getElementById('newLocationInput');
const parkBtn = document.getElementById('parkBtn');
const toast = document.getElementById('toast');
const toastMessage = document.getElementById('toastMessage');

// State
let selectedUser = null;

// Initialize
function init() {
    fetchStatus();
    setupEventListeners();
    // Poll every 5 seconds
    setInterval(fetchStatus, 5000);
}

function setupEventListeners() {
    // User Selection
    // Dynamically query user buttons as they might change
    const userBtns = document.querySelectorAll('.user-btn');
    userBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            selectedUser = btn.dataset.user;
            updateUserSelectionUI();
            checkFormValidity();
        });
    });

    // Input Change
    newLocationInput.addEventListener('input', checkFormValidity);

    // Park Action
    parkBtn.addEventListener('click', handleParkAction);

    // Quick Location Buttons
    document.querySelectorAll('.location-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const location = btn.dataset.loc;
            newLocationInput.value = location;
            checkFormValidity();

            // Optional: Scroll to submit button or auto-focus
            parkBtn.scrollIntoView({ behavior: 'smooth' });

            // Visual feedback
            btn.style.borderColor = 'var(--secondary)';
            setTimeout(() => {
                btn.style.borderColor = 'rgba(255, 255, 255, 0.1)';
            }, 300);
        });
    });
}

function updateUserSelectionUI() {
    const userBtns = document.querySelectorAll('.user-btn');
    userBtns.forEach(btn => {
        if (btn.dataset.user === selectedUser) {
            btn.classList.add('active');
        } else {
            btn.classList.remove('active');
        }
    });
}

function checkFormValidity() {
    const location = newLocationInput.value.trim();
    if (selectedUser && location.length > 0) {
        parkBtn.disabled = false;
    } else {
        parkBtn.disabled = true;
    }
}

async function fetchStatus() {
    try {
        const response = await fetch(API_URL);
        const data = await response.json();
        updateStatusUI(data);
    } catch (error) {
        console.error('Error fetching status:', error);
        currentLocationEl.textContent = 'Error de conexión';
    }
}

function updateStatusUI(data) {
    if (data.location) {
        currentLocationEl.textContent = data.location;
        parkedByEl.textContent = data.user || 'Desconocido';
        lastUpdatedEl.textContent = data.timestamp || '';
    }
}

async function handleParkAction() {
    if (!selectedUser) return;

    const location = newLocationInput.value.trim();
    if (!location) return;

    const now = new Date();
    // Format: "19:30 19 ene"
    const options = { hour: '2-digit', minute: '2-digit', day: 'numeric', month: 'short' };
    const timestamp = now.toLocaleDateString('es-ES', options).replace(',', '');

    const payload = {
        location: location,
        user: selectedUser,
        timestamp: timestamp,
        timestampRaw: now.getTime()
    };

    try {
        parkBtn.disabled = true;
        parkBtn.innerHTML = '<span>Guardando...</span>';

        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            showToast('✅ ¡Aparcado correctamente!');
            newLocationInput.value = '';
            fetchStatus(); // Update immediately
        } else {
            showToast('❌ Error al guardar', true);
        }
    } catch (error) {
        console.error('Error saving status:', error);
        showToast('❌ Error de conexión', true);
    } finally {
        parkBtn.disabled = false;
        parkBtn.innerHTML = '<span class="btn-icon">🅿️</span><span>He Aparcado Aquí</span>';
        checkFormValidity();
    }
}

function showToast(message, isError = false) {
    toastMessage.textContent = message;
    toast.classList.remove('hidden');

    if (isError) {
        toast.style.backgroundColor = 'var(--accent)';
    } else {
        toast.style.backgroundColor = '#10B981';
    }

    setTimeout(() => {
        toast.classList.add('hidden');
    }, 3000);
}

// Start
init();
