import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;

public class StockManager {

    public static void updateStock(JTable basketTable, JTable stockTable) {
        DefaultTableModel basketModel = (DefaultTableModel) basketTable.getModel();
        for (int i = 0; i < basketModel.getRowCount(); i++) {
            String barcode = basketModel.getValueAt(i, 0).toString();
            int quantity = Integer.parseInt(basketModel.getValueAt(i, 2).toString());
            // Decrease the quantity of the item in the stock by the quantity purchased
            for (int j = 0; j < stockTable.getRowCount(); j++) {
                if (stockTable.getValueAt(j, 0).toString().equals(barcode)) {
                    int currentQuantity = Integer.parseInt(stockTable.getValueAt(j, 6).toString());
                    stockTable.setValueAt(currentQuantity - quantity, j, 6); // Update in-memory stock table
                    // Update quantity in the Stock.txt file
                    updateStockFile(barcode, currentQuantity - quantity);
                    break;
                }
            }
        }
    }

    private static void updateStockFile(String barcode, int newQuantity) {
        try {
            File file = new File("Stock.txt");
            File tempFile = new File("TempStock.txt");

            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(barcode)) {
                    // Update the quantity
                    parts[6] = Integer.toString(newQuantity);
                    line = String.join(",", parts);
                }
                writer.write(line + System.lineSeparator());
            }
            reader.close();
            writer.close();

            // Replace the old file with the temporary file
            if (file.delete()) {
                if (!tempFile.renameTo(file)) {
                    System.out.println("Error: Unable to rename temporary file.");
                }
            } else {
                System.out.println("Error: Unable to delete file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void updateStockFile(String barcode, String category, String deviceType, String brand, String color, String connectivity, int quantity, double originalPrice, double retailPrice, String additionalInfo) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Stock.txt", true));
            writer.write(barcode + ", " + category + ", " + deviceType + ", " + brand + ", " + color + ", " + connectivity + ", " + quantity + ", " + originalPrice + ", " + retailPrice + ", " + additionalInfo);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void addProduct(JTable stockTable, String barcode, String brand, String color, String connectivity, String originalCostStr, String retailPriceStr, String productCategory, String keyboardType, String keyboardLayout, String mouseType, String noOfButtonsStr, String quantityStr, String selectedUser) {
        // Validate input fields
        if (barcode.isEmpty() || (!brand.isEmpty() && brand.isEmpty()) || (!color.isEmpty() && color.isEmpty()) || (!originalCostStr.isEmpty() && originalCostStr.isEmpty()) || (!retailPriceStr.isEmpty() && retailPriceStr.isEmpty()) || (!noOfButtonsStr.isEmpty() && noOfButtonsStr.isEmpty()) || (!quantityStr.isEmpty() && quantityStr.isEmpty())) {
            JOptionPane.showMessageDialog(null, "Please fill in all the required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Parse numeric fields
        double originalCost = originalCostStr.isEmpty() ? 0 : Double.parseDouble(originalCostStr);
        double retailPrice = retailPriceStr.isEmpty() ? 0 : Double.parseDouble(retailPriceStr);
        int noOfButtons = noOfButtonsStr.isEmpty() ? 0 : Integer.parseInt(noOfButtonsStr);
        int quantity = quantityStr.isEmpty() ? 0 : Integer.parseInt(quantityStr);

        // Add the new product to the table
        DefaultTableModel model = new DefaultTableModel();
        // Assuming stockTable is a class variable accessible to StockManager,
        // otherwise, you may need to pass it as a parameter to this method
        stockTable.setModel(model);
        model.addRow(new Object[]{barcode, productCategory, "TODO: Device Type", brand, color, connectivity, quantity, originalCost, retailPrice, "TODO: Additional Info"});

        // Add the new product to the file
        updateStockFile(barcode, productCategory, getCategoryType(productCategory, keyboardType, mouseType), brand, color, connectivity, quantity, originalCost, retailPrice, getAdditionalInfo(productCategory, keyboardType, keyboardLayout, mouseType, noOfButtons));
        showConfirmationMessage("Product has been successfully added.");
        
        AdminFrame adminFrame = new AdminFrame(null);
		// Close the current AdminFrame
        adminFrame.dispose();
        // Open a new AdminFrame
        AdminFrame newAdminFrame = new AdminFrame(selectedUser);
        newAdminFrame.setVisible(true);
        
    }
    
    
    private static String getCategoryType(String category, String keyboardType, String mouseType) {
        if (category.equals("Keyboard")) {
            return keyboardType;
        } else if (category.equals("Mouse")) {
            return mouseType;
        }
        return "";
    }

    private static String getAdditionalInfo(String category, String keyboardType, String keyboardLayout, String mouseType, int buttons) {
        if (category.equals("Keyboard")) {
            return keyboardLayout;
        } else if (category.equals("Mouse")) {
            return String.valueOf(buttons);
        }
        return "";
    }
    
    private static void showConfirmationMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static boolean removeProduct(String barcodeToRemove, DefaultTableModel stockTableModel) {
        try {
            File file = new File("Stock.txt");
            ArrayList<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while     
    ((line = reader.readLine()) != null) {
                String barcode = line.split(",")[0].trim();
                if (!barcode.equals(barcodeToRemove)) {
                    lines.add(line);
                }
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String newLine : lines) {
                writer.write(newLine);
                writer.newLine();
            }
            writer.close();

            for (int i = 0; i < stockTableModel.getRowCount(); i++) {
                String barcode = stockTableModel.getValueAt(i, 0).toString();
                if (barcode.equals(barcodeToRemove)) {
                    stockTableModel.removeRow(i);
                    break;
                }
            }

            return true; // Removal successful
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Removal failed
        }
    }
}