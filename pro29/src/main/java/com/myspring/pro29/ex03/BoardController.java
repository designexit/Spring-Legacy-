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
	
	// 클라이언트 주소 : /pro/boards/all
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
	
	@RequestMapping(value = "/{articleNO}", method = RequestMethod.GET)
	public ResponseEntity<ArticleVO> findArticle (@PathVariable("articleNO") Integer articleNO) {
		logger.info("findArticle �޼��� ȣ��");
		ArticleVO vo = new ArticleVO();
		vo.setArticleNO(articleNO);
		vo.setWriter("ȫ�浿");
		vo.setTitle("�ȳ��ϼ���");
		vo.setContent("ȫ�浿 ���Դϴ�");
		return new ResponseEntity(vo,HttpStatus.OK);
	}	
	
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<String> addArticle (@RequestBody ArticleVO articleVO) {
		ResponseEntity<String>  resEntity = null;
		try {
			logger.info("addArticle �޼��� ȣ��");
			logger.info(articleVO.toString());
			resEntity =new ResponseEntity("ADD_SUCCEEDED",HttpStatus.OK);
		}catch(Exception e) {
			resEntity = new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		
		return resEntity;
	}	
	
	//�����ϱ�
	@RequestMapping(value = "/{articleNO}", method = RequestMethod.PUT)
	public ResponseEntity<String> modArticle (@PathVariable("articleNO") Integer articleNO, @RequestBody ArticleVO articleVO) {
		ResponseEntity<String>  resEntity = null;
		try {
			logger.info("modArticle �޼��� ȣ��");
			logger.info(articleVO.toString());
			resEntity =new ResponseEntity("MOD_SUCCEEDED",HttpStatus.OK);
		}catch(Exception e) {
			resEntity = new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		
		return resEntity;
	}
	
	//�����ϱ�
	@RequestMapping(value = "/{articleNO}", method = RequestMethod.DELETE)
	public ResponseEntity<String> removeArticle (@PathVariable("articleNO") Integer articleNO) {
		ResponseEntity<String>  resEntity = null;
		try {
			logger.info("removeArticle �޼��� ȣ��");
			logger.info(articleNO.toString());
			resEntity =new ResponseEntity("REMOVE_SUCCEEDED",HttpStatus.OK);
		}catch(Exception e) {
			resEntity = new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		
		return resEntity;
	}	

}
