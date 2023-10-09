package project_final.model.service.impl.role;

import project_final.model.entity.Role;
import project_final.model.entity.RoleName;

public interface IRoleService {
    Role findByRoleName(RoleName roleName);
}
