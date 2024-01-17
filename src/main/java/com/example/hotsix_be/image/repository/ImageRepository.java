package com.example.hotsix_be.image.repository;

import com.example.hotsix_be.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
