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
import java.math.BigDecimal;

import static java.util.Collections.max;
import static java.util.Collections.sort;


@ApplicationScoped
@Log
public class RecommendationServiceImpl implements RecommendationService {
    private boolean isInteger(Object object) {
        if(object instanceof Integer) {
            return true;
        } else {
            String string = object.toString();

            try {
                Integer.parseInt(string);
            } catch(Exception e) {
                return false;
            }
        }

        return true;
    }

    public RecommendationServiceImpl() {}


    public Response getAllGenres(){
        String url = "https://api.themoviedb.org/3/genre/movie/list?api_key=b3299a1aa5ae43a9ae35cb544503117f";

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(url);

        Response response = webTarget.request(MediaType.TEXT_PLAIN).get();
        return Response.status(Response.Status.OK).entity(response.readEntity(String.class)).build();
    }

    private String searchMoviesByPage_uri(String page, String idGenres) {
        return "https://api.themoviedb.org/3/discover/movie?api_key=b3299a1aa5ae43a9ae35cb544503117f&language=en-US&include_adult=false&include_video=false&page="+page+"&with_genres="+idGenres;
    }

    public Response getAllFilmSelected(String idGenres){
        List<String> genres = Arrays.asList(idGenres.split(","));
        boolean areNumbers = genres.stream().allMatch(this::isInteger);
        if (!areNumbers) {
            JSONObject incorrectFormatMessage = new JSONObject(){{
                put("success", false);
                put("error", "Malformed request");
            }};
            return Response.status(Response.Status.fromStatusCode(400)).entity(incorrectFormatMessage.toString()).build();
        }

        String url = "https://api.themoviedb.org/3/discover/movie?api_key=b3299a1aa5ae43a9ae35cb544503117f&language=en-US&include_adult=false&include_video=false&with_genres="+idGenres;

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(url);

        Response response = webTarget.request(MediaType.TEXT_PLAIN).get();

        if (response.getStatus() != 200) {
            JSONObject APIUnreachableMessage = new JSONObject(){{
                put("success", false);
                put("error", "Malformed request");
            }};
            return Response.status(Response.Status.fromStatusCode(422)).entity(APIUnreachableMessage.toString()).build();
        }


        JSONObject requestResults = new JSONObject(response.readEntity(String.class));
        int maxPages = requestResults.optInt("total_pages", 0);
        System.out.println("maxPages: " + maxPages);
        Random rn = new Random();
        ArrayList<Integer> randomPages = new ArrayList<>();

        if (maxPages > 5) {
            while (randomPages.size() < 5) {
                int b = rn.nextInt(maxPages) + 1;
                if (!randomPages.contains(b)){
                    randomPages.add(b);
                }
            }
        } else if (maxPages != 0) {
            for (int i = 1; i <= maxPages; i++) {
                randomPages.add(i);
            }
        }

        JSONArray results = new JSONArray();
        for (int index: randomPages) {
            String randomizedUrl = searchMoviesByPage_uri(String.valueOf(index), idGenres);
            WebTarget randomizedWebTarget = client.target(randomizedUrl);
            Response randomizedResponse = randomizedWebTarget.request(MediaType.TEXT_PLAIN).get();
            JSONObject resultsObject = new JSONObject(randomizedResponse.readEntity(String.class));
            JSONArray resultsArray = new JSONArray(resultsObject.getJSONArray("results"));
            results.putAll(resultsArray);
        }

        JSONArray sortedResults = sort(results, "vote_average");
        JSONArray finalResults = new JSONArray();

        for (int i = 0; i < Math.min(5, sortedResults.length()); i++) {
            finalResults.put(sortedResults.get(i));
        }

        return Response.status(Response.Status.OK).entity(finalResults.toString()).build();
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
                BigDecimal valA;
                BigDecimal valB;

                valA = (a.getBigDecimal(KEY_NAME));
                valB = (b.getBigDecimal(KEY_NAME));

                return -valA.compareTo(valB);
            }
        });
        for(int i = 0; i < jsonArr.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }
        return sortedJsonArray;
    }




        public Response getAllDetail(String detail){
        String url = "https://api.themoviedb.org/3/movie/"+detail+"?api_key=b3299a1aa5ae43a9ae35cb544503117f";

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(url);

        Response response = webTarget.request(MediaType.TEXT_PLAIN).get();

        if (response.getStatus() != 200) {
            JSONObject errorMessage = new JSONObject(){{
                put("success", false);
                put("error", "Malformed request");}};
            return Response.status(Response.Status.fromStatusCode(422)).entity(errorMessage.toString()).build();
        }

        return Response.status(Response.Status.OK).entity(response.readEntity(String.class)).build();

    }

}
