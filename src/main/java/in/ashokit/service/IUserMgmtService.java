package in.ashokit.service;

import java.util.List;

import in.ashokit.bindings.ActivateAccount;
import in.ashokit.bindings.Login;
import in.ashokit.bindings.User;

public interface IUserMgmtService {
	public boolean saveUser(User user);

	public boolean activateUserAccount(ActivateAccount activateAcc);

	public List<User> getAllUsers();

	public User getUserById(Integer userId);

	public boolean deleteUserById(Integer userId);

	public boolean changeAccountStatus(Integer userId, String accStatus);

	public String login(Login login);

	public String forgotPassword(String email);
}
