package com.consulner.api;

import com.consulner.app.Application;
import com.consulner.app.Configuration;
import com.consulner.app.api.ObjectMapper;
import com.consulner.app.api.user.RegistrationRequest;
import com.consulner.domain.user.UserInfo;
import com.sun.net.httpserver.HttpServer;
import io.vavr.control.Try;
import org.junit.Test;

import java.io.*;
import java.net.*;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ApplicationTest {

    Configuration c = new Configuration();
    ObjectMapper objectMapper = c.getObjectMapper();

    @Test
    public void canRegisterUser() throws IOException {
        int serverPort = 8000;
        HttpServer server = getHttpServer(serverPort);

        Try<Integer> result = registerUser(getServerURL(serverPort),
                new RegistrationRequest("login", "password"));
        assertEquals(200, (int) result.get());
        server.stop(1);
    }


    @Test
    public void canListUsers() throws IOException {
        int serverPort = 8000;
        HttpServer server = getHttpServer(serverPort);
        URL serverUrl =getServerURL(serverPort);
        Try<Integer> userResult = registerUser(serverUrl,
                new RegistrationRequest("user", "password"));
        assertEquals(200, (int) userResult.get());
        Try<List<UserInfo>> usersResult = listUsers(serverUrl);
        assertEquals(1, usersResult.get().stream().count());
        server.stop(1);
    }
    private Try<List<UserInfo>> listUsers(URL server) {
        try {
            URL url = new URL(server + "/api/users");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
                try (Reader r = new InputStreamReader(con.getInputStream())) {
                    return Try.success(objectMapper.readValues(r, UserInfo.class));
                }
            } else {
                try (Reader r = new InputStreamReader(con.getErrorStream());
                     BufferedReader br = new BufferedReader(r)) {
                    return Try.failure(new Exception(br.readLine()));
                }
            }

        } catch (IOException e) {
            return Try.failure(e);
        }
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
    private URL getServerURL(int serverPort) throws MalformedURLException {
        return new URL("http://localhost:" + serverPort);
    }

    private HttpServer getHttpServer(int serverPort) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), serverPort), 0);
        new Application(c.getUserRepository(), objectMapper, c.getErrorHandler())
                .register(server);
        server.setExecutor(null);
        server.start();
        return server;
    }
}
