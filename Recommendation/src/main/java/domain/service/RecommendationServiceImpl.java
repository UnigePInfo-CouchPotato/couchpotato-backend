package domain.service;

import javax.enterprise.context.ApplicationScoped;
import lombok.extern.java.Log;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Log
public class RecommendationServiceImpl implements RecommendationService {

    public RecommendationServiceImpl() {}


    public String getAllGenres(){
        String url = "https://api.themoviedb.org/3/genre/movie/list?api_key=3aacfef6a62a872d2a4717b9b6cd5283&language=en-US";

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(url);

        Response response = webTarget.request(MediaType.TEXT_PLAIN).get();

        if (response.getStatus() != 200) {
            return "Failed : HTTP error code : " + response.getStatus();
        }

        return response.readEntity(String.class);

    }
}
