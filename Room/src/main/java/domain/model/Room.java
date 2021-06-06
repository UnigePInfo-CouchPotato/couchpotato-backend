package domain.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name ="Room")
public class Room {

	@Id
	private String roomId;
	@NotNull
	private int roomAdminId;
	@NotNull
	private boolean roomClosed = false;
	@NotNull
	private int numberOfMovies = 0;

}
