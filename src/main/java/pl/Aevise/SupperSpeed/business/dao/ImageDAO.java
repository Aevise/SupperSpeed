package pl.Aevise.SupperSpeed.business.dao;


import pl.Aevise.SupperSpeed.domain.Logo;

public interface ImageDAO {

    Logo saveImage(String fileLocation);
}
