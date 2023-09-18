package com.myspring.pro29.ex03;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController // 데이터만 전달
@RequestMapping("/boards")
public class BoardController {
	static Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	// 클라이언트 주소 : /pro29/boards/all
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	//ResponseEntity : 데이터 상태도 확인, 헤더 특정 속성도 같이 추가 가능
	public ResponseEntity<List<ArticleVO>> listArticles() {
		// 로그 기록을 조금 더 기능을 잘 구현한 라이브러리를 이용해서 기록
		logger.info("listArticles 호출");
		// 동네 1~4 순회 후 데이터를 직접 전달 (지금은 더미데이터)
		List<ArticleVO> list = new ArrayList<ArticleVO>();
		// ArticleVO 타입으로만 박스에 담아서 전달 해 주세요 (강력요구)
		for (int i = 0; i < 10; i++) {
			ArticleVO vo = new ArticleVO();
			vo.setArticleNO(i);
			vo.setWriter("김민경"+i);
			vo.setTitle("반려동물"+i);
			vo.setContent("고양이"+i);
			list.add(vo);
		}
		// 서버 -> 클라이언트에게 박스(데이터)전달 + 상태 코드도 전달
		return new ResponseEntity(list,HttpStatus.OK);
	}
	
	
	// 클라이언트 주소 : /pro29/boards/{articleNO}
	@RequestMapping(value = "/{articleNO}", method = RequestMethod.GET)
	//@PathVariable("articleNO") Integer articleNO : 클라이언트 주소 요청 시 주소 뒤에 매개변수처럼 전달, 서버에서 해당 매개변수를 이용가능
	public ResponseEntity<ArticleVO> findArticle (@PathVariable("articleNO") Integer articleNO) {
		logger.info("findArticle : 서버에 잘 호출 되었는지 확인");
		ArticleVO vo = new ArticleVO();
		vo.setArticleNO(articleNO);
		vo.setWriter("김민경");
		vo.setTitle("반려동물");
		vo.setContent("고양이");
		// 서버 -> 클라이언트에게 데이터 + 상태
		return new ResponseEntity(vo,HttpStatus.OK);
	}	
	
	// POST : 추가, PUT : 업데이트, GET : 조회, PATCH : 부분 수정
	// DELETE : 삭제
	// 추가 테스트
	// 클라이언트 주소 : /pro29/boards/, POST(:전달방식)
	// 1) 웹 js
	// 2) Postman 도구로 확인
	@RequestMapping(value = "", method = RequestMethod.POST)
	//ResponseEntity : 데이터 + 상태
	//@RequestBody : 클라이언트 전달 된 데이터를 서버에서 자동으로 모델 클래스 매핑
	public ResponseEntity<String> addArticle (@RequestBody ArticleVO articleVO) {
		ResponseEntity<String>  resEntity = null;
		try {
			logger.info("addArticle 호출");
			logger.info(articleVO.toString());
			// 서버 -> 클라이언트 
			resEntity =new ResponseEntity("ADD_SUCCEEDED",HttpStatus.OK);
		}catch(Exception e) {
			resEntity = new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		
		return resEntity;
	}	
	
	//수정확인
	//@PathVariable("articleNO") Integer articleNO : 주소의 매개변수를 서버에서 가져와
	
	// 클라이언트 주소 : /pro29/boards/0918, PUT(:전달방식)
	@RequestMapping(value = "/{articleNO}", method = RequestMethod.PUT)
	public ResponseEntity<String> modArticle (@PathVariable("articleNO") Integer articleNO, @RequestBody ArticleVO articleVO) {
		ResponseEntity<String>  resEntity = null;
		try {
			logger.info("modArticle 확인");
			logger.info(articleVO.toString());
			resEntity =new ResponseEntity("MOD_SUCCEEDED",HttpStatus.OK);
		}catch(Exception e) {
			resEntity = new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		
		return resEntity;
	}
	
	//삭제하기
	// 클라이언트 -> 서버에게 데이터 전달
	// 수정 삭제할 때 게시물 넘버로 알려줌
	// 클라이언트 주소 : /pro29/boards/0918, DELETE(:전달방식)
	// 1) 웹 js
	// 2) Postman 도구로 확인
	@RequestMapping(value = "/{articleNO}", method = RequestMethod.DELETE)
	public ResponseEntity<String> removeArticle (@PathVariable("articleNO") Integer articleNO) {
		ResponseEntity<String>  resEntity = null;
		try {
			logger.info("removeArticle 호출");
			System.out.println("반드시 클라이언트로부터 넘어온 데이터를 확인하는 습관!!");
			logger.info(articleNO.toString());
			resEntity =new ResponseEntity("REMOVE_SUCCEEDED",HttpStatus.OK);
		}catch(Exception e) {
			resEntity = new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		
		return resEntity;
	}	

}
