import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.regex.PatternSyntaxException;
import javax.swing.RowFilter.Entry;

public class Basket {
    public static void searchTable(JTable stockTable, String query) {
        DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        stockTable.setRowSorter(sorter);
        if (query.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 0)); // Search by barcode (column 0)
            } catch (PatternSyntaxException e) {
                // Invalid regex pattern, don't apply any filter
                sorter.setRowFilter(null);
            }
        }
    }

    // Updated filterTable method to filter by number of mouse buttons
    public static void filterTable(JTable stockTable, int buttons) {
        DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        stockTable.setRowSorter(sorter);
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                // Get the value from the "Additional Info" column (index 8)
                Object value = entry.getModel().getValueAt(entry.getIdentifier(), 8);
                if (value != null && value instanceof String) {
                    try {
                        int mouseButtons = Integer.parseInt((String) value);
                        return mouseButtons == buttons;
                    } catch (NumberFormatException e) {
                        return false; // Invalid data, exclude from filter
                    }
                }
                return false; // Null or non-integer value, exclude from filter
            }
        });
    }

    public static double calculateTotalCost(JTable basketTable) {
        double totalCost = 0.0;
        DefaultTableModel basketModel = (DefaultTableModel) basketTable.getModel();
        for (int i = 0; i < basketModel.getRowCount(); i++) {
            double price = Double.parseDouble(basketModel.getValueAt(i, 3).toString());
            int quantity = Integer.parseInt(basketModel.getValueAt(i, 2).toString());
            totalCost += price * quantity;
        }
        return totalCost;
    }
}