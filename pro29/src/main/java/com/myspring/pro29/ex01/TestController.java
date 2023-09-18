package com.myspring.pro29.ex01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController 
// 서버에서 클라이언트에게 JSON의 문자열 형태로 전달 : 데이터만 전달하겠다.
// 하위에 있는 모든 메서드는 JSON의 문자열 형태로 전달 : 데이터만 전달하겠다.
//@Controller 뷰+데이터
@RequestMapping("/test/*")
public class TestController {
  static Logger logger = LoggerFactory.getLogger(TestController.class);
	
  @RequestMapping("/hello")
  public String hello() {
	return "Hello REST!!";
  } 
  
  // 서버 -> 클라이언트 : MemberVO 타입 인스턴스를 JSON의 문자열로 응답할 예정
  @RequestMapping("/member")
  public MemberVO member() {
    MemberVO vo = new MemberVO();
    vo.setId("hong");
    vo.setPwd("1234");
    vo.setName("홍길동");
    vo.setEmail("hong@test.com");
    return vo;
  } 	
  
  // 복수개로 전달함
  // 앞, 뒷단에 전달 형식이 JSON타입으로 데이터를 많이 주고 받음
  // 서버 -> 크라이언트 : 데이터 전달, 복수, 요소로는 MemberVO타입으로 전달
  @RequestMapping("/membersList")
  public List<MemberVO> listMembers () {
    List<MemberVO> list = new ArrayList<MemberVO>();
	for (int i = 0; i < 10; i++) {
	  MemberVO vo = new MemberVO();
	  vo.setId("hong"+i);
	  vo.setPwd("123"+i);
	  vo.setName("홍길동"+i);
	  vo.setEmail("hong"+i+"@test.com");
	  list.add(vo);
	}
    return list; 
  }	
  
  // 반환타입 : 맵타입
  @RequestMapping("/membersMap")
  public Map<Integer, MemberVO> membersMap() {
	  // 임시 담을 컬랙션 맵 구성
    Map<Integer, MemberVO> map = new HashMap<Integer, MemberVO>();
    for (int i = 0; i < 10; i++) {
      MemberVO vo = new MemberVO();
      vo.setId("hong" + i);
      vo.setPwd("123"+i);
      vo.setName("홍길동" + i);
      vo.setEmail("hong"+i+"@test.com");
      map.put(i, vo); 
    }
    // 서버 -> 데이터만 전달하는 데 전달 박스 타입이 맵 형태
    return map; 
  } 	
  
  
  // @PathVariable : 서버의 URL주소에서 특정의 매개변수를 서버로 가져올 때 사용
  // 게시판에서 게시물 넘버 사용시 많이 활용
  @RequestMapping(value= "/notice/{num}" , method = RequestMethod.GET)
  public int notice(@PathVariable("num") int num ) throws Exception {
	  // 서버 -> 클라이언트로 데이터만 전달, 클라이언트에게서 받은 매개변수를 재전달
	  return num;
  }	

  // @RequestBody : 클라이언트 -> 서버에게 데이터만 전달하는 중
  // 전달하는 타입을 MemberVO 인스턴스를 잭슨 기능을 통해서 json 문자열로 전달
  // @ Controller와 혼합 사용 가능
  
  // 접근 주소 : pro29/test/info
  @RequestMapping(value= "/info", method = RequestMethod.POST)
  public void modify(@RequestBody MemberVO vo ){
    logger.info(vo.toString());
    logger.info(vo.getEmail());
  }
  
  // 반환타입형식에서 ResponseEntity 타입을 보는데, 데이터만 주고 받으면 전달이 잘 되었는지 확인이 어려움
  // 그래서 http status code 전달 받았다 200
  
  // 데이터 + 상태 같이 전달
  // 클라이언트 주소 : /pro29/test/membersList2
  @RequestMapping("/membersList2")
  public  ResponseEntity<List<MemberVO>> listMembers2() {
	List<MemberVO> list = new ArrayList<MemberVO>();
	for (int i = 0; i < 10; i++) {
	  MemberVO vo = new MemberVO();
	  vo.setId("lee" + i);
	  vo.setPwd("123"+i);
	  vo.setName("홍길동" + i);
      vo.setEmail("lee"+i+"@test.com");
	  list.add(vo);
	}
	// list : 데이터 전달
	// HttpStatus.INTERNAL_SERVER_ERROR : 상태값도 전달
//    return new ResponseEntity(list,HttpStatus.INTERNAL_SERVER_ERROR); // 500에러 테스트
    return new ResponseEntity(list,HttpStatus.OK);
  }	
  
  
  //ResponseEntity -> 데이터 + 상태 + 
	@RequestMapping(value = "/res3")
	public ResponseEntity res3() {
		HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.add("Content-Type", "text/html; charset=utf-8");
	    String message = "<script>";
		message += " alert('res3 테스트');";
		message += " location.href='/pro29/test/membersList2'; ";
		message += " </script>";
		return  new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	}
	
}
