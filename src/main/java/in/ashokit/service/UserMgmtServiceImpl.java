package in.ashokit.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import in.ashokit.bindings.ActivateAccount;
import in.ashokit.bindings.Login;
import in.ashokit.bindings.User;
import in.ashokit.email.MailSending;
import in.ashokit.entity.UserMaster;
import in.ashokit.repo.UserMgmtRepo;

@Service
public class UserMgmtServiceImpl implements IUserMgmtService {
	@Autowired
	private UserMgmtRepo userRepo;
	@Autowired
	private MailSending emailUtils;

	@Override
	public boolean saveUser(User user) {
		UserMaster entity = new UserMaster();
		BeanUtils.copyProperties(user, entity);

		entity.setTempPassword(generateRandomPassword());
		entity.setActiveAccount("In-Active");
		UserMaster save = userRepo.save(entity);

		String subject = "Your Registration success";
		String filename = "REG-EMAIL-BODY.txt";
		String body = readEmailBody(entity.getFullName(), entity.getTempPassword(), filename);
		emailUtils.sendEmail(user.getEmail(), subject, body);
		return save.getUserId() != null;
	}

	@Override
	public boolean activateUserAccount(ActivateAccount activateAcc) {
		UserMaster entity = new UserMaster();
		entity.setEmail(activateAcc.getEmail());
		entity.setTempPassword(activateAcc.getTempPassword());
		Example<UserMaster> of = Example.of(entity);
		List<UserMaster> findAll = userRepo.findAll(of);
		if (findAll.isEmpty()) {
			return false;
		} else {
			UserMaster userMaster = findAll.get(0);
			userMaster.setTempPassword(activateAcc.getNewPassword());
			userMaster.setActiveAccount("Active");
			userRepo.save(userMaster);
			return true;
		}

	}

	@Override
	public List<User> getAllUsers() {
		List<UserMaster> findAll = userRepo.findAll();
		List<User> userLists = new ArrayList<User>();
		for (UserMaster entity : findAll) {
			User user = new User();
			BeanUtils.copyProperties(entity, user);
			userLists.add(user);
		}
		return userLists;
	}

	@Override
	public User getUserById(Integer userId) {
		Optional<UserMaster> findById = userRepo.findById(userId);
		if (findById.isPresent()) {
			UserMaster userMaster = findById.get();
			User user = new User();
			BeanUtils.copyProperties(userMaster, user);
			return user;
		} else {
			return null;
		}
	}

	@Override
	public boolean deleteUserById(Integer userId) {
		Optional<UserMaster> findById = userRepo.findById(userId);
		if (findById.isPresent()) {
			userRepo.deleteById(userId);
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean changeAccountStatus(Integer userId, String accStatus) {
		Optional<UserMaster> findById = userRepo.findById(userId);
		if (findById.isPresent()) {
			UserMaster userMaster = findById.get();
			userMaster.setActiveAccount(accStatus);
			userRepo.save(userMaster);
			return true;
		} else {
			return false;
		}

	}

	@Override
	public String login(Login login) {

		UserMaster entity = new UserMaster();
		entity.setEmail(login.getEmail());
		entity.setTempPassword(login.getTempPassword());
		Example<UserMaster> of = Example.of(entity);
		List<UserMaster> findAll = userRepo.findAll(of);
		if (findAll.isEmpty()) {
			return "Invalid credentials";
		}

		UserMaster userMaster = findAll.get(0);
		if (userMaster.getActiveAccount().equals("Active")) {
			return "Success";
		} else {
			return "Account is not activated";
		}

	}

	@Override
	public String forgotPassword(String email) {
		UserMaster entity = new UserMaster();
		entity.setEmail(email);
		Example<UserMaster> of = Example.of(entity);
		List<UserMaster> findAll = userRepo.findAll(of);
		String subject = "Forgot Password";
		String fileName = "RECOVER-EMAIL-BODY.txt";
		if(findAll.isEmpty()) {
			return "wrong credentials";
		}
		UserMaster userMaster  =findAll.get(0);
		String body = readEmailBody(userMaster.getFullName(), userMaster.getTempPassword(), fileName);
		boolean sendMail = emailUtils.sendEmail(email, subject, body);
		if (sendMail) { 
			return "Password is send to your registered email";
		} 
		 return null;
		 
	}

	private String generateRandomPassword() {
		final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();
		int len = 6;
		for (int i = 0; i < len; i++) {
			int randomIndex = random.nextInt(chars.length());
			sb.append(chars.charAt(randomIndex));
		}
		return sb.toString();
	}

	private String readEmailBody(String fullname, String pwd, String filename) {
		String url = "";
		String mailBody = null;
		try {
			FileReader reader = new FileReader(filename);
			BufferedReader bufferReader = new BufferedReader(reader);
			String readLine = bufferReader.readLine();
			StringBuffer buffer = new StringBuffer();
			while (readLine != null) {
				buffer.append(readLine);
				readLine = bufferReader.readLine();
			}
			bufferReader.close();
			// reader.close();
			mailBody = buffer.toString();
			mailBody = mailBody.replaceAll("FULLNAME", fullname);
			mailBody = mailBody.replaceAll("TEMP-PWD", pwd);
			mailBody = mailBody.replaceAll("URL", url);
			mailBody = mailBody.replaceAll("PWD", pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mailBody;
	}

}
