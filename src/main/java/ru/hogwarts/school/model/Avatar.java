package ru.hogwarts.school.model;


import java.sql.Types;
import java.util.Arrays;
import java.util.Objects;

import org.hibernate.annotations.JdbcTypeCode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;



@Entity
public class Avatar {

    @Id
    @GeneratedValue
    private Long id;

    private String filePath, mediaType;
    private long fileSize;

    @Lob
    @JdbcTypeCode(Types.BINARY) 
    private byte[] data;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "student_id") 
    private Student student;

    public Avatar() {
    }

    public Avatar(Long id, String filePath, String mediaType, long fileSize, Student student) {
        this.id = id;
        this.filePath = filePath;
        this.mediaType = mediaType;
        this.fileSize = fileSize;
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Avatar avatar = (Avatar) o;
        return fileSize == avatar.fileSize && Objects.equals(id, avatar.id) && Objects.equals(filePath, avatar.filePath) && Objects.equals(mediaType, avatar.mediaType) && Arrays.equals(data, avatar.data) && Objects.equals(student, avatar.student);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, filePath, mediaType, fileSize, student);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", fileSize=" + fileSize +
                ", data=" + Arrays.toString(data) +
                ", student=" + student +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}