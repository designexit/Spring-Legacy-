package com.myspring.pro30.board.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.service.BoardService;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;
import com.myspring.pro30.member.vo.MemberVO;


@Controller("boardController")
public class BoardControllerImpl  implements BoardController{
	private static final String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
	@Autowired
	BoardService boardService;
	@Autowired
	ArticleVO articleVO;
	
	// 게시글 전체 목록보기
	@Override
	@RequestMapping(value= "/board/listArticles.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String)request.getAttribute("viewName");
		List articlesList = boardService.listArticles();
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("articlesList", articlesList);
		return mav;
		
	}
	
	 //글쓰기, 단일 이미지 업로드
	@Override
	@RequestMapping(value="/board/addNewArticle.do" ,method = RequestMethod.POST)
	// 전체 구조 :  @Controller 이미지 + 뷰 같이 전달하지만 혼합 구성 가능
	// 데이터만 전달, 뷰 전달 안함
	// 서버 -> 클라이언트 : 데이터 전달, 잭슨이 JSON형태로 전달
	@ResponseBody
	//ResponseEntity : 데이터만 전달하면 상태를 모르기에 상태(HTTP Status code)를 같이 전달
	public ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest, 
	HttpServletResponse response) throws Exception {
		// 클라이언트가 게시글 작성 후 서버로 전달하면 2가지 처리
		// 1) 일반데이터, 2) 파일 데이터
		//MultipartHttpServletRequest -> 일반 + 파일 데이터 같이 처리시 주로 사용
		multipartRequest.setCharacterEncoding("utf-8");
		// 클라이언트로부터 전달 받은 정보를 맵(컬렉션)에 담아서
		// 각 동네에 전달하는 과정 -> 동네1 ~ 동네4
		// controller > service > DAO > mapper > 실제 DB : 정방향
		// 동네 1~4번 순회 후 모든 정보를 다 가져옴
		// 박스 : articleMap, 내용 : 일반 + 파일
		Map<String,Object> articleMap = new HashMap<String, Object>();
		// getParameterNames : 일반데이터
		// 일반 데이터를 조회해 각 티에 대한 값 분류 예) 작성자, 게시글, 등록일
		Enumeration enu=multipartRequest.getParameterNames();
		while(enu.hasMoreElements()){
			String name=(String)enu.nextElement();
			String value=multipartRequest.getParameter(name);
			articleMap.put(name,value);
		}
		
		// upload 매서드는 아래 정의가 되어 있고 미디어 저장소에 파일형식으로 저장을 하고 저장된 파일 이미지의 이름을 반환하는 형식
		String imageFileName= upload(multipartRequest);
		// 로그인 후 세션에 로그인 된 정보를 등록
		// 세션에 접근하기 위한 인스턴스
		HttpSession session = multipartRequest.getSession();
		// session, 임시서버의 저장소에 로그인된 정보를 가지고 온다
		// memberVO : 로그인 된 회원의 정보
		MemberVO memberVO = (MemberVO) session.getAttribute("member");
		// 로그인 한 회원 아이디 조회
		String id = memberVO.getId();
		// articleMap 부모글 번호 기본 0으로 설정
		articleMap.put("parentNO", 0);
		// 로그인한 회원 아이디
		articleMap.put("id", id);
		// 게시글 첨부된 이미지 파일 이름
		articleMap.put("imageFileName", imageFileName);
		
		String message;
		ResponseEntity resEnt=null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		try {
			// 실제 데이터를 입력시 항상 try catch 구문 안에서 작업
			// 파일 IO 언제든 오류가 발생할 수 있어서 비정상 종료 방지 하기 위해 try안에서 작업
			
			// 동네 1 -> 동네 2 외주
			// 로그인 - 글쓰기 연결 확인 후
			
			// 일반데이터 + 파일이미지 이름
			// 일반 데이터 동네 1~4 통해서 디비에 저장
			// articleMap : 1)게시글 내용 2) 이미지 파일 이름 3) parentsNo 0
			int articleNO = boardService.addNewArticle(articleMap);
			// 이미지 파일 첨부 (이미지파일 있음)
			if(imageFileName!=null && imageFileName.length()!=0) {
				// 임시파일 저장소
				File srcFile = new 
				File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
				// 실제 파일 저장되는 저장소
				File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
				// 임시 저장소 -> 실제 저장소로 파일 이동
				FileUtils.moveFileToDirectory(srcFile, destDir,true);
			}
	
			message = "<script>";
			message += " alert('글쓰기 성공!');";
			message += " location.href='"+multipartRequest.getContextPath()+"/board/listArticles.do'; ";
			message +=" </script>";
		    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		}catch(Exception e) {
			// 오류 발생시 임시 저장소를 삭제를 하고 
			File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
			srcFile.delete();
			// 메세지 글쓰기 작성 오류
			message = " <script>";
			message +=" alert('글쓰기 작성 오류');";
			message +=" location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do'; ";
			message +=" </script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}
		return resEnt;
	}
	
	
	/*
	 * // 답글쓰기, 단일 이미지 업로드
	 * 
	 * @Override
	 * 
	 * @RequestMapping(value="/board/addNewArticle.do" ,method = RequestMethod.POST)
	 * 
	 * @ResponseBody public ResponseEntity
	 * replyNewArticle(MultipartHttpServletRequest multipartRequest,
	 * HttpServletResponse response) throws Exception {
	 * multipartRequest.setCharacterEncoding("utf-8"); Map<String,Object> articleMap
	 * = new HashMap<String, Object>(); Enumeration
	 * enu=multipartRequest.getParameterNames(); while(enu.hasMoreElements()){
	 * String name=(String)enu.nextElement(); String
	 * value=multipartRequest.getParameter(name); articleMap.put(name,value); }
	 * 
	 * String imageFileName= upload(multipartRequest); HttpSession session =
	 * multipartRequest.getSession(); MemberVO memberVO = (MemberVO)
	 * session.getAttribute("member"); String id = memberVO.getId();
	 * articleMap.put("parentNO", 0); articleMap.put("id", id);
	 * articleMap.put("imageFileName", imageFileName);
	 * 
	 * String message; ResponseEntity resEnt=null; HttpHeaders responseHeaders = new
	 * HttpHeaders(); responseHeaders.add("Content-Type",
	 * "text/html; charset=utf-8"); try { int articleNO =
	 * boardService.addNewArticle(articleMap); if(imageFileName!=null &&
	 * imageFileName.length()!=0) { File srcFile = new
	 * File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName); File destDir = new
	 * File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
	 * FileUtils.moveFileToDirectory(srcFile, destDir,true); }
	 * 
	 * message = "<script>"; message += " alert('글쓰기 성공!');"; message +=
	 * " location.href='"+multipartRequest.getContextPath()
	 * +"/board/listArticles.do'; "; message +=" </script>"; resEnt = new
	 * ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	 * }catch(Exception e) { // 오류 발생시 임시 저장소를 삭제를 하고 File srcFile = new
	 * File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName); srcFile.delete(); //
	 * 메세지 글쓰기 작성 오류 message = " <script>"; message +=" alert('글쓰기 작성 오류');";
	 * message +=" location.href='"+multipartRequest.getContextPath()
	 * +"/board/articleForm.do'; "; message +=" </script>"; resEnt = new
	 * ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	 * e.printStackTrace(); } return resEnt; }
	 */
	
	
	//상세페이지 조회
	@RequestMapping(value="/board/viewArticle.do" ,method = RequestMethod.GET)
	//@RequestParam : 클라이언트로 부터 전달 받은 매개변수를 서버에서 사용하는 방법
	// 게시글 제목 클릭시 서버로 전달된 주소  http://~~~~ articleNO=6
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		// 인터셉터를 이용해서 서버의 컨트롤러에 도달하기 전에 해당 뷰의 이름을 가져과 request 인스턴스에 미리 담아두기
		String viewName = (String)request.getAttribute("viewName");
		// 상세피이지를 보기 위해서 articleNO=6에 대한 정보를 조회 하는 과정
		// 동네 2번 외주 -> 결과는 6번 게시글 내용 가져오는 로직
		articleVO=boardService.viewArticle(articleNO);
		// 6번 게시글 정보를 디비에서 조회하고 돌아왓음
		ModelAndView mav = new ModelAndView();
		// 데이터와 뷰를 같이 전달
		mav.setViewName(viewName);
		// 뷰에 전달
		mav.addObject("article", articleVO);
		return mav;
	}
	
	/*
	//���� �̹��� �����ֱ�
	@RequestMapping(value="/board/viewArticle.do" ,method = RequestMethod.GET)
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO,
			  HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		Map articleMap=boardService.viewArticle(articleNO);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("articleMap", articleMap);
		return mav;
	}
   */
	

	
  //단일 이미지 수정을 적용하는 코드
  @RequestMapping(value="/board/modArticle.do" ,method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest,  
    HttpServletResponse response) throws Exception{
    multipartRequest.setCharacterEncoding("utf-8");
    // 임시저정 박스 수정된 글 및 이미지를 담는 박스
	Map<String,Object> articleMap = new HashMap<String, Object>();
	//일반 데이터 추출
	Enumeration enu=multipartRequest.getParameterNames();
	while(enu.hasMoreElements()){
		String name=(String)enu.nextElement();
		String value=multipartRequest.getParameter(name);
		articleMap.put(name,value);
	}
	
	// 수정된 이미지 미디어 저장소에 저장 후 파일 이름 가지고 오기
	String imageFileName= upload(multipartRequest);
	HttpSession session = multipartRequest.getSession();
	MemberVO memberVO = (MemberVO) session.getAttribute("member");
	String id = memberVO.getId();
	articleMap.put("id", id);
	articleMap.put("imageFileName", imageFileName);
	
	String articleNO=(String)articleMap.get("articleNO");
	String message;
	ResponseEntity resEnt=null;
	HttpHeaders responseHeaders = new HttpHeaders();
	responseHeaders.add("Content-Type", "text/html; charset=utf-8");
    try {
    	// 수정된 데이터를 디비에 반영하는 로직 -> 외주 줌
       boardService.modArticle(articleMap);
       // 실제 이미지를 업로드 하는 로직
       if(imageFileName!=null && imageFileName.length()!=0) {
         File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
         File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
         FileUtils.moveFileToDirectory(srcFile, destDir, true);
         
         // 기존 이미지 삭제
         String originalFileName = (String)articleMap.get("originalFileName");
         File oldFile = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO+"\\"+originalFileName);
         oldFile.delete();
       }	
       message = "<script>";
	   message += " alert('수정 완료');";
	   message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
	   message +=" </script>";
       resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
    }catch(Exception e) {
      File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
      srcFile.delete();
      message = "<script>";
	  message += " alert('수정 오류');";
	  message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
	  message +=" </script>";
      resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
    }
    return resEnt;
  }
  
  @Override
  @RequestMapping(value="/board/removeArticle.do" ,method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity  removeArticle(@RequestParam("articleNO") int articleNO,
                              HttpServletRequest request, HttpServletResponse response) throws Exception{
	response.setContentType("text/html; charset=UTF-8");
	String message;
	ResponseEntity resEnt=null;
	HttpHeaders responseHeaders = new HttpHeaders();
	responseHeaders.add("Content-Type", "text/html; charset=utf-8");
	try {
		// 삭제시 일반 데이터 + 이미지도 같이 삭세
		boardService.removeArticle(articleNO);
		//destDir : 미디어 서버, 저장소에 저장된 파일을 삭게
		File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
		FileUtils.deleteDirectory(destDir);
		
		message = "<script>";
		message += " alert('삭제 완료');";
		message += " location.href='"+request.getContextPath()+"/board/listArticles.do';";
		message +=" </script>";
	    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	       
	}catch(Exception e) {
		message = "<script>";
		message += " alert('삭제 오류');";
		message += " location.href='"+request.getContextPath()+"/board/listArticles.do';";
		message +=" </script>";
	    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	    e.printStackTrace();
	}
	return resEnt;
  }  
  
/*
  //���� �̹��� �� �߰��ϱ�
  @Override
  @RequestMapping(value="/board/addNewArticle.do" ,method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity  addNewArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception {
	multipartRequest.setCharacterEncoding("utf-8");
	String imageFileName=null;
	
	Map articleMap = new HashMap();
	Enumeration enu=multipartRequest.getParameterNames();
	while(enu.hasMoreElements()){
		String name=(String)enu.nextElement();
		String value=multipartRequest.getParameter(name);
		articleMap.put(name,value);
	}
	
	//�α��� �� ���ǿ� ����� ȸ�� �������� �۾��� ���̵� ���ͼ� Map�� �����մϴ�.
	HttpSession session = multipartRequest.getSession();
	MemberVO memberVO = (MemberVO) session.getAttribute("member");
	String id = memberVO.getId();
	articleMap.put("id",id);
	
	
	List<String> fileList =upload(multipartRequest);
	List<ImageVO> imageFileList = new ArrayList<ImageVO>();
	if(fileList!= null && fileList.size()!=0) {
		for(String fileName : fileList) {
			ImageVO imageVO = new ImageVO();
			imageVO.setImageFileName(fileName);
			imageFileList.add(imageVO);
		}
		articleMap.put("imageFileList", imageFileList);
	}
	String message;
	ResponseEntity resEnt=null;
	HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add("Content-Type", "text/html; charset=utf-8");
	try {
		int articleNO = boardService.addNewArticle(articleMap);
		if(imageFileList!=null && imageFileList.size()!=0) {
			for(ImageVO  imageVO:imageFileList) {
				imageFileName = imageVO.getImageFileName();
				File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
				//destDir.mkdirs();
				FileUtils.moveFileToDirectory(srcFile, destDir,true);
			}
		}
		    
		message = "<script>";
		message += " alert('������ �߰��߽��ϴ�.');";
		message += " location.href='"+multipartRequest.getContextPath()+"/board/listArticles.do'; ";
		message +=" </script>";
	    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	    
		 
	}catch(Exception e) {
		if(imageFileList!=null && imageFileList.size()!=0) {
		  for(ImageVO  imageVO:imageFileList) {
		  	imageFileName = imageVO.getImageFileName();
			File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
		 	srcFile.delete();
		  }
		}

		
		message = " <script>";
		message +=" alert('������ �߻��߽��ϴ�. �ٽ� �õ��� �ּ���');');";
		message +=" location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do'; ";
		message +=" </script>";
		resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		e.printStackTrace();
	}
	return resEnt;
  }
	
*/

	
// board/*Form.do : 글쓰기 수정 답글 폼
  //뷰만 띄워주는 로직
	@RequestMapping(value = "/board/*Form.do", method =  RequestMethod.GET)
	private ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	//�Ѱ� �̹��� ���ε��ϱ�
	private String upload(MultipartHttpServletRequest multipartRequest) throws Exception{
		String imageFileName= null;
		Iterator<String> fileNames = multipartRequest.getFileNames();
		
		while(fileNames.hasNext()){
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			imageFileName=mFile.getOriginalFilename();
			File file = new File(ARTICLE_IMAGE_REPO +"\\"+ fileName);
			if(mFile.getSize()!=0){ //File Null Check
				if(! file.exists()){ //��λ� ������ �������� ���� ���
					if(file.getParentFile().mkdirs()){ //��ο� �ش��ϴ� ���丮���� ����
							file.createNewFile(); //���� ���� ����
					}
				}
				mFile.transferTo(new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+ "\\"+imageFileName)); //�ӽ÷� ����� multipartFile�� ���� ���Ϸ� ����
			}
		}
		return imageFileName;
	}
	
	/*
	//���� �̹��� ���ε��ϱ�
	private List<String> upload(MultipartHttpServletRequest multipartRequest) throws Exception{
		List<String> fileList= new ArrayList<String>();
		Iterator<String> fileNames = multipartRequest.getFileNames();
		while(fileNames.hasNext()){
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			String originalFileName=mFile.getOriginalFilename();
			fileList.add(originalFileName);
			File file = new File(ARTICLE_IMAGE_REPO +"\\"+ fileName);
			if(mFile.getSize()!=0){ //File Null Check
				if(! file.exists()){ //��λ� ������ �������� ���� ���
					if(file.getParentFile().mkdirs()){ //��ο� �ش��ϴ� ���丮���� ����
							file.createNewFile(); //���� ���� ����
					}
				}
				mFile.transferTo(new File(ARTICLE_IMAGE_REPO +"\\"+"temp"+ "\\"+originalFileName)); //�ӽ÷� ����� multipartFile�� ���� ���Ϸ� ����
			}
		}
		return fileList;
	}
	*/
}
