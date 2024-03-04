package com.example.hotsix_be.chat.entity;

import static jakarta.persistence.GenerationType.*;

import com.example.hotsix_be.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member host;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member user;
}