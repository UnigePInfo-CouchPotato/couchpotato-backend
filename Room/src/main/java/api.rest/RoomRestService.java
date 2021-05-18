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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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

    @GET
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
    @Path("/room-user/{roomId}/{userId}")
    @ApiOperation(value = "Get a specific room_user")
    public Response getRoomUsers(@PathParam("roomId") int roomId, @PathParam("userId") int userId) {
        Room_User room_user = new Room_User();
        room_user.setRoomId(roomId);
        room_user.setUserId(userId);

        //Check if room_user exists
        if (!roomUserService.exists(room_user)) {
            String errorMessage = "{\"error\":\"This room_user does not exist\"}";
            return Response.status(404).entity(errorMessage).build();
        }

        return Response.status(200).entity(roomUserService.get(room_user)).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Count the number of rooms")
    public Long count() {
        return roomService.count();
    }

    @GET
    @Path("{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get a specific room using its id")
    public Room get(@PathParam("roomId") int roomId) {
        return roomService.get(roomId);
    }

    @GET
    @Path("exists/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Check if a specific room exists")
    public boolean exists(@PathParam("roomId") int roomId) {
        return roomService.exists(roomId);
    }

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String sayPlainTextHello() {
        return "Welcome to the room service";
    }

    @GET
    @Path("/user-management")
    @Produces(MediaType.TEXT_PLAIN)
    public String getWelcomeMessage() {
        return roomService.getWelcomeMessage();
    }

    @GET
    @Path("{roomId}/users")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all users in a specific room")
    public Response getRoomUsers(@PathParam("roomId") int roomId) {
        //Check if room exists
        if (!roomService.exists(roomId)) {
            String errorMessage = "{" + String.format("\"error\":\"Room %d does not exist\"", roomId) + "}";
            return Response.status(404).entity(errorMessage).build();
        }

        return Response.status(200).entity(roomService.getRoomUsers(roomId)).build();
    }

    @GET
    @Path("{roomId}/is-room-admin/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Check if user is admin of a specific room")
    public Response isRoomAdmin(@PathParam("roomId") int roomId, @PathParam("userId") int userId) {
        //Check if user id is valid
        if (roomService.isUserIdInvalid(userId)) {
            String errorMessage = "{" + String.format("\"error\":\"User %d does not exist\"", userId) + "}";
            return Response.status(404).entity(errorMessage).build();
        }

        //Check if room exists
        if (!roomService.exists(roomId)) {
            String errorMessage = "{" + String.format("\"error\":\"Room %d does not exist\"", roomId) + "}";
            return Response.status(404).entity(errorMessage).build();
        }

        return Response.status(200).entity(roomService.isRoomAdmin(roomId, userId)).build();
    }

    @GET
    @Path("{roomId}/admin")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get the admin of a specific room")
    public Response getRoomAdmin(@PathParam("roomId") int roomId) {
        //Check if room exists
        if (!roomService.exists(roomId)) {
            String errorMessage = "{" + String.format("\"error\":\"Room %d does not exist\"", roomId) + "}";
            return Response.status(404).entity(errorMessage).build();
        }

        Room room = roomService.get(roomId);
        int adminId = room.getRoomAdminId();
        return Response.status(200).entity(roomService.getRoomAdmin(adminId)).build();
    }

    @GET
    @Path("create/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create a room")
    public Response createRoom(@PathParam("userId") int userId) {
        //Check if user id is valid
        if (roomService.isUserIdInvalid(userId)) {
            String errorMessage = "{" + String.format("\"error\":\"User %d does not exist\"", userId) + "}";
            return Response.status(404).entity(errorMessage).build();
        }

        //Create a room
        int roomId = roomService.createRoom(userId);
        String successMessage = "{" + "\"data\":" + "{" + "\"roomId\":" + roomId + "}" + "}";
        return Response.status(201).entity(successMessage).build();
    }

    @GET
    @Path("delete/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete a room")
    public Response deleteRoom(@PathParam("roomId") int roomId) {
        //Check if room exists
        if (!roomService.exists(roomId)) {
            String errorMessage = "{" + String.format("\"error\":\"Room %d does not exist\"", roomId) + "}";
            return Response.status(404).entity(errorMessage).build();
        }

        //Delete a room
        roomService.deleteRoom(roomId);
        String successMessage = "{" + String.format("\"data\":\"Room %d has been deleted successfully\"", roomId) + "}";
        return Response.status(201).entity(successMessage).build();
    }

    @GET
    @Path("close/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Close a room")
    public Response closeRoom(@PathParam("roomId") int roomId) {
        //Check if room exists
        if (!roomService.exists(roomId)) {
            String errorMessage = "{" + String.format("\"error\":\"Room %d does not exist\"", roomId) + "}";
            return Response.status(404).entity(errorMessage).build();
        }

        //Close the room
        if (!roomService.closeRoom(roomId)) {
            String successMessage = "{" + String.format("\"error\":\"Room %d is already closed\"", roomId) + "}";
            return Response.status(200).entity(successMessage).build();
        }

        String successMessage = "{" + String.format("\"data\":\"Room %d has been closed\"", roomId) + "}";
        return Response.status(200).entity(successMessage).build();
    }

    @GET
    @Path("/{roomId}/join/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Join a room")
    public Response joinRoom(@PathParam("roomId") int roomId, @PathParam("userId") int userId) {
        //Check if room exists
        if (!roomService.exists(roomId)) {
            String errorMessage = "{" + String.format("\"error\":\"Room %d does not exist\"", roomId) + "}";
            return Response.status(404).entity(errorMessage).build();
        }

        //Check if user id is valid
        if (roomService.isUserIdInvalid(userId)) {
            String errorMessage = "{" + String.format("\"error\":\"User %d does not exist\"", userId) + "}";
            return Response.status(404).entity(errorMessage).build();
        }

        //Check if user is already in the room
        if(roomService.isUserInRoom(roomId, userId)) {
            String errorMessage = "{" + String.format("\"error\":\"User %d is already in this room\"", userId) + "}";
            return Response.status(409).entity(errorMessage).build();
        }

        //Add the user to a room
        roomService.joinRoom(roomId, userId);
        String successMessage = "{" + String.format("\"data\":\"User %d has joined the room %d successfully\"", userId, roomId) + "}";
        return Response.status(201).entity(successMessage).build();
    }

}