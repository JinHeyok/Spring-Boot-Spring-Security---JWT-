package com.colabear754.authentication_example_java.service;


import lombok.RequiredArgsConstructor;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@RequiredArgsConstructor
@Service
public class ZoomService {


    public void zoomCreate(String code) throws IOException {

        // MEMO 처음에 Build App을 생성 할 때 사용할 SDK(API)를 설정해야 하며 범위에서 해당 계정에게 어떤 권하을 줄지 선택해서 넣어주어야 한다.
        //      테스트용 App "General app 191"
        //
        // NOTE 처음에는 URL을 GET으로 타고 들어가야함 EndPooint
        //     https://zoom.us/oauth/authorize?response_type=code&client_id=vwlZRJqSQ7qwyjFSnE0PA&redirect_uri=http://localhost:8089/zoom/code
        //      Client_id = Build App 생성시 Client_Id를 발급받음
        //      response_type = code 고정
        //      redirect_uri = 해당 값을 Spring Boot API로 적은 후 RequestParam으로 코드 값을 가져와서 해당 코드로 Access Token을 가져온다.
        String zoomUrl = "https://zoom.us/oauth/token"; // NOTE 토큰 값을 얻는다.
        //통신을 위한 okhttp 사용 maven 추가 필요a
        OkHttpClient client = new OkHttpClient();
        // NOTE Base63 encodingd으로 ClientId:SecretKey 값을 넣어준다.
        String authorizationUser = Base64.getEncoder().encodeToString("vwlZRJqSQ7qwyjFSnE0PA:e4382ZYN0kxw0RebXmQpW2rFfvOrDzTM".getBytes());

        FormBody formBody = new FormBody.Builder()
                .add("code", code) // NOTE GET 요청으로 받은 code 값
                .add("redirect_uri", "http://localhost:8089/zoom/code") // NOTE Build App에 등록 된 URL을 적는다.
                .add("grant_type", "authorization_code") // NOTE 문서에 명시 된 grant_type 고정
                .build();
        Request zoomRequest = new Request.Builder()
                .url(zoomUrl) // 호출 url
                .addHeader("Content-Type", "application/x-www-form-urlencoded") // NOTE 공식 문서에 명시 된 type
                .addHeader("Authorization","Basic " + authorizationUser) // NOTE Client_ID:Client_Secret 을  Base64-encoded 한 값
                .post(formBody)
                .build();


        Response zoomResponse = client.newCall(zoomRequest).execute();
        String zoomText = zoomResponse.body().string();

        JSONObject jsonObject = new JSONObject(zoomText);


    }



    private void apiSendProxy(){

    }

}
