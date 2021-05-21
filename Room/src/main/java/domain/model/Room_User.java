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
	private String roomId;
	@Id
	private int userId;
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate = new Date();
	@Column(columnDefinition="TEXT CHECK (char_length(genres) <= 500)")
	@NotNull
	private String genres = "";
	@NotNull
	private String votes = "[0, 0, 0, 0, 0]";

}
