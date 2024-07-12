/*
 * created by ьфч$
 */


package com.phonebook.okhttp;

import com.google.gson.Gson;
import com.phobebook.dto.AddContactSuccessDto;
import com.phobebook.dto.ContactDto;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;

import static com.phonebook.okhttp.LoginOkhttpTests.JSON;

public class AddContactOkhttpTests {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWF4dGVzdEBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTcyMTMyMDMzNiwiaWF0IjoxNzIwNzIwMzM2fQ.XC76GcIE2cCOtmJV5m13K_kO-PGi6mbYVRnRrXvpDDQ";
    private static final String PostURL = "https://contactapp-telran-backend.herokuapp.com/v1/contacts";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    @Test
    public void addContactPositiveTest() throws IOException {
        SoftAssert softAssert = new SoftAssert();
        ContactDto requestDto = ContactDto.builder()
                .id("")
                .name("Bob")
                .lastName("Obama")
                .address("New York City")
                .email("")
                .phone("1472583690")
                .description("")
                .build();

        RequestBody requestBody = RequestBody.create(gson.toJson(requestDto), JSON);

        Request request = new Request.Builder()
                .url(PostURL)
                .post(requestBody)
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();

        softAssert.assertTrue(response.isSuccessful());
        softAssert.assertEquals(response.code(), 200);

        AddContactSuccessDto successDto = gson.fromJson(response.body().string(), AddContactSuccessDto.class);

        softAssert.assertTrue(successDto.getMessage().contains("Contact was added!"));
        softAssert.assertAll();
    }

}
