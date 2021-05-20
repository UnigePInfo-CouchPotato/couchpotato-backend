package domain.service;;

import javax.enterprise.context.ApplicationScoped;
import lombok.extern.java.Log;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.*;

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

        // TODO check whether idGenres is in fact a string of numbers separated by ',' otherwise return status code 400
        /*
        STATUS CODE 400
            {
                success: false,
                error: "Malformed request"
            }
        */

        // TODO Remove sorting by votes here as it forces determinism in the response.
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=b3299a1aa5ae43a9ae35cb544503117f&language=en-US&sort_by=vote_average.desc&include_adult=false&include_video=false&page=1&with_genres="+idGenres+"&with_watch_monetization_types=flatrate";

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(url);

        Response response = webTarget.request(MediaType.TEXT_PLAIN).header("result", 5).get();

        // JSONObject json = new JSONObject(response.getEntity());
        // JSONArray results = new JSONArray();
        
        // int maxPages = json.opt("total_pages");
        // if maxPages is not null and is greater than 5
        //      select 5 random unique numbers between 1 and maxPages
        // else
        //      take all page numbers from 1 to max_pages

        // query each URL then build the JSONObject from the response
        // Iterate over the JSONArray that is at: json.getJSONArray("results")
        // Add each JSONObject within to results ^

        // sort by vote_average (this implementation of Collections.sort would work: https://discourse.processing.org/t/sorting-a-jsonarray-by-one-of-its-values/4911/6)

        // Take the first 5
        // Return those in the format:
        /*
        STATUS CODE 200
            {
                success: true,
                results: [
                    { (movie details 1) },
                    { (movie details 2) },
                    { (movie details 3) },
                    { (movie details 4) },
                    { (movie details 5) }
                ]
            }
         */
        
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
