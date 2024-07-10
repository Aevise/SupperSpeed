package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressEntity;
import pl.Aevise.SupperSpeed.integration.configuration.PersistenceContainerTestConfiguration;
import pl.Aevise.SupperSpeed.util.EntityFixtures;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static pl.Aevise.SupperSpeed.util.Constants.*;
import static pl.Aevise.SupperSpeed.util.EntityFixtures.*;
import static pl.Aevise.SupperSpeed.util.EntityFixtures.deliveryAddressEntity1;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DeliveryAddressJpaRepositoryTest {

    private final DeliveryAddressJpaRepository deliveryAddressJpaRepository;

    @Test
    void checkThatYouCanFindAllDeliveryAddressesWithAllAddressValuesExceptId() {
        //given
        var deliveryAddresses = List.of(
                deliveryAddressEntity1(),
                deliveryAddressEntity2()
        );
        deliveryAddressJpaRepository.saveAllAndFlush(deliveryAddresses);

        //when
        Optional<DeliveryAddressEntity> fetchedAddress1 = deliveryAddressJpaRepository
                .findByAllFieldsExceptId(POLAND, WARSZAWA, "district1", "11-111", "street1");
        Optional<DeliveryAddressEntity> fetchedAddress2 = deliveryAddressJpaRepository
                .findByAllFieldsExceptId(POLAND, LUBLIN, "district2", "22-222", "street2");
        Optional<DeliveryAddressEntity> fetchedAddress3 = deliveryAddressJpaRepository
                .findByAllFieldsExceptId("NON", "NON", "NON", "NON", "NON");
        DeliveryAddressEntity testDeliveryAddressEntity1 = null, testDeliveryAddressEntity2 = null, testDeliveryAddressEntity3 = null;

        if(fetchedAddress1.isPresent()) testDeliveryAddressEntity1 = fetchedAddress1.get();
        if(fetchedAddress2.isPresent()) testDeliveryAddressEntity2 = fetchedAddress2.get();
        if(fetchedAddress3.isPresent()) testDeliveryAddressEntity3 = fetchedAddress3.get();

        //then
        assertThat(testDeliveryAddressEntity1).isNotNull().isIn(deliveryAddresses).isEqualTo(deliveryAddressEntity1());
        assertThat(testDeliveryAddressEntity2).isNotNull().isIn(deliveryAddresses).isEqualTo(deliveryAddressEntity2());
        assertThat(testDeliveryAddressEntity3).isNull();

    }

//    List<DeliveryAddressEntity> findAllByPostalCodeEquals(String postalCode);

    @Test
    void checkThatYouCanGetAllDeliveryAddressesWithGivenPostalCode() {
        //given
        var deliveryAddresses = List.of(
                deliveryAddressEntity1(),
                deliveryAddressEntity2(),
                deliveryAddressEntity3()
        );
        deliveryAddressJpaRepository.saveAllAndFlush(deliveryAddresses);

        //when
        var deliveryAddresses1 = deliveryAddressJpaRepository.findAllByPostalCodeEquals("11-111");
        var deliveryAddresses2 = deliveryAddressJpaRepository.findAllByPostalCodeEquals("22-222");

        //then
        assertThat(deliveryAddresses1).isNotNull().doesNotContainNull().hasSize(1).contains(deliveryAddressEntity1());
        assertThat(deliveryAddresses2).isNotNull().doesNotContainNull().hasSize(2)
                .containsExactlyInAnyOrder(deliveryAddressEntity2(), deliveryAddressEntity3());
    }
}