package org.joescaos.service;

import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.joescaos.model.SaleRequest;

import java.io.IOException;

public class SalesService {
    private static final String MAKE_SALE_URL = "http://localhost:7140/Api/PoliMarket/MakeSale";
    private final OkHttpClient client;
    private final Gson gson;
    private final String authToken;

    public SalesService(String authToken) {
        this.client = new OkHttpClient();
        this.gson = new Gson();
        this.authToken = authToken;
    }

    public String makeSale(SaleRequest saleRequest) throws IOException {
        String json = gson.toJson(saleRequest);

        RequestBody requestBody = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(MAKE_SALE_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + authToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error al generar la venta: " + response);
            }

            return response.body().string();
        }
    }
}
