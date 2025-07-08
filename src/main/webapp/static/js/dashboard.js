// Dashboard JavaScript functionality

document.addEventListener('DOMContentLoaded', function() {
    loadDashboardData();
    setupEventListeners();
});

// Load all dashboard data
async function loadDashboardData() {
    try {
        // Load dashboard summary
        const summary = await API.get('/reports/dashboard');
        updateDashboardSummary(summary);
        
        // Load today's orders
        loadTodaysOrders(summary.todaysOrders || []);
        
        // Load today's assignments
        loadTodaysAssignments(summary.todaysAssignments || []);
        
        // Load urgent tasks
        loadUrgentTasks(summary.urgentTasksToday || []);
        
        // Load low stock resources
        loadLowStockResources(summary.lowStockResources || []);
        
    } catch (error) {
        console.error('Error loading dashboard data:', error);
        Utils.showNotification('Error loading dashboard data', 'error');
    }
}

// Update dashboard summary cards
function updateDashboardSummary(summary) {
    // Update summary cards
    document.getElementById('todaysOrdersCount').textContent = summary.todaysOrdersCount || 0;
    document.getElementById('pendingOrdersCount').textContent = summary.pendingOrdersCount || 0;
    document.getElementById('pendingTasksCount').textContent = summary.pendingTasksCount || 0;
    document.getElementById('lowStockCount').textContent = summary.lowStockCount || 0;
}

// Load today's orders
function loadTodaysOrders(orders) {
    const container = document.getElementById('todaysOrdersList');
    
    if (!orders || orders.length === 0) {
        container.innerHTML = '<div class="empty-state"><div class="empty-state-icon">📋</div><p>No orders for today</p></div>';
        return;
    }
    
    container.innerHTML = orders.map(order => `
        <div class="order-item">
            <h4>${order.customer?.name || 'Unknown Customer'}</h4>
            <p><strong>Venue:</strong> ${order.eventVenue}</p>
            <p><strong>Type:</strong> ${Utils.formatEnumValue(order.orderType)}</p>
            <div class="item-meta">
                ${getStatusBadge(order.orderStatus)}
                <span class="text-muted">${order.guestCount || 0} guests</span>
            </div>
        </div>
    `).join('');
}

// Load today's assignments
function loadTodaysAssignments(assignments) {
    const container = document.getElementById('todaysAssignmentsList');
    
    if (!assignments || assignments.length === 0) {
        container.innerHTML = '<div class="empty-state"><div class="empty-state-icon">👥</div><p>No assignments for today</p></div>';
        return;
    }
    
    container.innerHTML = assignments.map(assignment => `
        <div class="assignment-item">
            <h4>${assignment.employee?.name || 'Unknown Employee'}</h4>
            <p><strong>Role:</strong> ${Utils.formatEnumValue(assignment.employee?.role)}</p>
            <p><strong>Order:</strong> ${assignment.order?.customer?.name || 'Unknown'}</p>
            <div class="item-meta">
                <span class="text-muted">${Utils.formatCurrency(assignment.paymentAmount)}</span>
                ${assignment.isPresent ? '<span class="status-badge status-confirmed">Present</span>' : '<span class="status-badge status-pending">Absent</span>'}
            </div>
        </div>
    `).join('');
}

// Load urgent tasks
function loadUrgentTasks(tasks) {
    const container = document.getElementById('urgentTasksList');
    
    if (!tasks || tasks.length === 0) {
        container.innerHTML = '<div class="empty-state"><div class="empty-state-icon">✅</div><p>No urgent tasks for today</p></div>';
        return;
    }
    
    container.innerHTML = tasks.map(task => `
        <div class="task-item priority-${task.priorityLevel?.toLowerCase()}">
            <h4>${task.title}</h4>
            <p>${task.description || 'No description'}</p>
            <div class="item-meta">
                ${getPriorityBadge(task.priorityLevel)}
                ${getStatusBadge(task.taskStatus)}
            </div>
        </div>
    `).join('');
}

// Load low stock resources
function loadLowStockResources(resources) {
    const container = document.getElementById('lowStockResourcesList');
    
    if (!resources || resources.length === 0) {
        container.innerHTML = '<div class="empty-state"><div class="empty-state-icon">📦</div><p>All resources are well stocked</p></div>';
        return;
    }
    
    container.innerHTML = resources.map(resource => {
        const stockLevel = resource.availableQuantity <= resource.minimumStock * 0.5 ? 'critical' : 'low';
        return `
            <div class="resource-item stock-${stockLevel}">
                <h4>${resource.name}</h4>
                <p><strong>Type:</strong> ${Utils.formatEnumValue(resource.resourceType)}</p>
                <p><strong>Available:</strong> ${resource.availableQuantity} ${resource.unitOfMeasure || ''}</p>
                <div class="item-meta">
                    <span class="text-muted">Min: ${resource.minimumStock}</span>
                    <span class="status-badge status-warning">Low Stock</span>
                </div>
            </div>
        `;
    }).join('');
}

// Setup event listeners
function setupEventListeners() {
    // New Order Form
    const newOrderForm = document.getElementById('newOrderForm');
    if (newOrderForm) {
        newOrderForm.addEventListener('submit', handleNewOrderSubmit);
        loadCustomersForSelect();
    }
    
    // New Task Form
    const newTaskForm = document.getElementById('newTaskForm');
    if (newTaskForm) {
        newTaskForm.addEventListener('submit', handleNewTaskSubmit);
        
        // Set default date to today
        const taskDateInput = document.getElementById('taskDate');
        if (taskDateInput) {
            taskDateInput.value = Utils.getTodayDate();
        }
    }
    
    // Set default event date to today
    const eventDateInput = document.getElementById('eventDate');
    if (eventDateInput) {
        eventDateInput.value = Utils.getTodayDate();
    }
}

// Load customers for order form
async function loadCustomersForSelect() {
    try {
        const customers = await API.get('/customers');
        const select = document.getElementById('customerSelect');
        
        if (select) {
            select.innerHTML = '<option value="">Select Customer</option>' +
                customers.map(customer => 
                    `<option value="${customer.id}">${customer.name} - ${customer.phoneNumber || 'No phone'}</option>`
                ).join('');
        }
    } catch (error) {
        console.error('Error loading customers:', error);
    }
}

// Handle new order form submission
async function handleNewOrderSubmit(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    const orderData = {
        customerId: parseInt(document.getElementById('customerSelect').value),
        eventDate: document.getElementById('eventDate').value,
        eventVenue: document.getElementById('eventVenue').value,
        orderType: document.getElementById('orderType').value,
        guestCount: parseInt(document.getElementById('guestCount').value) || null,
        notes: document.getElementById('orderNotes').value
    };
    
    // Validate form
    if (!validateOrderForm(orderData)) {
        return;
    }
    
    try {
        const newOrder = await API.post('/orders', orderData);
        Utils.showSuccess('Order created successfully!');
        closeModal('newOrderModal');
        
        // Refresh dashboard data
        setTimeout(() => {
            loadDashboardData();
        }, 1000);
        
    } catch (error) {
        console.error('Error creating order:', error);
        Utils.showNotification('Error creating order', 'error');
    }
}

// Handle new task form submission
async function handleNewTaskSubmit(event) {
    event.preventDefault();
    
    const taskData = {
        title: document.getElementById('taskTitle').value,
        description: document.getElementById('taskDescription').value,
        taskDate: document.getElementById('taskDate').value,
        priorityLevel: document.getElementById('taskPriority').value,
        orderId: null,
        employeeId: null
    };
    
    // Validate form
    if (!validateTaskForm(taskData)) {
        return;
    }
    
    try {
        const newTask = await API.post('/tasks', taskData);
        Utils.showSuccess('Task created successfully!');
        closeModal('newTaskModal');
        
        // Refresh dashboard data
        setTimeout(() => {
            loadDashboardData();
        }, 1000);
        
    } catch (error) {
        console.error('Error creating task:', error);
        Utils.showNotification('Error creating task', 'error');
    }
}

// Validate order form
function validateOrderForm(data) {
    if (!data.customerId) {
        Utils.showNotification('Please select a customer', 'error');
        return false;
    }
    
    if (!data.eventDate) {
        Utils.showNotification('Please select an event date', 'error');
        return false;
    }
    
    if (!data.eventVenue.trim()) {
        Utils.showNotification('Please enter an event venue', 'error');
        return false;
    }
    
    if (!data.orderType) {
        Utils.showNotification('Please select an order type', 'error');
        return false;
    }
    
    return true;
}

// Validate task form
function validateTaskForm(data) {
    if (!data.title.trim()) {
        Utils.showNotification('Please enter a task title', 'error');
        return false;
    }
    
    if (!data.taskDate) {
        Utils.showNotification('Please select a task date', 'error');
        return false;
    }
    
    return true;
}

// Quick action functions
function showNewOrderModal() {
    showModal('newOrderModal');
}

function showNewTaskModal() {
    showModal('newTaskModal');
}

function showNewEmployeeModal() {
    // Redirect to employees page with add mode
    window.location.href = 'employees.html?action=add';
}

function showNewResourceModal() {
    // Redirect to resources page with add mode
    window.location.href = 'resources.html?action=add';
}

// Refresh dashboard data every 5 minutes
setInterval(loadDashboardData, 5 * 60 * 1000);

