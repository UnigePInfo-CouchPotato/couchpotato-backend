package domain.service;


import java.util.List;

public interface RecommendationService {
    String getAllGenres();
    String getAllDetail(String detail);
    String getAllFilmSelected(String idGenres);
}
