package project_final.model.service.impl.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project_final.model.entity.Role;
import project_final.model.entity.RoleName;
import project_final.model.repository.IRoleRepository;
@Service
public class RoleService implements IRoleService {
    @Autowired
    private IRoleRepository roleRepository;
    @Override
    public Role findByRoleName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(()-> new RuntimeException("Role not found"));
    }
}
