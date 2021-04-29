package api.rest;

import domain.service.UserService;
import io.swagger.annotations.Api;
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

    // Fake credentials for login in pseudo-db
    private final LinkedHashMap<String, String> fakeLoginCredentials = new LinkedHashMap<String, String>(){{
        put("john", "johnDoeHashedPassword");
    }};

    // Fake username for register in pseudo-db
    private final String fakeUserName = "John";

    /*Mock API*/
    @POST
    @Path("/v0/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(LinkedHashMap<String, String> req) {
        String reg_username = req.get("username");
        String reg_email = req.get("email");
        String reg_password = req.get("password");

        LinkedHashMap<String, String> returnMessage = new LinkedHashMap<>();

        //Check if params are valid
        if (reg_username == null || reg_email == null || reg_password == null) {
            returnMessage.put("message", "invalid parameters, please check your syntax");
            return Response.status(400).entity(returnMessage).build();
        }

        //Check if username already exists: (i.e. username is equal to "John")
        if (reg_username.equals(fakeUserName)){
            //Username already exists, try another
            returnMessage.put("message", "username already exists, try another");
            return Response.status(409).entity(returnMessage).build();
        }

        //Check if email or password fields are empty
        if ((reg_email.isEmpty()) || (reg_password.isEmpty())) {
            returnMessage.put("message", "please fill in email and password");
            return Response.status(403).entity(returnMessage).build();
        }

        //Everything is ok
        returnMessage.put("message", "registration ok");
        return Response.status(201).entity(returnMessage).build();
    }

    @POST
    @Path("/v0/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LinkedHashMap<String, String> req) {
        String userName = req.get("username");
        String userPassword = req.get("password");

        LinkedHashMap<String, String> returnMessage = new LinkedHashMap<>();

        //Check if params are valid
        if (userName == null || userPassword == null) {
            returnMessage.put("message", "invalid parameters, please check your syntax");
            return Response.status(400).entity(returnMessage).build();
        }

        //Check if username exists
        if (fakeLoginCredentials.containsKey(userName)) {
            //Check if passwords are the same
            String password = fakeLoginCredentials.get(userName);

            if (password.equals(userPassword)) {
                returnMessage.put("message", "username ok");
                returnMessage.put("token", "fakeTokenForMockApiTestingPurposes");
                return Response.status(200).entity(returnMessage).build();
            }

            //Wrong password
            returnMessage.put("message", "wrong password");
            return Response.status(401).entity(returnMessage).build();
        }

        //Username not found
        returnMessage.put("message", "username does not exist");
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