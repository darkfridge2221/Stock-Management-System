import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class AdminFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTable stockTable;
    private JTextField barcodeField;
    private JTextField brandField;
    private JTextField colorField;
    private JComboBox<String> connectivityComboBox;
    private JTextField originalCostField;
    private JTextField retailPriceField;
    private JComboBox<String> productCategoryComboBox;
    private JComboBox<String> keyboardTypeComboBox;
    private JComboBox<String> keyboardLayoutComboBox;
    private JComboBox<String> mouseTypeComboBox;
    private JTextField noOfButtonsField;
    private JTextField quantityField;
    private JButton addProductButton;
    private JButton logoutButton;

    public AdminFrame(String selectedUser) {
        setTitle("Computer Accessories Shop - Welcome " + selectedUser);
        setSize(818, 489);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(800, 600));

        String[] columnNames = {"Barcode", "Product Category", "Device Type", "Brand", "Colour", "Connectivity", "Quantity", "Original Cost", "Retail Price", "Additional Info"};
        Object[][] stockData = loadStockData(); //Load data from Stock.txt into JTable
        DefaultTableModel stockTableModel = new DefaultTableModel(stockData, columnNames);
        stockTable = new JTable(stockTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) { //Disable editing of cells
                return false; 
            }
        };
        JScrollPane scrollPane = new JScrollPane(stockTable);
        scrollPane.setBounds(10, 10, 780, 265);
        panel.add(scrollPane);

        JLabel barcodeLabel = new JLabel("Barcode:");
        barcodeLabel.setBounds(10, 286, 80, 20);
        panel.add(barcodeLabel);
        barcodeField = new JTextField();
        barcodeField.setBounds(100, 286, 100, 20);
        panel.add(barcodeField);

        JLabel brandLabel = new JLabel("Brand:");
        brandLabel.setBounds(10, 317, 80, 20);
        panel.add(brandLabel);
        brandField = new JTextField();
        brandField.setBounds(100, 317, 100, 20);
        panel.add(brandField);

        JLabel colorLabel = new JLabel("Color:");
        colorLabel.setBounds(10, 348, 80, 20);
        panel.add(colorLabel);
        colorField = new JTextField();
        colorField.setBounds(100, 348, 100, 20);
        panel.add(colorField);

        JLabel connectivityLabel = new JLabel("Connectivity:");
        connectivityLabel.setBounds(10, 379, 80, 20);
        panel.add(connectivityLabel);
        String[] connectivityOptions = {"Wired", "Wireless"};
        connectivityComboBox = new JComboBox<>(connectivityOptions);
        connectivityComboBox.setBounds(100, 379, 100, 20);
        panel.add(connectivityComboBox);

        JLabel originalCostLabel = new JLabel("Original Cost:");
        originalCostLabel.setBounds(210, 286, 80, 20);
        panel.add(originalCostLabel);
        originalCostField = new JTextField();
        originalCostField.setBounds(300, 286, 100, 20);
        panel.add(originalCostField);

        JLabel retailPriceLabel = new JLabel("Retail Price:");
        retailPriceLabel.setBounds(210, 317, 80, 20);
        panel.add(retailPriceLabel);
        retailPriceField = new JTextField();
        retailPriceField.setBounds(300, 317, 100, 20);
        panel.add(retailPriceField);

        JLabel productCategoryLabel = new JLabel("Category:");
        productCategoryLabel.setBounds(210, 348, 100, 20);
        panel.add(productCategoryLabel);
        String[] productCategoryOptions = {"Keyboard", "Mouse"};
        productCategoryComboBox = new JComboBox<>(productCategoryOptions);
        productCategoryComboBox.setBounds(300, 348, 100, 20);
        panel.add(productCategoryComboBox);
        productCategoryComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedCategory = (String) productCategoryComboBox.getSelectedItem();
                if ("Keyboard".equals(selectedCategory)) { //Enable or disable input fields based on the selected product category
                    keyboardTypeComboBox.setEnabled(true);
                    keyboardLayoutComboBox.setEnabled(true);
                    mouseTypeComboBox.setEnabled(false);
                    noOfButtonsField.setEnabled(false);
                } else if ("Mouse".equals(selectedCategory)) {
                    keyboardTypeComboBox.setEnabled(false);
                    keyboardLayoutComboBox.setEnabled(false);
                    mouseTypeComboBox.setEnabled(true);
                    noOfButtonsField.setEnabled(true);
                }
            }
        });

        JLabel keyboardTypeLabel = new JLabel("Keyboard Type:");
        keyboardTypeLabel.setBounds(410, 317, 100, 20);
        panel.add(keyboardTypeLabel);
        String[] keyboardTypeOptions = {"Standard", "Flexible", "Gaming"};
        keyboardTypeComboBox = new JComboBox<>(keyboardTypeOptions);
        keyboardTypeComboBox.setBounds(520, 317, 100, 20);
        keyboardTypeComboBox.setEnabled(false); //Initially disabled
        panel.add(keyboardTypeComboBox);

        JLabel keyboardLayoutLabel = new JLabel("Keyboard Layout:");
        keyboardLayoutLabel.setBounds(410, 286, 100, 20);
        panel.add(keyboardLayoutLabel);
        String[] keyboardLayoutOptions = {"US", "UK"};
        keyboardLayoutComboBox = new JComboBox<>(keyboardLayoutOptions);
        keyboardLayoutComboBox.setBounds(520, 286, 100, 20);
        keyboardLayoutComboBox.setEnabled(false); //Initially disabled
        panel.add(keyboardLayoutComboBox);

        JLabel mouseTypeLabel = new JLabel("Mouse Type:");
        mouseTypeLabel.setBounds(410, 348, 100, 20);
        panel.add(mouseTypeLabel);
        String[] mouseTypeOptions = {"Standard", "Gaming"};
        mouseTypeComboBox = new JComboBox<>(mouseTypeOptions);
        mouseTypeComboBox.setBounds(520, 348, 100, 20);
        mouseTypeComboBox.setEnabled(false); //Initially disabled
        panel.add(mouseTypeComboBox);

        JLabel noOfButtonsLabel = new JLabel("No. of Buttons:");
        noOfButtonsLabel.setBounds(410, 379, 100, 20);
        panel.add(noOfButtonsLabel);
        noOfButtonsField = new JTextField();
        noOfButtonsField.setBounds(520, 379, 100, 20);
        noOfButtonsField.setEnabled(false); //Initially disabled
        panel.add(noOfButtonsField);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(210, 379, 100, 20);
        panel.add(quantityLabel);
        quantityField = new JTextField();
        quantityField.setBounds(300, 379, 100, 20);
        panel.add(quantityField);

        addProductButton = new JButton("Add Product");
        addProductButton.setBounds(10, 410, 780, 30);
        addProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String barcode = barcodeField.getText(); 
                if (StockManager.checkExists(barcode)) { //Check if barcode already exists in Stock.txt
                    JOptionPane.showMessageDialog(AdminFrame.this, "Error: Product with the same barcode already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String brand = brandField.getText();
                    String color = colorField.getText();
                    String connectivity = (String) connectivityComboBox.getSelectedItem();
                    String originalCostStr = originalCostField.getText();
                    String retailPriceStr = retailPriceField.getText();
                    String productCategory = (String) productCategoryComboBox.getSelectedItem();
                    String keyboardType = (String) keyboardTypeComboBox.getSelectedItem();
                    String keyboardLayout = (String) keyboardLayoutComboBox.getSelectedItem();
                    String mouseType = (String) mouseTypeComboBox.getSelectedItem();
                    String noOfButtonsStr = noOfButtonsField.getText();
                    String quantityStr = quantityField.getText();

                    try { //Check if input fields are valid numbers
                        double originalCost = Double.parseDouble(originalCostStr);
                        double retailPrice = Double.parseDouble(retailPriceStr);
                        int quantity = Integer.parseInt(quantityStr);
                        if (originalCost >= 0 && retailPrice >= 0 && quantity >= 0) { //Check if numerical values are not negative
                            StockManager.addProduct(stockTable, barcode, brand, color, connectivity, originalCostStr, retailPriceStr, productCategory, keyboardType, keyboardLayout, mouseType, noOfButtonsStr, quantityStr, selectedUser);
                            dispose(); //Reset window
                        } else {
                            JOptionPane.showMessageDialog(AdminFrame.this, "Error: Input values cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(AdminFrame.this, "Error: Must be numerical values.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        panel.add(addProductButton);

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(630, 286, 160, 108);
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();//Close the frame and open the login frame
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
        panel.add(logoutButton);
        getContentPane().add(panel);
        
        stockTable.addMouseListener(new MouseAdapter() { //Handle double click events on the table
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { //Check for double click
                    int selectedRow = stockTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String barcode = stockTable.getValueAt(selectedRow, 0).toString();
                        if (StockManager.removeProduct(barcode, (DefaultTableModel) stockTable.getModel())) {
                            JOptionPane.showMessageDialog(AdminFrame.this, "Product removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            dispose(); //Reset the frame
                            SwingUtilities.invokeLater(() -> {
                                AdminFrame newAdminFrame = new AdminFrame(selectedUser);
                                newAdminFrame.setVisible(true);
                            });
                        } else {
                            JOptionPane.showMessageDialog(AdminFrame.this, "Failed to remove product.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }
    
    private Object[][] loadStockData() { //Load stock data from Stock.txt
        ArrayList<Object[]> dataList = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File("Stock.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                Object[] rowData = new Object[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    rowData[i] = parts[i].trim();
                }
                dataList.add(rowData);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        dataList.sort(Comparator.comparing(o -> Double.parseDouble((String) o[8]))); //Sort dataList based on retail price
        Object[][] data = new Object[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            data[i] = dataList.get(i);
        }
        return data;
    }
}