package domain.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name ="Room_User")
public class Room_User implements Serializable {

	@Id
	private int roomId;
	@Id
	private int userId;
	@NotNull
	private Date creationDate;

}