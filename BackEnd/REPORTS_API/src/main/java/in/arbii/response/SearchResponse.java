package in.arbii.response;

import lombok.Data;

@Data
public class SearchResponse {

	private String name;
	private Long mobile;
	private String email;
	private Character gender;
	private Long ssn;	
	private String planName;
	private String planStatus;
	
}