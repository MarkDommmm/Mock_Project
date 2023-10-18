package project_final.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project_final.entity.User;
import project_final.entity.Verification;

import java.util.List;

@Repository
public interface IVerificationRepository extends JpaRepository<Verification,String> {
    List<Verification> findByUser(User user);
}
