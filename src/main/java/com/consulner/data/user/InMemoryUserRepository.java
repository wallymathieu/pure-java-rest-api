package com.consulner.data.user;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.consulner.domain.user.NewUser;
import com.consulner.domain.user.User;
import com.consulner.domain.user.UserInfo;
import com.consulner.domain.user.UserRepository;

public class InMemoryUserRepository implements UserRepository {

    private static final Map<String,User> USERS_STORE = new ConcurrentHashMap<>();

    @Override
    public String create(NewUser newUser) {
        String id = UUID.randomUUID().toString();
        User user = User.builder()
            .id(id)
            .login(newUser.getLogin())
            .password(newUser.getPassword())
            .build();
        USERS_STORE.put(newUser.getLogin(), user);

        return id;
    }

    @Override
    public UserInfo[] all() {
        return USERS_STORE.values().stream()
                .map(u->new UserInfo(u.getId(),u.getLogin()))
                .toArray(UserInfo[]::new);
    }
}
