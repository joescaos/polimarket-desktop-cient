package org.joescaos.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.joescaos.model.Customer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class CustomerService {
    private static final String CUSTOMERS_URL = "http://localhost:7140/Api/PoliMarket/customers";
    private final OkHttpClient client;
    private final Gson gson;

    public CustomerService() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public List<Customer> getCustomers(String authToken) throws IOException {
        Request request = new Request.Builder()
                .url(CUSTOMERS_URL)
                .get()
                .addHeader("Authorization", "Bearer " + authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al obtener clientes: " + response);
            }

            String responseBody = response.body().string();
            Type customerListType = new TypeToken<List<Customer>>(){}.getType();
            return gson.fromJson(responseBody, customerListType);
        }
    }
}
