package pl.Aevise.SupperSpeed.infrastructure.security.database.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.SupperUserEntity;
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
class SupperUserJpaRepositoryTest {

    private SupperUserJpaRepository supperUserJpaRepository;

    @Test
    void checkThatYouCanGetUserByEmail() {
        //given
        SupperUserEntity user1 = supperUserEntity1();
        SupperUserEntity user2 = supperUserEntity2();
        String email1 = user1.getEmail();
        String email2 = user2.getEmail();
        List<SupperUserEntity> users = List.of(user1, user2);

        supperUserJpaRepository.saveAllAndFlush(users);

        //when
        Optional<SupperUserEntity> result1 = supperUserJpaRepository.findByEmail(email1);
        Optional<SupperUserEntity> result2 = supperUserJpaRepository.findByEmail(email2);

        //then
        assertTrue(result1.isPresent());
        assertThat(result1.get()).isEqualTo(user1);

        assertTrue(result2.isPresent());
        assertThat(result2.get()).isEqualTo(user2);
    }
}