package com.reqres;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class ReqresTest {


    @BeforeTest
    public static void setup() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    public void testGetUsers() {
        String key = given().log().all()
                .when().get("api/users?page=2")
                .then().statusCode(200)
                .extract().response().asString();
        System.out.println(key);
        JsonPath jsonPath= new JsonPath(key);
        String id  = jsonPath.getString("data[0].id");
        System.out.println("PLease find the id");
        System.out.println(id);
    }
   @Test
    public void testSchemaValidationGetUsers() {
                given()
                        .log().all()
                        .when()
                            .get("api/users?page=2")
                        .then()
                            .assertThat().body(matchesJsonSchemaInClasspath("listuserdetails.json"));

    }


    @Test
    public void getSingleUser() {

        ValidatableResponse body = given()
                .when()
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .body("data.first_name", equalTo("Janet"))
                .body("data.last_name", equalTo("Weaver"));
    }
    @Test
    public void getListUser()
    {
        String luser =given()
                .when()
                .get("/api/unknown")
                .then()
                .statusCode(200)
                .extract().response().asString();
        JsonPath js =new JsonPath(luser);
        String a= js.getString("data");
        System.out.println(a);
    }
    @Test
    public void testDeleteUser() {
        given()
                .when()
                .delete("/api/users/2")
                .then()
                .statusCode(204).assertThat();
    }
    @Test
    public void create()
    {
        String requestBody = "{\n" +
                "  \"name\": \"morpheus\",\n" +
                "  \"job\": \"leader\"\n" +
                "}";
        String cre =given()
                .body(requestBody)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201).extract().response().asString();
        JsonPath j= new JsonPath(cre);
        String c = j.getString("id");
        System.out.println(c);
    }
    @Test
    public void update()
    {String requestBody = "{\n" +
            "  \"name\": \"morpheus\",\n" +
            "  \"job\": \"zion resident\"\n" +
            "}";
        String up= given()
                .log().all()
                .when()
                .put("/api/users/2")
                .then()
                .statusCode(200).extract().response().toString();
//        JsonPath ju= new JsonPath(up);
//        String p= ju.getString()
        System.out.println(up);
    }

}
