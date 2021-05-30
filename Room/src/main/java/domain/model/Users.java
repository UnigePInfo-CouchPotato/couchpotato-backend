package domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	@JsonIgnore
	private String saltedPwd;
	@NotNull
	private Date registrationDate;
}
