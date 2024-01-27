package com.example.hotsix_be.member.entity;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.example.hotsix_be.common.entity.DateEntity;
import com.example.hotsix_be.login.domain.RefreshToken;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "members")
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private Long restCash;

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

    public Member(final String username, final String password, final String nickname) {
       this.username = username;
       this.password = password;
       this.nickname = nickname;
    }

    public Member(final String nickname, final String profileImageUrl, final SocialProvider socialProvider) {
        this.nickname = nickname;
        this.imageUrl = profileImageUrl;
        this.socialProvider = socialProvider;
    }

    public boolean isNicknameChanged(final String inputNickname) {
        return !nickname.equals(inputNickname);
    }

}
