package domain.service;

import org.json.JSONObject;

import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import javax.ws.rs.core.MediaType;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import eu.drus.jpa.unit.api.JpaUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(JpaUnit.class)
@ExtendWith(MockitoExtension.class)

class RecommendationServiceImplTest {


    @InjectMocks
    private RecommendationServiceImpl recommendationServiceImpl;


    @Test
    void testIsInteger() {
        Integer integer = 27;
        String string = "Patate";

        assertTrue(recommendationServiceImpl.isInteger(integer));
        assertFalse(recommendationServiceImpl.isInteger(string));

    }

    @Test
    void testErrorSelectGenres() {
        String idGenres = "Chatton";
        String response = recommendationServiceImpl.getAllFilmSelected(idGenres).toString();

        String ERROR = "error";
        String SUCCESS = "success";
        String MALFORMED_REQUEST = "Malformed request";

        JSONObject errorMessage = new JSONObject();
        errorMessage.put(SUCCESS, false);
        errorMessage.put(ERROR, MALFORMED_REQUEST);

        assertEquals(errorMessage.toString(),response);


    }

}