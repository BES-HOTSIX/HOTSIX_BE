package com.example.hotsix_be.chat.repository;

import com.example.hotsix_be.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
