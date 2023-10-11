package project_final.service.mapper;

import project_final.model.dto.request.UpdateUserRequest;
import project_final.model.dto.request.UserRequest;
import project_final.model.dto.response.UserResponse;
import project_final.entity.User;
import project_final.service.mapper.IGenericMapper;

public interface IUserMapper extends IGenericMapper<User, UserRequest, UserResponse> {
      User toUpdate(UpdateUserRequest userRequest);
}
