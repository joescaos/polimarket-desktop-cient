package org.joescaos.service;

import com.google.gson.Gson;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private static final String LOGIN_URL = "http://localhost:7140/Api/Auth/login";
    private final OkHttpClient client;
    private final Gson gson;

    public AuthService() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    public LoginResponse login(String username, String password) throws IOException {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("user", username);
        loginData.put("password", password);

        // Convertir a JSON
        String json = gson.toJson(loginData);

        RequestBody requestBody = RequestBody.create(
                json,
                MediaType.parse("application/json; charset=utf-8")
        );
        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error en la autenticación: " + response);
            }

            String responseBody = response.body().string();
            return gson.fromJson(responseBody, LoginResponse.class);
        }
    }

    public static class LoginResponse {
        public int id;
        public String name;
        public String user;
        public String token;
    }
}
