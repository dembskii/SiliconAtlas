/**
 * Authentication Utilities
 * Handles cookie management, token storage, and API calls with authentication
 */

class AuthUtils {
    constructor() {
        this.TOKEN_KEY = 'access_token';
        this.REFRESH_TOKEN_KEY = 'refresh_token';
        this.TOKEN_EXPIRY_KEY = 'token_expiry';
        this.COOKIE_MAX_AGE = 7 * 24 * 60 * 60; // 7 days in seconds
    }

    /**
     * Set authentication token in cookie and localStorage
     * @param {string} token - JWT token
     * @param {number} expiresIn - Expiration time in milliseconds
     */
    setToken(token, expiresIn = 86400000) {
        const expiryDate = new Date();
        expiryDate.setTime(expiryDate.getTime() + expiresIn);
        
        // Store in cookie
        document.cookie = `${this.TOKEN_KEY}=${token}; path=/; expires=${expiryDate.toUTCString()}; SameSite=Lax`;
        
        // Store in localStorage as backup
        localStorage.setItem(this.TOKEN_KEY, token);
        localStorage.setItem(this.TOKEN_EXPIRY_KEY, expiryDate.getTime().toString());
    }

    /**
     * Set refresh token in cookie
     * @param {string} token - Refresh token
     */
    setRefreshToken(token) {
        const expiryDate = new Date();
        expiryDate.setTime(expiryDate.getTime() + this.COOKIE_MAX_AGE * 1000);
        
        document.cookie = `${this.REFRESH_TOKEN_KEY}=${token}; path=/; expires=${expiryDate.toUTCString()}; SameSite=Lax`;
        localStorage.setItem(this.REFRESH_TOKEN_KEY, token);
    }

    /**
     * Get authentication token from cookie or localStorage
     * @returns {string|null} Token or null if not found
     */
    getToken() {
        // Try to get from cookies first
        let token = this.getCookie(this.TOKEN_KEY);
        
        // Fallback to localStorage
        if (!token) {
            token = localStorage.getItem(this.TOKEN_KEY);
        }
        
        return token;
    }

    /**
     * Get refresh token
     * @returns {string|null} Refresh token or null if not found
     */
    getRefreshToken() {
        let token = this.getCookie(this.REFRESH_TOKEN_KEY);
        
        if (!token) {
            token = localStorage.getItem(this.REFRESH_TOKEN_KEY);
        }
        
        return token;
    }

    /**
     * Get cookie value by name
     * @param {string} name - Cookie name
     * @returns {string|null} Cookie value or null
     */
    getCookie(name) {
        const nameEQ = name + "=";
        const cookies = document.cookie.split(';');
        
        for (let cookie of cookies) {
            cookie = cookie.trim();
            if (cookie.indexOf(nameEQ) === 0) {
                return cookie.substring(nameEQ.length);
            }
        }
        
        return null;
    }

    /**
     * Check if token is expired
     * @returns {boolean} True if expired or not found
     */
    isTokenExpired() {
        const expiry = localStorage.getItem(this.TOKEN_EXPIRY_KEY);
        
        if (!expiry) {
            return true;
        }
        
        return new Date().getTime() > parseInt(expiry);
    }

    /**
     * Clear all authentication tokens
     */
    clearToken() {
        // Clear cookies
        document.cookie = `${this.TOKEN_KEY}=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC;`;
        document.cookie = `${this.REFRESH_TOKEN_KEY}=; path=/; expires=Thu, 01 Jan 1970 00:00:00 UTC;`;
        
        // Clear localStorage
        localStorage.removeItem(this.TOKEN_KEY);
        localStorage.removeItem(this.REFRESH_TOKEN_KEY);
        localStorage.removeItem(this.TOKEN_EXPIRY_KEY);
    }

    /**
     * Check if user is authenticated
     * @returns {boolean} True if token exists and is not expired
     */
    isAuthenticated() {
        const token = this.getToken();
        return token && token.length > 0 && !this.isTokenExpired();
    }

    /**
     * Make authenticated API call
     * @param {string} url - API endpoint URL
     * @param {object} options - Fetch options
     * @returns {Promise<Response>} Fetch response
     */
    async fetch(url, options = {}) {
        const token = this.getToken();
        
        // Setup headers
        if (!options.headers) {
            options.headers = {};
        }
        
        // Add authorization header
        if (token) {
            options.headers['Authorization'] = `Bearer ${token}`;
        }
        
        // Add content type for non-GET requests
        if (options.method && options.method !== 'GET' && !options.headers['Content-Type']) {
            options.headers['Content-Type'] = 'application/json';
        }
        
        const response = await fetch(url, options);
        
        // Handle 401 Unauthorized - token might be expired
        if (response.status === 401) {
            const refreshToken = this.getRefreshToken();
            
            if (refreshToken) {
                try {
                    await this.refreshAccessToken(refreshToken);
                    // Retry the original request with new token
                    return this.fetch(url, options);
                } catch (error) {
                    console.error('Token refresh failed:', error);
                    this.clearToken();
                    window.location.href = '/login';
                }
            } else {
                this.clearToken();
                window.location.href = '/login';
            }
        }
        
        return response;
    }

    /**
     * Refresh access token
     * @param {string} refreshToken - Refresh token
     * @returns {Promise<void>}
     */
    async refreshAccessToken(refreshToken) {
        const response = await fetch('/api/v1/auth/refresh', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ refresh_token: refreshToken })
        });
        
        if (!response.ok) {
            throw new Error('Failed to refresh token');
        }
        
        const data = await response.json();
        this.setToken(data.access_token, data.expires_in);
        this.setRefreshToken(data.refresh_token);
    }
}

// Create global instance
const authUtils = new AuthUtils();

// Auto-redirect to login if accessing protected pages without authentication
document.addEventListener('DOMContentLoaded', function() {
    const currentPath = window.location.pathname;
    const publicPaths = ['/login', '/register', '/api/v1/auth'];
    
    const isPublicPath = publicPaths.some(path => currentPath.startsWith(path));
    
    if (!isPublicPath && !authUtils.isAuthenticated()) {
        window.location.href = '/login';
    }
});
