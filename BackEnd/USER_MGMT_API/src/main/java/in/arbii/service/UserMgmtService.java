package in.arbii.service;

import java.util.List;

import in.arbii.bindings.ActivateAccount;
import in.arbii.bindings.Login;
import in.arbii.bindings.User;

public interface UserMgmtService {
	
	public boolean saveUser(User user);
	
	public boolean activateUser(ActivateAccount activateAcc);
	
	public List<User> getAllUsers();
	
	public User getUserById(Integer userId);
	
	public boolean deleteUser(Integer userId);
	
	public boolean changeAccountStatus(Integer userId, String accStatus);
	
	public String login(Login login);
	
	public String forgotPwd(String email);
}
