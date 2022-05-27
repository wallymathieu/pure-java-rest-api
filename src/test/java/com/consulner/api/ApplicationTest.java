package com.consulner.api;

import com.consulner.app.Application;
import com.consulner.app.Configuration;
import com.consulner.app.api.ObjectMapper;
import com.consulner.app.api.user.RegistrationRequest;
import com.sun.net.httpserver.HttpServer;
import io.vavr.control.Try;
import org.junit.Test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class ApplicationTest {

    Configuration c = new Configuration();
    ObjectMapper objectMapper = c.getObjectMapper();

    @Test
    public void canRegisterUser() throws IOException {
        int serverPort = 8000;
        HttpServer server = getHttpServer(serverPort);

        Try<Integer> result = registerUser(new URL("http://localhost:" + serverPort),
                new RegistrationRequest("login", "password"));
        assertEquals(200, (int) result.get());
        server.stop(1);
    }

    private Try<Integer> registerUser(URL server, RegistrationRequest request) {
        try {
            URL url = new URL(server + "/api/users/register");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            try (DataOutputStream out = new DataOutputStream(con.getOutputStream())) {
                out.write(objectMapper.writeValueAsBytes(request));
                out.flush();
                return Try.success(con.getResponseCode());
            }
        } catch (IOException e) {
            return Try.failure(e);
        }
    }

    private HttpServer getHttpServer(int serverPort) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), serverPort), 0);
        new Application(c.getUserService(), objectMapper, c.getErrorHandler())
                .register(server);
        server.setExecutor(null);
        server.start();
        return server;
    }
}
