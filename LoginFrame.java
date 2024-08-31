import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class LoginFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JComboBox<String> userComboBox;

    public LoginFrame() {
        setTitle("Login");
        setSize(556, 189);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null); 
        panel.setPreferredSize(new Dimension(300, 150));
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Select User:");
        userLabel.setBounds(151, 28, 80, 20);
        panel.add(userLabel);
        userComboBox = new JComboBox<>();
        userComboBox.setBounds(229, 28, 150, 20);
        loadUsers(); //Load users from UserAccounts.txt
        panel.add(userComboBox);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(190, 59, 150, 30);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedUser = (String) userComboBox.getSelectedItem();
                if (selectedUser != null && !selectedUser.isEmpty()) {
                    dispose(); //Close the login frame
                    try {
                        Scanner scanner = new Scanner(new File("UserAccounts.txt")); //Read accounts from UserAccounts.txt
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            String[] parts = line.split(",");
                            String username = parts[1].trim();
                            String role = parts[6].trim();
                            if (selectedUser.equals(username)) {
                                if (role.equals("admin")) { //Open Admin Frame
                                    AdminFrame adminFrame = new AdminFrame(selectedUser);
                                    adminFrame.setVisible(true);
                                } else { //Open Customer Frame
                                    CustomerFrame customerFrame = new CustomerFrame(selectedUser);
                                    customerFrame.setVisible(true);
                                } 
                                break;
                            }
                        }
                        scanner.close();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Please select a user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        panel.add(loginButton);
        getContentPane().add(panel);
        JLabel lblNewLabel = new JLabel("Double click on the table to remove item or to add to basket"); //Extra information for users
        lblNewLabel.setBounds(89, 100, 441, 39);
        panel.add(lblNewLabel);
    }

    private void loadUsers() { //Method to load users to drop down box
        try {
            Scanner scanner = new Scanner(new File("UserAccounts.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String username = parts[1].trim();
                userComboBox.addItem(username);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}