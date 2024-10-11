package in.arbii.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "DC_EDUCATION")
public class DcEducationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eduId;
	
	private Long caseNum;
	
	private String highestQualification;
	
	private Integer graduationYear;
	
	private String universityName;
	
}
