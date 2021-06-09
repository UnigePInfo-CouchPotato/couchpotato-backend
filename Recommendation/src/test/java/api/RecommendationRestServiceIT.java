package api;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;

class RecommendationRestServiceIT {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:28080/recommendation";
        RestAssured.port =8080;
    }
    @Test
    void testWelcomeMessage() {
        when()
                .get("/")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("service", equalTo("recommendation"))
                .body("message", equalTo("Welcome!"));
    }

    @Test
    void testGetValidGenres() {

        when()
                .get("/genres")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("genres.id", hasItems(28,12,16,35,80,99,18,10751,14,36,27,10402,9648,10749,878,10770,53,10752,37));
    }

    @Test
    void testResponseForm() {
        given()
            .pathParam("idGenres", 27)
            .when()
            .get("/selectGenres/{idGenres}")
            .then()
                .statusCode(HttpStatus.SC_OK)
            .contentType(ContentType.JSON);
    }


    @Test
    void testResponseId() {

        given()
                .pathParam("idGenres","Patate")
                .when()
                .get("/selectGenres/{idGenres}")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .contentType(ContentType.JSON);
    }


    @Test
    void testResponseLength() {
        given().
                pathParam("idGenres", 27).
                when().
                get("/selectGenres/{idGenres}").
                then().
                statusCode(HttpStatus.SC_OK).
                body("$.size()",lessThan(6));

    }

}