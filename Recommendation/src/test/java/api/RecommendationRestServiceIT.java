package api;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;

import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import io.restassured.RestAssured;

class RecommendationRestServiceIT {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:28080/recommendation";
        RestAssured.port =8080;
    }




    @Test
    void testGetValidFilms() {
        when()
                .get("/selectGenres=27,18")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

}