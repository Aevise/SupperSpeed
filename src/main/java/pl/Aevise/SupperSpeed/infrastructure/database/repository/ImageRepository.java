package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.ImageDAO;
import pl.Aevise.SupperSpeed.domain.Logo;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.LogoEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.LogoJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.LogoEntityMapper;

@Repository
@AllArgsConstructor
public class ImageRepository implements ImageDAO {

    private final LogoJpaRepository logoJpaRepository;
    private final LogoEntityMapper logoEntityMapper;


    @Override
    public Logo saveImage(String fileLocation) {
        LogoEntity logoEntity = LogoEntity.builder()
                .logoURL(fileLocation)
                .build();

        LogoEntity newLogoEntity = logoJpaRepository.saveAndFlush(logoEntity);
        return logoEntityMapper.mapFromEntity(newLogoEntity);
    }
}
