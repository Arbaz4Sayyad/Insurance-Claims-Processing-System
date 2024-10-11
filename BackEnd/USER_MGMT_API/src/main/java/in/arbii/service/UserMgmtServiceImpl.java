package in.arbii.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import in.arbii.bindings.ActivateAccount;
import in.arbii.bindings.Login;
import in.arbii.bindings.User;
import in.arbii.entity.UserMaster;
import in.arbii.repo.UserMasterRepo;
import in.arbii.utils.EmailUtils;

@Service
public class UserMgmtServiceImpl implements UserMgmtService {

	@Autowired
	private UserMasterRepo userMasterRepo;

	@Autowired
	private EmailUtils emailUtils;
	
	private Logger logger = LoggerFactory.getLogger(UserMgmtServiceImpl.class);
	
	private Random random = new Random();
	
	@Override
	public boolean saveUser(User user) {
		UserMaster entity = new UserMaster();
		BeanUtils.copyProperties(user, entity);

		entity.setPassword(generateRandomPwd());
		entity.setAccStatus("In-Active");

		UserMaster save = userMasterRepo.save(entity);

		// Send Registration Email
		String subject = "Your Registration Success";
		String fileName = "REG-EMAIL-BODY.txt";
		String body = readEmailBody(entity.getFullName(), entity.getPassword(), fileName);
		
		emailUtils.sendEmail(user.getEmail(), subject, body);

		return save.getUserId() != null;
	}

	@Override
	public boolean activateUser(ActivateAccount activateAcc) {

		UserMaster entity = new UserMaster();
		entity.setEmail(activateAcc.getEmail());
		entity.setPassword(activateAcc.getTempPwd());

		// select * from user_master where email=? and pwd=?
		Example<UserMaster> of = Example.of(entity);

		List<UserMaster> findAll = userMasterRepo.findAll(of);

		if (findAll.isEmpty()) {
			return false;
		} else {
			UserMaster userMaster = findAll.get(0);
			userMaster.setPassword(activateAcc.getNewPwd());
			userMaster.setAccStatus("Active");
			userMasterRepo.save(userMaster);
			return true;
		}
	}

	@Override
	public List<User> getAllUsers() {
		List<UserMaster> findAll = userMasterRepo.findAll();
		List<User> users = new ArrayList<>();
		for (UserMaster entity : findAll) {
			User user = new User();
			BeanUtils.copyProperties(entity, user);
			users.add(user);
		}
		return users;

	}

	@Override
	public User getUserById(Integer userId) {
		Optional<UserMaster> findById = userMasterRepo.findById(userId);
		if (findById.isPresent()) {
			User user = new User();
			UserMaster userMaster = findById.get();
			BeanUtils.copyProperties(userMaster, user);
			return user;
		}
		return null;
	}

	@Override
	public boolean deleteUser(Integer userId) {
		try {
			userMasterRepo.deleteById(userId);
			return true;
		} catch (Exception e) {
			logger.error("Exception Occured", e);
		}
		return false;
	}

	@Override
	public boolean changeAccountStatus(Integer userId, String accStatus) {

		Optional<UserMaster> findById = userMasterRepo.findById(userId);
		if (findById.isPresent()) {
			UserMaster userMaster = findById.get();
			userMaster.setAccStatus(accStatus);
			userMasterRepo.save(userMaster);
			return true;
		}
		return false;
	}

	@Override
	public String login(Login login) {
		
		/*
		 * UserMaster entity = new UserMaster(); entity.setEmail(login.getEmail());
		 * entity.setPassword(login.getPassword());
		 * 
		 * //select * from user_master where email=? and pwd=? Example<UserMaster> of =
		 * Example.of(entity);
		 * 
		 * List<UserMaster> findAll = userMasterRepo.findAll(of);
		 * if (findAll.isEmpty()) {
			return "Invalid Credentials";
		   } else {
			UserMaster userMaster = findAll.get(0);
			if(userMaster.getAccStatus().equals("Active")) {
				return "SUCCESS";
			}else {
				return "Account not Activated";
			}
		}
		 */
		
		//findByEmailAndPassword method created in repository
		UserMaster entity = userMasterRepo.findByEmailAndPassword(login.getEmail(), login.getPassword());
		
		if (entity == null) {
			return "Invalid Credentials";
		} 
		if(entity.getAccStatus().equals("Active")) {
			return "SUCCESS";
		}else {
			return "Account not Activated";
		}
	}

	@Override
	public String forgotPwd(String email) {
		
		UserMaster entity = userMasterRepo.findByEmail(email);
		if (entity == null) {
			return "Invalid Email";
		} 
	
		// Send pwd to user  email
		
		String subject = "Forgot Password Recovery";
		String fileName = "RECOVER-PWD-MAIL-BODY.txt";
		String body = readEmailBody(entity.getFullName(), entity.getPassword(), fileName);
		boolean sendEmail = emailUtils.sendEmail(email, subject, body);
		
		if(sendEmail) {
			return "Password sent to your registered email";
		}
		return null;
	}

	private String generateRandomPwd() {

		String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;

		StringBuilder sb = new StringBuilder();

		int length = 6;
		for (int i = 0; i < length; i++) {
			int index = this.random.nextInt(alphaNumeric.length());
			char randomChar = alphaNumeric.charAt(index);
			sb.append(randomChar);
		}
		return sb.toString();
	}
	
	private String readEmailBody(String fullName, String pwd, String fileName) {
		String url = "";
		String mailBody = null;
		try(
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			){
			
			StringBuilder builder = new  StringBuilder();
			
			String line = br.readLine();
			while(line!= null) {
				builder.append(line);
				line = br.readLine();
			}
		
			mailBody = builder.toString();
			mailBody = mailBody.replace("{FULLNAME}", fullName);
			mailBody = mailBody.replace("{TEMP-PWD}", pwd);
			mailBody = mailBody.replace("{URL}", url);
			mailBody = mailBody.replace("{PWD}", pwd);
			
		}catch(Exception e) {
			logger.error("Exception Occured", e);
		}
		return mailBody;
	}
}
