/*
 * created by max$
 */


package com.phonebook.restAssured;

import com.phobebook.dto.ContactDto;
import com.phobebook.dto.ErrorDto;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class DeleteContactByIdTests extends TestBase {

    String id;

    @BeforeMethod
    public void precondition() {
        ContactDto contactDto = ContactDto.builder()
                .name("Lamile")
                .lastName("Kuku")
                .email("kuku@gmail.com")
                .phone("09876543211")
                .address("Berlin")
                .description("rightWinger")
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

    @Test
    public void deleteContactByIdSuccessTest(){
      //  String message =
                given()
                .header(AUTH,TOKEN)
                .when()
                .delete("contacts/"+id)
                .then()
                .assertThat().statusCode(200)
                        .assertThat().body("message",equalTo("Contact was deleted!"));
              //  .extract().path("message");


    }

    @Test
    public void deleteContactByWrongId(){

                given()
                .header(AUTH, TOKEN)
                .when()
                .delete("contacts/2")
                .then()
                .assertThat()
                        .statusCode(400)
                .assertThat().body("message",containsString("not found in your"));
              // .extract().path("message");
     //   .extract().response().as(ErrorDto.class);

    }
}
