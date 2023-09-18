package com.myspring.pro29.ex02;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
// 데이터 + 뷰 기본 구조
// 추가로 특정 메서드에서 뷰없이 데이터만 전달하는 구조도 혼합가능
public class ResController {
	@RequestMapping(value = "/res1")
	@ResponseBody
	// 클라이언트 -> 서버 : 데이터 전달
	
	//@ResponseBody
	// 서버 - > 클라이언트에게 응답 : 데이터 전달
	public Map<String, Object> res1() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", "hong");
		map.put("name", "홍길동");
		return map;
	}
	
	// 뷰로 전달하는 구조
	@RequestMapping(value = "/res2")
	public ModelAndView res2() {
		return new ModelAndView("home");
	}
	
}
