/*
 * created by max$
 */


package com.phonebook.httpclient;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.phobebook.dto.AuthRequestDto;
import com.phobebook.dto.AuthResponseDto;
import com.phobebook.dto.ErrorDto;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoginHttpclientTests {
    @Test
    public void loginSuccessTest() throws IOException {

        Response response = Request.Post("https://contactapp-telran-backend.herokuapp." +
                        "com/v1/user/login/usernamepassword")
                .bodyString("{\n" +
                        "  \"username\": \"maxtest@gmail.com\",\n" +
                        "  \"password\": \"Maxtest123!\"\n" +
                        "}", ContentType.APPLICATION_JSON)
                .execute();

        String responseJson = response.returnContent().asString();
        // System.out.println(responseJson);

        JsonElement element = JsonParser.parseString(responseJson);
        JsonElement token = element.getAsJsonObject().get("token");
        System.out.println(token.getAsString());

    }

    @Test
    public void loginSuccessTestWithDto() throws IOException {

        AuthRequestDto requestDto = AuthRequestDto.builder()
                .username("maxtest@gmail.com")
                .password("Maxtest123!")
                .build();
        Gson gson = new Gson();
        Response response = Request.Post("https://contactapp-telran-backend.herokuapp." +
                        "com/v1/user/login/usernamepassword")
                .bodyString(gson.toJson(requestDto), ContentType.APPLICATION_JSON)
                .execute();


        String responseJson = response.returnContent().toString();
        AuthResponseDto dto = gson.fromJson(responseJson, AuthResponseDto.class);
        System.out.println(dto.getToken());


    }

    @Test
    public void loginErrorTestWithDto() throws IOException {
        AuthRequestDto requestDto = AuthRequestDto.builder()
                .username("maxtestgmail.com")
                .password("Maxtest123!")
                .build();

        Gson gson = new Gson();
        Response response = Request.Post("https://contactapp-telran-backend.herokuapp." +
                        "com/v1/user/login/usernamepassword")
                .bodyString(gson.toJson(requestDto), ContentType.APPLICATION_JSON)
                .execute();

        HttpResponse httpResponse = response.returnResponse();
        System.out.println(httpResponse.getStatusLine().getStatusCode());

        InputStream inputStream = httpResponse.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        ErrorDto errorDto = gson.fromJson(sb.toString(), ErrorDto.class);
        System.out.println(errorDto.getTimestamp() + "***" + errorDto.getStatus() + "***"
                + errorDto.getError() + "***" + errorDto.getMessage() + "***" + errorDto.getPath());
        Assert.assertEquals(errorDto.getStatus(),401);
    }
}
