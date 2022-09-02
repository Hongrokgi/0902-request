package com.training.Http;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/parameter")
    public void GetParameterCheck(@RequestParam("key") String key) {
        log.info("********Postman connect********");
        System.out.println("key = " + key);
    }
    /*
    *   json 형태 username: "",
    *            password: ""
    * */
    @PostMapping("/parameter")
    public void GetPostCheck(@RequestBody User user) {
        log.info("********Postman connect********");
        System.out.println("user = " + user);
    }

    /*
    *   request 로 getParameter 로는 안되네
    * */
    @PostMapping("/parameter2")
    public void NoUsingBodyCheck(HttpServletRequest request) {
        log.info("********Postman connect********");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("username = " + username);
        System.out.println("password = " + password);
    }
    /*
    *   @RequestParam 으로 접근하나 request.getParameter 나 동일함
    * */
    @GetMapping("/parameter3")
    public void requestCheck(HttpServletRequest request) {
        log.info("********Postman connect********");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("username = " + username);
        System.out.println("password = " + password);
    }
    /*
    *   ParameterBody = {
        "username":"안중근",
        "password":"1234"
        }
    * */
    @PostMapping("/parameter4")
    public void checkJsonValue(@RequestBody String ParameterBody) {
        log.info("********Postman connect********");
        System.out.println("ParameterBody = " + ParameterBody);
    }
    /*
    *   위의 예제 즉, 문자열을 json 라이브러리를 통해 JsonObject 변환하고 key 를 통해 value 에 접근
    * */
    @PostMapping("/parameter5")
    public void checkJsonValue2(@RequestBody String ParameterBody) {
        log.info("********Postman connect********");
        JSONObject object = new JSONObject(ParameterBody);
        System.out.println("object = " + object);
        System.out.println("object.get(\"password\") = " + object.get("password"));
        System.out.println("object.get(\"username\") = " + object.get("username"));
    }
    /*
    *      Information = {
                "day":"20220902",
                "user":[
                    {
                        "name":"Hongrokgi",
                        "job":"doctor",
                        "age":"3"
                    },
                    {
                        "name":"Hyeri",
                        "job":"nurse",
                        "age":"3"
                    }
                ]
        }
    *
    *   Json 객체 안에 Json
    *   length -> 배열의 길이를 알고자할때
    *   length() -> 문자열의 길이
    *   size() -> 컬렉션의 타입의 길이
    * */
    @PostMapping("/parameter6")
    public void SeeTheJsonParser(@RequestBody String Information) {
        log.info("postman Connect");
        JSONObject object = new JSONObject(Information);
        JSONArray array = (JSONArray) object.get("user");
        if(array.length() > 0) {
            for(int i=0; i<array.length(); i++) {
                JSONObject object1 = (JSONObject) array.get(i);
                System.out.println("object1 = " + object1);
                System.out.println("object1.get(\"name\") = " + object1.get("name"));
                System.out.println("object1.get(\"job\") = " + object1.get("job"));
                System.out.println("object1.get(\"age\") = " + object1.get("age"));
            }
        }
        System.out.println("object.get(\"day\") = " + object.get("day"));
//        JSONObject jsonObject = (JSONObject) array; -> Inconvertible types; cannot cast 'org.json.JSONArray' to 'org.json.JSONObject'
    }

    @PostMapping("/request")
    public ResponseEntity<User> CheckRequestInfo(HttpServletRequest request, @RequestBody String Information) {
        System.out.println("request.getRequestURI() = " + request.getRequestURI());
        System.out.println("request.getMethod() = " + request.getMethod());
        System.out.println("request.getHeader() = " + request.getHeader("Content-Type"));
        System.out.println("request.getHeader() = " + request.getHeader("Content-Length"));
        System.out.println("request.getHeader() = " + request.getHeader("Host"));
        System.out.println("request.getHeader() = " + request.getHeader("Accept"));
        System.out.println("request.getHeader() = " + request.getContentType());                        // header 에서 굳이 name 값을 지정하지 않아도 get 으로 해당 변수 바로 취득 가능
        System.out.println("request.getQueryString() = " + request.getQueryString());
        System.out.println("request.getLocale() = " + request.getLocale());
        System.out.println("request.getAuthType() = " + request.getAuthType());
        System.out.println("Information = " + Information);
        User user = new User("방정환","1324");
        ResponseEntity<User> req = ResponseEntity
                .status(HttpStatus.OK)
                .body(user);                            // Response 에 Body 와 Status 를 담아 응답이 가능하니, 예외처리를 하는 클래스를 만들어서 작성하면 훨씬 깔끔할거같음 .
        return req;
    }

    @PostMapping("/request1")
    public ResponseEntity<Response> testResponse() {
        Response response = new Response(200,"통과~");
        ResponseEntity<Response> req= ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
        return req;
    }
    /*
    *  String -> JsonObject -> JsonArray -> JsonObject -> Dto
    *
    * */
    @PostMapping("/parameter7")
    public void SeeTheJsonParser2(@RequestBody String Information) {
        log.info("postman Connect");
        JSONObject object = new JSONObject(Information);
        JSONArray array = (JSONArray) object.get("user");
        JSONObject o = (JSONObject)array.get(1);
        UserDto user  = new UserDto((String) o.get("name"),(String)o.get("job"),(String) o.get("age"));
        if(user.getName().equals("Hyeri")) {
            log.info("에이 아닐꺼야..");
            if(user.getJob().equals("nurse")) {
                log.info("어라?");
                if(user.getAge().equals("31")) {
                    log.info("잘 지내니?");
                }else {
                    log.info("아니구나..ㅎ");
                }
            }else {
                log.info("아니구나..ㅎ");
            }
        }else {
            log.info("아니구나..ㅎ");
        }

    }

//    @GetMapping("/write")
//    public void doSomething() {
//        for(int i = 1; i<10; i++) {
//            ;
//        }
//    }




}
