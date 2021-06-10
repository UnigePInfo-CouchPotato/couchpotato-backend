package api;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

import domain.model.Room;
import domain.model.RoomUser;
import io.restassured.http.ContentType;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import io.restassured.RestAssured;

import java.util.UUID;

class RoomRestServiceIT {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:28080/rooms";
        RestAssured.port = 9080;

        when()
                .get("/test-mode")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString("Test mode enabled"));
    }

    @Test
    void testWelcomeMessage() {
        when()
                .get("/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("service", equalTo("room"))
                .body("message", equalTo("Welcome!"));
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
                .body("roomClosed", equalTo(true))
                .body("numberOfMovies", equalTo(0));
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
    void testGetRoomAdmin() {
        given()
                .queryParam("roomId", "7b07c2qj7lvc")
                .when()
                .get("/admin")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString("{}"));
    }

    @Test
    void testIsRoomAdmin() {
        given()
                .queryParam("roomId", "7b07c2qj7lvc")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                .when()
                .get("/is-room-admin")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("data.isRoomAdmin", equalTo(true));
    }

    @Test
    void testGetUsers() {
        String userNo = "user0";
        String userNickname = "Test administrator";
        String str = "[{" + String.format("\"user0\":\"%s\"", userNickname) + "}]";
        JSONArray expected = new JSONArray(str);
        String response = given()
                .queryParam("roomId", "99rxfyog0a87")
                .when()
                .get("/users")
                .asString();

        JSONArray users = new JSONArray(response);
        JSONObject user = users.getJSONObject(0);

        assertThat(expected.toString(), equalTo(users.toString()));
        assertThat(user.keys().next(), equalTo(userNo));
        assertThat(user.get(userNo), equalTo(userNickname));
    }

    @Test
    void testCreateRoom() {
        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                .queryParam("roomId", "99rxfyog0a87")
                .when()
                .get("/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body(containsString("roomId"));

        given()
                .queryParam("roomId", "99rxfyog0a87")
                .when()
                .get("/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body(containsString("Unauthorized"));
    }

    @Test
    void testEndVotingPeriod() {
        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                .queryParam("roomId", "99rxfyog0a87")
                .when()
                .get("/end-vote")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body(containsString("Unauthorized"));

        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "{}")
                .queryParam("roomId", "Fgf2NLjhh9mx")
                .when()
                .get("/end-vote")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(String.format("Voting period of room %s has been ended successfully", "Fgf2NLjhh9mx")));

        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                .when()
                .get("/end-vote")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("error", equalTo("Invalid parameters. Please check your request"));
    }

    @Test
    void testEndJoinPeriod() {
        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                .queryParam("roomId", "JC3Tzrx2c1nx")
                .when()
                .get("/end-join")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body(containsString("Unauthorized"));

        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "{}")
                .queryParam("roomId", "Fgf2NLjhh9mx")
                .when()
                .get("/end-join")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(String.format("Join period of room %s has been ended successfully", "Fgf2NLjhh9mx")));

        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                .queryParam("roomId", "JC3Tzrx2c1nx")
                .when()
                .get("/end-join")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body(containsString("Unauthorized"));
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
    void testCloseInvalidRoom() {
        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                .queryParam("roomId", "99rxfyog0a87")
                .when()
                .get("/close")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("error", equalTo(String.format("Room %s does not exist", "99rxfyog0a87")));
    }

    @Test
    void testCloseValidRoom() {
        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                .queryParam("roomId", "WN5sgnxYD8tC")
                .when()
                .get("/close")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("error", equalTo(String.format("Unauthorized to close the room %s", "WN5sgnxYD8tC")));

        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "{}")
                .queryParam("roomId", "Fgf2NLjhh9mx")
                .when()
                .get("/close")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CONFLICT)
                .body("error", equalTo(String.format("Room %s is already closed", "Fgf2NLjhh9mx")));

        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "{}")
                .queryParam("roomId", "7b07c2qj7lvc")
                .when()
                .get("/close")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(String.format("Room %s has been closed successfully", "7b07c2qj7lvc")));
    }

    @Test
    void testJoinRoom() {
        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "{}")
                .queryParam("roomId", "WN5sgnxYD8tC")
                .when()
                .get("/join")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", equalTo(String.format("You have joined the room %s successfully", "WN5sgnxYD8tC")));

        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "{}")
                .queryParam("roomId", "Fgf2NLjhh9mx")
                .when()
                .get("/join")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CONFLICT)
                .body("error", equalTo(String.format("Join period has been ended for room %s", "Fgf2NLjhh9mx")));
    }

    @Test
    void testVote() {
        JSONObject requestBody = new JSONObject();
        String str = "{\r\n" +
                    "\"roomId\": \"WN5sgnxYD8tC\",\r\n" +
                    "\"choice\": {}\r\n" +
                    "}";

        String str2 = "{\r\n" +
                "\"roomId\": \"Fgf2NLjhh9mx\",\r\n" +
                "\"choice\": {}\r\n" +
                "}";

        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + "{}")
                .queryParam("roomId", "Fgf2NLjhh9mx")
                .when()
                .get("/end-vote")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("message", equalTo(String.format("Voting period of room %s has been ended successfully", "Fgf2NLjhh9mx")));

        given()
                .header("Content-type", "application/json")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                .contentType(ContentType.JSON)
                .body(str2)
                .when()
                .post("/vote")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CONFLICT)
                .body("error", equalTo(String.format("Voting is no more allowed for room %s", "Fgf2NLjhh9mx")));

        given()
                .header("Content-type", "application/json")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/vote")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("error", equalTo("Invalid parameters. Please check your request"));

        given()
                .header("Content-type", "application/json")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                .contentType(ContentType.JSON)
                .body(str)
                .when()
                .post("/vote")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body(containsString("Unauthorized"));
    }

    @Test
    void testGetFinalMovie() {
        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                .queryParam("roomId", "WN5sgnxYD8tC")
                .when()
                .get("/final")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body(containsString("Unauthorized"));
    }

    @Test
    void testGetResults() {
        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + UUID.randomUUID())
                .queryParam("roomId", "WN5sgnxYD8tC")
                .when()
                .get("/results")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(containsString(""));
    }

    @Test
    void testGetAllRoomUsers() {
        RoomUser[] roomUsers =
                when()
                        .get("/room-users")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract()
                        .as(RoomUser[].class);

        assertThat(roomUsers[0].getRoomId(), equalTo("7b07c2qj7lvc"));
        assertThat(roomUsers[7].getRoomId(), equalTo("JC3Tzrx2c1nx"));
        assertThat(roomUsers[12].getRoomId(), equalTo("WN5sgnxYD8tC"));
        assertThat(roomUsers[20].getRoomId(), equalTo("Fgf2NLjhh9mx"));

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
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("error", equalTo(String.format("Room %s does not exist", "1b02c2ej1lvc")));
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
