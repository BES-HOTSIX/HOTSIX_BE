package com.example.hotsix_be.member.controller;

import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.member.dto.request.MemberRegisterRequest;
import com.example.hotsix_be.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<?> registerMember(@RequestBody MemberRegisterRequest memberRegisterRequest) {

        memberService.save(memberRegisterRequest);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "성공적으로 등록되었습니다.", null,
                        null, null
                )
        );
    }
}
