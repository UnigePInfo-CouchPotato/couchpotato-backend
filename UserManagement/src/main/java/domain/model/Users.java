package domain.model;

import java.util.Date;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name ="Users")
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
