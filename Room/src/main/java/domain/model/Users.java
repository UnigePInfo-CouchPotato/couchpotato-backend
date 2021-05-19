package domain.model;

import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class Users {
	@Id
	private int id;
	@NotNull
	private String username;
	@NotNull
	private String email;
	@NotNull
	private String saltedPwd;
	@NotNull
	private Date registrationDate;
}
