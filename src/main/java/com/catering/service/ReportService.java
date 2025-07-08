package com.catering.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class for generating reports and dashboard data
 */
@Service
public class ReportService {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private ResourceService resourceService;
    
    @Autowired
    private ClientService clientService;
    
    /**
     * Get dashboard summary data
     */
    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();
        
        // Today's orders
        summary.put("todaysOrders", orderService.getTodaysOrders());
        summary.put("todaysOrdersCount", orderService.getTodaysOrders().size());
        
        // Pending orders
        summary.put("pendingOrdersCount", orderService.getPendingOrdersCount());
        
        // Pending tasks
        summary.put("pendingTasksCount", taskService.getPendingTasksCount());
        
        // Today's assignments
        summary.put("todaysAssignments", employeeService.getTodaysAssignments());
        
        // Low stock resources
        summary.put("lowStockResources", resourceService.getLowStockResources());
        summary.put("lowStockCount", resourceService.getLowStockResources().size());
        
        // Urgent tasks for today
        summary.put("urgentTasksToday", taskService.getUrgentTasksForToday());
        
        // Outstanding client balances
        summary.put("totalOutstandingBalance", clientService.getTotalOutstandingBalance());
        
        return summary;
    }
    
    /**
     * Get monthly report
     */
    public Map<String, Object> getMonthlyReport(int month, int year) {
        Map<String, Object> report = new HashMap<>();
        
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        
        // Monthly earnings
        BigDecimal monthlyEarnings = orderService.getMonthlyEarnings(month, year);
        report.put("monthlyEarnings", monthlyEarnings);
        
        // Orders in month
        report.put("ordersInMonth", orderService.getOrdersByDateRange(startDate, endDate));
        
        // Completed orders count
        report.put("completedOrdersCount", orderService.getCompletedOrdersCount(startDate, endDate));
        
        // Completed tasks count
        report.put("completedTasksCount", taskService.getCompletedTasksCount(startDate, endDate));
        
        // Employee assignments summary
        report.put("employeeAssignmentsSummary", getEmployeeAssignmentsSummary(startDate, endDate));
        
        return report;
    }
    
    /**
     * Get employee performance report
     */
    public Map<String, Object> getEmployeePerformanceReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        
        // All active employees
        report.put("activeEmployees", employeeService.getActiveEmployees());
        
        // Employee assignments in date range
        report.put("employeeAssignments", getEmployeeAssignmentsSummary(startDate, endDate));
        
        // Pending payments
        report.put("pendingPayments", employeeService.getPendingPayments());
        
        return report;
    }
    
    /**
     * Get resource utilization report
     */
    public Map<String, Object> getResourceUtilizationReport() {
        Map<String, Object> report = new HashMap<>();
        
        // All active resources
        report.put("activeResources", resourceService.getActiveResources());
        
        // Low stock resources
        report.put("lowStockResources", resourceService.getLowStockResources());
        
        // Resources needing restock
        report.put("resourcesNeedingRestock", resourceService.getResourcesNeedingRestock());
        
        // Unreturned assignments
        report.put("unreturnedAssignments", resourceService.getUnreturnedAssignments());
        
        // Resource utilization summary
        report.put("utilizationSummary", resourceService.getResourceUtilizationSummary());
        
        return report;
    }
    
    /**
     * Get financial report
     */
    public Map<String, Object> getFinancialReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        
        // Orders in date range
        var orders = orderService.getOrdersByDateRange(startDate, endDate);
        report.put("orders", orders);
        
        // Calculate total revenue
        BigDecimal totalRevenue = orders.stream()
            .filter(order -> order.getTotalAmount() != null)
            .map(order -> order.getTotalAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.put("totalRevenue", totalRevenue);
        
        // Orders with pending payments
        report.put("ordersWithPendingPayments", orderService.getOrdersWithPendingPayments());
        
        // Client balances
        report.put("clientsWithOutstandingBalance", clientService.getClientsWithOutstandingBalance());
        report.put("totalOutstandingBalance", clientService.getTotalOutstandingBalance());
        
        // Employee pending payments
        report.put("employeePendingPayments", employeeService.getPendingPayments());
        
        return report;
    }
    
    /**
     * Get task management report
     */
    public Map<String, Object> getTaskManagementReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        
        // Tasks in date range
        report.put("tasksInRange", taskService.getTasksByDateRange(startDate, endDate));
        
        // Pending tasks
        report.put("pendingTasks", taskService.getPendingTasks());
        
        // Overdue tasks
        report.put("overdueTasks", taskService.getOverdueTasks());
        
        // Completed tasks count
        report.put("completedTasksCount", taskService.getCompletedTasksCount(startDate, endDate));
        
        // Today's tasks
        report.put("todaysTasks", taskService.getTodaysTasks());
        
        return report;
    }
    
    /**
     * Get customer analysis report
     */
    public Map<String, Object> getCustomerAnalysisReport() {
        Map<String, Object> report = new HashMap<>();
        
        // All customers
        report.put("allCustomers", orderService.getAllOrders().stream()
            .map(order -> order.getCustomer())
            .distinct()
            .toList());
        
        // Repeat customers
        var customerService = new com.catering.service.CustomerService();
        // Note: This would need proper injection in real implementation
        
        return report;
    }
    
    /**
     * Helper method to get employee assignments summary
     */
    private Map<String, Object> getEmployeeAssignmentsSummary(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> summary = new HashMap<>();
        
        var activeEmployees = employeeService.getActiveEmployees();
        
        for (var employee : activeEmployees) {
            Map<String, Object> employeeData = new HashMap<>();
            
            // Assignment count
            Long assignmentCount = employeeService.getEmployeeAssignmentCountForDate(employee.getId(), startDate);
            employeeData.put("assignmentCount", assignmentCount);
            
            // Total payment
            BigDecimal totalPayment = employeeService.getTotalPaymentForEmployeeInDateRange(
                employee.getId(), startDate, endDate);
            employeeData.put("totalPayment", totalPayment);
            
            // Pending payment
            BigDecimal pendingPayment = employeeService.getTotalPendingPaymentForEmployee(employee.getId());
            employeeData.put("pendingPayment", pendingPayment);
            
            summary.put(employee.getName(), employeeData);
        }
        
        return summary;
    }
}

