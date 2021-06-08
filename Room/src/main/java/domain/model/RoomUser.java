package domain.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name ="RoomUser")
public class RoomUser implements Serializable {

	@Id
	private String roomId;
	@Id
	private String userNickname;
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate = new Date();
	@NotNull
	private String votes = "[]";

}
