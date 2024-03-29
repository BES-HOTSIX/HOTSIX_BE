package com.example.hotsix_be.member.entity;

import com.example.hotsix_be.common.entity.DateEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "members")
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Slf4j
public class Member extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true)
    private String nickname;

    private String email;

    private Long restCash = 0L;

    @Enumerated(EnumType.STRING)
    private SocialProvider socialProvider;

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

    public Member(final String username, final String password, final String nickname, final String imageUrl,
                  final String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.email = email;
    }

    public Member(final String nickname, final String profileImageUrl, final SocialProvider socialProvider) {
        this.nickname = nickname;
        this.imageUrl = profileImageUrl;
        this.socialProvider = socialProvider;
    }

    public void assignRole(final Role role) {
        this.role = role;
    }

    public boolean isNicknameChanged(final String inputNickname) {
        return !nickname.equals(inputNickname);
    }

    public void changePassword(final String password) {
        this.password = password;
    }

    public void changeNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void changeImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private void updateRestCash(Long newRestCash) {
        this.restCash = newRestCash;
    }
    
    public void addCash(Long amount, Long discountAmount) {
        long updatedRestCash;
        if (amount > 0){
            updatedRestCash = this.restCash + amount;
        }else {
            updatedRestCash = this.restCash + amount + discountAmount;
        }
        updateRestCash(updatedRestCash);
        log.info(this.restCash + "원이 추가되었습니다.");
    }
}
