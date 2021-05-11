package domain.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name ="Room_User")
public class Room_User {

	@Id
	private int userId;
	@NotNull
	private int roomId;
	@NotNull
	private Date creationDate;

}
