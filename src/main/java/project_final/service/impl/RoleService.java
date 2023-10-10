package project_final.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project_final.entity.Role;
import project_final.model.domain.RoleName;
import project_final.repository.IRoleRepository;
import project_final.service.IRoleService;

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
