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

    // Fake credentials for login/register in pseudo-db
    private final LinkedHashMap<String, String> fakeCredentials = new LinkedHashMap<String, String>(){{
        put("John", "johnDoeHashedPassword");
    }};

    /*Mock API*/
    @POST
    @Path("/v0/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(LinkedHashMap<String, String> req) {
        String reg_username = req.get("username");
        String reg_email = req.get("email");
        String reg_password = req.get("password");

        LinkedHashMap<String, String> errorMessage = new LinkedHashMap<>();
        LinkedHashMap<String, LinkedHashMap<String, String>> successMessage = new LinkedHashMap<>();

        //Check if params are valid
        if (reg_username == null || reg_email == null || reg_password == null) {
            errorMessage.put("error", "invalid parameters, please check your request");
            return Response.status(400).entity(errorMessage).build();
        }

        //Check if username already exists: (i.e. username is equal to "John")
        if (fakeCredentials.containsKey(reg_username)){
            //Username already exists, try another
            errorMessage.put("error", "username already exists, try another");
            return Response.status(409).entity(errorMessage).build();
        }

        //Check if email or password fields are empty
        if ((reg_email.isEmpty()) || (reg_password.isEmpty())) {
            errorMessage.put("error", "please fill in email and password");
            return Response.status(403).entity(errorMessage).build();
        }

        //Everything is ok
        successMessage.put("data", new LinkedHashMap<String, String>(){{
            put("id", "3");
            put("message", "registration ok");
        }});
        return Response.status(201).entity(successMessage).build();
    }

    @POST
    @Path("/v0/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LinkedHashMap<String, String> req) {
        String userName = req.get("username");
        String userPassword = req.get("password");

        LinkedHashMap<String, String> errorMessage = new LinkedHashMap<>();
        LinkedHashMap<String, LinkedHashMap<String, String>> successMessage = new LinkedHashMap<>();

        //Check if params are valid
        if (userName == null || userPassword == null) {
            errorMessage.put("error", "invalid parameters, please check your request");
            return Response.status(400).entity(errorMessage).build();
        }

        //Check if username exists
        if (fakeCredentials.containsKey(userName)) {
            //Check if passwords are the same
            String password = fakeCredentials.get(userName);

            if (password.equals(userPassword)) {
                successMessage.put("data", new LinkedHashMap<String, String>(){{
                    put("message", "username ok");
                    put("token", "fakeTokenForMockApiTestingPurposes");
                }});
                return Response.status(200).entity(successMessage).build();
            }

            //Wrong password
            errorMessage.put("error", "wrong password");
            return Response.status(401).entity(errorMessage).build();
        }

        //Username not found
        errorMessage.put("error", "username does not exist");
        return Response.status(404).entity(errorMessage).build();
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