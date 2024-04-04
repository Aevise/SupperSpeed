package pl.Aevise.SupperSpeed.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.Aevise.SupperSpeed.infrastructure.security.dao.RolesDAO;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.RolesEntity;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RolesService {

    private final RolesDAO rolesDAO;

    public Optional<RolesEntity> findById(Integer roleId) {
        return rolesDAO.findById(roleId);
    }
}

