package com.example.Eidiko.Mail.Sender.service;

import com.example.Eidiko.Mail.Sender.model.User;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@Service
public class ExcelReader {

    public Map<String, User> readExcel(String excelPath) throws Exception {
        Map<String, User> userMap = new HashMap<>();
        FileInputStream fis = new FileInputStream(new File(excelPath));
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row

            String name = row.getCell(1).getStringCellValue();
            String email = row.getCell(2).getStringCellValue();
            String data = row.getCell(3).getStringCellValue(); // Get 'Data'
            String fileNames = row.getCell(4).getStringCellValue();

            // Split filenames, trim spaces, and store in a list
            List<String> filesList = Arrays.asList(fileNames.split(","));
            List<String> trimmedFilesList = new ArrayList<>();
            for (String fileName : filesList) {
                trimmedFilesList.add(fileName.trim());
            }

            // Adding user data with trimmed filenames
            userMap.put(email, new User(name, email, data, trimmedFilesList));
        }

        workbook.close();
        fis.close();
        return userMap;
    }
}
