package project_final.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project_final.entity.User;
import project_final.entity.Verification;
import project_final.repository.IVerificationRepository;
import project_final.service.IVerificationService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class VerificationService implements IVerificationService {
    private final IVerificationRepository verificationRepository;

    @Override
    public Verification create(User user) {
        Verification verification = new Verification();
        verification.setUser(user);
        verification.setVerification(UUID.randomUUID().toString().substring(0, 6));
        verification.setExpirationTime(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(120));
        verification.setStatus(true);
        verificationRepository.save(verification);
        return verification;
    }

    @Override
    public List<Verification> findByUser(User user) {
        return verificationRepository.findByUser(user);
    }

    @Override
    public boolean isExpired(User user) {
        for (Verification verification : findByUser(user)) {
            if (verification.getExpirationTime() <= System.currentTimeMillis() && verification.isStatus()) {
                return true;
            }
        }

        return false;
    }

}
