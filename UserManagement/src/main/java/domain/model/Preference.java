package domain.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "Preference")
public class Preference {
    @Id
    @Column(name="userId")
    private int userId;

    @NotNull
    @Column(name="genreIds")
    private String genreIds;
}