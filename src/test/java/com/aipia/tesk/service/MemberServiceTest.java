package com.aipia.tesk.service;

import com.aipia.tesk.domain.Member;
import com.aipia.tesk.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .id(1L)
                .name("홍길동")
                .email("hong@example.com")
                .password("password123")
                .build();
    }

    @Test
    @DisplayName("회원 가입 성공 테스트")
    void joinSuccess() {
        // given
        given(memberRepository.existsByEmail(anyString())).willReturn(false);
        given(memberRepository.save(any(Member.class))).willReturn(testMember);

        // when
        Member savedMember = memberService.join("홍길동", "hong@example.com", "password123");

        // then
        assertThat(savedMember.getName()).isEqualTo("홍길동");
        assertThat(savedMember.getEmail()).isEqualTo("hong@example.com");
        assertThat(savedMember.getPassword()).isEqualTo("password123");

        verify(memberRepository).existsByEmail("hong@example.com");
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("중복 이메일로 회원 가입 시 예외 발생")
    void joinWithDuplicateEmail() {
        // given
        given(memberRepository.existsByEmail("hong@example.com")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.join("홍길동", "hong@example.com", "password123"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 회원입니다.");

        verify(memberRepository).existsByEmail("hong@example.com");
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("모든 회원 조회 테스트")
    void findMembers() {
        // given
        Member member1 = Member.builder()
                .id(1L)
                .name("회원1")
                .email("member1@example.com")
                .password("password1")
                .build();
        Member member2 = Member.builder()
                .id(2L)
                .name("회원2")
                .email("member2@example.com")
                .password("password2")
                .build();

        List<Member> memberList = Arrays.asList(member1, member2);
        given(memberRepository.findAll()).willReturn(memberList);

        // when
        List<Member> foundMembers = memberService.findMembers();

        // then
        assertThat(foundMembers).hasSize(2);
        assertThat(foundMembers).extracting(Member::getName)
                .contains("회원1", "회원2");

        verify(memberRepository).findAll();
    }

    @Test
    @DisplayName("ID로 회원 조회 성공 테스트")
    void findOneSuccess() {
        // given
        given(memberRepository.findById(1L)).willReturn(Optional.of(testMember));

        // when
        Optional<Member> foundMember = memberService.findOne(1L);

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getName()).isEqualTo("홍길동");
        assertThat(foundMember.get().getEmail()).isEqualTo("hong@example.com");

        verify(memberRepository).findById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 회원 조회 시 빈 Optional 반환")
    void findOneNotFound() {
        // given
        given(memberRepository.findById(999L)).willReturn(Optional.empty());

        // when
        Optional<Member> foundMember = memberService.findOne(999L);

        // then
        assertThat(foundMember).isEmpty();

        verify(memberRepository).findById(999L);
    }

    @Test
    @DisplayName("이메일로 회원 조회 성공 테스트")
    void findByEmailSuccess() {
        // given
        given(memberRepository.findByEmail("hong@example.com")).willReturn(Optional.of(testMember));

        // when
        Optional<Member> foundMember = memberService.findByEmail("hong@example.com");

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getName()).isEqualTo("홍길동");
        assertThat(foundMember.get().getEmail()).isEqualTo("hong@example.com");

        verify(memberRepository).findByEmail("hong@example.com");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 회원 조회 시 빈 Optional 반환")
    void findByEmailNotFound() {
        // given
        given(memberRepository.findByEmail("notexist@example.com")).willReturn(Optional.empty());

        // when
        Optional<Member> foundMember = memberService.findByEmail("notexist@example.com");

        // then
        assertThat(foundMember).isEmpty();

        verify(memberRepository).findByEmail("notexist@example.com");
    }
}