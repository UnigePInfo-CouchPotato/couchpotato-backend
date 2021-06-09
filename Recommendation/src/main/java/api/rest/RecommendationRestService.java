package api.rest;

import domain.service.RecommendationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@ApplicationScoped
@Path("/recommendation")
@Api(value = "recommendation", authorizations = {
        @Authorization(value="sampleoauth", scopes = {})
})


public class RecommendationRestService {

    // http://localhost:12080/recommendation/
    @Inject
    private RecommendationService recommendationService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response welcome() {
        JSONObject welcomeMessage = new JSONObject();
        welcomeMessage.put("service", "recommendation");
        welcomeMessage.put("message", "Welcome!");
        String str = welcomeMessage.toString();
        return Response.status(Response.Status.OK).entity(str).build();
    }

    @GET
    @Path("/genres")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGenres() {
        return recommendationService.getAllGenres();
    }

    //idGenres doit etre un integer dans un string
    //Get all horror and drama movies (id=27,18)
    //http://localhost:12080/recommendation/selectGenres/27,18
    @GET
    @Path("/selectGenres/{idGenres}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFilmSelected(@PathParam("idGenres") String idGenres) {
        return recommendationService.getAllFilmSelected(idGenres);
    }

    // detail doit etre un integer dans un string
    @GET
    @Path("/detail/{detail}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDetail(@PathParam("detail") String detail) {
        return recommendationService.getAllDetail(detail);
    }

}

