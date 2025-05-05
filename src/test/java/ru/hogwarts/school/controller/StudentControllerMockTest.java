package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetStudentInfo() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry");
        student.setAge(11);
        Mockito.when(studentService.findStudent(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Harry"))
                .andExpect(jsonPath("$.age").value(11));
    }

    @Test
    void testFindStudents() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Hermione");
        student.setAge(12);
        Mockito.when(studentService.findAllStudents()).thenReturn(List.of(student));

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Hermione"));
    }

    @Test
    void testCreateStudent() throws Exception {
        Student input = new Student();
        input.setName("Ron");
        input.setAge(13);
        Student saved = new Student();
        saved.setId(2L);
        saved.setName("Ron");
        saved.setAge(13);
        Mockito.when(studentService.addStudent(Mockito.any())).thenReturn(saved);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ron"))
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void testEditStudent() throws Exception {
        Student updated = new Student();
        updated.setId(3L);
        updated.setName("Draco");
        updated.setAge(14);
        Mockito.when(studentService.editStudent(Mockito.any())).thenReturn(updated);

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Draco"));
    }

    @Test
    void testDeleteStudent() throws Exception {
        Mockito.doNothing().when(studentService).deleteStudent(4L);

        mockMvc.perform(delete("/student/4"))
                .andExpect(status().isOk());
    }

    @Test
    void testUploadAvatar() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "avatar",
                "test.png",
                "image/png",
                "some image content".getBytes()
        );

        Mockito.doNothing().when(studentService).uploadAvatar(Mockito.eq(1L), Mockito.any());

        mockMvc.perform(multipart("/student/1/avatar")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Avatar uploaded successfully"));
    }

    @Test
    void testDownloadAvatar() throws Exception {
        byte[] data = "test".getBytes();
        Avatar avatar = new Avatar();
        avatar.setData(data);
        avatar.setMediaType("image/png");

        Mockito.when(studentService.findAvatar(1L)).thenReturn(avatar);

        mockMvc.perform(get("/student/1/avatar/preview"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/png"))
                .andExpect(content().bytes(data));
    }
}
