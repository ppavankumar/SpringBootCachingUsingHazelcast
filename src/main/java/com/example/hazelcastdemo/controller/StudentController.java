package com.example.hazelcastdemo.controller;

import com.example.hazelcastdemo.entity.Student;
import com.example.hazelcastdemo.service.StudentService;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/api/students")
@Slf4j
@CacheConfig(cacheNames = "students")
public class StudentController {
    private final StudentService studentService;
    private final HazelcastInstance hazelcastInstance;

    private ConcurrentMap<Object, Object> retrieveMap() {
        return hazelcastInstance.getMap("map");
    }

    public StudentController(StudentService studentService, HazelcastInstance hazelcastInstance) {
        this.studentService = studentService;
        this.hazelcastInstance = hazelcastInstance;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#id")
    public Student getStudentById(@PathVariable Integer id) {
        log.info("fetching the student with id {} from DB", id);
        Student existingStudent = studentService.getStudentById(id);
//        retrieveMap().put(id, existingStudent);
//        return existingStudent;
        Student cacheStudent = (Student) retrieveMap().get(id);

        if (cacheStudent == null) {
            retrieveMap().put(id, existingStudent);
            return (Student) retrieveMap().get(id);
        }
        return cacheStudent;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping("/{id}")
    @CachePut(key = "#id")
    public Student updateStudent(@RequestBody Student student, @PathVariable Integer id) {
        Student updateStudent = studentService.updateStudent(student, id);
        retrieveMap().put(id, updateStudent);
        return updateStudent;
    }

    @DeleteMapping("/{id}")
    @CacheEvict(key = "#id")
    public Student deleteStudent(@PathVariable Integer id) {
        return studentService.deleteStudent(id);
    }
}

