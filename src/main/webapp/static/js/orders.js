// Orders page JavaScript functionality

let allOrders = [];
let filteredOrders = [];
let customers = [];

document.addEventListener('DOMContentLoaded', function() {
    loadOrdersData();
    setupEventListeners();
});

// Load orders and related data
async function loadOrdersData() {
    try {
        // Load orders and customers in parallel
        const [ordersResponse, customersResponse] = await Promise.all([
            API.get('/orders'),
            API.get('/customers')
        ]);
        
        allOrders = ordersResponse;
        customers = customersResponse;
        filteredOrders = [...allOrders];
        
        renderOrdersTable();
        updateOrdersCount();
        populateCustomerSelect();
        
    } catch (error) {
        console.error('Error loading orders data:', error);
        Utils.showError('ordersTableBody', 'Failed to load orders data');
        Utils.showNotification('Error loading orders data', 'error');
    }
}

// Render orders table
function renderOrdersTable() {
    const tbody = document.getElementById('ordersTableBody');
    
    if (filteredOrders.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="10" class="empty-table">
                    <div class="empty-table-icon">📋</div>
                    <p>No orders found</p>
                </td>
            </tr>
        `;
        return;
    }
    
    tbody.innerHTML = filteredOrders.map(order => `
        <tr>
            <td><strong>#${order.id}</strong></td>
            <td>
                <div>
                    <strong>${order.customer?.name || 'Unknown'}</strong>
                    <br>
                    <small class="text-muted">${order.customer?.phoneNumber || 'No phone'}</small>
                </div>
            </td>
            <td>${Utils.formatDate(order.eventDate)}</td>
            <td>
                <div title="${order.eventVenue}">
                    ${order.eventVenue.length > 30 ? order.eventVenue.substring(0, 30) + '...' : order.eventVenue}
                </div>
            </td>
            <td>
                <span class="status-badge ${order.orderType?.toLowerCase().replace('_', '-')}">
                    ${Utils.formatEnumValue(order.orderType)}
                </span>
            </td>
            <td>${order.guestCount || '-'}</td>
            <td>
                <div>
                    <strong>${Utils.formatCurrency(order.totalAmount)}</strong>
                    ${order.advanceAmount ? `<br><small class="text-muted">Advance: ${Utils.formatCurrency(order.advanceAmount)}</small>` : ''}
                </div>
            </td>
            <td>${getStatusBadge(order.orderStatus)}</td>
            <td>${getStatusBadge(order.paymentStatus, 'payment')}</td>
            <td>
                <div class="action-buttons">
                    <button class="action-btn-sm btn-primary" onclick="editOrder(${order.id})" title="Edit Order">
                        ✏️
                    </button>
                    <button class="action-btn-sm btn-info" onclick="viewOrderDetails(${order.id})" title="View Details">
                        👁️
                    </button>
                    ${order.orderStatus === 'PENDING' ? `
                        <button class="action-btn-sm btn-success" onclick="confirmOrder(${order.id})" title="Confirm Order">
                            ✅
                        </button>
                    ` : ''}
                    ${order.orderStatus === 'CONFIRMED' || order.orderStatus === 'IN_PROGRESS' ? `
                        <button class="action-btn-sm btn-success" onclick="completeOrder(${order.id})" title="Complete Order">
                            🏁
                        </button>
                    ` : ''}
                    ${order.orderStatus !== 'COMPLETED' && order.orderStatus !== 'CANCELLED' ? `
                        <button class="action-btn-sm btn-danger" onclick="cancelOrder(${order.id})" title="Cancel Order">
                            ❌
                        </button>
                    ` : ''}
                </div>
            </td>
        </tr>
    `).join('');
}

// Update orders count
function updateOrdersCount() {
    const countElement = document.getElementById('ordersCount');
    if (countElement) {
        countElement.textContent = `${filteredOrders.length} of ${allOrders.length} orders`;
    }
}

// Populate customer select dropdown
function populateCustomerSelect() {
    const select = document.getElementById('customerSelect');
    if (select) {
        select.innerHTML = '<option value="">Select Customer</option>' +
            customers.map(customer => 
                `<option value="${customer.id}">${customer.name} - ${customer.phoneNumber || 'No phone'}</option>`
            ).join('');
    }
}

// Setup event listeners
function setupEventListeners() {
    // Search functionality
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('input', Utils.debounce(handleSearch, 300));
    }
    
    // Filter functionality
    const statusFilter = document.getElementById('statusFilter');
    const typeFilter = document.getElementById('typeFilter');
    const dateFilter = document.getElementById('dateFilter');
    
    if (statusFilter) statusFilter.addEventListener('change', handleFilter);
    if (typeFilter) typeFilter.addEventListener('change', handleFilter);
    if (dateFilter) dateFilter.addEventListener('change', handleFilter);
    
    // Form submissions
    const newOrderForm = document.getElementById('newOrderForm');
    const editOrderForm = document.getElementById('editOrderForm');
    
    if (newOrderForm) newOrderForm.addEventListener('submit', handleNewOrderSubmit);
    if (editOrderForm) editOrderForm.addEventListener('submit', handleEditOrderSubmit);
    
    // Set default date to today
    const eventDateInput = document.getElementById('eventDate');
    if (eventDateInput) {
        eventDateInput.value = Utils.getTodayDate();
    }
}

// Handle search
function handleSearch(event) {
    const searchTerm = event.target.value.toLowerCase().trim();
    
    if (!searchTerm) {
        filteredOrders = [...allOrders];
    } else {
        filteredOrders = allOrders.filter(order => 
            order.id.toString().includes(searchTerm) ||
            order.customer?.name?.toLowerCase().includes(searchTerm) ||
            order.eventVenue?.toLowerCase().includes(searchTerm) ||
            order.customer?.phoneNumber?.includes(searchTerm)
        );
    }
    
    applyFilters();
}

// Handle filters
function handleFilter() {
    applyFilters();
}

// Apply all filters
function applyFilters() {
    const statusFilter = document.getElementById('statusFilter').value;
    const typeFilter = document.getElementById('typeFilter').value;
    const dateFilter = document.getElementById('dateFilter').value;
    
    let filtered = [...filteredOrders];
    
    if (statusFilter) {
        filtered = filtered.filter(order => order.orderStatus === statusFilter);
    }
    
    if (typeFilter) {
        filtered = filtered.filter(order => order.orderType === typeFilter);
    }
    
    if (dateFilter) {
        filtered = filtered.filter(order => order.eventDate === dateFilter);
    }
    
    filteredOrders = filtered;
    renderOrdersTable();
    updateOrdersCount();
}

// Clear all filters
function clearFilters() {
    document.getElementById('searchInput').value = '';
    document.getElementById('statusFilter').value = '';
    document.getElementById('typeFilter').value = '';
    document.getElementById('dateFilter').value = '';
    
    filteredOrders = [...allOrders];
    renderOrdersTable();
    updateOrdersCount();
}

// Show new order modal
function showNewOrderModal() {
    showModal('newOrderModal');
}

// Show new customer form
function showNewCustomerForm() {
    const form = document.getElementById('newCustomerForm');
    const select = document.getElementById('customerSelect');
    
    if (form.style.display === 'none') {
        form.style.display = 'block';
        select.style.display = 'none';
        select.required = false;
        document.getElementById('customerName').required = true;
    } else {
        form.style.display = 'none';
        select.style.display = 'block';
        select.required = true;
        document.getElementById('customerName').required = false;
    }
}

// Handle new order form submission
async function handleNewOrderSubmit(event) {
    event.preventDefault();
    
    try {
        let customerId = document.getElementById('customerSelect').value;
        
        // Create new customer if needed
        if (!customerId && document.getElementById('newCustomerForm').style.display !== 'none') {
            const customerData = {
                name: document.getElementById('customerName').value,
                phoneNumber: document.getElementById('customerPhone').value,
                email: document.getElementById('customerEmail').value,
                address: document.getElementById('customerAddress').value,
                customerType: document.getElementById('customerType').value
            };
            
            const newCustomer = await API.post('/customers', customerData);
            customerId = newCustomer.id;
            customers.push(newCustomer);
            populateCustomerSelect();
        }
        
        const orderData = {
            customerId: parseInt(customerId),
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
        
        const newOrder = await API.post('/orders', orderData);
        
        // Update amounts if provided
        const totalAmount = parseFloat(document.getElementById('totalAmount').value);
        const advanceAmount = parseFloat(document.getElementById('advanceAmount').value);
        
        if (totalAmount || advanceAmount) {
            const balanceAmount = (totalAmount || 0) - (advanceAmount || 0);
            await API.put(`/orders/${newOrder.id}/amounts`, {
                totalAmount: totalAmount || null,
                advanceAmount: advanceAmount || null,
                balanceAmount: balanceAmount > 0 ? balanceAmount : null
            });
        }
        
        Utils.showSuccess('Order created successfully!');
        closeModal('newOrderModal');
        loadOrdersData(); // Refresh data
        
    } catch (error) {
        console.error('Error creating order:', error);
        Utils.showNotification('Error creating order', 'error');
    }
}

// Edit order
async function editOrder(orderId) {
    try {
        const order = await API.get(`/orders/${orderId}`);
        
        // Populate edit form
        document.getElementById('editOrderId').value = order.id;
        document.getElementById('editEventDate').value = Utils.formatDateForInput(order.eventDate);
        document.getElementById('editEventVenue').value = order.eventVenue;
        document.getElementById('editOrderType').value = order.orderType;
        document.getElementById('editGuestCount').value = order.guestCount || '';
        document.getElementById('editTotalAmount').value = order.totalAmount || '';
        document.getElementById('editAdvanceAmount').value = order.advanceAmount || '';
        document.getElementById('editBalanceAmount').value = order.balanceAmount || '';
        document.getElementById('editOrderStatus').value = order.orderStatus;
        document.getElementById('editPaymentStatus').value = order.paymentStatus;
        document.getElementById('editOrderNotes').value = order.notes || '';
        
        showModal('editOrderModal');
        
    } catch (error) {
        console.error('Error loading order for edit:', error);
        Utils.showNotification('Error loading order details', 'error');
    }
}

// Handle edit order form submission
async function handleEditOrderSubmit(event) {
    event.preventDefault();
    
    try {
        const orderId = document.getElementById('editOrderId').value;
        const orderData = {
            eventDate: document.getElementById('editEventDate').value,
            eventVenue: document.getElementById('editEventVenue').value,
            orderType: document.getElementById('editOrderType').value,
            guestCount: parseInt(document.getElementById('editGuestCount').value) || null,
            totalAmount: parseFloat(document.getElementById('editTotalAmount').value) || null,
            advanceAmount: parseFloat(document.getElementById('editAdvanceAmount').value) || null,
            balanceAmount: parseFloat(document.getElementById('editBalanceAmount').value) || null,
            orderStatus: document.getElementById('editOrderStatus').value,
            paymentStatus: document.getElementById('editPaymentStatus').value,
            notes: document.getElementById('editOrderNotes').value
        };
        
        await API.put(`/orders/${orderId}`, orderData);
        
        Utils.showSuccess('Order updated successfully!');
        closeModal('editOrderModal');
        loadOrdersData(); // Refresh data
        
    } catch (error) {
        console.error('Error updating order:', error);
        Utils.showNotification('Error updating order', 'error');
    }
}

// Confirm order
async function confirmOrder(orderId) {
    if (confirm('Are you sure you want to confirm this order?')) {
        try {
            await API.put(`/orders/${orderId}/status`, { status: 'CONFIRMED' });
            Utils.showSuccess('Order confirmed successfully!');
            loadOrdersData();
        } catch (error) {
            console.error('Error confirming order:', error);
            Utils.showNotification('Error confirming order', 'error');
        }
    }
}

// Complete order
async function completeOrder(orderId) {
    if (confirm('Are you sure you want to mark this order as completed?')) {
        try {
            await API.put(`/orders/${orderId}/complete`);
            Utils.showSuccess('Order completed successfully!');
            loadOrdersData();
        } catch (error) {
            console.error('Error completing order:', error);
            Utils.showNotification('Error completing order', 'error');
        }
    }
}

// Cancel order
async function cancelOrder(orderId) {
    if (confirm('Are you sure you want to cancel this order? This action cannot be undone.')) {
        try {
            await API.put(`/orders/${orderId}/cancel`);
            Utils.showSuccess('Order cancelled successfully!');
            loadOrdersData();
        } catch (error) {
            console.error('Error cancelling order:', error);
            Utils.showNotification('Error cancelling order', 'error');
        }
    }
}

// View order details
function viewOrderDetails(orderId) {
    // For now, redirect to a details page or show a modal
    // This could be expanded to show a detailed view
    window.open(`order-details.html?id=${orderId}`, '_blank');
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

// Get status badge with appropriate styling
function getStatusBadge(status, type = 'order') {
    if (!status) return '';
    
    let className = 'status-badge ';
    
    if (type === 'payment') {
        switch (status) {
            case 'ADVANCE':
                className += 'status-warning';
                break;
            case 'BALANCE':
                className += 'status-pending';
                break;
            case 'COMPLETE':
                className += 'status-confirmed';
                break;
            default:
                className += 'status-pending';
        }
    } else {
        switch (status) {
            case 'PENDING':
                className += 'status-pending';
                break;
            case 'CONFIRMED':
                className += 'status-confirmed';
                break;
            case 'IN_PROGRESS':
                className += 'status-medium';
                break;
            case 'COMPLETED':
                className += 'status-confirmed';
                break;
            case 'CANCELLED':
                className += 'status-cancelled';
                break;
            default:
                className += 'status-pending';
        }
    }
    
    return `<span class="${className}">${Utils.formatEnumValue(status)}</span>`;
}

