package pl.Aevise.SupperSpeed.infrastructure.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SupperUserRepository extends JpaRepository<SupperUserEntity, Integer> {

    SupperUserEntity findByEmail(String email);
}
