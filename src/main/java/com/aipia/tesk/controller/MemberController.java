package com.aipia.tesk.controller;

import com.aipia.tesk.domain.Member;
import com.aipia.tesk.dto.MemberJoinDto;
import com.aipia.tesk.dto.MemberResponseDto;
import com.aipia.tesk.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponseDto> join(@Valid @RequestBody MemberJoinDto memberJoinDto) {
        Member savedMember = memberService.join(memberJoinDto);
        MemberResponseDto responseDto = MemberResponseDto.from(savedMember);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getAllMembers() {
        List<Member> members = memberService.findMembers();
        List<MemberResponseDto> responseDtos = members.stream()
                .map(MemberResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMemberById(@PathVariable Long id) {
        Optional<Member> member = memberService.findOne(id);
        if (member.isPresent()) {
            MemberResponseDto responseDto = MemberResponseDto.from(member.get());
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<MemberResponseDto> getMemberByEmail(@PathVariable String email) {
        Optional<Member> member = memberService.findByEmail(email);
        if (member.isPresent()) {
            MemberResponseDto responseDto = MemberResponseDto.from(member.get());
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}