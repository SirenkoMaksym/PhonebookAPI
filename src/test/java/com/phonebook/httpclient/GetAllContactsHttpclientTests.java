/*
 * created by ьфч$
 */


package com.phonebook.httpclient;

import com.google.gson.Gson;
import com.phobebook.dto.AllContactsDto;
import com.phobebook.dto.ContactDto;
import com.phobebook.dto.ErrorDto;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class GetAllContactsHttpclientTests {
    Gson gson = new Gson();
    private static final String GetURL = "https://contactapp-telran-backend.herokuapp.com/v1/contacts";
    private static final String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3" +
            "ViIjoibWF4dGVzdEBnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTcyMTMyMDMzNiwiaWF0IjoxNzIw" +
            "NzIwMzM2fQ.XC76GcIE2cCOtmJV5m13K_kO-PGi6mbYVRnRrXvpDDQ";

    @Test
    public void getAllContactsSuccessTest() throws IOException {
        SoftAssert softAssert = new SoftAssert();


        Response response = Request.Get(GetURL)
                .addHeader("Authorization", token)
                .execute();


        String responseJson = response.returnContent().toString();
        AllContactsDto contactsDto = gson.fromJson(responseJson, AllContactsDto.class);

        softAssert.assertFalse(contactsDto.getContacts().isEmpty());


        List<ContactDto> contacts = contactsDto.getContacts();

        for (ContactDto c : contacts) {
            softAssert.assertFalse(c.getId().isEmpty());
            softAssert.assertNotNull(c.getName());
            softAssert.assertNotNull(c.getAddress());
            softAssert.assertNotNull(c.getLastName());
        }
        softAssert.assertAll();
    }

    @Test
    public void getAllContactsSuccessTestWithWrongToken() throws IOException {
        SoftAssert softAssert = new SoftAssert();
        Response response = Request.Get(GetURL)
                .addHeader("Authorization", "e")
                .execute();

        HttpResponse httpResponse = response.returnResponse();

        softAssert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 401
        );

        InputStream inputStream = httpResponse.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        ErrorDto errorDto = gson.fromJson(sb.toString(), ErrorDto.class);

        softAssert.assertEquals(errorDto.getStatus(),401);
        softAssert.assertEquals(errorDto.getError(),"Unauthorized");
        softAssert.assertNotNull(errorDto.getTimestamp());
        softAssert.assertEquals(errorDto.getMessage(),"JWT strings must contain exactly" +
                " 2 period characters. Found: 0");
        softAssert.assertEquals(errorDto.getPath(),"/v1/contacts");


        softAssert.assertAll();
    }

    @Test
    public void getAllContactsSuccessTestWithWrongHeader() throws IOException {

        Response response = Request.Get(GetURL)
                .addHeader("","")
                .execute();

        HttpResponse httpResponse = response.returnResponse();

        Assert.assertEquals(httpResponse.getStatusLine().getStatusCode(), 403
        );


    }


}
