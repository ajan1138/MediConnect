package dev.ahmedajan.mediconnect.auth;

import dev.ahmedajan.mediconnect.doctor.DoctorRegistrationRequest;
import dev.ahmedajan.mediconnect.doctor.DoctorService;
import dev.ahmedajan.mediconnect.email.EmailService;
import dev.ahmedajan.mediconnect.email.EmailTemplateName;
import dev.ahmedajan.mediconnect.patient.PatientRegistrationRequest;
import dev.ahmedajan.mediconnect.patient.PatientService;
import dev.ahmedajan.mediconnect.role.Role;
import dev.ahmedajan.mediconnect.security.JwtService;
import dev.ahmedajan.mediconnect.status.AccountStatus;
import dev.ahmedajan.mediconnect.user.Token;
import dev.ahmedajan.mediconnect.user.TokenRepository;
import dev.ahmedajan.mediconnect.user.User;
import dev.ahmedajan.mediconnect.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    // Patient Registration
    public void registerPatient(PatientRegistrationRequest request) throws MessagingException {
        validateEmailNotExists(request.getEmail());

        User user = buildUserFromPatientRequest(request);
        User savedUser = userRepository.save(user);

        // Store patient-specific information if needed
        patientService.createPatientProfile(savedUser);

        sendValidationEmail(savedUser, EmailTemplateName.PATIENT_ACTIVATE_ACCOUNT);
    }

    private User buildUserFromPatientRequest(PatientRegistrationRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .dateOfBirth(request.getDateOfBirth())
                .isAccountLocked(false)
                .isEnabled(false)
                .role(Role.PATIENT)
                .status(AccountStatus.PENDING_VERIFICATION)
                .build();
    }

    @Transactional
    public void registerDoctor(DoctorRegistrationRequest request) throws MessagingException {
        validateEmailNotExists(request.getEmail());

        User user = buildUserFromDoctorRequest(request);
        user.setStatus(AccountStatus.PENDING_ADMIN_APPROVAL);
        User savedUser = userRepository.save(user);

        // Store doctor-specific information if needed
        doctorService.createDoctorProfile(savedUser, request);

        sendAdminNotificationEmail(savedUser);
    }

    public void sendAdminNotificationEmail(User doctor) throws MessagingException {
        String adminEmail = userRepository.findById(2).get().getEmail();

        emailService.sendEmail(
                adminEmail,
                "Admin",
                EmailTemplateName.DOCTOR_PENDING_APPROVAL,
                //"url/needsToBeAdded" + "?doctorId=" + doctor.getId()
                // remake this url to be doctor's,
                activationUrl,
                null,
                "New doctor registration - Approval Required"
        );
    }

    private User buildUserFromDoctorRequest(DoctorRegistrationRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isAccountLocked(false)
                .isEnabled(false)
                .role(Role.DOCTOR)
                .status(AccountStatus.PENDING_VERIFICATION)
                .build();
    }

    private void validateEmailNotExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already in use!");
        }
    }

    private void sendValidationEmail(User user, EmailTemplateName template) throws MessagingException {
        String activationToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                template,
                activationUrl,
                activationToken,
                "Account activation"
        );
    }

    private void resendValidationEmail(User user) throws MessagingException {
        EmailTemplateName template = determineEmailTemplate(user.getRole());
        sendValidationEmail(user, template);
    }

    private EmailTemplateName determineEmailTemplate(Role role) {
        return switch (role) {
            case PATIENT -> EmailTemplateName.PATIENT_ACTIVATE_ACCOUNT;
            case DOCTOR -> EmailTemplateName.DOCTOR_ACTIVATE_ACCOUNT;
            default -> EmailTemplateName.PATIENT_ACTIVATE_ACCOUNT; // fallback
        };
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);
        int expirationMinutes = getTokenExpirationMinutes(user.getRole());

        Token token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(expirationMinutes))
                .user(user)
                .build();

        tokenRepository.save(token);
        return generatedToken;
    }

    private int getTokenExpirationMinutes(Role role) {
        return switch (role) {
            case DOCTOR -> 1440;
            case PATIENT -> 1440;
            default -> 15;
        };
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = (User) auth.getPrincipal();
        Map<String, Object> claims = buildJwtClaims(user);
        String jwtToken = jwtService.generateToken(claims, user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private Map<String, Object> buildJwtClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("fullName", user.getFullName());
        claims.put("role", user.getRole().name());
        claims.put("userId", user.getId());
        return claims;
    }

    public void activateAccount(String token) throws MessagingException {
        Token savedToken = findValidToken(token);

        if (isTokenExpired(savedToken)) {
            resendValidationEmail(savedToken.getUser());
            throw new RuntimeException(
                    "Activation token has expired. A new token has been sent to the same email address!"
            );
        }

        User user = findUserById(Math.toIntExact(savedToken.getUser().getId()));
        activateUserAccount(user);
        markTokenAsValidated(savedToken);
    }

    private Token findValidToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or non-existent token"));
    }

    private boolean isTokenExpired(Token token) {
        return LocalDateTime.now().isAfter(token.getExpiresAt());
    }

    private User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    private void activateUserAccount(User user) {
        user.setEnabled(true);
        user.setStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
    }

    private void markTokenAsValidated(Token token) {
        token.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(token);
    }

    public void approveDoctor(Integer doctorId) throws MessagingException {
        User doctor = findUserById(doctorId);

        if (!Role.DOCTOR.equals(doctor.getRole())) {
            throw new IllegalArgumentException("User is not a doctor");
        }

        if (!AccountStatus.PENDING_ADMIN_APPROVAL.equals(doctor.getStatus())) {
            throw new IllegalArgumentException("Doctor is not pending approval");
        }

        doctor.setStatus(AccountStatus.PENDING_VERIFICATION);
        userRepository.save(doctor);

        sendValidationEmail(doctor, EmailTemplateName.DOCTOR_ACTIVATE_ACCOUNT);
    }
}