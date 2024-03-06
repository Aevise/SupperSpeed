package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.StatusListDAO;
import pl.Aevise.SupperSpeed.domain.StatusList;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.StatusListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.StatusListJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.StatusListEntityMapper;

import java.util.List;

@Repository
@AllArgsConstructor
public class StatusListListRepository implements StatusListDAO {

    private final StatusListJpaRepository statusListJpaRepository;
    private final StatusListEntityMapper statusListEntityMapper;

    @Override
    public List<StatusList> getStatusList() {
        List<StatusListEntity> statusListEntity = statusListJpaRepository.findAll();

        if (!statusListEntity.isEmpty()) {
            return statusListEntity.stream()
                    .map(statusListEntityMapper::mapFromEntity)
                    .distinct()
                    .toList();
        }
        throw new EntityNotFoundException("Could not find order status list");
    }
}
