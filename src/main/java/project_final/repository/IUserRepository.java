package project_final.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project_final.entity.Role;
import project_final.model.domain.RoleName;
import project_final.entity.User;

import java.util.Optional;
import java.util.Set;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
    User findByEmail(String email);
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleName = :roleName AND u.username LIKE %:name%")
    Page<User> findAllUsersWithUserRoleAndUseAndUsernameContaining(@Param("roleName") RoleName roleName, @Param("name") String name, Pageable pageable);

}
