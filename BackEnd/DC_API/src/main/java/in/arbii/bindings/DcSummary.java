package in.arbii.bindings;

import java.util.List;

import lombok.Data;

@Data
public class DcSummary {

	private Income income;
	
	private Education education;
	
	private List<Child> childs;
	
	private String planName;
}
