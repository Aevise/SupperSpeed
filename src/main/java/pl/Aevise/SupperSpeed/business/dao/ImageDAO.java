package pl.Aevise.SupperSpeed.business.dao;


import pl.Aevise.SupperSpeed.domain.Logo;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.LogoEntity;

public interface ImageDAO {

    Logo saveImage(String fileLocation);
}
