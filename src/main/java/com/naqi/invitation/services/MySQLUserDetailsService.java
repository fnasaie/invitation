package com.naqi.invitation.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.naqi.invitation.models.MySQLUserDetails;
import com.naqi.invitation.models.User;
import com.naqi.invitation.repositories.UserRepository;
 
// https://www.codejava.net/frameworks/spring-boot/user-registration-and-login-tutorial
// https://www.bezkoder.com/spring-boot-login-example-mysql/
@Service
public class MySQLUserDetailsService implements UserDetailsService {
 
    //example tutorial 
    @Autowired
    private UserRepository userRepository;
     
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return MySQLUserDetails.build(user);
    }

    //kalsym sample

    public Integer generateRandomCode() {
        Random rNo = new Random();
        final Integer code = rNo.nextInt((999999 - 100000) + 1) + 100000;// generate six digit of code
        return code;
    }
 
}