public interface PaymentMethod {
    /**
     * Processes a payment with the specified amount and full address.
     * 
     * @param amount      The amount to be paid.
     * @param fullAddress The full address associated with the payment.
     * @return A Receipt object representing the receipt of the processed payment.
     */
    Receipt processPayment(double amount, Address fullAddress);
}