package org.joescaos;

import org.joescaos.layout.ConfirmationPanel;
import org.joescaos.layout.LoginPanel;
import org.joescaos.layout.SalesPanel;
import org.joescaos.service.SalesService;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private LoginPanel loginPanel;
    private SalesPanel salesPanel;
    private ConfirmationPanel confirmationPanel;

    private String authToken;
    private Integer userId;

    public AppFrame() {
        setTitle("Gestor de Ventas PoliMarket");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this);
        salesPanel = new SalesPanel(this);
        confirmationPanel = new ConfirmationPanel(this);

        cardPanel.add(loginPanel, "LOGIN");
        cardPanel.add(salesPanel, "SALES");
        cardPanel.add(confirmationPanel, "CONFIRMATION");

        add(cardPanel);
        showLogin();
    }

    public void showLogin() {
        cardLayout.show(cardPanel, "LOGIN");
    }

    public void showSales() {
        salesPanel.clearForm();
        salesPanel.loadData();
        cardLayout.show(cardPanel, "SALES");
    }

    public void showConfirmation(SalesService.ConfirmationMessage message) {
        confirmationPanel.setMessage(message.getMessage(), message.getSaleId());
        cardLayout.show(cardPanel, "CONFIRMATION");
    }

    public void setAuthData(String token, Integer userId) {
        this.authToken = token;
        this.userId = userId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getUserId() {
        return userId;
    }

    public void logout() {
        this.authToken = null;
        this.userId = null;
        showLogin();
    }
}
