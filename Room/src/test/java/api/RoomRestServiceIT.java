package api;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

public class RoomRestServiceIT {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:28080/rooms";
        RestAssured.port = 9000;
    }

    @Test
    public void testWelcomeMessage() {
        when().get("/").then().body(containsString("Welcome to the room service"));
    }

}