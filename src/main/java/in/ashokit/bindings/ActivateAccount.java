package in.ashokit.bindings;

import lombok.Data;

@Data
public class ActivateAccount {
 
	private String email;
	private String tempPassword;
	private String newPassword;
	private String confirmPassword;
}
