package org.joescaos.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.joescaos.model.Product;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ProductService {
    private static final String PRODUCTS_URL = "http://localhost:7140/Api/PoliMarket/products";
    private final OkHttpClient client;
    private final Gson gson;

    public ProductService() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public List<Product> getProducts(String authToken) throws IOException {
        Request request = new Request.Builder()
                .url(PRODUCTS_URL)
                .get()
                .addHeader("Authorization", "Bearer " + authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al obtener productos: " + response);
            }

            String responseBody = response.body().string();
            Type productListType = new TypeToken<List<Product>>(){}.getType();
            return gson.fromJson(responseBody, productListType);
        }
    }
}
