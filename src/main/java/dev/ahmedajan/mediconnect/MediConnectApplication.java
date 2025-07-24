package dev.ahmedajan.mediconnect;

import dev.ahmedajan.mediconnect.role.Role;
import dev.ahmedajan.mediconnect.user.User;
import dev.ahmedajan.mediconnect.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDate;

@SpringBootApplication
@RequiredArgsConstructor
@EnableJpaAuditing
public class MediConnectApplication {

    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(MediConnectApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner() {
        return args -> {
            if (!userRepository.existsByRole(Role.ADMIN)) {
                userRepository.save(
                        User.builder()
                                .firstName("admin")
                                .lastName("admin")
                                .dateOfBirth(LocalDate.now())
                                .email("ajanovicahmed10@gmail.com")
                                .password("sifra123")
                                .isAccountLocked(false)
                                .isEnabled(true)
                                .role(Role.ADMIN)
                                .build());

            }
        };
    }
}
