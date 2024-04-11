package com.colabear754.authentication_example_java.service;


import com.colabear754.authentication_example_java.DTO.ApiResponse;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@RequiredArgsConstructor
@Service
public class ZoomService {

    /**
     * 테세트 RequestBody 요청문
     * {
     *   "agenda": "미팅테스트", // 회의 안건 이름
     *   "default_password": false, // 사용자 설정을 사용하여 기본 비밀번호를 생성 할지 여부
     *   "password": "qwer*1234", // 회의 참여 비밀번호 설정 최대 10자까지
     *   "pre_schedule": false, // 사전 일정 상태 false
     *   "schedule_for": "kpjh044@gmail.com", // 회의를 예약할 사용자의 이메일 주소 또는 사 용자 ID
     *   "settings": { // 회의 설정에 대한 정보
     *     "additional_data_center_regions": [ // 추가 회의 데이터 센터 지역 국가코드로 지정
     *       "KR"
     *     ],
     *     "allow_multiple_devices": true, // 참석자가 여러 장치에서 미팅을 참여하도록 허용할지 여부
     *     "approval_type": 2, // 대체 호스트의 수
     *     "approved_or_denied_countries_or_regions": { // 승인 또는 거부 국가 지역
     *       "approved_list": [ // 승인 목록
     *         "KR"
     *       ],
     *       "denied_list": [ // 제한 목록
     *         "US"
     *       ],
     *       "enable": true, // 특정 국가/지역의 사용자에 대한 항목 승인 또는 차단 설정
     *       "method": "approve" // 특정 국가 또는 지역의 사용자가 회의에 참여 할 수 있도록 허용한다. , 'deny' 특정 국가 또는 지역의 사용자가 회으에 참여하지 못하도록 차단한다.
     *     },
     *     "audio": "telephony", // 미팅의 오디오 부분에 참여하는 방법
     *     // both : 전화 통신과 VoIP 모두
     *     // telephony : 전화로만 가능
     *     // voip : VoIP 전용
     *     // thirdParty : 제3자 오디오 컨퍼런스.
     *     "audio_conference_info": "test", // 오디오 회의 정보 (Description 같음)
     *     "authentication_domains": "http://localhost:8089", // 회의의 인증된 도메인. 이메일 주소에 인증도니 도메인이 포함된 Zoom 사용자만 회의에 참여할 수 있다.
     *     // "authentication_exception": [ // 인증 예외 처리 미팅 인증을 우회할 수 있는 참가자 목록
     *     //   {
     *     //     "email": "jchill@example.com",
     *     //     "name": "Jill Chill"
     *     //   }
     *     // ],
     *     "auto_recording": "cloud", // 자동 녹음 설정
     *     // local : 회의를 로컬에서 녹화한다.
     *     // cloud : 회의를 클라우드에 녹화한다.
     *     // nonoe : 자동 녹음이 비활성화 되었다.
     *     "email_notification": true,
     *     "encryption_type": "enhanced_encryption", // 암호화가 클라우드에 저장된다.
     *     "focus_mode": true, // 집중 모드 활성화
     *     "host_video": true, // 호스트 비디오를 켠 상태에서 미팅을 시작할 지 여부
     *     "jbh_time": 0, // 참가자가 미팅 호스트보다 먼저 미팅에 참여할수 있는 시간 제한
     *     // 0 : 참가자가 언제든지 회의에 참여할 수 있도록 허용
     *     // 5 : 참가자가 회의 시작 시간 5분 전에 참여할수 있도록 허용
     *     // 10 : 참가자가 회의 시작 시간 10분 전에 참여할 수 있도록 허용
     *     "join_before_host": false, // 참가자가 호스트보다 먼저 미팅에 참여할 수 있는지 여부
     *     "language_interpretation": { // 언어 통역
     *       "enable": true, // 회의에 대한 언어통역 활성화 여부
     *       "interpreters": [ // 통역사
     *         {
     *           "email": "interpreter@example.com", // 통역사 이메일
     *           "languages": "KR,US" // 한국어를 영어로
     *         }
     *       ]
     *     },
     *     "meeting_authentication": false, // 회의 인증도니 사용자만 false 처리
     *     "meeting_invitees": [ // 회의 초대받은 사람 리스트
     *       {
     *         "email": "jchill@example.com" // 초다받는 사람의 이메일 주소
     *       }
     *     ],
     *     "mute_upon_entry": false, // 입장시 음소거
     *     "participant_video": false, // 참가자 비디오를 켠 상태에서 미팅을 시작할지 여부
     *     "private_meeting": true, // 회의를 비공개 설정할지 여부
     *     "registration_type": 1, // 회의 등록 여부
     *     // 1 : 참석자는 한 번만 등록하면 모든 회의에 참여 가능
     *     // 2 : 참석자는 각 회의에 대해 등록을 해야함
     *     // 3 : 참석자는 한 번 등록하고 참석할 회의를 하나 이상 선택 불가능
     *     "host_save_video_order": true, // 호스트가 비디오를 저장하도록 허용 기능 활성화 여부
     *     "internal_meeting": false, // 내부 회의 여부
     *     "continuous_meeting_chat": { // 지속적인 미팅 채팅 활성화 기능
     *       "enable": true, // 활성화 여부
     *       "auto_add_invited_external_users": true // 초대된 외부 사용자를 자동으로 추가 설정 할지 여부
     *     },
     *     "participant_focused_meeting": false // 참가자 중심 회의로 설정할지 여부
     *   },
     *   "start_time": "2022-03-25T07:32:55Z", // 회의 시작 시간
     *   "template_id": "Dv4YdINdTk+Z5RToadh5ug==", // 회의 템블릿을 사용하여 회의를 예약하는데 사용하는 계정 관리자 회의 템플릿 ID
     *   "timezone": "Asia/Seoul", // 시간대 목록 아시아/서울로 고정
     *   "topic": "My Meeting", // 회의에 주제
     *   "type": 2 // 회의 유형
     *   // 1 : 즉석 회의
     *   // 2 : 예정된 회의
     *   // 3 : 정해진 시간 없이 반복되는 미팅
     *   // * : 정해진 시간에 반복되는 미팅
     * }
     *
     * @param code zoom endpoint url에 생성된 code를 받음
     * @throws IOException
     */
    public ApiResponse meetingRoomRegistration(String code) throws IOException {
        String token = getToken(code); // MEMO Token 을 발급받는다.
        String url = "https://api.zoom.us/v2/users/kpjh044@gmail.com/meetings"; // NOTE 중간에 호스트 이메일 추가

        OkHttpClient client = new OkHttpClient();

        // 주어진 데이터를 JSON 문자열로 변환
        String json = "{\n" +
                "  \"agenda\": \"미팅테스트\",\n" +
                "  \"default_password\": false,\n" +
                "  \"password\": \"qwer*1234\",\n" +
                "  \"pre_schedule\": false,\n" +
                "  \"schedule_for\": \"kpjh044@gmail.com\",\n" +
                "  \"settings\": {\n" +
                "    \"additional_data_center_regions\": [\n" +
                "      \"KR\"\n" +
                "    ],\n" +
                "    \"allow_multiple_devices\": true,\n" +
                "    \"approval_type\": 2,\n" +
                "    \"approved_or_denied_countries_or_regions\": {\n" +
                "      \"approved_list\": [\n" +
                "        \"KR\"\n" +
                "      ],\n" +
                "      \"denied_list\": [\n" +
                "        \"US\"\n" +
                "      ],\n" +
                "      \"enable\": true,\n" +
                "      \"method\": \"approve\"\n" +
                "    },\n" +
                "    \"audio\": \"telephony\",\n" +
                "    \"audio_conference_info\": \"test\",\n" +
                "    \"authentication_domains\": \"http://localhost:8089\",\n" +
                "    \"auto_recording\": \"cloud\",\n" +
                "    \"email_notification\": true,\n" +
                "    \"encryption_type\": \"enhanced_encryption\",\n" +
                "    \"focus_mode\": true,\n" +
                "    \"host_video\": true,\n" +
                "    \"jbh_time\": 0,\n" +
                "    \"join_before_host\": false,\n" +
                "    \"language_interpretation\": {\n" +
                "      \"enable\": true,\n" +
                "      \"interpreters\": [\n" +
                "        {\n" +
                "          \"email\": \"interpreter@example.com\",\n" +
                "          \"languages\": \"KR,US\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"meeting_authentication\": false,\n" +
                "    \"mute_upon_entry\": false,\n" +
                "    \"participant_video\": false,\n" +
                "    \"private_meeting\": true,\n" +
                "    \"registration_type\": 1,\n" +
                "    \"host_save_video_order\": true,\n" +
                "    \"internal_meeting\": false,\n" +
                "    \"continuous_meeting_chat\": {\n" +
                "      \"enable\": true,\n" +
                "      \"auto_add_invited_external_users\": true\n" +
                "    },\n" +
                "    \"participant_focused_meeting\": false\n" +
                "  },\n" +
                "  \"start_time\": \"2022-03-25T07:32:55Z\",\n" +
                "  \"template_id\": \"Dv4YdINdTk+Z5RToadh5ug==\",\n" +
                "  \"timezone\": \"Asia/Seoul\",\n" +
                "  \"topic\": \"My Meeting\",\n" +
                "  \"type\": 2\n" +
                "}";

        // JSON 문자열을 FormBody에 추가
        RequestBody formBody = RequestBody.create(MediaType.parse("application/json"), json);

        Request zoomRequest = new Request.Builder()
                .url(url) // NOTE  호출 url
                .addHeader("content-type", "application/json") // NOTE 공식 문서에 명시 된 type
                .addHeader("Authorization", "Bearer " + token) // NOTE Client_ID:Client_Secret 을  Base64-encoded 한 값
                .post(formBody)
                .build();

        System.out.println(token);

        Response meetingResponse = client.newCall(zoomRequest).execute();
        System.out.println(meetingResponse.body().string());
        if(meetingResponse.isSuccessful()){
            JSONObject response = new JSONObject(meetingResponse.body().string());
            return ApiResponse.success(response);
        }
        return ApiResponse.error("error");
    }


    private String getToken(String code) throws IOException {

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
                .addHeader("Authorization", "Basic " + authorizationUser) // NOTE Client_ID:Client_Secret 을  Base64-encoded 한 값
                .post(formBody)
                .build();

        Response zoomResponse = client.newCall(zoomRequest).execute();
        if(zoomResponse.isSuccessful()){
            String zoomText = zoomResponse.body().string();
            JSONObject jsonObject = new JSONObject(zoomText);
            String token = jsonObject.get("access_token").toString();
            return token;
        } else {
            throw new IOException();
        }
    }

}
