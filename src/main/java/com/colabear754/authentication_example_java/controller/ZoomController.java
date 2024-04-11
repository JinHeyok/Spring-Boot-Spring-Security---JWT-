package com.colabear754.authentication_example_java.controller;

import com.colabear754.authentication_example_java.DTO.ApiResponse;
import com.colabear754.authentication_example_java.service.ZoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "ZOOM API 테스트")
@RequestMapping("/zoom")
@RestController
@RequiredArgsConstructor
public class ZoomController {


    private final ZoomService zoomService;

    
    // NOTE zoom 처음 code발금 url endpoint에 URL을 해당 API로 지정한다.
    
    @Operation(summary = "ZOOM 미팅생성 API 구현" ,
        description = """
                ### Zoom Meeting Create API 구현 
                ### 작성자 : 방진혁
                
                --- 
                
                #### RequestBody
                
                \n
                    {
                      "agenda": "미팅테스트", // 회의 안건 이름\s
                      "default_password": false, // 사용자 설정을 사용하여 기본 비밀번호를 생성 할지 여부
                      "password": "qwer*1234", // 회의 참여 비밀번호 설정 최대 10자까지
                      "pre_schedule": false, // 사전 일정 상태 false
                      "schedule_for": "kpjh044@gmail.com", // 회의를 예약할 사용자의 이메일 주소 또는 사 용자 ID
                      "settings": { // 회의 설정에 대한 정보
                        "additional_data_center_regions": [ // 추가 회의 데이터 센터 지역 국가코드로 지정
                          "KR"
                        ],
                        "allow_multiple_devices": true, // 참석자가 여러 장치에서 미팅을 참여하도록 허용할지 여부\s
                        "approval_type": 2, // 대체 호스트의 수
                        "approved_or_denied_countries_or_regions": { // 승인 또는 거부 국가 지역
                          "approved_list": [ // 승인 목록\s
                            "KR"
                          ],
                          "denied_list": [ // 제한 목록
                            "US"
                          ],
                          "enable": true, // 특정 국가/지역의 사용자에 대한 항목 승인 또는 차단 설정\s
                          "method": "approve" // 특정 국가 또는 지역의 사용자가 회의에 참여 할 수 있도록 허용한다. , 'deny' 특정 국가 또는 지역의 사용자가 회으에 참여하지 못하도록 차단한다.\s
                        },
                        "audio": "telephony", // 미팅의 오디오 부분에 참여하는 방법\s
                        // both : 전화 통신과 VoIP 모두
                        // telephony : 전화로만 가능
                        // voip : VoIP 전용
                        // thirdParty : 제3자 오디오 컨퍼런스.
                        "audio_conference_info": "test", // 오디오 회의 정보 (Description 같음)
                        "authentication_domains": "http://localhost:8089", // 회의의 인증된 도메인. 이메일 주소에 인증도니 도메인이 포함된 Zoom 사용자만 회의에 참여할 수 있다.
                        // "authentication_exception": [ // 인증 예외 처리 미팅 인증을 우회할 수 있는 참가자 목록\s
                        //   {
                        //     "email": "jchill@example.com",
                        //     "name": "Jill Chill"
                        //   }
                        // ],
                        "auto_recording": "cloud", // 자동 녹음 설정\s
                        // local : 회의를 로컬에서 녹화한다.
                        // cloud : 회의를 클라우드에 녹화한다.
                        // nonoe : 자동 녹음이 비활성화 되었다.
                        "email_notification": true,
                        "encryption_type": "enhanced_encryption", // 암호화가 클라우드에 저장된다.
                        "focus_mode": true, // 집중 모드 활성화
                        "host_video": true, // 호스트 비디오를 켠 상태에서 미팅을 시작할 지 여부\s
                        "jbh_time": 0, // 참가자가 미팅 호스트보다 먼저 미팅에 참여할수 있는 시간 제한\s
                        // 0 : 참가자가 언제든지 회의에 참여할 수 있도록 허용
                        // 5 : 참가자가 회의 시작 시간 5분 전에 참여할수 있도록 허용
                        // 10 : 참가자가 회의 시작 시간 10분 전에 참여할 수 있도록 허용
                        "join_before_host": false, // 참가자가 호스트보다 먼저 미팅에 참여할 수 있는지 여부\s
                        "language_interpretation": { // 언어 통역\s
                          "enable": true, // 회의에 대한 언어통역 활성화 여부\s
                          "interpreters": [ // 통역사\s
                            {
                              "email": "interpreter@example.com", // 통역사 이메일\s
                              "languages": "KR,US" // 한국어를 영어로
                            }
                          ]
                        },
                        "meeting_authentication": false, // 회의 인증도니 사용자만 false 처리
                        "meeting_invitees": [ // 회의 초대받은 사람 리스트 \s
                          {
                            "email": "jchill@example.com" // 초다받는 사람의 이메일 주소\s
                          }
                        ],
                        "mute_upon_entry": false, // 입장시 음소거\s
                        "participant_video": false, // 참가자 비디오를 켠 상태에서 미팅을 시작할지 여부
                        "private_meeting": true, // 회의를 비공개 설정할지 여부
                        "registration_type": 1, // 회의 등록 여부
                        // 1 : 참석자는 한 번만 등록하면 모든 회의에 참여 가능
                        // 2 : 참석자는 각 회의에 대해 등록을 해야함
                        // 3 : 참석자는 한 번 등록하고 참석할 회의를 하나 이상 선택 불가능
                        "host_save_video_order": true, // 호스트가 비디오를 저장하도록 허용 기능 활성화 여부\s
                        "internal_meeting": false, // 내부 회의 여부\s
                        "continuous_meeting_chat": { // 지속적인 미팅 채팅 활성화 기능\s
                          "enable": true, // 활성화 여부
                          "auto_add_invited_external_users": true // 초대된 외부 사용자를 자동으로 추가 설정 할지 여부
                        },
                        "participant_focused_meeting": false // 참가자 중심 회의로 설정할지 여부
                      },
                      "start_time": "2022-03-25T07:32:55Z", // 회의 시작 시간\s
                      "template_id": "Dv4YdINdTk+Z5RToadh5ug==", // 회의 템블릿을 사용하여 회의를 예약하는데 사용하는 계정 관리자 회의 템플릿 ID
                      "timezone": "Asia/Seoul", // 시간대 목록 아시아/서울로 고정
                      "topic": "My Meeting", // 회의에 주제\s
                      "type": 2 // 회의 유형\s
                      // 1 : 즉석 회의\s
                      // 2 : 예정된 회의
                      // 3 : 정해진 시간 없이 반복되는 미팅
                      // * : 정해진 시간에 반복되는 미팅
                    }
                \n
                
                ---
                
                """
    )
    @GetMapping(value="/code")
    public ApiResponse meetingRoomRegistration(HttpServletRequest req, @RequestParam String code) throws IOException {
        System.out.println("code: "+ code);
        return ApiResponse.success(zoomService.meetingRoomRegistration(code));
    }


}
