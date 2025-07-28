package org.joescaos.model;

import java.util.List;

public class SaleRequest {
    private int client_id;
    private int seller_id;
    private List<Product> listProducts;

    public SaleRequest() {
    }

    public SaleRequest(int client_id, int seller_id, List<Product> listProducts) {
        this.client_id = client_id;
        this.seller_id = seller_id;
        this.listProducts = listProducts;
    }

    public int getClient_id() {
        return client_id;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public List<Product> getListProducts() {
        return listProducts;
    }
}
