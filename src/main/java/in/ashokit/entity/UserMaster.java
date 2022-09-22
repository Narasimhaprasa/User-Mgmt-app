package in.ashokit.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "USER_MASTER")
public class UserMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer userId;

	@Column(length = 30)
	private String fullName;

	@Column(length = 40)
	private String email;

	@Column(length = 10)
	private Long mobileNumber;

	@Column(length = 5)
	private String gender;

	private LocalDate dob;

	private Long ssn;

	@Column(length = 10)
	private String activeAccount;

	@Column(length = 12)
	private String tempPassword;
	@CreationTimestamp
	@Column(name = "create_date", updatable = false)
	private LocalDate createDate;
	@UpdateTimestamp
	@Column(name="update_date" , insertable =  false)
	private LocalDate updateDate;

	private String createdBy;

	private String updatedBy;

}
