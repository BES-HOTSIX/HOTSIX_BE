package com.example.hotsix_be.image.service;

import static com.example.hotsix_be.common.exception.ExceptionCode.*;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.hotsix_be.image.config.NcpS3Properties;
import com.example.hotsix_be.image.entity.Image;
import com.example.hotsix_be.image.exception.ImageException;
import com.example.hotsix_be.image.repository.ImageRepository;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
@RequiredArgsConstructor
public class ImageService {

    private final NcpS3Properties ncpS3Properties;
    private final AmazonS3Client amazonS3Client;
    private final ImageRepository imageRepository;

    // Ncp
    public List<Image> uploadImages(final List<MultipartFile> multipartFiles, final String filePath,
                                    final String name) {
        List<Image> images = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            String originalFileName = multipartFile.getOriginalFilename();
            String newFileName = filePath + "_" + name + "_" + UUID.randomUUID() + "_" + originalFileName;
            String folderName = createFolderNameWithTodayDate();
            String keyName = filePath + "/" + folderName + "/" + newFileName;
            String uploadUrl = "";

            try (InputStream inputStream = multipartFile.getInputStream()) {

                amazonS3Client.putObject(
                        new PutObjectRequest(
                                ncpS3Properties.getS3().getBucketName(), keyName, inputStream, objectMetadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead));

                uploadUrl = ncpS3Properties.getS3().getEndPoint() + "/" + ncpS3Properties.getS3().getBucketName() + "/"
                        + keyName;

            } catch (SdkClientException e) {
                e.printStackTrace();
                throw new SdkClientException(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                throw new ImageException(EXCEED_IMAGE_CAPACITY);
            }

            Image image = Image.builder()
                    .imageId(keyName)
                    .name(newFileName)
                    .url(uploadUrl)
                    .size(multipartFile.getSize())
                    .build();

            images.add(image);
        }

        return images;
    }

    private String createFolderNameWithTodayDate() {
        LocalDateTime now = LocalDateTime.now();
        String year = String.valueOf(now.getYear());
        String month = String.format("%02d", now.getMonthValue());

        return year + "/" + month;
    }

    public void deleteImageInS3Bucket(final String keyName) {
        try {
            amazonS3Client.deleteObject(ncpS3Properties.getS3().getBucketName(), keyName);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
            throw new AmazonS3Exception(e.getMessage());
        } catch (SdkClientException e) {
            e.printStackTrace();
            throw new SdkClientException(e.getMessage());
        }
    }

    public String getImageIdAndDeleteImage(final String imageUrl) {
        Image image = findImageByUrl(imageUrl);
        imageRepository.delete(image);
        return image.getImageId();
    }

    private Image findImageByUrl(final String imageUrl) {
        return imageRepository.findByUrl(imageUrl)
                .orElseThrow(() -> new ImageException(IMAGE_NOT_FOUND));
    }

}
