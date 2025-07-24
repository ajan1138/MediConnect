package dev.ahmedajan.mediconnect.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {

    User(Collections.emptySet()),
    ADMIN(
            Set.of(
                    Permission.ADMIN_READ,
                    Permission.ADMIN_POST,
                    Permission.ADMIN_UPDATE,
                    Permission.ADMIN_PUT,
                    Permission.ADMIN_DELETE
            )
    ),
    DOCTOR(
            Set.of(
                    Permission.DOCTOR_READ,
                    Permission.DOCTOR_POST,
                    Permission.DOCTOR_PUT,
                    Permission.DOCTOR_UPDATE,
                    Permission.DOCTOR_DELETE
            )
    ),
    PATIENT(
            Set.of(
                    Permission.PATIENT_READ,
                    Permission.PATIENT_POST,
                    Permission.PATIENT_PUT,
                    Permission.PATIENT_UPDATE,
                    Permission.PATIENT_DELETE
            )
    )
    ;

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}