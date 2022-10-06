package com.example.springshop.service;

import com.example.springshop.constant.entity.Member;
import com.example.springshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Long saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByEmail(member.getEmail());
        if (findMembers.size() > 0) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
//        for (Member findMember : findMembers) {
//            if (findMember != null) {
//                throw new IllegalStateException("이미 가입된 회원입니다.");
//            }
//        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<Member> findMembers = memberRepository.findByEmail(email);
        if (findMembers.size() == 0) {
            throw new UsernameNotFoundException(email);
        }
        return User.builder()
                .username(findMembers.get(0).getEmail())
                .password(findMembers.get(0).getPassword())
                .roles(findMembers.get(0).getRole().toString())
                .build();
    }
}
