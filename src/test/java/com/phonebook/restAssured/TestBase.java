/*
 * created by max$
 */


package com.phonebook.restAssured;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeMethod;

public class TestBase {
    public static final String TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibWF4dGVzdEBnbWFpbC" +
            "5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTcyMTM5OTkzMiwiaWF0IjoxNzIwNzk5OTMyfQ.eHGVbAsfQ2YRex" +
            "_SdxuDfjoaA2FxCTsDJndUiNXko9w";

    public static final String AUTH="Authorization";
    @BeforeMethod
    public void init(){
        RestAssured.baseURI="https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath="v1";
    }
}
