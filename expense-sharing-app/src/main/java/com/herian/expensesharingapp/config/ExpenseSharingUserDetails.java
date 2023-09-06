package com.herian.expensesharingapp.config;

import com.herian.expensesharingapp.entity.Person;
import com.herian.expensesharingapp.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//Custom loading user from my database (instead of spring boot security default User, I use my own Person)
@Service
public class ExpenseSharingUserDetails implements UserDetailsService {

    @Autowired
    PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userName, password = null;
        List<GrantedAuthority> authorities = null;
        List<Person> personList = personRepository.findByEmail(username);
        if (personList.size() == 0){
            throw new UsernameNotFoundException("User details not found for the user:" + username);
        } else {
            userName = personList.get(0).getEmail();
            password = personList.get(0).getPassword();
            authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(personList.get(0).getRole()));
        }
        return new User(userName, password, authorities);
    }
}
