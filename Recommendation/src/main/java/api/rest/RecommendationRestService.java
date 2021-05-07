package api.rest;

//import domain.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Random;



@ApplicationScoped
@Path("/recommendation")
@Api(value = "recommendation", authorizations = {
        @Authorization(value="sampleoauth", scopes = {})
})


public class RecommendationRestService {

    // http://localhost:8080/recommendation/test

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_HTML)
    public String test_html() {
        return "<html><body> christina</body></html>";
    }


}

