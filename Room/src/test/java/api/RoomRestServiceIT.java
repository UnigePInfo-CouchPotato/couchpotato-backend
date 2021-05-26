package api;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

import domain.model.Room;
import domain.model.Room_User;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import io.restassured.RestAssured;

public class RoomRestServiceIT {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:28080/rooms";
        RestAssured.port = 8080;
    }

    @Test
    void testWelcomeMessage() {
        when()
                .get("/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString("Welcome to the room service"));
    }

    @Test
    void testCount() {
        int numberOfRooms = when()
                                .get("/count")
                                .then()
                                .statusCode(HttpStatus.SC_OK)
                                .extract()
                                .as(int.class);

        assertThat(numberOfRooms, equalTo(5));
    }

    @Test
    void testGetValidRoom() {
        when()
                .get("/Fgf2NLjhh9mx")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("roomId", equalTo("Fgf2NLjhh9mx"))
                .body("roomAdminId", equalTo(6))
                .body("roomClosed", equalTo(true));
    }

    @Test
    void testGetInvalidRoom() {
        when()
                .get("/1b02c2ej1lvc")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("error", equalTo(String.format("Room %s does not exist", "1b02c2ej1lvc")));
    }

    @Test
    void testGetAllRooms() {
        Room[] rooms =
                when()
                        .get("/all")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract()
                        .as(Room[].class);

        assertThat(rooms.length, equalTo(5));
        assertThat(rooms[0].getRoomId(), equalTo("WN5sgnxYD8tC"));
        assertThat(rooms[1].getRoomId(), equalTo("99rxfyog0a87"));
        assertThat(rooms[2].getRoomId(), equalTo("7b07c2qj7lvc"));
        assertThat(rooms[3].getRoomId(), equalTo("JC3Tzrx2c1nx"));
        assertThat(rooms[4].getRoomId(), equalTo("Fgf2NLjhh9mx"));

        assertThat(rooms[0].isRoomClosed(), equalTo(false));
        assertThat(rooms[4].isRoomClosed(), equalTo(true));
    }

    @Test
    void testGetAllRoomUsers() {
        Room_User[] roomUsers =
                when()
                        .get("/room-users")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract()
                        .as(Room_User[].class);

        assertThat(roomUsers.length, equalTo(24));
        assertThat(roomUsers[0].getRoomId(), equalTo("7b07c2qj7lvc"));
        assertThat(roomUsers[7].getRoomId(), equalTo("JC3Tzrx2c1nx"));
        assertThat(roomUsers[12].getRoomId(), equalTo("WN5sgnxYD8tC"));
        assertThat(roomUsers[20].getRoomId(), equalTo("Fgf2NLjhh9mx"));

        assertThat(roomUsers[0].getUserId(), equalTo(1));
        assertThat(roomUsers[0].getGenres(), equalTo("action,comedy"));
        assertThat(roomUsers[0].getVotes(), equalTo("[2, 5, 1, -1, 0]"));
    }

    @Test
    void testExistsIsFalse() {
        given()
                .queryParam("roomId", "1b02c2ej1lvc")
                .when()
                .get("/exists")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("data.exists", equalTo(false));
    }

    @Test
    void testExistsIsTrue() {
        given()
                .queryParam("roomId", "7b07c2qj7lvc")
                .when()
                .get("/exists")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("data.exists", equalTo(true));
    }

    @Test
    void testDeleteValidRoom() {
        given()
                .queryParam("roomId", "99rxfyog0a87")
                .when()
                .get("/delete")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(String.format("Room %s has been deleted successfully", "99rxfyog0a87")));
    }

    @Test
    void testDeleteInvalidRoom() {
        given()
                .queryParam("roomId", "1b02c2ej1lvc")
                .when()
                .get("/delete")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("error", equalTo(String.format("Room %s does not exist", "1b02c2ej1lvc")));
    }

}