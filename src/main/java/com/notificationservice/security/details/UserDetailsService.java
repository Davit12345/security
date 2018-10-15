package com.notificationservice.security.details;

import com.notificationservice.security.model.Authority;
import com.notificationservice.security.model.User;
import com.notificationservice.security.model.enums.Status;
import com.notificationservice.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;


@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {


    @Autowired
    private UserRepository userRepository;


    //spring security zavadskoj klassic jarangum enq u metod@ override anum mer buiznessin hamapatasxan
    @Override
    @Transactional(noRollbackFor = UserNotActivatedException.class)
    public UserDetails loadUserByUsername(final String login) {

        //email@ sarqum enq poqratar
        String lowercaseLogin = login.toLowerCase();

        //bazzayic kardum enq mer userin
        User userFromDatabase = userRepository.getByEmail(lowercaseLogin);


        //stugum enq tenc user unenq te che
        if (userFromDatabase == null) {
            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database");
            //stugum enq mer user@ mer pahanjnerin hamapatasxana te che
        }else if (!userFromDatabase.getStatus().equals(Status.Active)) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " is not verified");
        }

        //shprtum enq UserNotActivetedException vorpeszi spring@ imana vor token chta
        //et exception@ menq jarangel enq AuthenticationException classic vor pahanjin bavarari




        //sarqum enq springi roleri collection
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();


        //from enq mer useri roelri vrov
        for (Authority authority : userFromDatabase.getAuthorities()) {
            //sqrqum enq springi Role=i classis u mej@ dnum enq mer roli anun@
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
            //lcnum enq springi roleri collectioni mej
            grantedAuthorities.add(grantedAuthority);
        }


        //veradarcnum enq psringi useric dnelov mej@ mer useri email,parol@ ev sptingi Roleri collection@
        return new org.springframework.security.core.userdetails.User(userFromDatabase.getEmail(), userFromDatabase.getPassword(), grantedAuthorities);

    }

}