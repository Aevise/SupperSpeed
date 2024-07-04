package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ClientEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.integration.configuration.PersistenceContainerTestConfiguration;
import pl.Aevise.SupperSpeed.util.EntityFixtures;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static pl.Aevise.SupperSpeed.util.EntityFixtures.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class ClientJpaRepositoryTest {

    private final ClientJpaRepository clientJpaRepository;
    @Test
    void findClientBySupperUserId() {
        //given
        var clients = List.of(
                clientEntity1(),
                clientEntity2()
        );
        clientJpaRepository.saveAllAndFlush(clients);
        var savedClients = clientJpaRepository.findAll();
        ClientEntity savedClient1 = savedClients.get(0);
        ClientEntity savedClient2 = savedClients.get(1);

        //when
        Optional<ClientEntity> fetchedClient1 = clientJpaRepository.findBySupperUser_SupperUserId(savedClient1.getSupperUser().getSupperUserId());
        Optional<ClientEntity> fetchedClient2 = clientJpaRepository.findBySupperUser_SupperUserId(savedClient2.getSupperUser().getSupperUserId());
        Optional<ClientEntity> fetchedClient3 = clientJpaRepository.findBySupperUser_SupperUserId(123456789);
        ClientEntity testClientEntity1 = null, testClientEntity2 = null, testClientEntity3 = null;
        if(fetchedClient1.isPresent()) testClientEntity1 = fetchedClient1.get();
        if(fetchedClient2.isPresent()) testClientEntity2 = fetchedClient2.get();
        if(fetchedClient3.isPresent()) testClientEntity3 = fetchedClient3.get();

        //then
        assertThat(testClientEntity1).isNotNull().isInstanceOf(ClientEntity.class).isNotIn(clients);
        assertThat(testClientEntity2).isNotNull().isInstanceOf(ClientEntity.class).isIn(clients);
        assertThat(testClientEntity3).isNull();
    }
}