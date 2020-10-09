package rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import javafx.application.Application;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.jws.soap.SOAPBinding;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class VerbosTest {


    @Test
    public void deveSalvarUsuario(){
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"Jose\", \"age\": 50 }")
                .when()
                .post("http://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("id",is(notNullValue()))
                .body("name", is("Jose"));

    }

    @Test
    public void deveSalvarUsuarioUsandoMap(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "usuario via map");
        params.put("age", 28);

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when()
                .post("http://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("id",is(notNullValue()))
                .body("name", is("usuario via map"))
                .body("age", is(28));
    }

    @Test
    public void deveSalvarUsuarioUsandoObjeto(){
        User user = new User("Usuario via objeto", 35);

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("http://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(201)
                .body("id",is(notNullValue()))
                .body("name", is("Usuario via objeto"))
                .body("age", is(35));
    }

    @Test
    public void deveDeserializarObjetoAoSalvarUsuario(){
        User user = new User("Usuario desserializado", 35);

       User usuarioInserido = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("http://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(201)
                .extract().body().as(User.class);

       Assert.assertThat(usuarioInserido.getId(), notNullValue());
        Assert.assertEquals("Usuario desserializado", usuarioInserido.getName());
        Assert.assertThat(usuarioInserido.getAge(), is(35));

    }

    @Test
    public void naoDeveSalvarUsuarioSemNome(){
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{\"age\": 50 }")
                .when()
                .post("http://restapi.wcaquino.me/users")
                .then()
                .log().all()
                .statusCode(400)
                .body("id", is(nullValue()))
                .body("error", is("Name é um atributo obrigatório"));
    }

    @Test
    public void deveAlterarNomeDoUsuario(){
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"Diego Lauriano\", \"age\": 30 }")
                .when()
                .put("http://restapi.wcaquino.me/users/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Diego Lauriano"));
    }

    @Test
    public void devoCustomizarURL(){
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"Diego Lauriano\", \"age\": 30 }")
                .when()
                .put("http://restapi.wcaquino.me/{entidade}/{userId}", "users","1")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Diego Lauriano"));
    }

    @Test
    public void devoCustomizarURL2(){
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"Diego Lauriano\", \"age\": 30 }")
                .pathParam("entidade", "users")
                .pathParam("userId", 1)
                .when()
                .put("http://restapi.wcaquino.me/{entidade}/{userId}")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Diego Lauriano"));
    }

    @Test
    public void deveRemoverUsuario(){
        given()
                .log().all()
                .when()
                .delete("http://restapi.wcaquino.me/users/1")
                .then()
                .log().all()
                .statusCode(204);
    }

    @Test
    public void naoDeveRemoverUsuarioInexistente(){
        given()
                .log().all()
                .when()
                .delete("http://restapi.wcaquino.me/users/167")
                .then()
                .log().all()
                .statusCode(400)
                .body("error", is("Registro inexistente"));
    }
}
