package domain.service;


import javax.ws.rs.core.Response;
import java.util.List;

public interface RecommendationService {
    Response getAllGenres();
    String getAllDetail(String detail);
    String getAllFilmSelected(String idGenres);
}
