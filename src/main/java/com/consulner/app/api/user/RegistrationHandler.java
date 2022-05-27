package com.consulner.app.api.user;

import com.consulner.app.api.ObjectMapper;
import com.consulner.app.api.PostHandler;
import com.consulner.app.errors.ExceptionHandler;
import com.consulner.domain.user.NewUser;
import com.consulner.domain.user.UserService;

public class RegistrationHandler extends PostHandler<RegistrationRequest,RegistrationResponse> {

    private final UserService userService;

    public RegistrationHandler(UserService userService, ObjectMapper objectMapper,
                               ExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler, RegistrationRequest.class);
        this.userService = userService;
    }

    @Override
    protected RegistrationResponse doPost(RegistrationRequest registerRequest) {
        NewUser user = NewUser.builder()
                .login(registerRequest.getLogin())
                .password(PasswordEncoder.encode(registerRequest.getPassword()))
                .build();

        String userId = userService.create(user);

        RegistrationResponse response = new RegistrationResponse(userId);
        return response;
    }

}
