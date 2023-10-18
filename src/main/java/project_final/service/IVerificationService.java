package project_final.service;

import project_final.entity.User;
import project_final.entity.Verification;

import java.util.List;

public interface IVerificationService {
    Verification create(User user);

    List<Verification> findByUser(User user);

    boolean isExpired(User user);
}
