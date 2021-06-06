package domain.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(String genreIds) {
        this.genreIds = genreIds;
    }

    public Preference() {}

    public Preference(int userId, String genreIds) {
        this.userId = userId;
        this.genreIds = genreIds;
    }
}