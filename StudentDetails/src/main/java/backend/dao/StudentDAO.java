package backend.dao;

import backend.entity.StudentEntity;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentDAO extends JpaRepository<StudentEntity, Long> {
	@Query("SELECT DISTINCT s.studentClass FROM StudentEntity s")
    List<String> findDistinctStudentClasses();
	
	@Query("SELECT DISTINCT s.studentDOJ FROM StudentEntity s")
    List<Date> findDistinctStudentDOJ();
	
	@Query("SELECT DISTINCT s.studentDistrict FROM StudentEntity s")
    List<String> findDistinctStudentDistricts();

    @Query("SELECT DISTINCT s.studentGrade FROM StudentEntity s")
    List<String> findDistinctStudentGrades();
    
    @Query("SELECT DISTINCT s.studentFeesPaid FROM StudentEntity s")
    List<String> findDistinctFeesPaid();
}
