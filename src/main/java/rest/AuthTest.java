package rest;

import org.hamcrest.Matchers;
import org.hamcrest.Matchers.*;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class AuthTest {

    @Test
    public void deveAcessarSWapi(){
        given()
                .log().all()
                .when()
                .get("https://swapi.dev/api/people/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", Matchers.is("Luke Skywalker"))
                ;
    }
}
