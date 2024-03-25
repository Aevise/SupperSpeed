package pl.Aevise.SupperSpeed.infrastructure.security.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.infrastructure.security.dao.UserRoleDAO;

@Repository
@AllArgsConstructor
public class UserRoleRepository implements UserRoleDAO {

    @Override
    public void updateUserRole() {

    }
}
