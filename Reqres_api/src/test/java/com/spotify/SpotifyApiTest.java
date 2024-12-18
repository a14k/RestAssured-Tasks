package com.spotify;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class SpotifyApiTest {
    String token;
    //private static final String BASE_URL = "https://api.spotify.com";
    //private static final String ACCESS_TOKEN = "BQDzZoz5tSUs_S501stoZ5dLDUTOQ-0_v_01O733mW1E99YBbxi-BgOhu2igEIBwjWJAVuY3k4bZqij4xjTKIH3ndK25Mg36Mk25B29rSGpIQSu-o8LGeeCXBO2oeCjMn_k4nmOhAXWfKjhsN_LnSJHn6IxNjnCSEP1CLr-jTl_kJYrUbfBIOSW5kPZztI6elmZgJz9E4ByhVg";
    private static String userId;
    private static String playlistId;

//    @BeforeTest
//    public static void setup() {
//        RestAssured.baseURI = BASE_URL;
//    }
    @Test
    public String postAccessToken(){
        baseURI = "https://accounts.spotify.com/api";


        Response tok = given().log().all()
                .formParam("grant_type", "client_credentials")
                .formParam("client_id", "5af8a47b362d4de381a8d0c8f962bba2")
                .formParam("client_secret", "aeaff74011cf404397f7380b1168b132")
                .when()
                .post(" /token ").then().log().all().assertThat().statusCode(200).extract().response();

         token = tok.jsonPath().getString("access_token");

        System.out.println("token "+token);
        return token;
    }
    @Test
    public void getCurrentUserProfile() {
        baseURI="https://api.spotify.com";
        String accessToken = postAccessToken();
        String response = given().auth().oauth2(accessToken).log().all()
//                .header("Authorization", "Bearer " +accessToken)
                .when()
                .get("/v1/me")
                .then().log().all()
                .statusCode(200).extract()
                .response().toString();
        JsonPath jsonPath=new JsonPath(response);
        String userId = jsonPath.getString("id");
        System.out.println("User ID: " + userId);
    }
//    @Test
    public void postCreatePlaylist() {
        String requestBody = "{\n" +
                "    \"name\": \"Ank's Playlist\",\n" +
                "    \"description\": \"New Playlist ..\",\n" +
                "    \"public\": false\n" +
                "}";

        Response response = given()
               // .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/users/" + userId + "/playlists")
                .then()
                .statusCode(201)
                .body("name", equalTo("New Playlist"))
                .log().all()
                .extract()
                .response();

        playlistId = response.jsonPath().getString("id");
        System.out.println("Playlist ID: " + playlistId);
    }

}
