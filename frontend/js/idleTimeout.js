// Idle Timeout Configuration (e.g., 15 minutes)
const IDLE_TIMEOUT_MS = 15 * 60 * 1000;
let idleTimer;

function logoutUser() {
    // Clear authentication tokens from storage
    localStorage.removeItem('authToken');
    sessionStorage.clear();
    
    // Optional: Make an API call to your backend to invalidate the Session record
    // fetch('/api/auth/logout', { method: 'POST' });
    
    // Redirect to login page or update UI
    console.log('User has been logged out due to inactivity.');
    // window.location.href = '/login.html'; // Adjust this to your actual login route
}

function resetIdleTimer() {
    clearTimeout(idleTimer);
    idleTimer = setTimeout(logoutUser, IDLE_TIMEOUT_MS);
}

// Set up event listeners to monitor user activity
function setupIdleTracking() {
    const activeEvents = ['mousemove', 'keydown', 'mousedown', 'touchstart', 'scroll'];
    
    activeEvents.forEach(event => {
        window.addEventListener(event, resetIdleTimer, true);
    });
    
    resetIdleTimer(); // Initialize the timer on page load
}

setupIdleTracking();