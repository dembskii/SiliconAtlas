/**
 * WebSocket Notification Service
 * Obsługuje połączenie do WebSocket i wyświetlanie notyfikacji
 */

class NotificationService {
    constructor() {
        this.stompClient = null;
        this.isConnected = false;
        this.reconnectAttempts = 0;
        this.maxReconnectAttempts = 5;
        this.reconnectDelay = 3000;
    }

    /**
     * Inicjalizuje połączenie WebSocket
     */
    init() {
        console.log('Initializing WebSocket Notification Service...');
        
        const socket = new SockJS('/ws/events');
        this.stompClient = Stomp.over(socket);

        this.stompClient.connect(
            {},
            (frame) => this.onConnect(frame),
            (error) => this.onError(error)
        );
    }

    /**
     * Handler dla pomyślnego połączenia
     */
    onConnect(frame) {
        console.log('WebSocket connected:', frame);
        this.isConnected = true;
        this.reconnectAttempts = 0;

        // Subskrybuj na CPU eventy
        this.stompClient.subscribe('/topic/cpu-events', (message) => {
            const event = JSON.parse(message.body);
            this.handleCpuEvent(event);
        });

        // Subskrybuj na Technology eventy
        this.stompClient.subscribe('/topic/technology-events', (message) => {
            const event = JSON.parse(message.body);
            this.handleTechnologyEvent(event);
        });

        // Subskrybuj na Manufacturer eventy
        this.stompClient.subscribe('/topic/manufacturer-events', (message) => {
            const event = JSON.parse(message.body);
            this.handleManufacturerEvent(event);
        });

        // Subskrybuj na wszystkie eventy
        this.stompClient.subscribe('/topic/all-events', (message) => {
            const event = JSON.parse(message.body);
            console.log('All-events received:', event);
        });

        this.showNotification('Połączono z serwerem', 'success');
    }

    /**
     * Handler dla błędów WebSocket
     */
    onError(error) {
        console.error('WebSocket error:', error);
        this.isConnected = false;

        if (this.reconnectAttempts < this.maxReconnectAttempts) {
            this.reconnectAttempts++;
            console.log(
                `Próba ponownego połączenia (${this.reconnectAttempts}/${this.maxReconnectAttempts}) za ${this.reconnectDelay}ms...`
            );
            setTimeout(() => this.init(), this.reconnectDelay);
        } else {
            this.showNotification(
                'Nie udało się połączyć z serwerem. Odśwież stronę.',
                'error'
            );
        }
    }

    /**
     * Obsługuje event CPU
     */
    handleCpuEvent(event) {
        console.log('CPU Event received:', event);

        let message = '';
        let type = 'info';

        switch (event.eventType) {
            case 'CREATE':
                message = `✨ Dodano nowy CPU: ${event.cpuModel}`;
                type = 'success';
                break;
            case 'UPDATE':
                message = `✏️ Zaktualizowano CPU: ${event.cpuModel}`;
                type = 'info';
                break;
            case 'DELETE':
                message = `🗑️ Usunięto CPU: ${event.cpuModel}`;
                type = 'warning';
                break;
            default:
                message = `Event CPU: ${event.eventType}`;
        }

        this.showNotification(message, type);
    }

    /**
     * Obsługuje event Technology
     */
    handleTechnologyEvent(event) {
        console.log('Technology Event received:', event);

        let message = '';
        let type = 'info';

        switch (event.eventType) {
            case 'CREATE':
                message = `✨ Dodana nowa technologia: ${event.technologyName}`;
                type = 'success';
                break;
            case 'UPDATE':
                message = `✏️ Zaktualizowano technologię: ${event.technologyName}`;
                type = 'info';
                break;
            case 'DELETE':
                message = `🗑️ Usunięto technologię: ${event.technologyName}`;
                type = 'warning';
                break;
            default:
                message = `Event Technology: ${event.eventType}`;
        }

        this.showNotification(message, type);
    }

    /**
     * Obsługuje event Manufacturer
     */
    handleManufacturerEvent(event) {
        console.log('Manufacturer Event received:', event);

        let message = '';
        let type = 'info';

        switch (event.eventType) {
            case 'CREATE':
                message = `✨ Dodany nowy producent: ${event.manufacturerName}`;
                type = 'success';
                break;
            case 'UPDATE':
                message = `✏️ Zaktualizowano producenta: ${event.manufacturerName}`;
                type = 'info';
                break;
            case 'DELETE':
                message = `🗑️ Usunięto producenta: ${event.manufacturerName}`;
                type = 'warning';
                break;
            default:
                message = `Event Manufacturer: ${event.eventType}`;
        }

        this.showNotification(message, type);
    }

    /**
     * Wyświetla notyfikację na ekranie
     */
    showNotification(message, type = 'info') {
        // Stwórz kontener na notyfikacje, jeśli go nie ma
        let container = document.getElementById('notification-container');
        if (!container) {
            container = document.createElement('div');
            container.id = 'notification-container';
            container.className = 'notification-container';
            document.body.appendChild(container);
        }

        // Stwórz element notyfikacji
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;

        // Dodaj do kontenera
        container.appendChild(notification);

        // Usuń notyfikację po 5 sekundach
        setTimeout(() => {
            notification.classList.add('notification-exit');
            setTimeout(() => notification.remove(), 300);
        }, 5000);
    }

    /**
     * Zamyka połączenie WebSocket
     */
    disconnect() {
        if (this.stompClient && this.stompClient.connected) {
            this.stompClient.disconnect(() => {
                console.log('WebSocket disconnected');
                this.isConnected = false;
            });
        }
    }
}

// Inicjalizuj serwis po załadowaniu strony
document.addEventListener('DOMContentLoaded', () => {
    const notificationService = new NotificationService();
    notificationService.init();

    // Opcjonalnie: Zamknij połączenie gdy opuszcisz stronę
    window.addEventListener('beforeunload', () => {
        notificationService.disconnect();
    });
});
