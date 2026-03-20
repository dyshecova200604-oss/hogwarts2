package ru.hogwarts.school.model;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;

@ru.hogwarts.school.model.NoArgsConstructor
@ru.hogwarts.school.model.AllArgsConstructor


@Entity
public class Avatar {
    @Id
    @GeneratedValue
    private Long id;
    private String filePath, mediaType;
    private long fileSize;

    @Lob
    private byte[] data;

    @OneToOne
    private Student student;

    public Avatar(Long id, String filePath, String mediaType, long fileSize, Student student) {
        this.id = id;
        this.filePath = filePath;
        this.mediaType = mediaType;
        this.fileSize = fileSize;
        this.student = student;
    }

    public Avatar() {

    }

    public void setStudent(Student student) {
    }

    public void setFilePath(String string) {
    }

    public void setFileSize(long size) {
    }

    public void setMediaType(String contentType) {
    }

    public void setData(byte[] bytes) {
    }
}