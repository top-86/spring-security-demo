package com.hczq.hz.security.permit.security.browser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.hczq.hz.security.permit.domain.MyUser;

/**
 * @author zhust
 */
@Configuration
public class UserDetailService implements UserDetailsService {

    private final BCryptPasswordEncoder passwordEncoder;

    public UserDetailService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 模拟一个用户，替代数据库获取逻辑
        MyUser user = new MyUser();
        user.setUserName(username);
        user.setPassword(this.passwordEncoder.encode("123456"));
        // 输出加密后的密码
        System.out.println(user.getPassword());

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (StringUtils.equalsIgnoreCase("admin", username)) {
            authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("admin,test");
        } else {
            authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("test");
        }
        return new User(username, user.getPassword(), user.isEnabled(), user.isAccountNonExpired(),
            user.isCredentialsNonExpired(), user.isAccountNonLocked(), authorities);
    }
}
