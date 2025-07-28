package org.joescaos.model;

public class Product {
    private int tax_id;
    private String name;
    private int quantity;

    public Product() {
    }

    public Product(int tax_id, String name, int quantity) {
        this.tax_id = tax_id;
        this.name = name;
        this.quantity = quantity;
    }

    public int getTax_id() {
        return tax_id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return name + " (Disponibles: " + quantity + ")";
    }
}
