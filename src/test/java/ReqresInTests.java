import lombok.LombokUserData;
import lombok.UpdateUserModel;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

import static helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresInTests {
 /*
        1. Make POST request to https://reqres.in/api/register
            with body { "email": "eve.holt@reqres.in", "password": "pistol" }
        2. Get response { "id": 4, "token": "QpwL5tke4Pnpja7X4" }
        3. Check token is notNullValue, id 4
     */

    @Test
    void registerTestToken() {
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";

        given()
                .spec(Specs.request)
                .body(data)
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .spec(Specs.responseSpec)
                .body("token", notNullValue())
                .body("id", is(4));

    }

    @Test
    void missingPasswordTest() {
        String data = "{ \"email\": \"eve.holt@reqres.in\"}";

        given()
                .spec(Specs.request)
                .body(data)
                .when()
                .post("/register")
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
    void singleUserWithLombokModel() {
        // @formatter:off
        LombokUserData data = given()
                .spec(Specs.request)
                .when()
                .get("/users/2")
                .then()
                .spec(Specs.responseSpec)
                .log().body()
                .extract().as(LombokUserData.class);
        // @formatter:on
        assertEquals(2, data.getUser().getId());
    }

    @Test
    void updateUserTest() {

        UpdateUserModel data = new UpdateUserModel();
        data.setName("morpheus");
        data.setJob("zion resident");
        data.setUpdatedAt("2023-02-08T07:06:10.062Z");

        UpdateUserModel updateUserModel = given()
                .spec(Specs.request)
                .when()
                .body(data)
                .put("/users/2")
                .then()
                .spec(Specs.responseSpec)
                .extract().as((Type) UpdateUserModel.class);

        assertThat(updateUserModel.getName(), is("morpheus"));
    }

        /*
       1. Make GET request to https://reqres.in/api/users
       2. Get response { }
       3. Check statusCode is 200 and Last_Name is Morris
    */

    @Test
    public void checkUsersWithGroove() {
        given()
                .spec(Specs.request)
                .when()
                .get("/users")
                .then()
                .log().body()
                .body("data.findAll{it.id == 5}.last_name", hasItem("Morris"));
    }

    /*
       1. Make DELETE request to https://reqres.in/api/users/2
       2. Check statusCode is 204
    */
            @Test
            void deleteAccTest() {
                given()
                        .spec(Specs.request)
                        .when()
                        .delete("/users/2")
                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(204);
            }


    @Test
    public void resourcesListWithGroove() {
        given()
                .spec(Specs.request)
                .filter(withCustomTemplates())
                .when()
                .get("/unknown")
                .then()
                .spec(Specs.responseSpec)
                .body("data.findAll{it.id == 2}.name", hasItem("fuchsia rose"))
                .body("data.findAll{it.id == 5}.color", hasItem("#E2583E"))
                .body("data.findAll{it.id == 3}.year", hasItem(2002))
                .body("support", hasKey("url"))
                .body("support", hasKey("text"));
    }
}
