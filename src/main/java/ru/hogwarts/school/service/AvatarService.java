package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Value("${books.cover.dir.path}")
    private String coverDir;

    public final StudentService studentService;
    public AvatarRepository avatarRepository;


    public AvatarService(StudentService studentService, AvatarRepository avatarRepository) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }

    public void uploadCover(Long studentId, MultipartFile file) throws IOException {
        logger.info("Was invoked method uploadCover");
        Student student = studentService.findStudent(studentId);

        Path filePath = Path.of(coverDir, studentId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ){
            bis.transferTo(bos);
        }

        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(generateImagePreview(filePath));

        avatarRepository.save(avatar);
        logger.debug("Was invoked method uploadCover");
    }

    public Avatar findAvatar(Long studentId){
        logger.info("Was invoked method findAvatar");
        return avatarRepository.findAvatarByStudentId(studentId).orElse(new Avatar());
    }

    private byte[] generateImagePreview(Path filePath) throws IOException{
        logger.info("Was invoked method generateImagePreview");
        try(InputStream is = Files.newInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(is, 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            logger.debug("Was invoked method generateImagePreview");
            return baos.toByteArray();
        }
    }

    private String getExtension(String fileName){
        logger.info("Was invoked method getExtension");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Collection<Avatar> getAllAvatars(Integer pageNumber, Integer pageSize){
        logger.info("Was invoked method getAllAvatars");
        PageRequest page = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(page).getContent();
    }
}