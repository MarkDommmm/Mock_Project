package project_final.service;

import project_final.entity.Role;
import project_final.model.domain.RoleName;

public interface IRoleService {
    Role findByRoleName(RoleName roleName);
}
