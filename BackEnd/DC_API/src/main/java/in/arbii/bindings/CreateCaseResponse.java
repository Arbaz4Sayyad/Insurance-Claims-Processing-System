package in.arbii.bindings;

import java.util.Map;

import lombok.Data;

@Data
public class CreateCaseResponse {
	
	private Long caseNum;
	
	private Map<Integer, String> planNames;
}
