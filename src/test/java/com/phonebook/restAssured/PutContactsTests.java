/*
 * created by max$
 */


package com.phonebook.restAssured;

import com.phobebook.dto.ContactDto;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class PutContactsTests extends TestBase {

    String id;

    @BeforeMethod
    public void precondition() {
        ContactDto contactDto = ContactDto.builder()
                .name("Lalala")
                .lastName("Kuku")
                .email("kuku@gmail.com")
                .phone("09876543211")
                .address("Berlin")
                .description("leftWinger")
                .build();

        String message = given()
                .header(AUTH, TOKEN)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .extract().path("message");

        String[] split = message.split(": ");
        id = split[1];
    }

    //200
    @Test
    public void updateContactPositiveTest() {
        ContactDto updateContactDto = ContactDto.builder()
                .name("Bob")
                .lastName("Vorobey")
                .address("Paris")
                .id(id)
                .build();


        given()
                .header(AUTH, TOKEN)
                .body(updateContactDto)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", equalTo("Contact was updated"));

    }

    //500
    @Test
    public void updateContactsWithWrongContentType() {
        ContactDto updateContactDto = ContactDto.builder()
                .name("Bob")
                .lastName("Vorobey")
                .address("Paris")
                .id(id)
                .build();

        given()
                .header(AUTH, TOKEN)
                .body(updateContactDto)
                .contentType(ContentType.TEXT)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(500)
                .assertThat().body("message",
                        equalTo("Content type 'text/plain;charset=ISO-8859-1' not supported"))
                .assertThat().body("error",
                        equalTo("Internal Server Error"));

    }

    //500
    @Test
    public void updateContactsWithWrongBody() {

        given()
                .header(AUTH, TOKEN)
                .body("updateContactDto")
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(500)
                .assertThat().body("message",
                        containsString("JSON parse error: Unrecognized token"))
                .assertThat().body("error",
                        equalTo("Internal Server Error"));

    }

    //401
    @Test
    public void updateContactsWithWrongTokenAuthorization() {
        ContactDto updateContactDto = ContactDto.builder()
                .name("Bob")
                .lastName("Vorobey")
                .address("Paris")
                .id(id)
                .build();


        given()
                .header(AUTH, "sdgasha")
                .body(updateContactDto)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message",
                        equalTo("JWT strings must contain exactly 2 period characters. Found: 0"))
                .assertThat().body("error",
                        equalTo("Unauthorized"));


    }

    //400
    @Test
    public void updateContactsWithWrongId() {
        ContactDto updateContactDto = ContactDto.builder()
                .name("Bob")
                .lastName("Vorobey")
                .address("Paris")
                .id("id")
                .build();

        given()
                .header(AUTH, TOKEN)
                .body(updateContactDto)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message",
                        equalTo("Contact with id: id not found in your contacts!"))
                .assertThat().body("error",
                        equalTo("Bad Request"));
    }

    //400
    @Test
    public void updateContactsWithoutId() {
        ContactDto updateContactDto = ContactDto.builder()
                .name("Bob")
                .lastName("Vorobey")
                .address("Paris")
                .build();

        given()
                .header(AUTH, TOKEN)
                .body(updateContactDto)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("error",
                        equalTo("Bad Request"))
                .assertThat().body("message.toString()", containsString("id:must not be blank"));

    }

    @Test
    public void updateContactsWithWrongEndpoint() {
        ContactDto updateContactDto = ContactDto.builder()
                .name("null")
                .lastName("null")
                .address("Berlin")
                .id(id)
                .build();

        given()
                .header(AUTH, TOKEN)
                .body(updateContactDto)
                .contentType(ContentType.JSON)
                .when()
                .put("contacts/add")
                .then()
                .assertThat().statusCode(403);
    }


}
