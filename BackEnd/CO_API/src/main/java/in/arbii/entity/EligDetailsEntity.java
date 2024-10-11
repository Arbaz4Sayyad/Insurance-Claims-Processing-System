package in.arbii.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ED_ELIG_DTLS")
public class EligDetailsEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer edTraceId;
	
	private Long caseNum;
	
	private String holderName;
	
    private Long holderSsn;
	
	private String planName;

	private String planStatus;
	
	private LocalDate planStartDate;
	
	private LocalDate planEndDate;
	
	private Double benefitAmt;
	
	private String denialReason;

}