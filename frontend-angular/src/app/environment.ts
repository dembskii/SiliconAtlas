const currentHost = typeof window !== 'undefined' ? window.location.hostname : 'localhost';

export const environment = {
  apiUrl: `http://${currentHost}:8080/api/v1`
};
