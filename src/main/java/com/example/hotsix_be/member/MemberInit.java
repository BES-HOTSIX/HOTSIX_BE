package com.example.hotsix_be.member;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class MemberInit {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public void init() {
        if (memberRepository.count() <= 0) {
            Member admin = new Member("admin", passwordEncoder.encode("admin"), "admin",
                    "https://t4.ftcdn.net/jpg/02/15/84/43/360_F_215844325_ttX9YiIIyeaR7Ne6EaLLjMAmy4GvPC69.jpg",
                    "admin@naver.com");
            memberRepository.save(admin);

            IntStream.rangeClosed(2, 20).forEach(i -> {
                Member member = new Member("test" + i, passwordEncoder.encode("test" + i), "test" + i, "https://t4.ftcdn.net/jpg/02/15/84/43/360_F_215844325_ttX9YiIIyeaR7Ne6EaLLjMAmy4GvPC69.jpg", "test" + i + "naver.com");

                memberRepository.save(member);
            });
        }
    }
}
