package com.example.hotsix_be.member.entity;

import com.example.hotsix_be.common.entity.DateEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "members")
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Member extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private Long restCash;

    private String socialLoginId;

    private LocalDateTime lastLoginDate;

    private String imageUrl;


    public Member(final Long id, final String socialLoginId, final String nickname, final String imageUrl) {
        this.id = id;
        this.socialLoginId = socialLoginId;
        this.nickname = nickname;
        this.lastLoginDate = LocalDateTime.now();
        this.imageUrl = imageUrl;
    }

    public Member(final String username, final String password, final String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public boolean isNicknameChanged(final String inputNickname) {
        return !nickname.equals(inputNickname);
    }

}
