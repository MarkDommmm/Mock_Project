package project_final.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project_final.entity.Category;
import project_final.entity.Payment;

import java.util.List;

@Repository
public interface IPaymentRepository extends JpaRepository<Payment,Long> {


}
