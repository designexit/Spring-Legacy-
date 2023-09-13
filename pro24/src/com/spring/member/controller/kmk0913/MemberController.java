package com.spring.member.controller.kmk0913;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataAccessException;
import org.springframework.web.servlet.ModelAndView;

public interface MemberController {
	public ModelAndView modMember(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView updateMember(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView listMembers(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView addMember(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView removeMember(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
