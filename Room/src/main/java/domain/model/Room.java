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
	@Column(columnDefinition="TEXT")
	private String roomAdmin;
	@NotNull
	@Column(columnDefinition="TEXT")
	private String userPreferences = "";
	@NotNull
	@Column(columnDefinition="TEXT")
	private String movies = "";
	@NotNull
	private boolean roomClosed = false;
	@NotNull
	private boolean usersCanVote = false;
	@NotNull
	private boolean usersCanJoin = true;
	@NotNull
	private int numberOfMovies = 0;

}
