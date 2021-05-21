package domain.model;

import java.util.Date;

import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "Users")
public class Users {

	@Id
	@Column(name="id")
//	@SequenceGenerator(name = "USERS_SEQ", sequenceName = "USERS_SEQ")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ")
	private int id;

	@NotNull
	@Column(name="username")
	private String username;

	@NotNull
	@Column(name="email")
	private String email;

	@NotNull
	@Column(name="saltedPwd")
	private String saltedPwd;

	@NotNull
	@Column(name="registrationDate")
	private Date registrationDate;

	public Users() {}

	public Users(int id,String username, String email, String saltedPwd,Date registrationDate) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.saltedPwd = saltedPwd;
		this.registrationDate = registrationDate;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSaltedPwd() {
		return saltedPwd;
	}

	public void setSaltedPwd(String saltedPwd) {
		this.saltedPwd = saltedPwd;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
}
