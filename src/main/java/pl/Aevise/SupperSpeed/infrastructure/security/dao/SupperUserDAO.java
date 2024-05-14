package pl.Aevise.SupperSpeed.infrastructure.security.dao;

import pl.Aevise.SupperSpeed.domain.SupperUser;

import java.util.Optional;

public interface SupperUserDAO {
    Optional<SupperUser> findByEmail(String email);

    void deleteUserByEmail(String email);

    void deleteUserById(Integer userId);
}
