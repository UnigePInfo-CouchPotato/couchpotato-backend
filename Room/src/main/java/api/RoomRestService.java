package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.model.Room;
import domain.model.RoomUser;
import domain.service.RoomService;
import domain.service.RoomUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@ApplicationScoped
@Path("/rooms")
@Api(value = "rooms", authorizations = {
        @Authorization(value="sampleoauth", scopes = {})
})
public class RoomRestService {

    @Inject
    private RoomService roomService;

    @Inject
    private RoomUserService roomUserService;

    /*CONSTANTS*/
    private static final String DATA = "\"data\":";
    private static final String BEARER = "Bearer ";
    private static final String MODE_PROPERTY = "MODE";
    private static final String UNAUTHORIZED = "Unauthorized";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BAD_REQUEST_ERROR_MESSAGE = "{" + "\"error\":\"Invalid parameters. Please check your request\"" + "}";

    /*HANDLING PARAMETERS*/
    private Response handleRoomIdQueryParam(String roomId) {
        //Check if params are valid (i.e. userId is equal to -1)
        if (roomId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(BAD_REQUEST_ERROR_MESSAGE).build();
        }

        //Check if room exists
        if (!roomService.exists(roomId)) {
            String errorMessage = "{" + String.format("\"error\":\"Room %s does not exist\"", roomId) + "}";
            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }

        //Default -> Return no content
        return Response.noContent().build();
    }

    private Response handleParams(String roomId, List<String> authorization, boolean notJoiningRoom) {
        //Check if params are valid
        if (roomId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(BAD_REQUEST_ERROR_MESSAGE).build();
        }

        if (authorization == null || authorization.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(UNAUTHORIZED).build();
        }

        //Check if room exists
        if (!roomService.exists(roomId)) {
            String errorMessage = "{" + String.format("\"error\":\"Room %s does not exist\"", roomId) + "}";
            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }

        String bearerToken = authorization.get(0).replace(BEARER, "");
        //Check if token is invalid
        if (roomService.isTokenInvalid(bearerToken)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(UNAUTHORIZED).build();
        }

        JSONObject userInfo = roomService.getUserInfo(bearerToken);
        String userNickname = userInfo.getString("nickname");
        /*Check if user is in this specific room
        If "notJoiningRoom" is false, do not check if user is in the room*/
        if (notJoiningRoom && !roomUserService.exists(roomId, userNickname)) {
            String errorMessage = "{" + String.format("\"error\":\"User %s is not in this room\"", userNickname) + "}";
            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }

        //Default -> Return no content
        return Response.noContent().build();
    }

    private Response handleTestMode(String roomId, HttpHeaders headers) {
        final String property = System.getProperty(MODE_PROPERTY);
        if (Objects.equals(property, "TEST")) {
            Response response = handleRoomIdQueryParam(roomId);
            if (response.getStatusInfo() != Response.Status.NO_CONTENT)
                return response;
        }
        else {
            List<String> authorization = headers.getRequestHeader(AUTHORIZATION);
            Response response = handleParams(roomId, authorization, true);
            if (response.getStatusInfo() != Response.Status.NO_CONTENT)
                return response;
        }

        //Default -> Return no content
        return Response.noContent().build();
    }

    @GET
    @Path("/test-mode")
    @Produces(MediaType.TEXT_PLAIN)
    public Response setTestMode() {
        System.setProperty("MODE", "TEST");
        return Response.status(Response.Status.OK).entity("Test mode enabled").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response welcome() {
        JSONObject welcomeMessage = new JSONObject();
        welcomeMessage.put("service", "room");
        welcomeMessage.put("message", "Welcome!");
        return Response.status(Response.Status.OK).entity(welcomeMessage.toString()).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all rooms")
    public List<Room> getAllRooms() {
        return roomService.getAll();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Count the number of rooms")
    public Long count() {
        return roomService.count();
    }

    @GET
    @Path("/room-users")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all room_users")
    public List<RoomUser> getAllRoomUsers() {
        return roomUserService.getAll();
    }

    @GET
    @Path("{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get a specific room using its id")
    public Response get(@PathParam("roomId") String roomId, @Context HttpHeaders headers) {
        Response response = handleTestMode(roomId, headers);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        return Response.status(Response.Status.OK).entity(roomService.get(roomId)).build();
    }

    @GET
    @Path("/exists")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Check if a specific room exists")
    public Response exists(@QueryParam("roomId") String roomId, @Context HttpHeaders headers) {
        Response response = handleTestMode(roomId, headers);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        String message = "{" + DATA + "{" + "\"exists\":" + roomService.exists(roomId) + "}" + "}";
        return Response.status(Response.Status.OK).entity(message).build();
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all users in a specific room")
    public Response getRoomUsers(@QueryParam("roomId") String roomId, @Context HttpHeaders headers) {
        List<String> authorization = headers.getRequestHeader(AUTHORIZATION);
        Response response = handleParams(roomId, authorization, true);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        return Response.status(Response.Status.OK).entity(roomService.getRoomUsers(roomId)).build();
    }

    @GET
    @Path("/is-room-admin")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Check if user is admin of a specific room")
    public Response isRoomAdmin(@QueryParam("roomId") String roomId, @Context HttpHeaders headers) {
        List<String> authorization = headers.getRequestHeader(AUTHORIZATION);
        Response response = handleParams(roomId, authorization, true);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        String bearerToken = authorization.get(0).replace(BEARER, "");

        String message = "{" + DATA + "{" + "\"isRoomAdmin\":" + roomService.isRoomAdmin(roomId, bearerToken) + "}" + "}";
        return Response.status(Response.Status.OK).entity(message).build();
    }

    @GET
    @Path("/admin")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get the admin of a specific room")
    public Response getRoomAdmin(@QueryParam("roomId") String roomId, @Context HttpHeaders headers) {
        List<String> authorization = headers.getRequestHeader(AUTHORIZATION);
        Response response = handleParams(roomId, authorization, true);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        Room room = roomService.get(roomId);
        return Response.status(Response.Status.OK).entity(room.getRoomAdmin()).build();
    }

    @GET
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create a room")
    public Response createRoom(@Context HttpHeaders headers) {
        List<String> authorization = headers.getRequestHeader(AUTHORIZATION);
        if (authorization == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(UNAUTHORIZED).build();
        }

        String bearerToken = authorization.get(0).replace(BEARER, "");

        //Check if token is invalid
        if (roomService.isTokenInvalid(bearerToken)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(UNAUTHORIZED).build();
        }

        //Create a room
        String roomId = roomService.createRoom(bearerToken);
        String successMessage = "{" + DATA + "{" + "\"roomId\":" + roomId + "}" + "}";
        return Response.status(Response.Status.CREATED).entity(successMessage).build();
    }

    @GET
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete a room")
    public Response deleteRoom(@QueryParam("roomId") String roomId, @Context HttpHeaders headers) {
        Response response = handleTestMode(roomId, headers);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        //Delete a room
        roomService.deleteRoom(roomId);
        String successMessage = "{" + String.format("\"message\":\"Room %s has been deleted successfully\"", roomId) + "}";
        return Response.status(Response.Status.OK).entity(successMessage).build();
    }

    @GET
    @Path("/end-vote")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "End voting period")
    public Response endVotingPeriod(@QueryParam("roomId") String roomId, @Context HttpHeaders headers) {
        List<String> authorization = headers.getRequestHeader(AUTHORIZATION);
        Response response = handleParams(roomId, authorization, true);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        String bearerToken = authorization.get(0).replace(BEARER, "");

        //Check if user is the administrator of the room
        if (!roomService.isRoomAdmin(roomId, bearerToken)) {
            String errorMessage = "{" + String.format("\"error\":\"Unauthorized to end the voting period of the room %s\"", roomId) + "}";
            return Response.status(Response.Status.FORBIDDEN).entity(errorMessage).build();
        }

        //End the voting period
        roomService.endVotingPeriod(roomId, bearerToken);
        String successMessage = "{" + String.format("\"message\":\"Voting period of room %s has been ended successfully\"", roomId) + "}";
        return Response.status(Response.Status.OK).entity(successMessage).build();
    }

    @GET
    @Path("/end-join")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "End join period")
    public Response endJoinPeriod(@QueryParam("roomId") String roomId, @Context HttpHeaders headers) {
        List<String> authorization = headers.getRequestHeader(AUTHORIZATION);
        Response response = handleParams(roomId, authorization, true);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        String bearerToken = authorization.get(0).replace(BEARER, "");

        //Check if user is the administrator of the room
        if (!roomService.isRoomAdmin(roomId, bearerToken)) {
            String errorMessage = "{" + String.format("\"error\":\"Unauthorized to end the join period of the room %s\"", roomId) + "}";
            return Response.status(Response.Status.FORBIDDEN).entity(errorMessage).build();
        }

        //End the join period
        roomService.endJoinPeriod(roomId, bearerToken);
        String successMessage = "{" + String.format("\"message\":\"Join period of room %s has been ended successfully\"", roomId) + "}";
        return Response.status(Response.Status.OK).entity(successMessage).build();
    }

    @GET
    @Path("/close")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Close a room")
    public Response closeRoom(@QueryParam("roomId") String roomId, @Context HttpHeaders headers) {
        List<String> authorization = headers.getRequestHeader(AUTHORIZATION);
        Response response = handleParams(roomId, authorization, true);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        String bearerToken = authorization.get(0).replace(BEARER, "");
        //Check if user is admin of the room
        if(!roomService.isRoomAdmin(roomId, bearerToken)) {
            String errorMessage = "{" + String.format("\"error\":\"Unauthorized to close the room %s\"", roomId) + "}";
            return Response.status(Response.Status.FORBIDDEN).entity(errorMessage).build();
        }

        //Close the room
        if (!roomService.closeRoom(roomId)) {
            String errorMessage = "{" + String.format("\"error\":\"Room %s is already closed\"", roomId) + "}";
            return Response.status(Response.Status.CONFLICT).entity(errorMessage).build();
        }

        roomService.deleteRoom(roomId);

        String successMessage = "{" + String.format("\"message\":\"Room %s has been closed successfully\"", roomId) + "}";
        return Response.status(Response.Status.OK).entity(successMessage).build();
    }

    @GET
    @Path("/join")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Join a room")
    public Response joinRoom(@QueryParam("roomId") String roomId, @Context HttpHeaders headers) {
        List<String> authorization = headers.getRequestHeader(AUTHORIZATION);
        Response response = handleParams(roomId, authorization, false);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        String bearerToken = authorization.get(0).replace(BEARER, "");

        //Check if user is already in the room
        if(roomService.isUserInRoom(roomId, bearerToken)) {
            String errorMessage = "{" + "\"error\":\"You are already in this room\"" + "}";
            return Response.status(Response.Status.CONFLICT).entity(errorMessage).build();
        }

        //Check if user can join the room
        if (!roomService.canJoinRoom(roomId)) {
            String errorMessage = "{" + String.format("\"error\":\"Join period has been ended for room %s\"", roomId) + "}";
            return Response.status(Response.Status.CONFLICT).entity(errorMessage).build();
        }

        //Add the user to the room
        roomService.joinRoom(roomId, bearerToken);
        String successMessage = "{" + String.format("\"message\":\"You have joined the room %s successfully\"", roomId) + "}";
        return Response.status(Response.Status.CREATED).entity(successMessage).build();
    }

    @POST
    @Path("/vote")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get and save user votes")
    public Response setUserVotes(Map<String, Object> body, @Context HttpHeaders headers) {
        String roomId = (String) body.get("roomId");
        List<String> authorization = headers.getRequestHeader(AUTHORIZATION);

        Response response = handleParams(roomId, authorization, true);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        //Check if user can vote
        if (!roomService.canVote(roomId)) {
            String errorMessage = "{" + String.format("\"error\":\"Voting is no more allowed for room %s\"", roomId) + "}";
            return Response.status(Response.Status.CONFLICT).entity(errorMessage).build();
        }

        String bearerToken = authorization.get(0).replace(BEARER, "");

        //Get user choice
        ObjectMapper objectMapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        LinkedHashMap<String, Integer> choice = objectMapper.convertValue(body.get("choice"), LinkedHashMap.class);

        //Handle errors with array
        if (choice == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(BAD_REQUEST_ERROR_MESSAGE).build();
        }

        int length = roomService.get(roomId).getNumberOfMovies();
        if (choice.size() != length) {
            String errorMessage = "{" + String.format("\"error\":\"Your array should be of length %d\"", length) + "}";
            return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
        }

        HashMap<String, String> hashMap = new HashMap<>();
        choice.forEach((key, value) -> hashMap.put(key, String.valueOf(value)));

        JSONObject jsonObject = new JSONObject(hashMap);
        JSONArray jsonArray = new JSONArray("[" + jsonObject + "]");
        JSONObject userInfo = roomService.getUserInfo(bearerToken);

        roomUserService.setUserVotes(roomId, userInfo.getString("nickname"), jsonArray);
        String successMessage = "{" + "\"message\":\"Your votes have been saved successfully\"" + "}";
        return Response.status(Response.Status.OK).entity(successMessage).build();
    }

    @GET
    @Path("/final")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Find the movie with the most votes")
    public Response getMovieWithMostVotes(@QueryParam("roomId") String roomId, @Context HttpHeaders headers) {
        List<String> authorization = headers.getRequestHeader(AUTHORIZATION);
        Response response = handleParams(roomId, authorization, true);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        final String movie = roomService.getMovieWithMostVotes(roomId);

        return Response.status(Response.Status.OK).entity(movie).build();
    }

    @GET
    @Path("/results")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get the movies according to the users preferences")
    public Response getMovies(@QueryParam("roomId") String roomId, @Context HttpHeaders headers) {
        List<String> authorization = headers.getRequestHeader(AUTHORIZATION);
        Response response = handleParams(roomId, authorization, true);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        String bearerToken = authorization.get(0).replace(BEARER, "");

        String movies = roomService.getMovies(roomId, bearerToken);
        return Response.status(Response.Status.OK).entity(movies).build();
    }

}
