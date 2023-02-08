import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class DemoworkshopTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://demowebshop.tricentis.com";
    }

    @Test
    void newsletterSigningTest() {
        String cookieValue = "9A808237CA30E62C3C9916AE2FC3A416392E64172555" +
                "C4551FB32DA74D6D18DEB8824E18D4ABC750F7D24ACF9760039218F73AC7C" +
                "EA21B51DF5A5F1870E4727A1DF782A8BCB982114029BCDC77277CF65DF9AE48" +
                "0367618320C67CDE7E0179E01AD8898478D33305E43A299B2322152D63DBD23D" +
                "34637F1FC2922D0CCA752E4E;",
                body = "email=anna@drake.ru";

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("NOPCOMMERCE.AUTH", cookieValue)
                .body(body)
                .when()
                .post("/subscribenewsletter")
                .then()
                .log().all()
                .statusCode(200)
                .body("Success", is(true))
                .body("Result", is("Thank you for signing up! A verification email has been sent. We appreciate your interest."));
    }

}
