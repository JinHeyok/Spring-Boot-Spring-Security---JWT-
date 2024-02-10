package com.colabear754.authentication_example_java.service;


import com.colabear754.authentication_example_java.entity.MemberEntity;
import com.colabear754.authentication_example_java.entity.UserDetailsImpl;
import com.colabear754.authentication_example_java.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MemberEntity> member = memberRepository.findByAccount(username);
        if(member.isEmpty()){throw new UsernameNotFoundException("loadUserByUsername Error");}
        return UserDetailsImpl.build(member.get());
    }
}
