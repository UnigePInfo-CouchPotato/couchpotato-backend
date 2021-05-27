package api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.model.Room;
import domain.model.Room_User;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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

    /*HANDLING SUCCESS AND ERROR MESSAGES*/
    private static final String DATA = "\"data\":";
    private static final String BAD_REQUEST_ERROR_MESSAGE = "{" + "\"error\":\"Invalid parameters. Please check your request\"" + "}";

    private static String generateRoomClosedErrorMessage(String roomId) {
        return "{" + String.format("\"error\":\"Room %s is already closed\"", roomId) + "}";
    }

    /*HANDLING QUERY PARAMETERS*/
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

    private Response handleRoomIdAndUserIdQueryParams(String roomId, int userId, boolean notJoiningRoom) {
        //Check if params are valid (i.e. roomId is null or userId is equal to -1)
        if (roomId == null || userId == -1) {
            return Response.status(Response.Status.BAD_REQUEST).entity(BAD_REQUEST_ERROR_MESSAGE).build();
        }

        //Check if room exists
        if (!roomService.exists(roomId)) {
            String errorMessage = "{" + String.format("\"error\":\"Room %s does not exist\"", roomId) + "}";
            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }

        //Check if user id is valid
        if (roomService.isUserIdInvalid(userId)) {
            String errorMessage = "{" + String.format("\"error\":\"User %d does not exist\"", userId) + "}";
            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }

        /*Check if user is in this specific room
        If "notJoiningRoom" is false, do not check if user is in the room*/
        if (notJoiningRoom && !roomUserService.exists(roomId, userId)) {
            String errorMessage = "{" + String.format("\"error\":\"User %d is not in this room\"", userId) + "}";
            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }

        //Default -> Return no content
        return Response.noContent().build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String welcome() {
        return "Welcome to the room service";
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
    public List<Room_User> getAllRoomUsers() {
        return roomUserService.getAll();
    }

    @GET
    @Path("{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get a specific room using its id")
    public Response get(@PathParam("roomId") String roomId) {
        Response response = handleRoomIdQueryParam(roomId);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        return Response.status(Response.Status.OK).entity(roomService.get(roomId)).build();
    }

    @GET
    @Path("/exists")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Check if a specific room exists")
    public Response exists(@QueryParam("roomId") String roomId) {
        //Check if params are valid (i.e. roomId is equal to -1)
        if (roomId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(BAD_REQUEST_ERROR_MESSAGE).build();
        }

        String message = "{" + DATA + "{" + "\"exists\":" + roomService.exists(roomId) + "}" + "}";
        return Response.status(Response.Status.OK).entity(message).build();
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all users in a specific room")
    public Response getRoomUsers(@QueryParam("roomId") String roomId) {
        Response response = handleRoomIdQueryParam(roomId);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        return Response.status(Response.Status.OK).entity(roomService.getRoomUsers(roomId)).build();
    }

    @GET
    @Path("/is-room-admin")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Check if user is admin of a specific room")
    public Response isRoomAdmin(@QueryParam("roomId") String roomId, @QueryParam("userId") @DefaultValue("-1") int userId) {
        Response response = handleRoomIdAndUserIdQueryParams(roomId, userId, true);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        String message = "{" + DATA + "{" + "\"isRoomAdmin\":" + roomService.isRoomAdmin(roomId, userId) + "}" + "}";
        return Response.status(Response.Status.OK).entity(message).build();
    }

    @GET
    @Path("/admin")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get the admin of a specific room")
    public Response getRoomAdmin(@QueryParam("roomId") String roomId) {
        Response response = handleRoomIdQueryParam(roomId);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        Room room = roomService.get(roomId);
        int adminId = room.getRoomAdminId();
        return Response.status(Response.Status.OK).entity(roomService.getRoomAdmin(adminId)).build();
    }



    @GET
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create a room")
    public Response createRoom(@QueryParam("userId") @DefaultValue("-1") int userId) {
        //Check if params are valid (i.e. userId is equal to -1)
        if (userId == -1) {
            return Response.status(Response.Status.BAD_REQUEST).entity(BAD_REQUEST_ERROR_MESSAGE).build();
        }

        //Check if user id is valid
        if (roomService.isUserIdInvalid(userId)) {
            String errorMessage = "{" + String.format("\"error\":\"User %d does not exist\"", userId) + "}";
            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }

        //Create a room
        String roomId = roomService.createRoom(userId);
        String successMessage = "{" + DATA + "{" + "\"roomId\":" + roomId + "}" + "}";
        return Response.status(Response.Status.CREATED).entity(successMessage).build();
    }

    @GET
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete a room")
    public Response deleteRoom(@QueryParam("roomId") String roomId) {
        Response response = handleRoomIdQueryParam(roomId);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        //Delete a room
        roomService.deleteRoom(roomId);
        String successMessage = "{" + String.format("\"message\":\"Room %s has been deleted successfully\"", roomId) + "}";
        return Response.status(Response.Status.OK).entity(successMessage).build();
    }

    @GET
    @Path("/close")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Close a room")
    public Response closeRoom(@QueryParam("roomId") String roomId, @QueryParam("userId") @DefaultValue("-1") int userId) {
        Response response = handleRoomIdAndUserIdQueryParams(roomId, userId, true);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;
        //Check if  user is admin of the room
        if(!roomService.isRoomAdmin(roomId, userId)) {
            String errorMessage = "{" + String.format("\"error\":\"You are not the administrator of the room %s\"", roomId) + "}";
            return Response.status(Response.Status.FORBIDDEN).entity(errorMessage).build();
        }

        //Close the room
        if (!roomService.closeRoom(roomId))
            return Response.status(Response.Status.CONFLICT).entity(generateRoomClosedErrorMessage(roomId)).build();

        String successMessage = "{" + String.format("\"message\":\"Room %s has been closed successfully\"", roomId) + "}";
        return Response.status(Response.Status.OK).entity(successMessage).build();
    }

    @GET
    @Path("/join")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Join a room")
    public Response joinRoom(@QueryParam("roomId") String roomId, @QueryParam("userId") @DefaultValue("-1") int userId) {
        Response response = handleRoomIdAndUserIdQueryParams(roomId, userId, false);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        //Check if room is closed
        if (roomService.isRoomClosed(roomId))
            return Response.status(Response.Status.CONFLICT).entity(generateRoomClosedErrorMessage(roomId)).build();

        //Check if user is already in the room
        if(roomService.isUserInRoom(roomId, userId)) {
            String errorMessage = "{" + String.format("\"error\":\"User %s is already in this room\"", userId) + "}";
            return Response.status(Response.Status.CONFLICT).entity(errorMessage).build();
        }

        //Add the user to the room
        roomService.joinRoom(roomId, userId);
        String successMessage = "{" + String.format("\"message\":\"User %d has joined the room %s successfully\"", userId, roomId) + "}";
        return Response.status(Response.Status.CREATED).entity(successMessage).build();
    }

    @POST
    @Path("/vote")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get and save user votes")
    public Response setUserVotes(LinkedHashMap<String, Object> body) {
        //Get roomId/userId and handle errors
        String roomId = (String) body.get("roomId");
        Integer userId = (Integer) body.get("userId");

        Response response = handleRoomIdAndUserIdQueryParams(roomId, userId, true);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

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

        roomUserService.setUserVotes(roomId, userId, jsonArray);
        String successMessage = "{" + "\"message\":\"Your votes have been saved successfully\"" + "}";
        return Response.status(Response.Status.OK).entity(successMessage).build();
    }

    @GET
    @Path("/final")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Find the movie with the most votes")
    public Response getMovieWithMostVotes(@QueryParam("roomId") String roomId, @QueryParam("userId") @DefaultValue("-1") int userId) {
        Response response = handleRoomIdAndUserIdQueryParams(roomId, userId, true);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        final String movie = roomService.getMovieWithMostVotes(roomId);

        return Response.status(Response.Status.OK).entity(movie).build();
    }

    @GET
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Insert into a room the genres of specific user")
    public Response setUserGenres(@QueryParam("roomId") String roomId, @QueryParam("userId") @DefaultValue("-1") int userId, @QueryParam("genres") String genres) {
        Response response = handleRoomIdAndUserIdQueryParams(roomId, userId, true);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        //Check if "genres" is null
        if (genres == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(BAD_REQUEST_ERROR_MESSAGE).build();
        }

        //Check if "genres" is empty
        if (genres.isEmpty()) {
            String errorMessage = "{" + "\"error\":\"Please specify at least one genre\"" + "}";
            return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
        }

        //Check if room is closed
        if (roomService.isRoomClosed(roomId))
            return Response.status(Response.Status.CONFLICT).entity(generateRoomClosedErrorMessage(roomId)).build();

        //Add the genres
        roomUserService.setUserGenres(roomId, userId, genres);
        String successMessage = "{" + "\"message\":\"The genres have been set successfully\"" + "}";
        return Response.status(Response.Status.OK).entity(successMessage).build();
    }

    @GET
    @Path("/results")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get the movies according to the users preferences")
    public Response getMovies(@QueryParam("roomId") String roomId, @QueryParam("userId") @DefaultValue("-1") int userId) {
        Response response = handleRoomIdAndUserIdQueryParams(roomId, userId, true);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        String movies = roomService.getMovies(roomId, userId);
        return Response.status(Response.Status.OK).entity(movies).build();
    }

}
