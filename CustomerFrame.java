import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class CustomerFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTable stockTable;
    private JTable basketTable;
    private JButton emptyBasketButton;
    private JComboBox<String> paymentMethodComboBox;
    private JTextField cardNumberField;
    private JTextField securityCodeField;
    private JTextField emailAddressField;
    private JButton payButton;
    private JLabel totalCostLabel;
    private JTextField searchField;
    private JComboBox<Integer> filterComboBox;

    public CustomerFrame(String selectedUser) {
        setTitle("Computer Accessories Shop - Welcome " + selectedUser);
        setSize(818, 679);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null); 
        panel.setPreferredSize(new Dimension(800, 600));
        
        String[] columnNames = {"Barcode", "Product Category", "Device Type", "Brand", "Colour", "Connectivity", "Quantity", "Price", "Additional Info"}; //Load data from Stock.txt into a table
        Object[][] stockData = loadStockData();
        Object[][] adjustedStockData = new Object[stockData.length][columnNames.length]; //Don't display the Original Price
        for (int i = 0; i < stockData.length; i++) {
            System.arraycopy(stockData[i], 0, adjustedStockData[i], 0, 7);
            System.arraycopy(stockData[i], 8, adjustedStockData[i], 7, 1);
            adjustedStockData[i][8] = stockData[i][9];
        }
        DefaultTableModel stockTableModel = new DefaultTableModel(adjustedStockData, columnNames);
        stockTable = new JTable(stockTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) { //Disable editing of table
                return false;
            }
        };
        panel.setLayout(null);

        JLabel searchLabel = new JLabel("Search by Barcode:"); 
        searchLabel.setBounds(324, 493, 120, 20); 
        panel.add(searchLabel);
        searchField = new JTextField();
        searchField.setBounds(464, 493, 100, 20);
        panel.add(searchField);

        searchField.getDocument().addDocumentListener(new DocumentListener() { //Dynamically change the table based on the search
        	@Override
        	public void insertUpdate(DocumentEvent e) {
                Basket.searchTable(stockTable, searchField.getText());
        	}
        	@Override
        	public void removeUpdate(DocumentEvent e) {
                Basket.searchTable(stockTable, searchField.getText());
        	}
        	@Override
        	public void changedUpdate(DocumentEvent e) {
        	}
        });

        JLabel filterLabel = new JLabel("Filter by Mouse Buttons:");
        filterLabel.setBounds(10, 493, 150, 20); 
        panel.add(filterLabel);
        filterComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
        filterComboBox.setBounds(152, 493, 150, 20);
        panel.add(filterComboBox);
        filterComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedButtons = (Integer) filterComboBox.getSelectedItem();
                Basket.filterTable(stockTable, selectedButtons);
            }
        });

        stockTable.addMouseListener(new MouseAdapter() { //Mouse listener for table
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { //Double click event
                    int selectedRow = stockTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String quantityStr = JOptionPane.showInputDialog(CustomerFrame.this, "Enter quantity:", "Add to Basket", JOptionPane.QUESTION_MESSAGE);
                        if (quantityStr != null && !quantityStr.isEmpty()) {
                            try {
                                int quantity = Integer.parseInt(quantityStr);
                                if (quantity > 0) {
                                    int stockQuantity = Integer.parseInt(stockTable.getValueAt(selectedRow, 6).toString()); //Get quantity
                                    if (quantity <= stockQuantity) { //Add the item to the basket table
                                        DefaultTableModel basketModel = (DefaultTableModel) basketTable.getModel();
                                        Object[] rowData = {
                                            stockTable.getValueAt(selectedRow, 0), //Barcode
                                            stockTable.getValueAt(selectedRow, 3), //Brand
                                            quantity, //Quantity
                                            stockTable.getValueAt(selectedRow, 7) //Retail Price
                                        };
                                        basketModel.addRow(rowData);
                                        double totalCost = Basket.calculateTotalCost(basketTable);//Update total cost
                                        totalCostLabel.setText("Total Cost: £" + String.format("%.2f", totalCost));
                                    } else {
                                        JOptionPane.showMessageDialog(CustomerFrame.this, "Not enough in stock.", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(CustomerFrame.this, "Please enter a valid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(CustomerFrame.this, "Invalid quantity. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });
        JScrollPane stockScrollPane = new JScrollPane(stockTable);
        stockScrollPane.setBounds(10, 10, 780, 256);
        panel.add(stockScrollPane);

        String[] basketColumnNames = {"Barcode", "Product Name", "Quantity", "Price"};
        Object[][] basketData = {}; //Initialize basket as empty
        DefaultTableModel basketTableModel = new DefaultTableModel(basketData, basketColumnNames);
        basketTable = new JTable(basketTableModel);
        JScrollPane basketScrollPane = new JScrollPane(basketTable);
        basketScrollPane.setBounds(10, 277, 780, 200);
        panel.add(basketScrollPane);

        emptyBasketButton = new JButton("Empty Basket");
        emptyBasketButton.setBounds(324, 524, 240, 20);
        emptyBasketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel basketModel = (DefaultTableModel) basketTable.getModel();
                basketModel.setRowCount(0); //Clear all rows from the basket
        	    totalCostLabel.setText("Total Cost: £0.00"); //Set total cost to zero
            }
        });
        panel.add(emptyBasketButton);

        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        paymentMethodLabel.setBounds(10, 524, 150, 20);
        panel.add(paymentMethodLabel);
        String[] paymentMethodOptions = {"Credit Card", "PayPal"};
        paymentMethodComboBox = new JComboBox<>(paymentMethodOptions);
        paymentMethodComboBox.setBounds(152, 524, 150, 20);
        panel.add(paymentMethodComboBox);

        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberLabel.setBounds(10, 555, 100, 20);
        panel.add(cardNumberLabel);
        cardNumberField = new JTextField();
        cardNumberField.setBounds(152, 555, 150, 20);
        panel.add(cardNumberField);

        JLabel securityCodeLabel = new JLabel("Security Code:");
        securityCodeLabel.setBounds(324, 555, 100, 20);
        panel.add(securityCodeLabel);
        securityCodeField = new JTextField();
        securityCodeField.setBounds(464, 555, 100, 20);
        panel.add(securityCodeField);

        JLabel emailAddressLabel = new JLabel("Email Address:");
        emailAddressLabel.setBounds(10, 586, 100, 20);
        panel.add(emailAddressLabel);
        emailAddressField = new JTextField();
        emailAddressField.setBounds(152, 586, 150, 20);
        panel.add(emailAddressField);

        paymentMethodComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { //Enable and disable depending on payment method
                if (paymentMethodComboBox.getSelectedItem().equals("Credit Card")) {
                    emailAddressField.setEnabled(false);
                    cardNumberField.setEnabled(true);
                    securityCodeField.setEnabled(true);
                } else if (paymentMethodComboBox.getSelectedItem().equals("PayPal")) {
                    emailAddressField.setEnabled(true);
                    cardNumberField.setEnabled(false);
                    securityCodeField.setEnabled(false);
                }
            }
        });

        payButton = new JButton("Pay");
        payButton.setBounds(464, 586, 100, 20);
        payButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	    String paymentMethod = paymentMethodComboBox.getSelectedItem().toString();
        	    Address address = Address.getAddress(selectedUser);//Get the address
        	    Receipt receipt = new Receipt(); //Create a receipt
        	    if (paymentMethod.equals("Credit Card")) {
	        	    String cardNumber = cardNumberField.getText();
	        	    if (cardNumber.length() != 6) {
	        	        JOptionPane.showMessageDialog(CustomerFrame.this, "Error: Card number must be 6 digits.", "Error", JOptionPane.ERROR_MESSAGE);
	        	        return;
	        	    }
	        	    String securityNumber = securityCodeField.getText();
	        	    if (securityNumber.length() != 3) {
	        	        JOptionPane.showMessageDialog(CustomerFrame.this, "Error: Security number must be 3 digits.", "Error", JOptionPane.ERROR_MESSAGE);
	        	        return;
	        	    }
        	    } else {
        	        String emailAddress = emailAddressField.getText().trim();
        	    	if (emailAddress.isEmpty() || !emailAddress.contains("@")) {
        	            JOptionPane.showMessageDialog(CustomerFrame.this, "Error: Please enter a valid email address for PayPal payment.", "Payment Error", JOptionPane.ERROR_MESSAGE);
        	            return;
        	    	}
        	    }
                double totalCost = Basket.calculateTotalCost(basketTable);
        	    String receiptContent = receipt.toString(paymentMethod, emailAddressField.getText(), cardNumberField.getText(), address.toString(), totalCost); //Print receipt
        	    StockManager.updateStock(basketTable, stockTable);// Remove items from stock
        	    DefaultTableModel basketModel = (DefaultTableModel) basketTable.getModel();
        	    basketModel.setRowCount(0); //Empty basket
        	    totalCostLabel.setText("Total Cost: £0.00"); //Set total cost to zero
        	    JOptionPane.showMessageDialog(CustomerFrame.this, receiptContent, "Payment Receipt", JOptionPane.INFORMATION_MESSAGE); //Display the receipt
        	}
        });
        panel.add(payButton);

        totalCostLabel = new JLabel("Total Cost: £0.00");
        totalCostLabel.setBounds(324, 581, 150, 30);
        panel.add(totalCostLabel);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(630, 493, 160, 105);
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); //Close the customer frame and open the login frame
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
        panel.add(logoutButton);
        getContentPane().add(panel);
    }

    private Object[][] loadStockData() { //Load data from Stock.txt
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
        dataList.sort(Comparator.comparing(o -> Double.parseDouble((String) o[8]))); //Sort based on the retail price
        Object[][] data = new Object[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            data[i] = dataList.get(i);
        }
        return data;
    }
}