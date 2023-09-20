package com.myspring.pro30.board.service;

import java.util.List;
import java.util.Map;

import com.myspring.pro30.board.vo.ArticleVO;

public interface BoardService {
	public List<ArticleVO> listArticles() throws Exception;
	// 기존, 단일 이미지 글쓰기와 같은 메서드, 
	// 다중 이미지 글쓰기 재사용.
	public int addNewArticle(Map articleMap) throws Exception;
	
	// 이미지만 업로드 
	public void addOnlyImage(Map articleMap, int articleVO) throws Exception;
	
	// 단일 이미지 상세 뷰 보기
//	public ArticleVO viewArticle(int articleNO) throws Exception;
	// 다중 이미지 상세 뷰보기
	public Map viewArticle(int articleNO) throws Exception;
	// 단일 이미지 버전 수정 적용 처리 
	public void modArticle(Map articleMap) throws Exception;
	// 다중 이미지 버전 수정 적용 처리, 일반 데이터만,  
		public void modArticle2(Map articleMap) throws Exception;
	//답글작성하기.
	public int addReplyNewArticle(Map articleMap) throws Exception;
	
	public void removeArticle(int articleNO) throws Exception;
}
