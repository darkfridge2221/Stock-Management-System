import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Address {
    private String houseNumber;
    private String postcode;
    private String city;

    public Address(String houseNumber, String postcode, String city) {
        this.houseNumber = houseNumber;
        this.postcode = postcode;
        this.city = city;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return houseNumber + ", " + postcode + ", " + city;
    }

    public static Address getAddress(String username) { //Method to get address from UserAccounts.txt
        String fileName = "UserAccounts.txt";
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 7 && parts[1].equals(username)) {
                    return new Address(parts[3], parts[4], parts[5]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}