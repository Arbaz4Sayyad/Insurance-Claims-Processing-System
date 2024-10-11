package in.arbii.entity;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "CITIZEN_APPS")
public class CitizenAppEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer app_id;
	
	private String fullName;
	
	private String email;
	
	private Long phno;
	
	private Long ssn;
	
	private String gender;
	
	private LocalDate dob;
	
	private String stateName;
	
	@CreationTimestamp
	private LocalDate createdDate;
	
	@CreationTimestamp
	private LocalDate updatedDate;	
	
	private String createdBy;	
	
	private String updatedBy;		
 
}
