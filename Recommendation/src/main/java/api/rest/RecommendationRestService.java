package api.rest;

import java.util.List;


import domain.service.RecommendationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
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
        return Response.status(Response.Status.OK).entity((
          new JSONObject(){{
              put("service", "recommendation");
              put("message", "Welcome!");
          }}
        ).toString()).build();
    }

    @GET
    @Path("/genres")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGenres() {
        return recommendationService.getAllGenres();
    }

    //idGenres doit etre un integer dans un string
    //Get all horror and drama movies (id=27,18)
    //http://localhost:12080/recommendation/selectGenres=27,18
    @GET
    @Path("/selectGenres={idGenres}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFilmSelected(@PathParam("idGenres") String idGenres) {
        return recommendationService.getAllFilmSelected(idGenres);
    }

    // detail doit etre un integer dans un string
    @GET
    @Path("/detail={detail}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDetail(@PathParam("detail") String detail) {
        return recommendationService.getAllDetail(detail);
    }

}

