package com.aipia.tesk.repository;

import com.aipia.tesk.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 저장 테스트")
    void saveMember() {
        // given
        Member member = Member.builder()
                .name("홍길동")
                .email("hong@example.com")
                .password("password123")
                .build();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getName()).isEqualTo("홍길동");
        assertThat(savedMember.getEmail()).isEqualTo("hong@example.com");
        assertThat(savedMember.getPassword()).isEqualTo("password123");
    }

    @Test
    @DisplayName("이메일로 회원 조회 테스트")
    void findByEmail() {
        // given
        Member member = Member.builder()
                .name("김철수")
                .email("kim@example.com")
                .password("password456")
                .build();
        entityManager.persistAndFlush(member);

        // when
        Optional<Member> foundMember = memberRepository.findByEmail("kim@example.com");

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getName()).isEqualTo("김철수");
        assertThat(foundMember.get().getEmail()).isEqualTo("kim@example.com");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 조회 시 빈 Optional 반환")
    void findByEmailNotFound() {
        // when
        Optional<Member> foundMember = memberRepository.findByEmail("notexist@example.com");

        // then
        assertThat(foundMember).isEmpty();
    }

    @Test
    @DisplayName("이메일 존재 여부 확인 테스트")
    void existsByEmail() {
        // given
        Member member = Member.builder()
                .name("이영희")
                .email("lee@example.com")
                .password("password789")
                .build();
        entityManager.persistAndFlush(member);

        // when & then
        assertThat(memberRepository.existsByEmail("lee@example.com")).isTrue();
        assertThat(memberRepository.existsByEmail("notexist@example.com")).isFalse();
    }

    @Test
    @DisplayName("모든 회원 조회 테스트")
    void findAll() {
        // given
        Member member1 = Member.builder()
                .name("회원1")
                .email("member1@example.com")
                .password("password1")
                .build();
        Member member2 = Member.builder()
                .name("회원2")
                .email("member2@example.com")
                .password("password2")
                .build();

        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.flush();

        // when
        List<Member> members = memberRepository.findAll();

        // then
        assertThat(members).hasSize(2);
        assertThat(members).extracting(Member::getName)
                .contains("회원1", "회원2");
    }

    @Test
    @DisplayName("ID로 회원 조회 테스트")
    void findById() {
        // given
        Member member = Member.builder()
                .name("박민수")
                .email("park@example.com")
                .password("password123")
                .build();
        Member savedMember = entityManager.persistAndFlush(member);

        // when
        Optional<Member> foundMember = memberRepository.findById(savedMember.getId());

        // then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getName()).isEqualTo("박민수");
        assertThat(foundMember.get().getEmail()).isEqualTo("park@example.com");
    }
}