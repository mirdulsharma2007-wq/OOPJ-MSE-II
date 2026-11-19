
package com.kiet.studentmanagement.controller;

import com.kiet.studentmanagement.entity.Student;
import com.kiet.studentmanagement.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Comparator;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    // Add Student
    @PostMapping
    public Student addStudent(@RequestBody Student student) {

        if (repository.existsById(student.getStudentId())) {
            throw new RuntimeException("Student ID already exists");
        }

        if (student.getProgrammingScore() < 0 ||
                student.getProgrammingScore() > 100 ||
                student.getMathematicsScore() < 0 ||
                student.getMathematicsScore() > 100) {
            throw new RuntimeException("Scores must be between 0 and 100");
        }

        return repository.save(student);
    }

    // Retrieve All Students
    @GetMapping
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    // Search Student By ID
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable int id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    // Update Student Scores and Skills
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable int id,
                                 @RequestBody Student updatedStudent) {

        Student student = getStudentById(id);

        if (updatedStudent.getProgrammingScore() < 0 ||
                updatedStudent.getProgrammingScore() > 100 ||
                updatedStudent.getMathematicsScore() < 0 ||
                updatedStudent.getMathematicsScore() > 100) {
            throw new RuntimeException("Scores must be between 0 and 100");
        }

        student.setProgrammingScore(updatedStudent.getProgrammingScore());
        student.setMathematicsScore(updatedStudent.getMathematicsScore());
        student.setTechnicalSkills(updatedStudent.getTechnicalSkills());

        return repository.save(student);
    }

    // Delete Student
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable int id) {
        Student student = getStudentById(id);
        repository.delete(student);
        return "Student deleted successfully";
    }

    // Display Merit List
    @GetMapping("/merit")
    public List<Student> getMeritList() {
        return repository.findAll().stream()
                .sorted(Comparator.comparingDouble(
                        (Student s) -> (s.getProgrammingScore()
                                + s.getMathematicsScore()) / 2
                ).reversed())
                .toList();
    }
}
