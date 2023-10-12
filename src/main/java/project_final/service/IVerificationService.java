package project_final.service;

import project_final.entity.User;
import project_final.entity.Verification;

public interface IVerificationService {
    Verification create(User user);

    Verification findByUser(User user);

    boolean isExpired(User user);
}
