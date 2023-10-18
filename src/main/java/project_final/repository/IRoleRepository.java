package project_final.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project_final.entity.Role;
import project_final.model.domain.RoleName;

import java.util.Optional;
import java.util.Set;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(RoleName roleName);
    Set<Role> findAllByIdIn(Set<Long> ids);
}
