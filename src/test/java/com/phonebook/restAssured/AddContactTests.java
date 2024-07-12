/*
 * created by max$
 */


package com.phonebook.restAssured;

import com.phobebook.dto.AuthRequestDto;
import com.phobebook.dto.ContactDto;
import com.phobebook.dto.ErrorDto;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class AddContactTests extends TestBase {
    ContactDto contactDto = ContactDto.builder()
            .name("Lamile")
            .lastName("Kuku")
            .email("kuku@gmail.com")
            .phone("09876543211")
            .address("Berlin")
            .description("rightWinger")
            .build();


    @Test
    public void addContactSuccessTest() {
        //    String message =
        given()
                .header(AUTH, TOKEN)
                .body(contactDto)
                .contentType(ContentType.JSON)
                .that()
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body(containsString("Contact was added!"));
        //      .extract().path("message");

        //  System.out.println(message);
        //Contact was added! ID: d70a4261-33b5-4510-9739-df7cd3018a45

    }

    @Test
    public void addContactWithoutNameTest() {
        ContactDto contactDto1 = ContactDto.builder()
                .lastName("Kuku")
                .email("kuku@gmail.com")
                .phone("09876543211")
                .address("Berlin")
                .description("rightWinger")
                .build();


        ErrorDto errorDto = given()
                .header(AUTH, TOKEN)
                .body(contactDto1)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .extract().response().as(ErrorDto.class);

        Assert.assertTrue(errorDto.getMessage().toString().contains("name=must not be blank"));

    }
    @Test
    public void addContactWithInvalidPhone(){
        ContactDto contactDto2 = ContactDto.builder()
                .name("Lamile")
                .lastName("Kuku")
                .email("kuku@gmail.com")
                .phone("09876")
                .address("Berlin")
                .description("rightWinger")
                .build();

      //  ErrorDto errorDto =
                given()
                .header(AUTH, TOKEN)
                .body(contactDto2)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                        .assertThat().body("message.phone",containsString("Phone number must contain " +
                                "only digits! And length min 10, max 15!"));
        // .extract().response().as(ErrorDto.class);

    }
}
