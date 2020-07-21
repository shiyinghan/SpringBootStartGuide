package com.hansy.uploadingfiles;

import com.hansy.uploadingfiles.storage.StorageProperties;
import com.hansy.uploadingfiles.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class UploadingFilesApplication {

    public static void main(String[] args) {
        SpringApplication.run(UploadingFilesApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(StorageService storageService) {
        return args -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
