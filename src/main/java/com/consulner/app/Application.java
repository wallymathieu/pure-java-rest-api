package com.consulner.app;

import static com.consulner.app.api.ApiUtils.splitQuery;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.consulner.app.api.ObjectMapper;
import com.consulner.app.api.user.RegistrationHandler;
import com.consulner.app.errors.ExceptionHandler;
import com.consulner.domain.user.UserService;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

public class Application {
    private final RegistrationHandler registrationHandler;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final ExceptionHandler errorHandler;

    public Application(UserService userService, ObjectMapper objectMapper, ExceptionHandler errorHandler){
        this.userService = userService;
        this.objectMapper = objectMapper;

        this.errorHandler = errorHandler;
        this.registrationHandler = new RegistrationHandler(userService, objectMapper,
                errorHandler);
    }
    public void register(HttpServer server){

        server.createContext("/api/users/register", registrationHandler::handle);

        HttpContext context =server.createContext("/api/hello", (exchange -> {

            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());
                String noNameText = "Anonymous";
                String name = params.getOrDefault("name", Collections.singletonList(noNameText)).stream().findFirst().orElse(noNameText);
                String respText = String.format("Hello %s!", name);
                exchange.sendResponseHeaders(200, respText.getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));
        context.setAuthenticator(new BasicAuthenticator("myrealm") {
            @Override
            public boolean checkCredentials(String user, String pwd) {
                return user.equals("admin") && pwd.equals("admin");
            }
        });

    }
    public static void main(String[] args) throws IOException {
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), serverPort), 0);
        Configuration c = new Configuration();
        new Application(c.getUserService(), c.getObjectMapper(), c.getErrorHandler())
                .register(server);
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
