package com.spring.member.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import com.spring.member.vo.MemberVO;

public interface MemberService {
	 public MemberVO getOneMember(String id) throws DataAccessException;
	 public int updateMember(MemberVO membeVO) throws DataAccessException;
	 public List listMembers() throws DataAccessException;
	 public int addMember(MemberVO memberVO) throws DataAccessException;
	 public int removeMember(String id) throws DataAccessException;

}
