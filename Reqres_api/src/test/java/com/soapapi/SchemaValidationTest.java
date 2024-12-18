package com.soapapi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.*;
import static io.restassured.path.json.JsonPath.given;

public class SchemaValidationTest {
    @BeforeTest
    public static void setup() {
        RestAssured.baseURI = "http://www.dneonline.com/";
    }
    @Test
    public void validate()throws IOException
    {
       File file =new File("src/main/resources/calculator.xml");
        FileInputStream fileInputStream= new FileInputStream(file);
        String body= IOUtils.toString(fileInputStream,"UTF-8");
        Response res = given()
                .log().all().header("Content-Type","text/xml")
                .body(body).accept(ContentType.XML)
                .when().post("calculator.asmx")
                .then()
                .body("//*:AddResult.text()", Matchers.equalTo("5"))
                .log().all()
                .statusCode(200)
                .extract().response();

    }
}
