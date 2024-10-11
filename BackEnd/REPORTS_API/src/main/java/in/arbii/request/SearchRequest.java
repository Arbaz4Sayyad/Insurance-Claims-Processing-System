package in.arbii.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SearchRequest {

	private String planName;
	private String planStatus;
	private LocalDate planStartDate;
	private LocalDate planEndDate;
	
}