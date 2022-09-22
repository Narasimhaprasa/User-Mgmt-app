package in.ashokit.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailSending {
	@Autowired
	private JavaMailSender mailSender;

	public boolean sendEmail(String to, String  subject, String body) {
		boolean isMailSent = false;
		try {
			MimeMessage createMimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(createMimeMessage);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			mailSender.send(createMimeMessage);
			isMailSent = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isMailSent;
	}
}
