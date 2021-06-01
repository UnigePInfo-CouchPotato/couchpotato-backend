package domain.service;

import domain.model.Preference;

import java.util.List;

public interface PreferenceService {
    List<Preference> getAll();
    Preference get(int userId);
    Long count();
    void updatePreference(Preference pref);
    boolean exists(int userId);
    void createPreference(int userID, String genreIdsString);
}