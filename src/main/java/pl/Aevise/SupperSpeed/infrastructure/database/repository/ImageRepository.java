package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.ImageDAO;
import pl.Aevise.SupperSpeed.domain.Image;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ImageEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.LogoJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.ImageEntityMapper;

@Repository
@AllArgsConstructor
public class ImageRepository implements ImageDAO {

    private final LogoJpaRepository logoJpaRepository;
    private final ImageEntityMapper imageEntityMapper;


    @Override
    public Image saveImage(String fileLocation) {
        ImageEntity imageEntity = ImageEntity.builder()
                .imageURL(fileLocation)
                .build();

        ImageEntity newImageEntity = logoJpaRepository.saveAndFlush(imageEntity);
        return imageEntityMapper.mapFromEntity(newImageEntity);
    }
}
