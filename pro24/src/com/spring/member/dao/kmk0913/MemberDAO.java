package com.spring.member.dao.kmk0913;

import java.util.List;

import org.springframework.dao.DataAccessException;
import com.spring.member.vo.MemberVO;

// 동네3 , 인터페이스 , 기능 3개정도. 
public interface MemberDAO {
	 public List selectAllMemberList() throws DataAccessException;
	 public MemberVO selectOneMember(String id) throws DataAccessException;
	 public int updateMember(MemberVO memberVO) throws DataAccessException ;
	 public int insertMember(MemberVO memberVO) throws DataAccessException ;
	 public int deleteMember(String id) throws DataAccessException;
	 

}
