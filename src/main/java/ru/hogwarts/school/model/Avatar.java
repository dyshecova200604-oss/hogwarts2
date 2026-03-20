package ru.hogwarts.school.model;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Lob;
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Avatar {
    @Id
    @GeneratedValue
    private Long id;
    private String filePath;
    private String mediaType;
    private Student student;

    @Lob
    private byte[] data;
    private long size;
    private Student student1;

    public Avatar(Long id, String filePath, String mediaType, long ignoredFileSize, Student student) {
        this.id = id;
        this.filePath = filePath;
        this.mediaType = mediaType;
        this.student = student;
    }

    public Avatar() {

    }

    public void setStudent(Student student) {
        student1 = student;
    }

    public void setFilePath(String string) {
    }

    public void setFileSize(long size) {
        this.size = size;
    }

    public void setMediaType(String contentType) {
    }

    public void setData(byte[] bytes) {
    }

    public String getFilePath() {
        return filePath;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Long getId() {
        return id;
    }
}