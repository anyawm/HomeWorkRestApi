import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;

public class ReqresInTests {
 /*
        1. Make POST request to https://reqres.in/api/register
            with body { "email": "eve.holt@reqres.in", "password": "pistol" }
        2. Get response { "id": 4, "token": "QpwL5tke4Pnpja7X4" }
        3. Check token is QpwL5tke4Pnpja7X4, id 4
     */

    @Test
    void registerTestToken() {
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", notNullValue())
                .body("id", is(4));
    }

    @Test
    void missingPasswordTest() {
        String data = "{ \"email\": \"eve.holt@reqres.in\"}";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    /*
       1. Make PUT request to https://reqres.in/api/users/2
           with body { "name": "morpheus", "job": "zion resident" }
       2. Get response { "name": "morpheus", "job": "zion resident", "updatedAt": "2023-02-06T14:50:35.796Z" }
       3. Check name is morpheus
    */

    @Test
    void updateDataTest() {
        String data2 = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(data2)
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("morpheus"));
    }

        /*
       1. Make PUT request to https://reqres.in/api/unknown/23
       2. Get response { }
       3. Check statusCode is 404
    */

    @Test
    void userNotFoundTest() {

        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/unknown/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

    /*
       1. Make DELETE request to https://reqres.in/api/users/2
       2. Check statusCode is 204
    */
            @Test
            void deleteAccTest() {
                given()
                        .log().uri()
                        .when()
                        .delete("https://reqres.in/api/users/2")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(204);
            }
}
