/**
 * Authentication Form Handler
 * Handles login form submission and authentication
 */

document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const alertDiv = document.getElementById('alert');
    
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }
});

/**
 * Handle login form submission
 * @param {Event} e - Form submit event
 */
async function handleLogin(e) {
    e.preventDefault();
    
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();
    const submitBtn = document.querySelector('.login-btn');
    const alertDiv = document.getElementById('alert');
    
    // Validation
    if (!username || !password) {
        showAlert('Proszę wpisać nazwę użytkownika i hasło', 'error');
        return;
    }
    
    // Disable button and show loading state
    submitBtn.disabled = true;
    submitBtn.classList.add('loading');
    alertDiv.classList.remove('show');
    
    try {
        const response = await fetch('/api/v1/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });
        
        const data = await response.json();
        
        if (!response.ok) {
            throw new Error(data.message || 'Logowanie nie powiodło się');
        }
        
        // Store tokens
        authUtils.setToken(data.access_token, data.expires_in);
        authUtils.setRefreshToken(data.refresh_token);
        
        // Show success message
        showAlert('Zalogowano pomyślnie! Przekierowywanie...', 'success');
        
        // Redirect to dashboard after a short delay
        setTimeout(() => {
            window.location.href = '/admin';
        }, 1000);
        
    } catch (error) {
        console.error('Login error:', error);
        showAlert(error.message || 'Błąd podczas logowania. Spróbuj ponownie.', 'error');
    } finally {
        // Re-enable button and hide loading state
        submitBtn.disabled = false;
        submitBtn.classList.remove('loading');
    }
}

/**
 * Show alert message
 * @param {string} message - Alert message
 * @param {string} type - Alert type: 'success' or 'error'
 */
function showAlert(message, type = 'error') {
    const alertDiv = document.getElementById('alert');
    
    alertDiv.textContent = message;
    alertDiv.className = `alert show alert-${type}`;
    
    // Auto-hide error alerts after 5 seconds
    if (type === 'error') {
        setTimeout(() => {
            alertDiv.classList.remove('show');
        }, 5000);
    }
}
