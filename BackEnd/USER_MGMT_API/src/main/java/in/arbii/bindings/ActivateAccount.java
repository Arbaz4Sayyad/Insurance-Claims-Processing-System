package in.arbii.bindings;

import lombok.Data;

@Data
public class ActivateAccount {

	private String email;
	
	private String tempPwd;
	
	private String newPwd;
	
	private String confirmPwd;
	
}
