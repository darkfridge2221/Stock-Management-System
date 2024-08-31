import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Receipt {
    private double amount;
    private String paymentMethod;
    private Address address;


    public double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Address getAddress() {
        return address;
    }

    public String toString(String paymentMethod, String emailAddress, String cardNumber, String address, double amount) { //Method to print receipt
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String paymentDetails;
        
        if (paymentMethod.equals("PayPal")) {
            paymentDetails = String.format("%.2f paid by PayPal using %s on %s, and the delivery address is %s", amount, emailAddress, today, address);
        } else {
            paymentDetails = String.format("%.2f paid by Credit Card using %s on %s, and the delivery address is %s", amount, cardNumber, today, address);
        }
        
        return paymentDetails;
    }

}