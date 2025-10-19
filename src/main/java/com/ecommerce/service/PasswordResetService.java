package com.ecommerce.service;

import com.ecommerce.dto.auth.ForgotPasswordRequest;
import com.ecommerce.dto.auth.ResetPasswordRequest;
import com.ecommerce.model.auth.PasswordResetToken;
import com.ecommerce.model.user.Client;
import com.ecommerce.repository.auth.PasswordResetTokenRepository;
import com.ecommerce.repository.user.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final ClientRepository clientRepository;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${reset.password.url}")
    private String resetUrl;

    @Transactional
    public void sendRequestLink(ForgotPasswordRequest forgotPasswordRequest) {
        Client client = clientRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> new IllegalStateException("User with this email does not exist!"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .user(client)
                .expirationDate(LocalDateTime.now().plusMinutes(30))
                .build();

        passwordResetTokenRepository.save(passwordResetToken);


        emailService.sendSimpleMessage(
                client.getEmail(),
                "Resetowanie hasła",
                "Cześć " + client.getFirstName() + ",\n\n" +
                        "Kliknij poniższy link, aby zresetować hasło:\n" +
                        resetUrl + "?token=" + token + "\n\n" +
                        "Jeśli to nie Ty inicjowałeś reset hasła, zignoruj tę wiadomość.\n\n" +
                        "Pozdrawiamy,\nZespół Roślinny Dom 🌿"
        );
    }

    @Transactional
    public void resetPassword (ResetPasswordRequest resetPasswordRequest) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(resetPasswordRequest.getToken())
                .orElseThrow(()-> new IllegalStateException("Could not find any token"));

        if (resetToken.isExpired()){
            throw new IllegalStateException("Token expired");
        }

        Client client = resetToken.getUser();
        client.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        clientRepository.save(client);
        passwordResetTokenRepository.delete(resetToken);
    }
}
