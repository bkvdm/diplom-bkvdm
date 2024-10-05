package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;

import java.util.Optional;

public interface AuthService {
    boolean login(String userName, String password);

    boolean register(Register register);

    Optional<String> getCurrentUserEmail();
}
