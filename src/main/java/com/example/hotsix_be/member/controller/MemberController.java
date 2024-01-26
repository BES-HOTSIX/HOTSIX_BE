package com.example.hotsix_be.member.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.member.dto.request.MemberRegisterRequest;
import com.example.hotsix_be.member.dto.response.MemberInfoResponse;
import com.example.hotsix_be.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<?> registerMember(@RequestBody MemberRegisterRequest memberRegisterRequest) {

        log.info("memberRegisterRequest: " + memberRegisterRequest.getNickname());

        memberService.save(memberRegisterRequest);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "회원가입이 성공적으로 완료되었습니다.", null,
                        null, null
                )
        );
    }

    @GetMapping("/info")
    @MemberOnly
    public ResponseEntity<?> getMemberInfo(@Auth final Accessor accessor) {

        MemberInfoResponse memberInfo = memberService.getMemberInfo(accessor.getMemberId());

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "회원정보 조회가 성공적으로 완료되었습니다.", null,
                        null, memberInfo
                )
        );
    }


}
