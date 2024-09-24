package com.example.Eidiko.Mail.Sender.controller;

import com.example.Eidiko.Mail.Sender.model.User;
import com.example.Eidiko.Mail.Sender.service.EmailService;
import com.example.Eidiko.Mail.Sender.service.ExcelReader;
import com.example.Eidiko.Mail.Sender.service.ZipFileExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private ExcelReader excelReader;

    @Autowired
    private ZipFileExtractor zipFileExtractor;

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendEmails(@RequestParam("from") String from,
                             @RequestParam("subject") String subject,
                             @RequestParam("body") String body,
                             @RequestParam("cc") String cc,
                             @RequestParam("zip") MultipartFile zipFile,
                             @RequestParam("excel") MultipartFile excelFile) {

        try {
            // Extract zip to a temp directory
            String tempDir = System.getProperty("java.io.tmpdir");
            String zipFilePath = tempDir + File.separator + zipFile.getOriginalFilename();
            zipFile.transferTo(new File(zipFilePath));

            // Extract ZIP files
            zipFileExtractor.extractZip(zipFilePath, tempDir);

            // Read Excel data (use uploaded Excel)
            String excelFilePath = tempDir + File.separator + excelFile.getOriginalFilename();
            excelFile.transferTo(new File(excelFilePath));
            Map<String, User> userMap = excelReader.readExcel(excelFilePath);

            // Send emails
            for (Map.Entry<String, User> entry : userMap.entrySet()) {
                User user = entry.getValue();
                List<File> attachments = new ArrayList<>();

                // Fetch all files for the user
                for (String fileName : user.getFileNames()) {
                    File attachment = new File(tempDir + File.separator + fileName);
                    if (attachment.exists()) {
                        attachments.add(attachment);
                    } else {
                        System.out.println("File not found: " + fileName); // Log missing file
                    }
                }

                // Customize email body with 'Data' field
                String customizedBody = body + "<br/><br/>" + user.getData();

                // Send email with attachments
                emailService.sendEmailWithAttachments(user.getEmail(), subject, customizedBody, attachments);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error in processing emails!";
        }

        return "Emails Sent!";
    }
}
