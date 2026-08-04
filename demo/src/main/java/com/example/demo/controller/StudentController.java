package com.example.demo.controller;

import com.example.demo.model.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {
    private  List<Student> students=new ArrayList<>(List.of(
            new Student(1,60f,"esmail")
            ,new Student(2,80.0f,"taghrida")
    ));
    @GetMapping("/students")
    public ResponseEntity<List<Student>> getStudents(){
        return ResponseEntity.status(200).body(students);
    }
    @PostMapping ("/students")
    public ResponseEntity<String> createStudent(@RequestBody Student student){
        students.add(student);
        return ResponseEntity.status(201).body("created user");
    }
}
