package rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class OlaMundoTest {

    @Test
    public void testOlaMundo(){

        Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
        Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        Assert.assertEquals(200, response.getStatusCode());

        ValidatableResponse validatableResponse = response.then();
        validatableResponse.statusCode(200);
    }

    @Test
    public void devoConhecerOutrasFormasRestAssured(){

        given()
        .when()
            .get("http://restapi.wcaquino.me/ola")
        .then()
            .statusCode(200);
    }

    @Test
    public void devoConhecerMatchersHamcrest(){
        Assert.assertThat("Maria", Matchers.is("Maria"));
        Assert.assertThat(128d, Matchers.greaterThan(120d));
        Assert.assertThat(128, Matchers.isA(Integer.class));
    }

    @Test
    public void deveValidarBody(){
        given()
                .when()
                .get("http://restapi.wcaquino.me/ola")
                .then()
                .statusCode(200)
                .body(Matchers.is("Ola Mundo"));

    }
}
