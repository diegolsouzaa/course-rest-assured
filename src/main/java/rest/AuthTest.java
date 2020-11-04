package rest;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import org.hamcrest.Matchers;
import org.hamcrest.Matchers.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void deveFazerAutenticacaoComTokenJWT(){
        Map<String, String> login = new HashMap<String, String>();
        login.put("email", "diego@lauriano");
        login.put("senha", "1234");

        String token =
        given()
                .log().all()
                .body(login)
                .contentType(ContentType.JSON)
                .when()
                .post("http://barrigarest.wcaquino.me/signin")
                .then()
                .log().all()
                .statusCode(200)
                .extract().path("token")
                ;
        given()
                .log().all()
                .header("Authorization", "JWT " + token)
                .when()
                .get("http://barrigarest.wcaquino.me/contas")
                .then()
                .log().all()
                .statusCode(200)
                .body("nome", Matchers.hasItem("Conta de teste"))
        ;
    }

    @Test
    public void deveAcessarAplicacaoWeb(){

        String cookie =
        //login
        given()
                .log().all()
                .formParam("email", "diego@lauriano")
                .formParam("senha", "1234")
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
                .when()
                .post("http://seubarriga.wcaquino.me/logar")
                .then()
                .log().all()
                .extract().header("set-cookie");


        cookie = cookie.split("=")[1].split(";")[0];
        System.out.println(cookie);

        //obter conta
        String body =
        given()
                .log().all()
                .cookie("connect.sid", cookie)
                .when()
                .get("http://seubarriga.wcaquino.me/contas")
                .then()
                .log().all()
                .statusCode(200)
                .body("html.body.table.tbody.tr[0].td[0]", Matchers.is("Conta de teste"))
                .extract().body().asString();


        System.out.println("------------------");
        XmlPath xmlPath = new XmlPath(XmlPath.CompatibilityMode.HTML, body);
        System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));

    }
}
