package ru.gb.security;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.util.Utils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gb.api.security.dto.UserDto;
import ru.gb.service.ConfirmationService;

import javax.annotation.Resource;

@Slf4j
@AllArgsConstructor
@Service
public class SecretManagerService implements ConfirmationService {
    @Resource
    private SecretGenerator secretGenerator;


    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String generateSecretKey() {
        return secretGenerator.generate();
    }

    @Override
    public String generateToken(String secret, UserDto userDto) {
        return jwtTokenProvider.createToken(userDto.getUsername(), secret);
    }

    @Override
    public boolean validate(String token, String secret) {
        return jwtTokenProvider.validateToken(token, secret);
    }

}