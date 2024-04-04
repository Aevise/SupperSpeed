package pl.Aevise.SupperSpeed.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityService {

    public String getUserAuthority() {
        /**
         * gets user role based on his authoritity
         * user authorities are user roles in system eg. RESTAURANT or CLIENT
         */
        Optional<String> userRole = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst();

        if (userRole.isPresent()) {
            return userRole.get();
        }

        throw new RuntimeException(
                "Security error, could not get user Authorities from Spring Security Context Holder"
        );
    }
}
