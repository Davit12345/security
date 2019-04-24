package com.notificationservice.security.details;

import com.notificationservice.security.model.Authority;
import com.notificationservice.security.model.User;
import com.notificationservice.security.model.enums.Status;
import com.notificationservice.security.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.Collection;


@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {


    @Autowired
    private UserRepository userRepository;


    @Override
    @Transactional(noRollbackFor = UserNotActivatedException.class)
    public UserDetails loadUserByUsername(final String login) {

        String lowercaseLogin = login.toLowerCase();

        User userFromDatabase = userRepository.getByEmail(lowercaseLogin);


        if (userFromDatabase == null) {
            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database");
            
        }else if (!userFromDatabase.getStatus().equals(Status.Active)) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " is not verified");
        }





        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();


        for (Authority authority : userFromDatabase.getAuthorities()) {

            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());

            grantedAuthorities.add(grantedAuthority);
        }


        return new org.springframework.security.core.userdetails.User(userFromDatabase.getEmail(), userFromDatabase.getPassword(), grantedAuthorities);

    }

}