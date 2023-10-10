package project_final.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project_final.entity.User;
import project_final.service.IUserService;


@Service
@AllArgsConstructor
public class  UserDetailService implements UserDetailsService {

    private final IUserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUserName(username).orElseThrow(()-> new UsernameNotFoundException("Username Not Found"));
        return UserPrinciple.build(user);
    }

}
