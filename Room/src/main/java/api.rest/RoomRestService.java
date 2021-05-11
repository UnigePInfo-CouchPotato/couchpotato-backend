package api.rest;

import java.util.List;

import domain.model.Room;
import domain.service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
    @Path("{roomId}/exists")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Check if a specific room exists")
    public boolean exists(@PathParam("roomId") int roomId) {
        return roomService.exists(roomId);
    }

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String sayPlainTextHello() {
        return "Welcome to the rooms service";
    }

}