package com.consulner.app;

import static com.consulner.app.api.ApiUtils.splitQuery;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.consulner.app.api.*;
//import com.consulner.app.api.StatusCode;
import com.consulner.app.api.user.PasswordEncoder;
import com.consulner.app.api.user.RegistrationRequest;
import com.consulner.app.api.user.RegistrationResponse;
import com.consulner.app.errors.ExceptionHandler;
import com.consulner.domain.user.NewUser;
import com.consulner.domain.user.UserRepository;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

public class Application {
    private final UserRepository userService;
    private final ObjectMapper objectMapper;
    private final ExceptionHandler errorHandler;

    public Application(UserRepository userService, ObjectMapper objectMapper, ExceptionHandler errorHandler){
        this.userService = userService;
        this.objectMapper = objectMapper;

        this.errorHandler = errorHandler;
    }
    public void register(HttpServer server){
        HandlerBuilder builder = new HandlerBuilder(objectMapper,errorHandler)
                .setDefaultContentType(Constants.APPLICATION_JSON);

        server.createContext("/api/users/register", builder.post().okBodyContent(
                (request)->{
                    NewUser user = NewUser.builder()
                            .login(request.getLogin())
                            .password(PasswordEncoder.encode(request.getPassword()))
                            .build();

                    String userId = userService.create(user);

                    return new RegistrationResponse(userId);
                }, RegistrationRequest.class));
        server.createContext("/api/users", builder.get().okSupplier(()-> userService.all()));
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
        new Application(c.getUserRepository(), c.getObjectMapper(), c.getErrorHandler())
                .register(server);
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
