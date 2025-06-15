package com.strathmore.grocery.services;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order updateOrder(Long id, Order updatedOrder) {
        Order order = getOrderById(id);
        order.setCustomerName(updatedOrder.getCustomerName());
        order.setOrderDate(updatedOrder.getOrderDate());
        order.setTotalAmount(updatedOrder.getTotalAmount());
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public Page<Order> searchOrders(String customerName, int page, int size) {
        return orderRepository.findByCustomerNameContainingIgnoreCase(customerName, PageRequest.of(page, size));
    }

    public Page<Order> getAllOrders(int page, int size) {
        return orderRepository.findAll(PageRequest.of(page, size));
    }
}
