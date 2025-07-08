// Utility functions for the Catering Management System

// API Base URL
const API_BASE_URL = '/catering/api';

// Utility functions
const Utils = {
    // Format date for display
    formatDate: function(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    },

    // Format currency
    formatCurrency: function(amount) {
        if (!amount) return '₹0.00';
        return '₹' + parseFloat(amount).toLocaleString('en-IN', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });
    },

    // Format date for input fields
    formatDateForInput: function(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toISOString().split('T')[0];
    },

    // Get today's date in YYYY-MM-DD format
    getTodayDate: function() {
        return new Date().toISOString().split('T')[0];
    },

    // Capitalize first letter
    capitalize: function(str) {
        if (!str) return '';
        return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
    },

    // Format enum values for display
    formatEnumValue: function(value) {
        if (!value) return '';
        return value.replace(/_/g, ' ').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
    },

    // Show loading spinner
    showLoading: function(elementId) {
        const element = document.getElementById(elementId);
        if (element) {
            element.innerHTML = '<div class="loading"><div class="spinner"></div></div>';
        }
    },

    // Show error message
    showError: function(elementId, message) {
        const element = document.getElementById(elementId);
        if (element) {
            element.innerHTML = `<div class="error-message">Error: ${message}</div>`;
        }
    },

    // Show success message
    showSuccess: function(message) {
        this.showNotification(message, 'success');
    },

    // Show notification
    showNotification: function(message, type = 'info') {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;
        
        // Add styles
        notification.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 1rem 1.5rem;
            border-radius: 5px;
            color: white;
            font-weight: 600;
            z-index: 10000;
            animation: slideInRight 0.3s ease;
        `;
        
        // Set background color based on type
        switch(type) {
            case 'success':
                notification.style.backgroundColor = '#28a745';
                break;
            case 'error':
                notification.style.backgroundColor = '#dc3545';
                break;
            case 'warning':
                notification.style.backgroundColor = '#ffc107';
                notification.style.color = '#212529';
                break;
            default:
                notification.style.backgroundColor = '#17a2b8';
        }
        
        document.body.appendChild(notification);
        
        // Remove after 3 seconds
        setTimeout(() => {
            notification.remove();
        }, 3000);
    },

    // Validate email
    isValidEmail: function(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    },

    // Validate phone number
    isValidPhone: function(phone) {
        const phoneRegex = /^[+]?[\d\s\-\(\)]{10,}$/;
        return phoneRegex.test(phone);
    },

    // Debounce function for search
    debounce: function(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }
};

// API helper functions
const API = {
    // Generic GET request
    get: async function(endpoint) {
        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            console.error('API GET error:', error);
            throw error;
        }
    },

    // Generic POST request
    post: async function(endpoint, data) {
        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data)
            });
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            console.error('API POST error:', error);
            throw error;
        }
    },

    // Generic PUT request
    put: async function(endpoint, data) {
        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data)
            });
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            console.error('API PUT error:', error);
            throw error;
        }
    },

    // Generic DELETE request
    delete: async function(endpoint) {
        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                method: 'DELETE'
            });
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.status === 204 ? null : await response.json();
        } catch (error) {
            console.error('API DELETE error:', error);
            throw error;
        }
    }
};

// Modal functions
function showModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = 'block';
        document.body.style.overflow = 'hidden';
    }
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = 'none';
        document.body.style.overflow = 'auto';
        
        // Reset form if exists
        const form = modal.querySelector('form');
        if (form) {
            form.reset();
        }
    }
}

// Close modal when clicking outside
window.onclick = function(event) {
    if (event.target.classList.contains('modal')) {
        event.target.style.display = 'none';
        document.body.style.overflow = 'auto';
    }
}

// Status badge helper
function getStatusBadge(status, type = 'order') {
    const statusClass = `status-${status.toLowerCase().replace('_', '-')}`;
    const displayText = Utils.formatEnumValue(status);
    return `<span class="status-badge ${statusClass}">${displayText}</span>`;
}

// Priority badge helper
function getPriorityBadge(priority) {
    const priorityClass = `status-${priority.toLowerCase()}`;
    return `<span class="status-badge ${priorityClass}">${Utils.capitalize(priority)}</span>`;
}

// Table helper functions
function createTableRow(data, columns) {
    const row = document.createElement('tr');
    
    columns.forEach(column => {
        const cell = document.createElement('td');
        
        if (typeof column === 'string') {
            cell.textContent = data[column] || '';
        } else if (typeof column === 'object') {
            if (column.render) {
                cell.innerHTML = column.render(data);
            } else {
                cell.textContent = data[column.key] || '';
            }
        }
        
        row.appendChild(cell);
    });
    
    return row;
}

// Form validation helper
function validateForm(formId, rules) {
    const form = document.getElementById(formId);
    if (!form) return false;
    
    let isValid = true;
    const errors = [];
    
    Object.keys(rules).forEach(fieldName => {
        const field = form.querySelector(`[name="${fieldName}"], #${fieldName}`);
        const rule = rules[fieldName];
        
        if (!field) return;
        
        // Required validation
        if (rule.required && !field.value.trim()) {
            isValid = false;
            errors.push(`${rule.label || fieldName} is required`);
            field.classList.add('error');
        } else {
            field.classList.remove('error');
        }
        
        // Email validation
        if (rule.type === 'email' && field.value && !Utils.isValidEmail(field.value)) {
            isValid = false;
            errors.push(`${rule.label || fieldName} must be a valid email`);
            field.classList.add('error');
        }
        
        // Phone validation
        if (rule.type === 'phone' && field.value && !Utils.isValidPhone(field.value)) {
            isValid = false;
            errors.push(`${rule.label || fieldName} must be a valid phone number`);
            field.classList.add('error');
        }
        
        // Min length validation
        if (rule.minLength && field.value.length < rule.minLength) {
            isValid = false;
            errors.push(`${rule.label || fieldName} must be at least ${rule.minLength} characters`);
            field.classList.add('error');
        }
    });
    
    if (!isValid) {
        Utils.showNotification(errors.join(', '), 'error');
    }
    
    return isValid;
}

// Add CSS for error styling
const style = document.createElement('style');
style.textContent = `
    .error {
        border-color: #dc3545 !important;
        box-shadow: 0 0 0 0.2rem rgba(220, 53, 69, 0.25) !important;
    }
    
    .error-message {
        color: #dc3545;
        text-align: center;
        padding: 1rem;
        font-style: italic;
    }
    
    @keyframes slideInRight {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
`;
document.head.appendChild(style);

