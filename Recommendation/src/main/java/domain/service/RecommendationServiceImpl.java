package domain.service;

import lombok.extern.java.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

import static java.util.Collections.sort;


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

    private String searchMoviesByPage_uri(String page, String idGenres) {
        return "https://api.themoviedb.org/3/discover/movie?api_key=b3299a1aa5ae43a9ae35cb544503117f&language=en-US&include_adult=false&include_video=false&page="+page+"&with_genres="+idGenres;
    }

    public String getAllFilmSelected(String idGenres){
        JSONObject errorMessage = new JSONObject(){{
            put("success", false);
            put("error", "Malformed request");
        }};

    // TODO check whether idGenres is in fact a string of numbers separated by ',' otherwise return status code 400
        /*
        STATUS CODE 400
            {
                success: false,
                error: "Malformed request"
            }
        */
        if (!(idGenres.contains(","))) { return errorMessage.toString();}


        //TODO select the page randomly.
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=b3299a1aa5ae43a9ae35cb544503117f&language=en-US&include_adult=false&include_video=false&with_genres="+idGenres;

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(url);

        Response response = webTarget.request(MediaType.TEXT_PLAIN).get();


        JSONObject jsnobject = new JSONObject(response.readEntity(String.class));
        JSONArray result = new JSONArray();

       //int maxPages = jsnobject.optInt("total_pages");
        int maxPages = 6;
        System.out.println("maxPages: "+maxPages);
        Random rn = new Random();
        System.out.println("hello");
        ArrayList <Integer> RandomPages = new ArrayList<Integer>();

        int a = 1;
        if (maxPages != 0 & maxPages >= 5){
            while (a <= 5){
                int b = rn.nextInt(maxPages) + 1;
                System.out.println("hello1");
                String url_rnd = searchMoviesByPage_uri(String.valueOf(b),idGenres);
                System.out.println("hello2");
                WebTarget webTarget_rnd = client.target(url_rnd);
                Response response_rnd = webTarget_rnd.request(MediaType.TEXT_PLAIN).get();
                JSONObject jsnobject_rnd = new JSONObject(response_rnd.readEntity(String.class));
                JSONArray array = new JSONArray(jsnobject_rnd.getJSONArray("results"));

                //JSONArray sorted = sort(array, "vote_average");

                System.out.println("Which page ? "+b);
                result.put(array);
                a++;
            }

        }
        else {
            while (a <= maxPages){
                String url_rnd = searchMoviesByPage_uri(String.valueOf(a),idGenres);
                WebTarget webTarget_rnd = client.target(url_rnd);
                Response response_rnd = webTarget_rnd.request(MediaType.TEXT_PLAIN).get();
                JSONObject jsnobject_rnd = new JSONObject(response_rnd.readEntity(String.class));
                JSONArray array = new JSONArray(jsnobject_rnd.getJSONArray("results"));
                JSONArray sorted = sort(array, "vote_average");
                System.out.println("Which page ? "+a);
                result.put(sorted);
                a++;
            }
        }

        //for(int i=0; i <= 4; i++){result.put(array.get(i));}

        return result.toString();

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

    }

    public JSONArray sort(JSONArray jsonArr, String sortBy) {
        JSONArray sortedJsonArray = new JSONArray();

        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArr.length(); i++) {
            jsonValues.add(jsonArr.getJSONObject(i));
        }
        final String KEY_NAME = sortBy;
        Collections.sort( jsonValues, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();

                valA = (String) a.get(KEY_NAME);
                valB = (String) b.get(KEY_NAME);

                return -valA.compareTo(valB);
            }
        });
        return sortedJsonArray;
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