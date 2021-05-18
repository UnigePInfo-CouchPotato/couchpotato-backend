package api.rest;

import domain.model.Room;
import domain.model.Room_User;
import domain.service.RoomService;
import domain.service.RoomUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    private Response handleRoomIdQueryParam(int roomId) {
        //Check if params are valid (i.e. userId is equal to -1)
        if (roomId == -1) {
            String errorMessage = "{" + "\"error\":\"Invalid parameters. Please check your request\"" + "}";
            return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
        }

        //Check if room exists
        if (!roomService.exists(roomId)) {
            String errorMessage = "{" + String.format("\"error\":\"Room %d does not exist\"", roomId) + "}";
            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }

        //Default -> Return no content
        return Response.noContent().build();
    }

    private Response handleRoomIdAndUserIdQueryParams(int roomId, int userId) {
        //Check if params are valid (i.e. roomId/userId are equal to -1)
        if (roomId == -1 || userId == -1) {
            String errorMessage = "{" + "\"error\":\"Invalid parameters. Please check your request\"" + "}";
            return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
        }

        //Check if room exists
        if (!roomService.exists(roomId)) {
            String errorMessage = "{" + String.format("\"error\":\"Room %d does not exist\"", roomId) + "}";
            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }

        //Check if user id is valid
        if (roomService.isUserIdInvalid(userId)) {
            String errorMessage = "{" + String.format("\"error\":\"User %d does not exist\"", userId) + "}";
            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }

        //Default -> Return no content
        return Response.noContent().build();
    }

    @GET
    @Path("/welcome")
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
    @Path("/room-users")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all room_users")
    public List<Room_User> getAllRoomUsers() {
        return roomUserService.getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get a specific room using its id")
    public Response get(@QueryParam("roomId") @DefaultValue("-1") int roomId) {
        Response response = handleRoomIdQueryParam(roomId);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        return Response.status(Response.Status.OK).entity(roomService.get(roomId)).build();
    }

    @GET
    @Path("/exists")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Check if a specific room exists")
    public Response exists(@QueryParam("roomId") @DefaultValue("-1") int roomId) {
        //Check if params are valid (i.e. roomId is equal to -1)
        if (roomId == -1) {
            String errorMessage = "{" + "\"error\":\"Invalid parameters. Please check your request\"" + "}";
            return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
        }

        String message = "{" + "\"data\":" + "{" + "\"exists\":" + roomService.exists(roomId) + "}" + "}";
        return Response.status(Response.Status.OK).entity(message).build();
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all users in a specific room")
    public Response getRoomUsers(@QueryParam("roomId") @DefaultValue("-1") int roomId) {
        Response response = handleRoomIdQueryParam(roomId);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        return Response.status(Response.Status.OK).entity(roomService.getRoomUsers(roomId)).build();
    }

    @GET
    @Path("/is-room-admin")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Check if user is admin of a specific room")
    public Response isRoomAdmin(@QueryParam("roomId") @DefaultValue("-1") int roomId, @QueryParam("userId") @DefaultValue("-1") int userId) {
        Response response = handleRoomIdAndUserIdQueryParams(roomId, userId);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        String message = "{" + "\"data\":" + "{" + "\"isRoomAdmin\":" + roomService.isRoomAdmin(roomId, userId) + "}" + "}";
        return Response.status(Response.Status.OK).entity(message).build();
    }

    @GET
    @Path("/admin")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get the admin of a specific room")
    public Response getRoomAdmin(@QueryParam("roomId") @DefaultValue("-1") int roomId) {
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
            String errorMessage = "{" + "\"error\":\"Invalid parameters. Please check your request\"" + "}";
            return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
        }

        //Check if user id is valid
        if (roomService.isUserIdInvalid(userId)) {
            String errorMessage = "{" + String.format("\"error\":\"User %d does not exist\"", userId) + "}";
            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }

        //Create a room
        int roomId = roomService.createRoom(userId);
        String successMessage = "{" + "\"data\":" + "{" + "\"roomId\":" + roomId + "}" + "}";
        return Response.status(Response.Status.CREATED).entity(successMessage).build();
    }

    @GET
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete a room")
    public Response deleteRoom(@QueryParam("roomId") @DefaultValue("-1") int roomId) {
        Response response = handleRoomIdQueryParam(roomId);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        //Delete a room
        roomService.deleteRoom(roomId);
        String successMessage = "{" + String.format("\"message\":\"Room %d has been deleted successfully\"", roomId) + "}";
        return Response.status(Response.Status.OK).entity(successMessage).build();
    }

    @GET
    @Path("/close")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Close a room")
    public Response closeRoom(@QueryParam("roomId") @DefaultValue("-1") int roomId, @QueryParam("userId") @DefaultValue("-1") int userId) {
        Response response = handleRoomIdAndUserIdQueryParams(roomId, userId);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        //Check if user is admin of the room
        if(!roomService.isRoomAdmin(roomId, userId)) {
            String errorMessage = "{" + String.format("\"error\":\"You are not the administrator of the room %d\"", roomId) + "}";
            return Response.status(Response.Status.FORBIDDEN).entity(errorMessage).build();
        }

        //Close the room
        if (!roomService.closeRoom(roomId)) {
            String successMessage = "{" + String.format("\"error\":\"Room %d is already closed\"", roomId) + "}";
            return Response.status(Response.Status.CONFLICT).entity(successMessage).build();
        }

        String successMessage = "{" + String.format("\"message\":\"Room %d has been closed successfully\"", roomId) + "}";
        return Response.status(Response.Status.OK).entity(successMessage).build();
    }

    @GET
    @Path("/join")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Join a room")
    public Response joinRoom(@QueryParam("roomId") @DefaultValue("-1") int roomId, @QueryParam("userId") @DefaultValue("-1") int userId) {
        Response response = handleRoomIdAndUserIdQueryParams(roomId, userId);
        if (response.getStatusInfo() != Response.Status.NO_CONTENT)
            return response;

        //Check if room is closed
        if (roomService.isRoomClosed(roomId)) {
            String errorMessage = "{" + String.format("\"error\":\"Room %d is already closed\"", roomId) + "}";
            return Response.status(Response.Status.CONFLICT).entity(errorMessage).build();
        }

        //Check if user is already in the room
        if(roomService.isUserInRoom(roomId, userId)) {
            String errorMessage = "{" + String.format("\"error\":\"User %d is already in this room\"", userId) + "}";
            return Response.status(Response.Status.CONFLICT).entity(errorMessage).build();
        }

        //Add the user to a room
        roomService.joinRoom(roomId, userId);
        String successMessage = "{" + String.format("\"message\":\"User %d has joined the room %d successfully\"", userId, roomId) + "}";
        return Response.status(Response.Status.CREATED).entity(successMessage).build();
    }

}