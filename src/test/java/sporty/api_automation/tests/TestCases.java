package sporty.api_automation.tests;

import static io.restassured.RestAssured.*;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sporty.api_automation.endpoints.Routes;
import sporty.api_automation.models.LocationResponse;
import sporty.api_automation.util.DataReader;
import io.qameta.allure.*;

@Epic("Zippopotam API Automation")
public class TestCases {

    @DataProvider(name = "validZipCodes")
    public Object[][] validZipCodesProvider() {
        return DataReader.getSheetData("validZipCodes");
    }

    @Test(dataProvider = "validZipCodes", description = "TC-001: Verify valid country and zip codes return status 200 and correct location values")
    public void testValidZipCodes(String country, String zipCode, String expectedCountry, String expectedCity) {
        Response response = given()
                .pathParam("country", country)
                .pathParam("zipCode", zipCode)
                .when()
                .get(Routes.get_location_url);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getContentType().contains("application/json"));

        LocationResponse location = response.as(LocationResponse.class);
        Assert.assertEquals(location.getCountry(), expectedCountry);
        Assert.assertNotNull(location.getPlaces());
        Assert.assertFalse(location.getPlaces().isEmpty());
        Assert.assertEquals(location.getPlaces().get(0).getPlaceName(), expectedCity);
    }

    @DataProvider(name = "invalidZipCodes")
    public Object[][] invalidZipCodesProvider() {
        return DataReader.getSheetData("invalidZipCodes");
    }

    @Test(dataProvider = "invalidZipCodes", description = "TC-002: Verify invalid country and zip codes return status 404")
    public void testInvalidZipCodes(String country, String zipCode) {
        Response response = given()
                .pathParam("country", country)
                .pathParam("zipCode", zipCode)
                .when()
                .get(Routes.get_location_url);

        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test(description = "TC-003:Verify that the JSON response structure matches the defined JSON Schema")
    public void testResponseSchema() {
        Response response = given()
                .pathParam("country", "us")
                .pathParam("zipCode", "90210")
                .when()
                .get(Routes.get_location_url);

        Assert.assertEquals(response.getStatusCode(), 200);

        response.then().assertThat()
                .body(io.restassured.module.jsv.JsonSchemaValidator
                        .matchesJsonSchemaInClasspath("sporty/api_automation/resources/location-schema.json"));
    }

    @Test(description = "TC-004: Verify that response time is less than 3000ms")
    public void testResponseTime() {
        Response response = given()
                .pathParam("country", "us")
                .pathParam("zipCode", "90210")
                .when()
                .get(Routes.get_location_url);

        Assert.assertEquals(response.getStatusCode(), 200);
        response.then().assertThat().time(lessThan(3000L));
    }

    @Test(description = "TC-005: Verify POST request is rejected with status 405")
    public void testPostMethod() {
        Response response = given()
                .pathParam("country", "us")
                .pathParam("zipCode", "90210")
                .when()
                .post(Routes.get_location_url);

        Assert.assertEquals(response.getStatusCode(), 405, "Expected HTTP 405 for POST");
    }

    @Test(description = "TC-006: Verify PUT request is rejected with status 405")
    public void testPutMethod() {
        Response response = given()
                .pathParam("country", "us")
                .pathParam("zipCode", "90210")
                .when()
                .put(Routes.get_location_url);

        Assert.assertEquals(response.getStatusCode(), 405, "Expected HTTP 405 for PUT");
    }

    @Test(description = "TC-007: Verify PATCH request is rejected with status 405")
    public void testPatchMethod() {
        Response response = given()
                .pathParam("country", "us")
                .pathParam("zipCode", "90210")
                .when()
                .patch(Routes.get_location_url);

        Assert.assertEquals(response.getStatusCode(), 405, "Expected HTTP 405 for PATCH");
    }

    @Test(description = "TC-008: Verify DELETE request is rejected with status 405")
    public void testDeleteMethod() {
        Response response = given()
                .pathParam("country", "us")
                .pathParam("zipCode", "90210")
                .when()
                .delete(Routes.get_location_url);

        Assert.assertEquals(response.getStatusCode(), 405, "Expected HTTP 405 for DELETE");
    }
}
