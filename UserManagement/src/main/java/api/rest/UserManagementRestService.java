package api.rest;

import domain.model.Preference;
import domain.model.Users;
import domain.service.PreferenceService;
import domain.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.json.JSONObject;
import org.json.JSONArray;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

@ApplicationScoped
@Path("/users")
@Api(value = "user-management", authorizations = {
        @Authorization(value="sampleoauth", scopes = {})
})
public class UserManagementRestService {

    @Inject private UserService userService;
    @Inject private PreferenceService prefService;

//  Petter:
//  Temporary test: get preferences of one user by his id in the url
//  NOTE: This will change with oauth2 as we will get the user info from the JWT token (ID token)
//  TODO: Find out how to integrate JWT token into the requests and retrieve their fields (called "claims")


    /* -------- PREFERENCES REQUESTS --------- */



    @GET
    @Path("{id}/get-preferences")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get the preferences for a user from his userId")
    public Response getPreferences(@PathParam("id") int userId) {
        Preference pref = prefService.get(userId);
//      TODO: Genre ids should ideally be validated in the backend before just storing them as a raw JSONArray string.
//      If time permits we should modify this method and the domain model to to convert the JSONArray to int[] array
//      and iterate the genreIds to validate before storing the whole array string in the genreIds field of the Preference table.
        JSONArray genreIdsArray = new JSONArray(pref.getGenreIds());
        return Response.status(Response.Status.OK).entity((
                new JSONObject(){{
                    put("genreIds", genreIdsArray);
                }}
        ).toString()).build();
    }

    @POST
//    @Path("users/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create a new user with new preferences")
    public void setPreference(Preference preference) {
        prefService.create(preference);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update genre ids")
    public Response update(Preference newPrefs) {
        try {
            prefService.updatePreference(newPrefs);
        } catch(IllegalArgumentException i) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch(Exception e) {
            return Response.status(Response.Status.BAD_GATEWAY).build();
        }
        return Response.status(Response.Status.CREATED).location(URI.create("/home")).build();
    }





    /* --------- Mock API --------- */

    // Fake credentials for login/register in pseudo-db
    private final LinkedHashMap<String, String> fakeCredentials = new LinkedHashMap<String, String>(){{
        put("John", "johnDoeHashedPassword");
    }};

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response welcome() {
        return Response.status(Response.Status.OK).entity((
            new JSONObject(){{
              put("service", "user-mgmt");
              put("message", "Welcome!");
            }}
        ).toString()).build();
    }



    //mock apis
    /*

    @POST
    @Path("/v0/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Mock request to register user")
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
        Random rand = new Random();
        successMessage.put("data", new LinkedHashMap<String, String>(){{
            put("id", String.valueOf(rand.nextInt(100)));
            put("message", "registration ok");
        }});
        return Response.status(201).entity(successMessage).build();
    }

    @POST
    @Path("/v0/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Mock request to login")
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

    */


    /* --------- USER REQUESTS (Deprecated)--------- */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get a specific user")
    public Users get(@PathParam("id") int userId) {
        return userService.get(userId);
    }

    @GET
    @Path("{id}/exists") //verify convention
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Verify if a user exists")
    public boolean checkUserExists(@PathParam("id") int userId) { return userService.exists(userId);  }

//    @PUT
//    @Produces(MediaType.APPLICATION_JSON)
//    @ApiOperation(value = "Update a given user")
//    public Response update(Users newUser) {
//        try {
//            userService.update(newUser);
//        } catch(IllegalArgumentException i) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        } catch(Exception e) {
//            return Response.status(Response.Status.BAD_GATEWAY).build();
//        }
//        return Response.status(Response.Status.CREATED).location(URI.create("/home")).build();
//    }

//    @POST
////    @Path("users/create")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @ApiOperation(value = "Create a new user")
//    public Response createUser(Users user) {
//        try {
//            userService.create(user);
//        } catch(IllegalArgumentException i) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        } catch(Exception e) {
//            return Response.status(Response.Status.BAD_GATEWAY).build();
//        }
//        return Response.status(Response.Status.CREATED).location(URI.create("/home")).build();
//    }


    /* --------- TEST REQUESTS  --------- */
    @GET
    @Path("/all")
    @Produces("application/json")
    @ApiOperation(value = "Obtain all users")
    public List<Users> getAll() {
        return userService.getAll();
    }

    @GET
    @Path("/preferences/all")
    @Produces("application/json")
    @ApiOperation(value = "Obtain all preference instances")
    public List<Preference> getAllPreferences() {
        return prefService.getAll();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayPlainTextHello() {
        return "Welcome to UserManagement";
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