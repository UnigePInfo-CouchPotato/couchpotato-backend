package api;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import javax.ws.rs.core.MediaType;

//import static io.restassured.RestAssured.when;
//import static io.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.equalTo;
//import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;


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
/*
    @Test
    public void testResponseForm() {
        String num = "27";
        given().
                pathParam("idGenres", num).

                when().
                get("/selectGenres/{idGenres}").
                then().
                body(,lessThan(5));

    }*/

    @Test
    public void testResponseForm() {
        String num = "27";
        given().
                pathParam("idGenres", num).

        when().
                get("/selectGenres/{idGenres}").
                then().
                contentType(ContentType.JSON);

    }



}