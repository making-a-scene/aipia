package com.aipia.tesk.service;

import com.aipia.tesk.domain.Member;
import com.aipia.tesk.dto.MemberJoinDto;
import com.aipia.tesk.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member join(MemberJoinDto memberJoinDto) {
        validateDuplicateMember(memberJoinDto.getEmail());

        Member member = Member.builder()
                .name(memberJoinDto.getName())
                .email(memberJoinDto.getEmail())
                .password(memberJoinDto.getPassword())
                .build();

        return memberRepository.save(member);
    }

    private void validateDuplicateMember(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}