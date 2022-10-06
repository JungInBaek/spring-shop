package com.example.springshop.constant.entity;

import com.example.springshop.constant.Role;
import com.example.springshop.dto.MemberFormDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    protected Member(String name, String email, String password, String address, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
    }

//    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
//        Member member = new Member(
//                memberFormDto.getName(),
//                memberFormDto.getEmail(),
//                passwordEncoder.encode(memberFormDto.getPassword()),
//                memberFormDto.getAddress(),
//                Role.USER);
//        return member;
//    }

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member(
                memberFormDto.getName(),
                memberFormDto.getEmail(),
                passwordEncoder.encode(memberFormDto.getPassword()),
                memberFormDto.getAddress(),
                Role.ADMIN);
        return member;
    }
}
