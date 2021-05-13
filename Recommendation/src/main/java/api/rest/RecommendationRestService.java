package api.rest;

import java.util.List;


import domain.service.RecommendationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


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
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllGenres() {
        return recommendationService.getAllGenres();
    }

}

