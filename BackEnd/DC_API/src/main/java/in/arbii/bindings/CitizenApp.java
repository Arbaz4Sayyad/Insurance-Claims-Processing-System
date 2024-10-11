package in.arbii.bindings;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CitizenApp {

	private String fullName;
	
	private String email;
	
	private Long phno;
	
	private Long ssn;
	
	private String gender;
	
	private LocalDate dob;
}
