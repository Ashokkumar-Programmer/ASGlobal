package backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Entity
@Table(name = "studentdata")
public class StudentEntity {

    @Id
    @Column(name="studentRollno")
    private Long studentRollno;
    
    @Column(name="studentName")
    private String studentName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="studentDOB")
    private Date studentDOB;

    @Column(name="studentClass")
    private String studentClass;

    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="studentDOJ")
    private Date studentDOJ;

    @Column(name="StudentDistrict")
    private String studentDistrict;
    
    @Column(name="StudentGrade")
    private String studentGrade;
    
    @Column(name="studentFeesPaid")
    private String studentFeesPaid;

    public Long getStudentRollno() {
		return studentRollno;
	}
	public void setStudentRollno(Long studentRollno) {
		this.studentRollno = studentRollno;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public Date getStudentDOB() {
		return studentDOB;
	}
	public void setStudentDOB(Date studentDOB) {
		this.studentDOB = studentDOB;
	}
	public String getStudentClass() {
		return studentClass;
	}
	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}
	public Date getStudentDOJ() {
		return studentDOJ;
	}
	public void setStudentDOJ(Date studentDOJ) {
		this.studentDOJ = studentDOJ;
	}
	public String getStudentDistrict() {
		return studentDistrict;
	}
	public void setStudentDistrict(String studentDistrict) {
		this.studentDistrict = studentDistrict;
	}
	public String getStudentGrade() {
		return studentGrade;
	}
	public void setStudentGrade(String studentGrade) {
		this.studentGrade = studentGrade;
	}
	public String getStudentFeesPaid() {
		return studentFeesPaid;
	}
	public void setStudentFeesPaid(String studentFeesPaid) {
		this.studentFeesPaid = studentFeesPaid;
	}

    
}
