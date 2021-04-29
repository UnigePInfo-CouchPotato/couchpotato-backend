package api.rest;

import domain.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;

@ApplicationScoped
@Path("/user-management")
@Api(value = "user-management", authorizations = {
        @Authorization(value="sampleoauth", scopes = {})
})
public class UserManagementRestService {

    @Inject
    private UserService userService;

    // False credentials for login
    private LinkedHashMap<String, String> falseLoginCredentials = new LinkedHashMap<String, String>(){{
        put("jon", "johnDoeHashedPassword");
    }};

    /*Mock API*/
    @POST
    @Path("/v0/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(LinkedHashMap<String, String> req) {
        System.out.println(req.get("username"));
        System.out.println(req.get("email"));
        System.out.println(req.get("password"));
        LinkedHashMap<String, String> message = new LinkedHashMap<>();
        message.put("result", "It's working");
        return Response.status(200).entity(message).build();
    }

    @POST
    @Path("/v0/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LinkedHashMap<String, String> req) {
        String userName = req.get("username");
        String userPassword = req.get("password");

        LinkedHashMap<String, String> returnMessage = new LinkedHashMap<>();
//        returnMessage.put("result", "It's working");

        //Check if username exists
        if (falseLoginCredentials.containsKey(userName)) {
            //Check if passwords are the same
            String password = falseLoginCredentials.get(userName);

            if (password.equals(userPassword)) {
                returnMessage.put("result", "username ok");
                returnMessage.put("token", "falseTokenForTest");
                return Response.status(200).entity(returnMessage).build();
            }

            //Wrong password
            returnMessage.put("result", "wrong password");
            return Response.status(403).entity(returnMessage).build();
        }

        //Pas de username
        returnMessage.put("result", "username does not exist");
        return Response.status(404).entity(returnMessage).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayPlainTextHello() {
        return "Hello Jersey";
    }

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_HTML)
    public String test_html(){
        return "<html><body> patoche</body></html>";
    }

    @GET
    @Path("/greet")
    @Produces("text/html")
    public String greet() {
        return System.currentTimeMillis() + "";
    }

}