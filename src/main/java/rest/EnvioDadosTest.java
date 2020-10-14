package rest;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class EnvioDadosTest {

    @Test
    public void deveEnviarValorViaQuery(){
        given()
                .log().all()
                .when()
                .get("http://restapi.wcaquino.me/v2/users?format=json")
                .then()
                .log().all()
        .statusCode(200)
        .contentType(ContentType.JSON)
                ;
    }

    @Test
    public void deveEnviarValorViaQueryParam(){
        given()
                .log().all()
                .queryParam("format", "xml")
                .queryParam("outra", "coisa")
                .when()
                .get("http://restapi.wcaquino.me/v2/users")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.XML)
                .contentType(Matchers.containsString("utf-8"))
        ;
    }

    @Test
    public void deveEnviarValorViaHeader(){
        given()
                .log().all()
                .accept(ContentType.JSON)
                .when()
                .get("http://restapi.wcaquino.me/v2/users")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)

        ;
    }
}
