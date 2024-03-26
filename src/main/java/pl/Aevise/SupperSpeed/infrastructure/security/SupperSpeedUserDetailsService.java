package pl.Aevise.SupperSpeed.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.RolesEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.SupperUserEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.database.jpa.SupperUserJpaRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SupperSpeedUserDetailsService implements UserDetailsService {

    private final SupperUserJpaRepository supperUserJpaRepository;

    /**
     * email can be interpreted as username in simple login logic: username/password
     *
     * @param email
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        SupperUserEntity user = supperUserJpaRepository.findByEmail(email).get();
        List<GrantedAuthority> authorities = getUserAuthority(user.getRole());
        return buildUserForAuthentication(user, authorities);
    }

    private List<GrantedAuthority> getUserAuthority(RolesEntity userRoles) {
        return List.of(new SimpleGrantedAuthority(userRoles.getRole()));
//        new (new SimpleGrantedAuthority(userRoles.getRole()))
//
//                userRoles.stream()
//                .map(role -> new SimpleGrantedAuthority(role.getRole()))
//                .distinct()
//                .collect(Collectors.toList());
    }

    private UserDetails buildUserForAuthentication(SupperUserEntity user, List<GrantedAuthority> authorities) {
        return new User(
                user.getEmail(),
                user.getPassword(),
                user.getActive(),
                true,
                true,
                true,
                authorities
        );
    }
}
