package ru.gb.web;

import dev.samstevens.totp.exceptions.QrGenerationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.api.security.dto.UserDto;
import ru.gb.entity.security.AccountUser;
import ru.gb.service.ConfirmationService;
import ru.gb.service.MailService;
import ru.gb.service.UserService;

import javax.validation.Valid;
import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@@ -21,6 +26,8 @@
public class AuthController {

    private final UserService userService;
    private final ConfirmationService confirmationService;
    private final MailService mailService;

    @GetMapping("/login")
    public String showLoginForm() {
        @@ -34,7 +41,7 @@ public String showRegistrationForm(Model model) {
        }

        @PostMapping("/register")
        public String handleRegistration(@Valid UserDto userDto, BindingResult bindingResult, Model model) throws QrGenerationException {
            String username = userDto.getUsername();
            log.info("Process registration form for: " + username);
            if (bindingResult.hasErrors()) {
                return "auth/registration-form";
            }
            try {
                userService.findByUsername(username);
                model.addAttribute("user", userDto);
                model.addAttribute("registrationError", "Пользователь с таким именем уже существует");
                log.info("Username {} already exists", username);
                return "auth/registration-form";
            } catch (UsernameNotFoundException ignored) {}

            String secret = Base64.getEncoder().encodeToString(confirmationService.generateSecretKey().getBytes());
            userDto.setSecret(secret);
            UserDto registeredUser = userService.register(userDto);
            log.info("Successfully created user with username: {}", username);
            model.addAttribute("username", username);
            model.addAttribute("user", registeredUser);
            mailService.sendMail("DonVitalko@gmail.com",
                    "gb-shop registration",
                    "Your token - " + confirmationService.generateToken(secret, userDto));
            return "auth/registration-confirmation";
        }

        @PostMapping("/confirm")
        public String handleConfirmation(UserDto userDto) {
            AccountUser user = userService.findByUsername(userDto.getUsername());
            if (confirmationService.validate(userDto.getSecret(), user.getSecret())) {
                userService.confirm(user);
            } else {
                return "auth/registration-confirmation";
            }
            return "redirect:/auth/login";
        }

    }