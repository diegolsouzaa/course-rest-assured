package rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.responseSpecification;
import static org.hamcrest.Matchers.*;

public class UserJsonTest {

    public static RequestSpecification requestSpecification;
    public static ResponseSpecification responseSpecification;


    @BeforeClass
    public static void setup(){
        RestAssured.baseURI = "http://restapi.wcaquino.me";
    }

    @Test
    public void deveVerificarPrimeiroNivel(){

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.log(LogDetail.ALL);
        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(200);
        responseSpecification = responseSpecBuilder.build();


        given()
                .spec(requestSpecification)
                .when()
                .get("/users/1")
                .then()
        //.statusCode(200)
                .spec(responseSpecification)
        .body("id", is(1))
        .body("name", containsString("Silva"))
        .body("age", greaterThan(18))
        ;
    }

    @Test
    public void deveVerificarPrimeiroNivelOutrasFormas(){

        Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/users/1");

        //path
        //Assert.assertEquals(1,response.path("id"));

        //jsonpath
        JsonPath jpath = new JsonPath(response.asString());
        //Assert.assertEquals(1, jpath.getInt("id"));

        //from
        int id = JsonPath.from(response.asString()).getInt("id");
        Assert.assertEquals(1, id);
    }

    @Test
    public void deveVerificarSegundoNivel(){
        given()
                .when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .body("name", containsString("Joaquina"))
                .body("endereco.rua", is("Rua dos bobos"));
    }

    @Test
    public void deveVerificarLista(){
        given()
                .when()
                .get("/users/3")
                .then()
                .statusCode(200)
                .body("name", containsString("Ana"))
                .body("filhos", hasSize(2))
                .body("filhos[0].name", is("Zezinho"))
                .body("filhos[1].name", is("Luizinho"))
                .body("filhos.name", hasItems("Zezinho", "Luizinho"))
        ;
    }

    @Test
    public void deveRetornarErroUsuarioInexistente(){
        given()
                .when()
                .get("/users/4")
                .then()
                .statusCode(404)
                .body("error", is("Usuário inexistente"));
    }

    @Test
    public void deveVerificarListaRaiz(){
        given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
                .body("age[1]", is(25))
                .body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
                .body("salary", contains(1234.5678f, 2500, null))
        ;
    }

    @Test
    public void devoFazerVerificacoesAvancadas(){
        given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("age.findAll{it <= 25}.size", is(2))
                .body("age.findAll{it <= 25 && it > 20}.size", is(1))
                .body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
                .body("findAll{it.age <= 25}[0].name", is("Maria Joaquina"))
                .body("findAll{it.age <= 25}[-1].name", is("Ana Júlia"))
                .body("find{it.age <= 25}.name", is("Maria Joaquina"))
                .body("find{it.age <= 25}.name", is("Maria Joaquina"));
    }

    @Test
    public void devoUnirJsonPathComJava(){
        ArrayList<String> names =
        given()
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .extract().path("name.findAll{it.startsWith('Maria')}")

                ;
        Assert.assertEquals(1, names.size());
        Assert.assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());


    }



}
