package dev.ahmedajan.mediconnect.user;

import dev.ahmedajan.mediconnect.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    boolean existsByRole(Role role);


}