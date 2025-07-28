package org.joescaos.layout;

import org.joescaos.AppFrame;
import org.joescaos.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private AppFrame parent;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPanel(AppFrame parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Inicio de Sesión");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(new JLabel("Usuario:"), gbc);

        usernameField = new JTextField(20);
        gbc.gridy = 2;
        add(usernameField, gbc);

        gbc.gridy = 3;
        add(new JLabel("Contraseña:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridy = 4;
        add(passwordField, gbc);

        loginButton = new JButton("Ingresar");
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPanel.this,
                            "Usuario y contraseña son requeridos",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    AuthService authService = new AuthService();
                    AuthService.LoginResponse response = authService.login(username, password);

                    parent.setAuthData(response.token, response.id);
                    parent.showSales(); // Esto ahora activará la carga de datos
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginPanel.this,
                            "Error al iniciar sesión: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
