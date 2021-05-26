package domain.service;


import javax.ws.rs.core.Response;

public interface RecommendationService {
    Response getAllGenres();
    Response getAllDetail(String detail);
    Response getAllFilmSelected(String idGenres);
}
