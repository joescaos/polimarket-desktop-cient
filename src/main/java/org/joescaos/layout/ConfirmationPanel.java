package org.joescaos.layout;

import org.joescaos.AppFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfirmationPanel extends JPanel {
    private AppFrame parent;
    private JLabel messageLabel;
    private JButton okButton;

    public ConfirmationPanel(AppFrame parent) {
        this.parent = parent;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        messageLabel = new JLabel();
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(messageLabel, gbc);

        okButton = new JButton("OK");
        gbc.gridy = 1;
        add(okButton, gbc);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.showSales();
            }
        });
    }

    public void setMessage(String message) {
        messageLabel.setText("<html><div style='text-align: center;'>" + message + "</div></html>");
    }
}
