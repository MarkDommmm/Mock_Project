package project_final.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import project_final.entity.Role;
import project_final.model.domain.GooglePojo;
import project_final.model.domain.RoleName;
import project_final.repository.IUserRepository;
import project_final.service.IRoleService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class GoogleUtils {
    private final Environment env;
    private final IUserRepository userRepository;
    private final IRoleService roleService;

    public String getToken(final String code) throws IOException {
        String link = env.getProperty("google.link.get.token");
        String response = Request.Post(link)
                .bodyForm(Form.form().add("client_id", env.getProperty("google.app.id"))
                        .add("client_secret", env.getProperty("google.app.secret"))
                        .add("redirect_uri", env.getProperty("google.redirect.uri")).add("code", code)
                        .add("grant_type", "authorization_code").build())
                .execute().returnContent().asString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response).get("access_token");
        return node.textValue();
    }

    public GooglePojo getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        String link = env.getProperty("google.link.get.user_info") + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        ObjectMapper mapper = new ObjectMapper();
        GooglePojo googlePojo = mapper.readValue(response, GooglePojo.class);
        System.out.println(googlePojo);
        if (!userRepository.existsByEmail(googlePojo.getEmail())) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleService.findByRoleName(RoleName.ROLE_USER));
            project_final.entity.User user = new project_final.entity.User();
            user.setName(googlePojo.getEmail());
            user.setEmail(googlePojo.getEmail());
            user.setAvatar(googlePojo.getPicture());
            user.setRoles(roles);
            user.setStatus(true);
            userRepository.save(user);
        }

        return googlePojo;

    }

    public UserDetails buildUser(GooglePojo googlePojo) {
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        UserDetails userDetail = new User(
                googlePojo.getEmail(),
                "",
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                authorities);
        return userDetail;
    }
}
