package org.joescaos.layout;

import org.joescaos.AppFrame;
import org.joescaos.model.Customer;
import org.joescaos.model.Product;
import org.joescaos.model.SaleRequest;
import org.joescaos.service.CustomerService;
import org.joescaos.service.ProductService;
import org.joescaos.service.SalesService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SalesPanel extends JPanel {
    private AppFrame parent;
    private JComboBox<Customer> customerComboBox;
    private JComboBox<Product> productComboBox;
    private JSpinner quantitySpinner;
    private JButton addProductButton;
    private JButton makeSaleButton;
    private JButton logoutButton;
    private JButton refreshProductsButton;
    private JButton refreshCustomersButton;
    private JTextArea productsArea;

    private List<Product> productItems;
    private List<Product> availableProducts;
    private List<Customer> availableCustomers;

    public SalesPanel(AppFrame parent) {
        this.parent = parent;
        this.productItems = new ArrayList<>();
        this.availableProducts = new ArrayList<>();
        this.availableCustomers = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Generar Nueva Venta");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        JPanel customerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        customerPanel.setBorder(BorderFactory.createTitledBorder("Cliente"));

        customerComboBox = new JComboBox<>();
        customerComboBox.setPreferredSize(new Dimension(300, 25));
        customerPanel.add(customerComboBox);

        refreshCustomersButton = new JButton("Actualizar");
        refreshCustomersButton.addActionListener(e -> loadCustomers());
        customerPanel.add(refreshCustomersButton);

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(customerPanel, gbc);

        JPanel productPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        productPanel.setBorder(BorderFactory.createTitledBorder("Producto"));

        productComboBox = new JComboBox<>();
        productComboBox.setPreferredSize(new Dimension(300, 25));
        productPanel.add(productComboBox);

        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        productPanel.add(new JLabel("Cantidad:"));
        productPanel.add(quantitySpinner);

        addProductButton = new JButton("Agregar");
        addProductButton.addActionListener(e -> addProduct());
        productPanel.add(addProductButton);

        refreshProductsButton = new JButton("Actualizar");
        refreshProductsButton.addActionListener(e -> loadProducts());
        productPanel.add(refreshProductsButton);

        gbc.gridy = 2;
        formPanel.add(productPanel, gbc);

        productsArea = new JTextArea(5, 30);
        productsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(productsArea);
        gbc.gridy = 3;
        formPanel.add(scrollPane, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        makeSaleButton = new JButton("Generar Venta");
        makeSaleButton.addActionListener(e -> makeSale());
        buttonPanel.add(makeSaleButton);

        logoutButton = new JButton("Cerrar Sesión");
        logoutButton.addActionListener(e -> parent.logout());
        buttonPanel.add(logoutButton);

        gbc.gridy = 4;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.CENTER);

        loadCustomers();
        loadProducts();
    }

    public void loadData() {
        if (parent.getAuthToken() != null) {
            loadCustomers();
            loadProducts();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se ha iniciado sesión",
                    "Error", JOptionPane.ERROR_MESSAGE);
            parent.logout();
        }
    }

    private void loadCustomers() {
        if (parent.getAuthToken() == null) return;

        SwingWorker<List<Customer>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Customer> doInBackground() throws Exception {
                CustomerService customerService = new CustomerService();
                return customerService.getCustomers(parent.getAuthToken());
            }

            @Override
            protected void done() {
                try {
                    availableCustomers = get();
                    customerComboBox.removeAllItems();
                    for (Customer customer : availableCustomers) {
                        customerComboBox.addItem(customer);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SalesPanel.this,
                            "Error al cargar clientes: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void loadProducts() {
        if (parent.getAuthToken() == null) return;

        SwingWorker<List<Product>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Product> doInBackground() throws Exception {
                ProductService productService = new ProductService();
                return productService.getProducts(parent.getAuthToken());
            }

            @Override
            protected void done() {
                try {
                    availableProducts = get();
                    productComboBox.removeAllItems();
                    for (Product product : availableProducts) {
                        productComboBox.addItem(product);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SalesPanel.this,
                            "Error al cargar productos: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void addProduct() {
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un producto",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantity = (Integer) quantitySpinner.getValue();

        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this,
                    "La cantidad debe ser mayor a cero",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar disponibilidad
        if (quantity > selectedProduct.getQuantity()) {
            JOptionPane.showMessageDialog(this,
                    "No hay suficiente stock. Disponible: " + selectedProduct.getQuantity(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Product item = new Product(selectedProduct.getTax_id(),
                selectedProduct.getName(),
                selectedProduct.getQuantity());

        productItems.add(item);
        updateProductsArea();
    }

    private void updateProductsArea() {
        StringBuilder sb = new StringBuilder("Productos seleccionados:\n");
        for (Product item : productItems) {
            sb.append(String.format("- %s (Tax ID: %d, Cantidad: %d)\n",
                    item.getName(), item.getTax_id(), item.getQuantity()));
        }
        productsArea.setText(sb.toString());
    }

    private void makeSale() {
        Customer selectedCustomer = (Customer) customerComboBox.getSelectedItem();
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un cliente",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (productItems.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe agregar al menos un producto",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            SaleRequest request = new SaleRequest(selectedCustomer.getId(),
                    parent.getUserId(),
                    productItems);

            SalesService salesService = new SalesService(parent.getAuthToken());
            List<SalesService.ConfirmationMessage> response = salesService.makeSale(request);


            parent.showConfirmation(response.get(0));
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al generar la venta: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void clearForm() {
        customerComboBox.setSelectedIndex(-1);
        productComboBox.setSelectedIndex(-1);
        quantitySpinner.setValue(1);
        productsArea.setText("");
        productItems.clear();
    }


}
