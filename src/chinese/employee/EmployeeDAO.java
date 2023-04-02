package chinese.employee;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import chinese.main.JdbcTemplet;
import chinese.main.RealPOS;

public class EmployeeDAO implements RealPOS {

	JdbcTemplet jdbcTemplet = new JdbcTemplet();
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;


	@Override
	public void run() {
		int choice = 0;
		int run = 1;
		while (run==1) {
			
			try {
				System.out.println("=====================================<직원관리>========================================");
				System.out.println("1)전체직원\t2)직원검색\t3)회원가입\t4)정보수정\t5)회원탈퇴\t0)이전");
				System.out.println("=======================================================================================");
				System.out.print("▶ ");
				choice = sc.nextInt(); sc.nextLine();
				
				switch (choice) {
				case 1:
					emplist();
					break;
				case 2:
					select();
					break;
				case 3:
					insert();
					break;
				case 4:
					update();
					break;
				case 5:
					delete();
					break;
				case 0: System.out.println("이전 메뉴로 돌아갑니다");
					run--;
					
					break;
				default: System.out.println("번호를 다시 선택하세요\n");break;
					
					
				}
				
			} catch (Exception e) {
				System.out.println("숫자만 입력해주세요");
				sc.nextLine();
			}	
		}//while
		
	}

	// 전체 정보 출력
	public List<EmployeeVO> emplist() {
        List<EmployeeVO> list = new ArrayList<EmployeeVO>();
        conn = jdbcTemplet.connDB();
        String sql = "select * from employee ";
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {

                String id = rs.getString(1);
                String pwd = rs.getString(2);
                String name = rs.getString(3);
                String phone = rs.getString(4);
                Date hiredate = rs.getDate(5);
                String rank = rs.getString(6);
                EmployeeVO vo = new EmployeeVO();
                vo.setId(id);
                vo.setPw(pwd);
                vo.setName(name);
                vo.setPhone(phone);
                vo.setHiredate(hiredate);
                vo.setRank(rank);
                
                list.add(vo);
            }
            System.out.println("------------------------------------------------------------------------");
            System.out.println("아이디\t비밀번호\t이름\t연락처\t\t입사일\t\t직급");
            System.out.println("------------------------------------------------------------------------");

            for(int i=0; i<list.size(); i++) {
            	System.out.printf("%s\t%6s\t%10s\t%s\t%s\t%s\n",
            			list.get(i).getId(), list.get(i).getPw(),
            			list.get(i).getName(), list.get(i).getPhone(),
            			list.get(i).getHiredate(),list.get(i).getRank());
            }			
            System.out.println("------------------------------------------------------------------------");
            
				rs.close();
				pstmt.close();
				conn.close();
			
			} catch (Exception e) {
				e.printStackTrace();
			}

			return list;
		}
	
	
	// 회원 가입 메서드
	@Override
	public void insert() {
		int rs=0;
		boolean result;
		String id_ =null;
		String sql = "insert into employee values(?,?,?,?,sysdate,?)";
		conn = jdbcTemplet.connDB();
		
		System.out.println("아이디를 입력해 주세요.");
		System.out.print("▶ ");
		id_ = sc.nextLine();
		result = idSearch(id_);
		if(result == false) {
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id_);
			System.out.println("비밀번호를 입력해 주세요.");
			System.out.print("▶ ");
			pstmt.setString(2, sc.nextLine());
			System.out.println("이름을 입력해 주세요.");
			System.out.print("▶ ");
			pstmt.setString(3, sc.nextLine());
			System.out.println("연락처를 입력해 주세요.");
			System.out.print("▶ ");
			pstmt.setString(4, sc.nextLine());
			System.out.println("직급을 입력해 주세요.");
			System.out.print("▶ ");
			pstmt.setString(5, sc.nextLine());
			
		    rs = pstmt.executeUpdate();		
		    if(rs >= 1) {System.out.println("직원등록을 완료했습니다");}
			pstmt.close();
			conn.close();
		
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		}else {System.out.println("사용중인 아이디 입니다");}
	}

	

	// 정보 출력
	@Override
	public List<EmployeeVO> select() {
		
		List<EmployeeVO> list = new ArrayList<>();
		int result = 0;
		conn = jdbcTemplet.connDB();
		String sql = "select * from employee where emp_id=?";
 					
		try {

			pstmt = conn.prepareStatement(sql);
			System.out.println("아이디를 입력해 주세요.");
			String id_ = sc.nextLine();
								
				pstmt.setString(1, id_);
				rs = pstmt.executeQuery();
				
				while (rs.next()) {

					String id = rs.getString(1);
					String pw = rs.getString(2);
					String name = rs.getString(3);
					String phone = rs.getString(4);
					Date hiredate = rs.getDate(5);
					String rank = rs.getString(6);

					EmployeeVO vo = new EmployeeVO();

					vo.setId(id);
					vo.setPw(pw);
					vo.setName(name);
					vo.setPhone(phone);
					vo.setHiredate(hiredate);
					vo.setRank(rank);
                   
					list.add(vo);
				}	
				
				 result = pstmt.executeUpdate();	
				
				 
				 if(result > 0) {
				 System.out.println("------------------------------------------------------------------------");
		         System.out.println("아이디\t비밀번호\t이름\t연락처\t\t입사일\t\t직급");
		         System.out.println("------------------------------------------------------------------------");

		          for(int i=0; i<list.size(); i++) {
		          System.out.printf("%s\t%6s\t%10s\t%s\t%s\t%s\n",
		          list.get(i).getId(), list.get(i).getPw(),
		          list.get(i).getName(), list.get(i).getPhone(),
		          list.get(i).getHiredate(),list.get(i).getRank());
		           }			
		          System.out.println("------------------------------------------------------------------------");
				 	
				 }else{
					 System.out.println("존재하지 않는 직원입니다");
				 }
				pstmt.close();
				conn.close();
				

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	// 수정
	@Override
	public void update() {
		String id =null;
		conn = jdbcTemplet.connDB();
		
		boolean result = false;
		String sql = "update employee set  emp_pwd = ?, emp_phone = ?, EMP_RANK = ?  where emp_id = ?";
		
		System.out.println("아이디를 입력해 주세요.");
		System.out.print("▶ ");
		id = sc.nextLine();
		result = idSearch(id);
		if(result == true) {
		try {
			pstmt = conn.prepareStatement(sql);

			 
			System.out.println("변경할 비밀번호를 입력해 주세요");
			System.out.print("▶ ");
			String pwd = sc.nextLine();
			System.out.println("변경할 연락처를 입력해 주세요");
			System.out.print("▶ ");
			String pnum = sc.nextLine();
			System.out.println("변경할 직급을 입력해 주세요");
			System.out.print("▶ ");
			String rank = sc.nextLine();

			pstmt.setString(4, id);
			pstmt.setString(1, pwd);
			pstmt.setString(2, pnum);
			pstmt.setString(3, rank);
			
		    pstmt.executeUpdate();
			
			System.out.println("정보수정이 완료 되었습니다");
			
			pstmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		}else {System.out.println("존재하지 않는 아이디입니다");}
		
	}

	// 삭제
	@Override
	public void delete() {
		
		String id= null;
		boolean result = false;
		String sql = "delete employee where emp_id=?";
		conn = jdbcTemplet.connDB();
		

		System.out.println("삭제할 아이디를 입력해 주세요.");
		System.out.print("▶ ");
		id = sc.nextLine();
		result = idSearch(id);
		if(result == true) {
		try {
			pstmt = conn.prepareStatement(sql);
		
               
			pstmt.setString(1, id);

			pstmt.executeUpdate();

			System.out.println("삭제되었습니다");

			pstmt.close();
			conn.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		}else {System.out.println("존재하지 않는 아이디입니다");}
	}
	
	
	public boolean idSearch(String id_) {
		conn = jdbcTemplet.connDB();
		try {
			int count = 0;			
			String sql = "SELECT COUNT(*) FROM employee WHERE emp_id =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id_);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				count = rs.getInt(1);
			}
			if(count != 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	
	
	

}