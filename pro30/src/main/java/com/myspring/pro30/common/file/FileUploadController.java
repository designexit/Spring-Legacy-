package com.myspring.pro30.common.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class FileUploadController  {
	private static final String CURR_IMAGE_REPO_PATH = "c:\\spring\\image_repo"; // 임시 이미지 파일 저장소
//		private static final String CURR_IMAGE_REPO_PATH = "/Users/minkyoungkim/upload"; // 임시 이미지 파일 저장소
	@RequestMapping(value="/form")
	public String form() {
	    return "uploadForm";
	  }
	
	@RequestMapping(value="/upload",method = RequestMethod.POST)
	// 일반 데이터 + 파일 데이터를 같이 처리하는 MultipartHttpServletRequest
	public ModelAndView upload(MultipartHttpServletRequest multipartRequest,HttpServletResponse response)
	  throws Exception{
		// 폼에 사용자가 입력한 일반데이터2개와 이미지데이터 여러개 처리 준비
		multipartRequest.setCharacterEncoding("utf-8");
		Map map = new HashMap();
		Enumeration enu=multipartRequest.getParameterNames();
		while(enu.hasMoreElements()){
			String name=(String)enu.nextElement(); //일반 데이터 키 가지고 오기
			String value=multipartRequest.getParameter(name); // 키에 대응되는 값 불러오기
			//System.out.println(name+", "+value);
			map.put(name,value); //임시 저장소에 첫번째 일반 데이터 저장
		}
		
		List fileList= fileProcess(multipartRequest); // fileProcess 임의의 메서드. 여기로 이동
		map.put("fileList", fileList);
		ModelAndView mav = new ModelAndView();
		mav.addObject("map", map);
		mav.setViewName("result");
		return mav;
	}
	
	private List<String> fileProcess(MultipartHttpServletRequest multipartRequest) throws Exception{
		List<String> fileList= new ArrayList<String>();
		Iterator<String> fileNames = multipartRequest.getFileNames(); //Iterator : 반복처리 도와줌
		// multipartRequest.getFileNames() : 이미지 파일 이름을 가지고 옴
		
		while(fileNames.hasNext()){
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName); 
			String originalFileName=mFile.getOriginalFilename();
			fileList.add(originalFileName);
			File file = new File(CURR_IMAGE_REPO_PATH +"\\"+ fileName);
			if(mFile.getSize()!=0){ //File Null Check
				if(! file.exists()){ //��λ� ������ �������� ���� ���
					if(file.getParentFile().mkdirs()){ //��ο� �ش��ϴ� ���丮���� ����
						file.createNewFile(); //���� ���� ����
					}
				}
				mFile.transferTo(new File(CURR_IMAGE_REPO_PATH +"\\"+ originalFileName)); //�ӽ÷� ����� multipartFile�� ���� ���Ϸ� ����
			}
		}
		return fileList;
	}
}
