package project_final.model.service.mapper.user;

import project_final.model.dto.request.UserRequest;
import project_final.model.dto.response.UserResponse;
import project_final.model.entity.User;
import project_final.model.service.mapper.IGenericMapper;

public interface IUserMapper extends IGenericMapper<User, UserRequest, UserResponse> {
}
