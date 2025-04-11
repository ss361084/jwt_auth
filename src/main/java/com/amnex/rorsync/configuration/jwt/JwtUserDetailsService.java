package com.amnex.rorsync.configuration.jwt;

import com.amnex.rorsync.entity.UserMaster;
import com.amnex.rorsync.repository.UserMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserMasterRepository userMasterRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<UserMaster> userMasterOp =  userMasterRepository.findByUserIdAndIsDeletedAndIsActive(new Long(userId),Boolean.FALSE, Boolean.TRUE);
        if(userMasterOp.isPresent()) {
            UserMaster user = userMasterOp.get();
            if(user.getUserToken() == null) {
                user.setUserToken("");
            }
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("Guest"));

            return new User(user.getUserId()+"", user.getUserToken(), grantedAuthorities);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + userId);
        }
    }
}
