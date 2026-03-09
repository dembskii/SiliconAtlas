/**
 * Registration Form Handler
 * Handles user registration
 */

document.addEventListener('DOMContentLoaded', function() {
    const registerForm = document.getElementById('registerForm');
    const passwordInput = document.getElementById('password');
    
    if (registerForm) {
        registerForm.addEventListener('submit', handleRegister);
    }
    
    if (passwordInput) {
        passwordInput.addEventListener('input', updatePasswordStrength);
    }
});

/**
 * Update password strength indicator
 */
function updatePasswordStrength() {
    const password = document.getElementById('password').value;
    const strengthBar = document.getElementById('strengthBar');
    const strengthText = document.getElementById('strengthText');
    
    let strength = 'weak';
    let text = 'Słabe hasło';
    
    // Check password strength
    if (password.length >= 8) {
        if (/[a-z]/.test(password) && /[A-Z]/.test(password) && /[0-9]/.test(password)) {
            strength = 'strong';
            text = 'Silne hasło';
        } else if (/[a-z]/.test(password) && /[A-Z]/.test(password)) {
            strength = 'medium';
            text = 'Średnie hasło';
        } else {
            strength = 'medium';
            text = 'Średnie hasło';
        }
    } else if (password.length >= 6) {
        strength = 'medium';
        text = 'Średnie hasło';
    }
    
    // Update strength bar
    strengthBar.className = `strength-bar-fill ${strength}`;
    strengthText.textContent = text;
}

/**
 * Handle registration form submission
 * @param {Event} e - Form submit event
 */
async function handleRegister(e) {
    e.preventDefault();
    
    const username = document.getElementById('username').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    const confirmPassword = document.getElementById('confirmPassword').value.trim();
    const submitBtn = document.querySelector('.register-btn');
    const alertDiv = document.getElementById('alert');
    
    // Validation
    if (!username || !email || !password || !confirmPassword) {
        showAlert('Proszę wypełnić wszystkie pola', 'error');
        return;
    }
    
    if (username.length < 3) {
        showAlert('Nazwa użytkownika musi mieć co najmniej 3 znaki', 'error');
        return;
    }
    
    if (password.length < 6) {
        showAlert('Hasło musi mieć co najmniej 6 znaków', 'error');
        return;
    }
    
    // Disable button and show loading state
    submitBtn.disabled = true;
    submitBtn.classList.add('loading');
    alertDiv.classList.remove('show');
    
    try {
        const response = await fetch('/api/v1/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: username,
                email: email,
                password: password,
                confirmPassword: confirmPassword
            })
        });
        
        const data = await response.json();
        
        if (!response.ok) {
            throw new Error(data.message || 'Rejestracja nie powiodła się');
        }
        
        // Store tokens
        authUtils.setToken(data.access_token, data.expires_in);
        authUtils.setRefreshToken(data.refresh_token);
        
        // Show success message
        showAlert('Rejestracja pomyślna! Przekierowywanie...', 'success');
        
        // Redirect to dashboard after a short delay
        setTimeout(() => {
            window.location.href = '/admin';
        }, 1000);
        
    } catch (error) {
        console.error('Registration error:', error);
        showAlert(error.message || 'Błąd podczas rejestracji. Spróbuj ponownie.', 'error');
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
