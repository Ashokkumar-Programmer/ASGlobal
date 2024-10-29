package backend.controller;

import backend.dao.StudentDAO;
import backend.entity.StudentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StudentController {

    @Autowired
    private StudentDAO studentDAO;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    private boolean matchesDate(String search, Date date) {
        if (date == null) return false;

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        try {
            LocalDate parsedDate = LocalDate.parse(search, inputFormatter);
            String formattedDate = parsedDate.format(outputFormatter);
            return formattedDate.equals(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    @GetMapping("/students/search")
    public String searchStudents(@RequestParam(required = false, defaultValue = "") String search,
                                 @RequestParam(required = false) String classes,
                                 @RequestParam(required = false) String doj,
                                 @RequestParam(required = false) String address,
                                 @RequestParam(required = false) String grade,
                                 @RequestParam(required = false) String fees,
                                 Model model) {
        List<StudentEntity> allStudents = studentDAO.findAll();
        List<StudentEntity> filteredStudents = allStudents.stream()
            .filter(student -> (search.isEmpty() || 
                                student.getStudentName().toLowerCase().contains(search.toLowerCase()) ||
                                student.getStudentRollno().toString().toLowerCase().contains(search.toLowerCase()) ||
                                student.getStudentClass().toLowerCase().contains(search.toLowerCase()) ||
                                student.getStudentDistrict().toLowerCase().contains(search.toLowerCase()) ||
                                student.getStudentGrade().toLowerCase().contains(search.toLowerCase()) ||
                                matchesDate(search, student.getStudentDOJ()) ||
                                matchesDate(search, student.getStudentDOB()) ||
                                student.getStudentFeesPaid().toLowerCase().equals(search.toLowerCase())) &&
                               (classes == null || classes.equals("None") || student.getStudentClass().equals(classes)) &&
                               (doj == null || doj.equals("None") || matchesDate(doj, student.getStudentDOJ())) &&
                               (address == null || address.equals("None") || student.getStudentDistrict().equals(address)) &&
                               (grade == null || grade.equals("None") || student.getStudentGrade().equals(grade)) &&
                               (fees == null || fees.equals("None") || student.getStudentFeesPaid().equals(fees)))
            .collect(Collectors.toList());

        model.addAttribute("students", filteredStudents);
        model.addAttribute("studentClasses", studentDAO.findDistinctStudentClasses());
        model.addAttribute("studentDOJs", studentDAO.findDistinctStudentDOJ());
        model.addAttribute("studentDistricts", studentDAO.findDistinctStudentDistricts());
        model.addAttribute("studentGrades", studentDAO.findDistinctStudentGrades());
        model.addAttribute("studentFees", studentDAO.findDistinctFeesPaid());
        
        return "view";
    }

    @GetMapping("/student")
    public String viewStudents(Model model) {
        List<StudentEntity> students = studentDAO.findAll();
        model.addAttribute("students", students);
        model.addAttribute("studentClasses", studentDAO.findDistinctStudentClasses());
        model.addAttribute("studentDOJs", studentDAO.findDistinctStudentDOJ());
        model.addAttribute("studentDistricts", studentDAO.findDistinctStudentDistricts());
        model.addAttribute("studentGrades", studentDAO.findDistinctStudentGrades());
        model.addAttribute("studentFees", studentDAO.findDistinctFeesPaid());
        return "view";
    }

    @GetMapping("/students/new")
    public String createStudentForm(Model model) {
        model.addAttribute("student", new StudentEntity());
        return "create";
    }

    @PostMapping("/students")
    public String saveStudent(@ModelAttribute("student") StudentEntity student, Model model) {
        if (studentDAO.existsById(student.getStudentRollno())) {
            model.addAttribute("error", "Student with this roll number already exists.");
            return "create";
        }
        studentDAO.save(student);
        return "redirect:/student";
    }

    @GetMapping("/students/edit/{id}")
    public String editStudentForm(@PathVariable("id") Long id, Model model) {
        StudentEntity student = studentDAO.findById(id).orElse(null);
        if (student == null) {
            model.addAttribute("error", "Student not found.");
            return "redirect:/student";
        }
        model.addAttribute("student", student);
        return "edit";
    }

    @PostMapping("/students/{id}")
    public String updateStudent(@PathVariable("id") Long id, @ModelAttribute("student") StudentEntity student) {
        if (!studentDAO.existsById(id)) {
            return "redirect:/student";
        }
        student.setStudentRollno(id);
        studentDAO.save(student);
        return "redirect:/student";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        if (studentDAO.existsById(id)) {
            studentDAO.deleteById(id);
        }
        return "redirect:/student";
    }

    // REST API
    
    @GetMapping("/api/students")
    public ResponseEntity<List<StudentEntity>> getAllStudents() {
        List<StudentEntity> students = studentDAO.findAll();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/api/students/search")
    public ResponseEntity<List<StudentEntity>> searchStudentsAPI(@RequestParam(required = false, defaultValue = "") String search,
                                                                 @RequestParam(required = false) String classes,
                                                                 @RequestParam(required = false) String doj,
                                                                 @RequestParam(required = false) String address,
                                                                 @RequestParam(required = false) String grade,
                                                                 @RequestParam(required = false) String fees) {
        List<StudentEntity> allStudents = studentDAO.findAll();
        List<StudentEntity> filteredStudents = allStudents.stream()
            .filter(student -> (search.isEmpty() || 
                                student.getStudentName().toLowerCase().contains(search.toLowerCase()) ||
                                student.getStudentRollno().toString().toLowerCase().contains(search.toLowerCase()) ||
                                student.getStudentClass().toLowerCase().contains(search.toLowerCase()) ||
                                student.getStudentDistrict().toLowerCase().contains(search.toLowerCase()) ||
                                student.getStudentGrade().toLowerCase().contains(search.toLowerCase()) ||
                                matchesDate(search, student.getStudentDOJ()) ||
                                matchesDate(search, student.getStudentDOB()) ||
                                student.getStudentFeesPaid().toLowerCase().equals(search.toLowerCase())) &&
                               (classes == null || classes.equals("None") || student.getStudentClass().equals(classes)) &&
                               (doj == null || doj.equals("None") || matchesDate(doj, student.getStudentDOJ())) &&
                               (address == null || address.equals("None") || student.getStudentDistrict().equals(address)) &&
                               (grade == null || grade.equals("None") || student.getStudentGrade().equals(grade)) &&
                               (fees == null || fees.equals("None") || student.getStudentFeesPaid().equals(fees)))
            .collect(Collectors.toList());

        return ResponseEntity.ok(filteredStudents);
    }

    @PostMapping("/api/students")
    public ResponseEntity<StudentEntity> createStudentAPI(@RequestBody StudentEntity student) {
    	if(student.getStudentClass().equals("")||student.getStudentDistrict().equals("")||
    			student.getStudentDOB().equals("")||student.getStudentDOJ().equals("")||
    			student.getStudentFeesPaid().equals("")||student.getStudentGrade().equals("")||
    			student.getStudentName().equals("")||student.getStudentRollno().equals("")) {
    		return ResponseEntity.status(HttpStatus.CONFLICT).build();
    	}
        if (studentDAO.existsById(student.getStudentRollno())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        studentDAO.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }

    @PutMapping("/api/students/{id}")
    public ResponseEntity<StudentEntity> updateStudentAPI(@PathVariable Long id, @RequestBody StudentEntity student) {
    	if(student.getStudentClass().equals("")||student.getStudentDistrict().equals("")||
    			student.getStudentDOB().equals("")||student.getStudentDOJ().equals("")||
    			student.getStudentFeesPaid().equals("")||student.getStudentGrade().equals("")||
    			student.getStudentName().equals("")||student.getStudentRollno().equals("")) {
    		return ResponseEntity.status(HttpStatus.CONFLICT).build();
    	}
    	if (!studentDAO.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        student.setStudentRollno(id);
        studentDAO.save(student);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/api/students/{id}")
    public ResponseEntity<String> deleteStudentAPI(@PathVariable Long id) {
        if (studentDAO.existsById(id)) {
            studentDAO.deleteById(id);
            return ResponseEntity.ok("Rollno "+id + " has been deleted.");
        }
        return ResponseEntity.notFound().build();
    }
}
