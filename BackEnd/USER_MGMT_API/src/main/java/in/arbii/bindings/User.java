package in.arbii.bindings;

import java.time.LocalDate;

import lombok.Data;

@Data
public class User {
	
	private String fullName;
	
	private String email;
	
	private Long mobile;
	
	private Long ssn;
	
	private String gender;
	
	private LocalDate dob;
}
