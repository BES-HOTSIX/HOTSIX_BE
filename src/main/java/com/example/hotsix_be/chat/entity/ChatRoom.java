package com.example.hotsix_be.chat.entity;

import com.example.hotsix_be.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member host;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member user;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();

    private boolean isLeft = false;

    public ChatRoom(
            final Member host,
            final Member user
    ) {
        this.host = host;
        this.user = user;
    }

    public void updateIsLeft(boolean isLeft) {
        this.isLeft = isLeft;
    }
}