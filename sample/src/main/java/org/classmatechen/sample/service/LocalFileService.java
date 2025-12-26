package org.classmatechen.sample.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class LocalFileService implements FileService {

    private String prefix = "D:/advertise/";

    @Override
    public String save(InputStream stream, String suffix) {

        String path = generatePath(suffix);
        try (FileOutputStream output = new FileOutputStream(new File(path))) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = stream.read(buffer)) != -1) {

                output.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return path;
    }

    private String generatePath(String suffix) {

        String path = prefix + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        String second = null;
        File[] files = file.listFiles();
        if (files.length > 0) {

            List<Integer> list = Arrays.asList(files).stream().map(i -> Integer.parseInt(i.getName())).sorted().collect(Collectors.toList());
            Map<Integer, File> fileMap = Arrays.asList(files).stream().collect(Collectors.toMap(i -> Integer.parseInt(i.getName()), i -> i));
            if (fileMap.get(list.get(list.size() - 1)).listFiles().length < 2) {
                second = fileMap.get(list.get(list.size() - 1)).getName();
            }
        }
        if (null == second) {
            second = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
            new File(path + "/" + second).mkdir();
        }
        path = path + "/" + second + "/" + UUID.randomUUID().toString().replaceAll("-", "") + suffix;
        return path;
    }
}
