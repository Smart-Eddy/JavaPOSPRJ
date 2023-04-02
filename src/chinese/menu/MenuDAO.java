package chinese.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import chinese.main.JdbcTemplet;
import chinese.main.RealPOS;
import chinese.main.RealPosMain;

public class MenuDAO implements RealPOS{
	
	
	private JdbcTemplet jdbcTemplet = new JdbcTemplet();
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	
	@Override
	public void run() {
		int run = 1;
		while (run == 1) {
			System.out.println("=============================<주문관리>================================");
			System.out.println("1)메뉴추가\t2)메뉴조회\t3)가격수정\t4)메뉴삭제\t0)이전");
			System.out.println("=======================================================================");
			System.out.print("▶ ");
			try {
				int choice = 0;
				choice = sc.nextInt();sc.nextLine();
				switch (choice) {
				case 1:
					insert();
					break;
				case 2:
					select();
					break;
				case 3:
					update();
					break;
				case 4:
					delete();
					break;
				case 0: System.out.println("이전 메뉴로 돌아갑니다"); run--;
					break;
				default: System.out.println("번호를 다시 선택하세요\n");break;
					
				}
				
			} catch(Exception e) {
				System.out.println("숫자만 입력해주세요");
				sc.nextLine();
			}
		}
	}
	
		
		
	

	@Override
	public void insert() {
		conn = jdbcTemplet.connDB();
		String sql = "insert into menulist values(?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			System.out.println("메뉴이름을 입력하세요");
			System.out.print("▶ ");
			pstmt.setString(1, sc.nextLine());
			System.out.println("메뉴가격을 입력하세요");
			System.out.print("▶ ");
			pstmt.setInt(2, sc.nextInt());sc.nextLine();
			System.out.println("메뉴 종류를 입력하세요");
			System.out.print("▶ ");
			pstmt.setString(3, sc.nextLine());
			
			pstmt.executeUpdate();
			System.out.println("메뉴가 추가되었습니다");
			
			
			pstmt.close();
			conn.close();
		} catch(SQLIntegrityConstraintViolationException e) {
			System.out.println("메뉴명은 필수입력 입니다");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public List<MenuVO> select() {
		conn = jdbcTemplet.connDB();
		String sql = "select * from menulist order by menu_type , menu_price desc";
		List<MenuVO> list = new ArrayList<>();
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String menuName = rs.getString(1);
				int menuPrice   = rs.getInt(2);
				String menuType = rs.getString(3);
				
				MenuVO vo = new MenuVO();
				vo.setMenuName(menuName);
				vo.setMenuPrice(menuPrice);
				vo.setMenuType(menuType);
				
				list.add(vo);	
			}
			
				System.out.println("---------------------------");
				System.out.println("품명\t단가\t종류");
				System.out.println("---------------------------");
			for(int i=0; i<list.size(); i++) {
				System.out.printf("%s\t%,3d\t%s\n",
						list.get(i).getMenuName(), list.get(i).getMenuPrice(),
						list.get(i).getMenuType()); 
			}
				System.out.println("---------------------------");
			
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void update() {
		conn = jdbcTemplet.connDB();
		String sql = "update menulist set menu_price = ? where menu_name = ?";
		
		String menuName=null;
		boolean result = false;

		System.out.println("메뉴명을 입력하세요");
		System.out.print("▶ ");
		menuName = sc.nextLine();
		result = MenuSearch(menuName);
		if(result == true) {
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(2, menuName);
			System.out.println("변경하실 가격을 입력해주세요");
			System.out.print("▶ ");
			pstmt.setString(1, sc.nextLine());
			pstmt.executeUpdate();
			System.out.println("가격이 수정되었습니다");
			
			
			
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		}else {System.out.println("존재하지 않는 메뉴입니다");}
	}
	

	@Override
	public void delete() {
		conn  =	jdbcTemplet.connDB();
		String sql = "delete from menulist where menu_name = ?";
		String menuName=null;
		boolean result = false;

		System.out.println("메뉴명을 입력하세요");
		System.out.print("▶ ");
		menuName = sc.nextLine();
		result = MenuSearch(menuName);
		if(result == true) {
		
		try {
			pstmt = conn.prepareStatement(sql);
		
			pstmt.setString(1, menuName);
			pstmt.executeUpdate();
			System.out.println("메뉴가 삭제되었습니다.");
			
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		}else {System.out.println("존재하지 않는 메뉴입니다");}
	}
	
	
	public boolean MenuSearch(String menu) {
		conn = jdbcTemplet.connDB();
		try {
			int count = 0;			
			String sql = "SELECT COUNT(*) FROM MENULIST WHERE MENU_NAME =?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, menu);
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