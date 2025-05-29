package com.chatApp.ChatApp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class VoiceUploadController {

    @PostMapping("/voice")
    public String handleVoiceUpload(@RequestParam("voice") MultipartFile file) {
        if (file.isEmpty()) {
            return "No file uploaded!";
        }

        // Log file information
        System.out.println("Received file:");
        System.out.println(" - Name: " + file.getOriginalFilename());
        System.out.println(" - Type: " + file.getContentType());
        System.out.println(" - Size: " + file.getSize() + " bytes");

        // Optional: Save the file locally
        try {
            String uploadDir = "uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            File dest = new File(uploadDir + file.getOriginalFilename());
            file.transferTo(dest);
            return "File uploaded and saved to: " + dest.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to save file!";
        }
    }
}

