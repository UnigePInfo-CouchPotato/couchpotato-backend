package domain.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name ="Room")
public class Room {

	@Id
	private int roomId;
	@NotNull
	private int roomAdminId;
	@NotNull
	private boolean roomClosed;

}