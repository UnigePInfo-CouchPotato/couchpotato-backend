package api;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

import domain.model.Users;
import domain.model.Preference;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import io.restassured.RestAssured;

class UserManagementRestServiceIT {

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
                .body("service", equalTo("user-mgmt"))
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


}