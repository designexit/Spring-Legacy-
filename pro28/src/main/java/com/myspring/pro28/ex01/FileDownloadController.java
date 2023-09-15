package com.myspring.pro28.ex01;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/*@Controller*/
public class FileDownloadController {
	// 다운로드 위치, 파일 이미지 메모리에 읽어서 출력. 웹브라우저에....
	// c:임시저장소
	private static String CURR_IMAGE_REPO_PATH = "c:\\spring\\image_repo";

	@RequestMapping("/download")
	//imageFileName : 업로드 후 추가한 이미지들의 파일 이름
	protected void download(@RequestParam("imageFileName") String imageFileName,
			                 HttpServletResponse response)throws Exception {
		OutputStream out = response.getOutputStream(); // 메모리상 임시로 저장된 이미지를 웹 브라우저에 출력하는 도구
		String downFile = CURR_IMAGE_REPO_PATH + "\\" + imageFileName; // 불러올 이미지 저장된 위치경로 (절대경로)
		File file = new File(downFile); // 저장된 이미지를 메모리상 처리하기 위해 객체생성

		//클라이언트 - 서버간에 주고 받는 도구가 
		response.setHeader("Cache-Control", "no-cache"); //캐시 저장 안하고 매번 똑같이 파일 출력 (재사용 안함)
		
		//응답객체에 파일이름 첨부하겠다
		// in 인스턴트에 바이트 단위로 이미지가 저장되어 있음. 메모리
		response.addHeader("Content-disposition", "attachment; fileName=" + imageFileName); 
		FileInputStream in = new FileInputStream(file); //
		byte[] buffer = new byte[1024 * 8];
		
		// in 메모리상에 바이트로 저장된 이미지 -> out이라는 객체에 옮기는 작업
		while (true) {
			int count = in.read(buffer); // 버퍼의 크기만큼 읽겠다
			if (count == -1) // 다 읽고 더 이상 읽을게 없다면 반환음수로.. 이미지 다 읽음
				break;
			out.write(buffer, 0, count); //out : 웹 브라우저에 출력
		}
		in.close();
		out.close();
	}

}
