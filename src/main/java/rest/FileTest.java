package rest;

import static org.hamcrest.Matchers.is;

import org.junit.Assert;
import org.junit.Test;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class FileTest {

    @Test
    public void deveObrigarEnvioArquivo(){
        given()
                .log().all()
                .when()
                .post("http://restapi.wcaquino.me/upload")
                .then()
                .log().all()
                .statusCode(404)
                .body("error" , is("Arquivo não enviado"));
    }

    @Test
    public void deveFazerUploadDeArquivo(){
        given()
                .log().all()
                .multiPart("arquivo", new File("src/main/resources/teste com.pdf"))
                .when()
                .post("http://restapi.wcaquino.me/upload")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("teste com.pdf"))
        ;
    }

    @Test
    public void naoDeveFazerUploadDeArquivoGrande(){
        given()
                .log().all()
                .multiPart("arquivo", new File("src/main/resources/Testes de API Rest.pdf"))
                .when()
                .post("http://restapi.wcaquino.me/upload")
                .then()
                .log().all()
                .statusCode(413)
        ;
    }


        @Test
        public void deveBaixarArquivo() throws IOException {

            byte[] img =
            given()
                    .log().all()
                    .when()
                    .get("http://restapi.wcaquino.me/download")
                    .then()
                    .statusCode(200)
                    .extract().asByteArray()
            ;
            File imagem = new File("src/main/resources/file.jpeg");
            OutputStream out = new FileOutputStream(imagem);
            out.write(img);
            out.close();

            Assert.assertThat(imagem.length(), lessThan(100000L));
        }
    }

