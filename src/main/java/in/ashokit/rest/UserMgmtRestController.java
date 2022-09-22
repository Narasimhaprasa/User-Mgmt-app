package in.ashokit.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.ashokit.bindings.ActivateAccount;
import in.ashokit.bindings.Login;
import in.ashokit.bindings.User;
import in.ashokit.service.IUserMgmtService;

@RestController
public class UserMgmtRestController {
	@Autowired
	private IUserMgmtService service;

	@PostMapping("/save")
	public ResponseEntity<String> saveUser(@RequestBody User user) {
		boolean isSaved = service.saveUser(user);
		if (isSaved) {
			return new ResponseEntity<String>("Record is saved ", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<String>("Record is not saved", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/activateAcc")
	public ResponseEntity<String> activateUserAccount(@RequestBody ActivateAccount activateAcc) {
		boolean isActivated = service.activateUserAccount(activateAcc);
		if (isActivated) {
			return new ResponseEntity<String>("User account is activated", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("User account is not activating", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/usersData")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> usersList = service.getAllUsers();
		return new ResponseEntity<List<User>>(usersList, HttpStatus.OK);
	}

	@GetMapping("/get/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
		User userById = service.getUserById(userId);
		if (userById != null) {
			return new ResponseEntity<User>(userById, HttpStatus.OK);
		} else {
			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<String> deleteUserById(@PathVariable Integer userId) {
		boolean isDeleted = service.deleteUserById(userId);
		if (isDeleted) {
			return new ResponseEntity<String>("Record is Deleted Successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Record is not deleting", HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/changeStatus/{userId}/{accStatus}")
	public ResponseEntity<String> changeAccountStatus(@PathVariable Integer userId, @PathVariable String accStatus) {
		boolean isStatusChanged = service.changeAccountStatus(userId, accStatus);
		if (isStatusChanged) {
			return new ResponseEntity<String>(" Account Status is changed", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Account Status is not changed", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Login login) {
		String message = service.login(login);
		return new ResponseEntity<String>(message, HttpStatus.CREATED);
	}

	@GetMapping("/forgotPwd")
	public ResponseEntity<String> forgotPassword(String email) {
		String message = service.forgotPassword(email);
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}
}
