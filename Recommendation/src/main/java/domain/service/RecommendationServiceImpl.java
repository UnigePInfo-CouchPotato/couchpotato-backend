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
        String url = "https://api.themoviedb.org/3/genre/movie/list?api_key=b3299a1aa5ae43a9ae35cb544503117f";

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(url);

        Response response = webTarget.request(MediaType.TEXT_PLAIN).get();

        if (response.getStatus() != 200) {
            return "Failed : HTTP error code : " + response.getStatus();
        }

        return response.readEntity(String.class);

    }

    public String getAllFilmSelected(String idGenres){
        // generate page num
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=b3299a1aa5ae43a9ae35cb544503117f&language=en-US&sort_by=vote_average.desc&include_adult=false&include_video=false&page=1&with_genres="+idGenres+"&with_watch_monetization_types=flatrate";

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(url);

        Response response = webTarget.request(MediaType.TEXT_PLAIN).header("result", 5).get();

        // jsonStrin = response.getEntity()
        // JsonObject json = JsonObject.fromString(jsonString)
        
        // order by field...
        // get n first elements
        // return that
        
        if (response.getStatus() != 200) {
            return "Failed : HTTP error code : " + response.getStatus();
        }

        return response.readEntity(String.class);

    }

    public String getAllDetail(String detail){
        String url = "https://api.themoviedb.org/3/movie/"+detail+"?api_key=b3299a1aa5ae43a9ae35cb544503117f";

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(url);

        Response response = webTarget.request(MediaType.TEXT_PLAIN).get();

        if (response.getStatus() != 200) {
            return "Failed : HTTP error code : " + response.getStatus();
        }

        return response.readEntity(String.class);

    }
}
