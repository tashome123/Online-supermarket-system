package com.strathmore.grocery.services;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public Payment updatePayment(Long id, Payment updatedPayment) {
        Payment payment = getPaymentById(id);
        payment.setOrderId(updatedPayment.getOrderId());
        payment.setPaymentMethod(updatedPayment.getPaymentMethod());
        payment.setAmount(updatedPayment.getAmount());
        payment.setPaymentDate(updatedPayment.getPaymentDate());
        return paymentRepository.save(payment);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    public Page<Payment> searchPayments(String method, int page, int size) {
        return paymentRepository.findByPaymentMethodContainingIgnoreCase(method, PageRequest.of(page, size));
    }

    public Page<Payment> getAllPayments(int page, int size) {
        return paymentRepository.findAll(PageRequest.of(page, size));
    }
}

