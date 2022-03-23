package ru.gb.service;

import dev.samstevens.totp.exceptions.QrGenerationException;
import ru.gb.api.security.dto.UserDto;

public interface ConfirmationService {
    public String generateSecretKey();

    public String generateToken(String secret, UserDto userDto);
    public boolean validate(String token, String secret);

}