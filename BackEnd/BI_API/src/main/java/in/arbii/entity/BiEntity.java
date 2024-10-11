package in.arbii.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "ISSUENCE")
public class BiEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String caseNumber;
	private String name;
	private String ssn;
	private BigDecimal benefitAmount;
	private String accountNumber;
	private String bankName;

}
