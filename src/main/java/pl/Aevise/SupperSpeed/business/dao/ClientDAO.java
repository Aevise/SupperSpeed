package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.Client;

import java.util.Optional;

public interface ClientDAO {


    Optional<Client> findByEmail(String email);
}
