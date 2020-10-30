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

    // para obter o appid, logar na pagina openweathermap.org/
    @Test
    public void deveObterClima(){
        given()
                .log().all()
                .queryParam("q", "austin")
                .queryParam("appid", "")
                .queryParam("units", "metric")
                .when()
                .get("http://api.openweathermap.org/data/2.5/weather")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", Matchers.is("Austin"))
                .body("coord.lon", Matchers.is(-97.74f))
                .body("main.temp", Matchers.notNullValue())
        ;
    }

    @Test
    public void naoDeveAcessarSemSenha(){
        given()
                .log().all()
                .when()
                .get("http://restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(401)
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasica(){
        given()
                .log().all()
                .when()
                .get("http://admin:senha@restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", Matchers.is("logado"))
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasica2(){
        given()
                .log().all()
                .auth().basic("admin", "senha")
                .when()
                .get("http://restapi.wcaquino.me/basicauth")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", Matchers.is("logado"))
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasicaChallenge(){
        given()
                .log().all()
                .auth().preemptive().basic("admin", "senha")
                .when()
                .get("http://restapi.wcaquino.me/basicauth2")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", Matchers.is("logado"))
        ;
    }
}
