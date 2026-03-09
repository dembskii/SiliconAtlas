/**
 * Fetch Interceptor
 * Automatically adds authentication token to all fetch requests
 */

(function() {
    // Store the original fetch function
    const originalFetch = window.fetch;
    
    // Override the global fetch function
    window.fetch = async function(...args) {
        let [resource, config = {}] = args;
        
        // Only add auth header for API calls, not for login/register endpoints
        const isAuthEndpoint = typeof resource === 'string' && 
                             (resource.includes('/api/v1/auth/login') || 
                              resource.includes('/api/v1/auth/register'));
        
        if (!isAuthEndpoint && authUtils && typeof authUtils.getToken === 'function') {
            const token = authUtils.getToken();
            
            if (token) {
                // Ensure headers object exists
                if (!config.headers) {
                    config.headers = {};
                }
                
                // Add authorization header
                config.headers['Authorization'] = `Bearer ${token}`;
            }
        }
        
        // Call the original fetch
        let response = await originalFetch(resource, config);
        
        // Handle 401 - token might be expired
        if (response.status === 401 && authUtils && typeof authUtils.getRefreshToken === 'function') {
            const refreshToken = authUtils.getRefreshToken();
            
            if (refreshToken && !isAuthEndpoint) {
                try {
                    // Try to refresh the token
                    const refreshResponse = await originalFetch('/api/v1/auth/refresh', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ refresh_token: refreshToken })
                    });
                    
                    if (refreshResponse.ok) {
                        const data = await refreshResponse.json();
                        
                        // Store new tokens
                        if (authUtils && typeof authUtils.setToken === 'function') {
                            authUtils.setToken(data.access_token, data.expires_in);
                            if (data.refresh_token) {
                                authUtils.setRefreshToken(data.refresh_token);
                            }
                            
                            // Retry original request with new token
                            if (!config.headers) {
                                config.headers = {};
                            }
                            config.headers['Authorization'] = `Bearer ${data.access_token}`;
                            
                            response = await originalFetch(resource, config);
                        }
                    } else {
                        // Refresh failed - redirect to login
                        if (authUtils && typeof authUtils.clearToken === 'function') {
                            authUtils.clearToken();
                        }
                        window.location.href = '/login';
                    }
                } catch (error) {
                    console.error('Token refresh error:', error);
                    if (authUtils && typeof authUtils.clearToken === 'function') {
                        authUtils.clearToken();
                    }
                    window.location.href = '/login';
                }
            } else if (!refreshToken && !isAuthEndpoint) {
                // No refresh token - redirect to login
                if (authUtils && typeof authUtils.clearToken === 'function') {
                    authUtils.clearToken();
                }
                window.location.href = '/login';
            }
        }
        
        return response;
    };
})();
