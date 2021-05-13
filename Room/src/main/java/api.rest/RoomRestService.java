package api.rest;

import domain.model.Room;
import domain.service.RoomService;
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all rooms")
    public List<Room> getAll() {
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
    @Path("{roomId}/admin")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get the admin of a specific room")
    public Response getRoomAdmin(@PathParam("roomId") int roomId) {
        //Check if room exists
        if (!roomService.exists(roomId)) {
            String errorMessage = "\"error\":\"Room does not exist\"";
            return Response.status(404).entity(errorMessage).build();
        }

        Room room = roomService.get(roomId);
        int adminId = room.getRoomAdminId();
        return Response.status(200).entity(roomService.getRoomAdmin(adminId)).build();
    }

}