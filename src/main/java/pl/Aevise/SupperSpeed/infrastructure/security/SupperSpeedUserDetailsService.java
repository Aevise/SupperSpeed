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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SupperSpeedUserDetailsService implements UserDetailsService {

    private final SupperUserRepository supperUserRepository;

    /**
     * email can be interpreted as username in simple login logic: username/password
     *
     * @param email
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        SupperUserEntity user = supperUserRepository.findByEmail(email);
        List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
        return buildUserForAuthentication(user, authorities);
    }

    private List<GrantedAuthority> getUserAuthority(Set<RolesEntity> userRoles) {
        return userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .distinct()
                .collect(Collectors.toList());
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
