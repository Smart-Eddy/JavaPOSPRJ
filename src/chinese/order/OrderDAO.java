package chinese.order;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chinese.main.JdbcTemplet;
import chinese.main.RealPOS;
import chinese.menu.MenuDAO;


public class OrderDAO implements RealPOS {

	JdbcTemplet jdbcTemplet = new JdbcTemplet();
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	MenuDAO menuDao = new MenuDAO();
	
	@Override
	public void run() {
		int run = 1;

		while (run==1) {
			System.out.println("=============================<주문관리>================================");
			System.out.println("1)주문하기\t2)주문조회\t3)자리이동\t4)주문취소\t0)이전");
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
				case 0: System.out.println("이전 메뉴로 돌아갑니다");
					run--;
					break;
				default: System.out.println("번호를 다시 선택하세요\n");break; 
				
				}
			} catch (Exception e) {
				System.out.println("숫자만 입력해주세요");
				sc.nextLine();
			}
		}
	}

	
	// 주문하기
		@Override
		public void insert()   {

			int result = 0;
			List<OrderVO> list = new ArrayList<OrderVO>();
			conn = jdbcTemplet.connDB();
			boolean mcheck = true;
			System.out.println("테이블 번호를 입력하세요");
			System.out.print("▶ ");
			while (!sc.hasNextInt()) {
			System.out.println("다시 입력해주세요");
			System.out.print("▶ ");
			sc.nextLine();
			}
			int tableNo = sc.nextInt();
			sc.nextLine();
			try {
				Loop1: while (mcheck) {
					OrderVO vo = new OrderVO();
					vo.setTableNo(tableNo);	
					
					System.out.println("메뉴를 입력해주세요 | 0:주문완료 ");
					System.out.print("▶ ");
					String menuName = sc.nextLine();
					
					
					boolean existMenu = menuDao.MenuSearch(menuName);
					
					
					while (!existMenu) {
					        if ("0".equals(menuName)) {
								break Loop1;
							} 
							System.out.println("존재하지 않는 메뉴 입니다");
							System.out.println("메뉴를 입력해주세요 | 0:주문완료 ");
							System.out.print("▶ ");
							menuName = sc.nextLine();
						
							existMenu = menuDao.MenuSearch(menuName);
							
						
						}

						vo.setMenuName(menuName);
						System.out.println("메뉴 갯수를 입력해주세요");
						System.out.print("▶ ");
						int menuCount = sc.nextInt();
						sc.nextLine();
						vo.setOrderQty(menuCount);
						list.add(vo);
					}

					

				
			for (int i = 0; i < list.size(); i++) {
				
				String sql = "insert into orders (order_no, order_price, table_no, menu_name, order_qty, order_date)values(SEQ_ORDERS.nextval,(\r\n"
						+ "                                                                                           select menu_price * ?\r\n"
						+ "                                                                                           from menulist\r\n"
						+ "                                                                                           where menu_name = ?\r\n"
						+ "                                                                                            ),?,?,?,sysdate)";

				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, list.get(i).getOrderQty());
				pstmt.setString(2, list.get(i).getMenuName());
				pstmt.setInt(3, list.get(i).getTableNo());
				pstmt.setString(4, list.get(i).getMenuName());
				pstmt.setInt(5, list.get(i).getOrderQty());
				result = pstmt.executeUpdate();
				
			}
			if(result>=1)
			System.out.println("주문이 완료되었습니다");
			pstmt.close();
			conn.close();
			}catch(InputMismatchException e) {
				System.out.println("입력형식 예외 입니다");
			}catch (Exception e) {

				e.printStackTrace();
			}	
		}

	@Override
	public List<OrderVO> select() {
		boolean tcheck = true;
	
		
		conn = jdbcTemplet.connDB();
		int table = 0;
		List<OrderVO> list = new ArrayList<OrderVO>();
		while (tcheck) {
			
		
		try {

			String sql = "select o.menu_name, \r\n" + 
					"        m.menu_price, \r\n" + 
					"        o.order_qty ,\r\n" + 
					"        m.menu_price * o.order_qty, \r\n" + 
					"        o.order_date \r\n" + 
					"        from menulist m, orders o \r\n" + 
					"        where m.menu_name = o.menu_name \r\n" + 
					"        and o.table_no = ?\r\n" + 
					"";
			
			pstmt = conn.prepareStatement(sql);
			System.out.println("조회하실 테이블 번호를 입력하세요");
			System.out.print("▶ ");
			table = sc.nextInt();sc.nextLine();
			pstmt.setInt(1, table);
			rs = pstmt.executeQuery();
				tcheck = false;
			while (rs.next()) {
				
				String menu_Name = rs.getString(1);
				int menu_Price = rs.getInt(2);
				int order_Qty = rs.getInt(3);
				int order_Price = rs.getInt(4);
				String order_Date = rs.getString(5);

				OrderVO vo = new OrderVO();

				vo.setMenuName(menu_Name);
				vo.setMenuPrice(menu_Price);
				vo.setOrderQty(order_Qty);
				vo.setOrderPrice(order_Price);
				vo.setOrderDate(order_Date);

				list.add(vo);
			}
			
			System.out.println("<테이블 "+table+">-----------------------");
			System.out.println("품명\t단가\t수량\t금액");
			System.out.println("---------------------------------");	
			
			for (int i = 0; i < list.size(); i++) {
			System.out.printf("%s\t%,6d\t%3d\t%,6d원\n",
			list.get(i).getMenuName(), list.get(i).getMenuPrice(),
			list.get(i).getOrderQty(), list.get(i).getOrderPrice());
			
			}
			System.out.println("---------------------------------");
		
			rs.close();
			pstmt.close();
			conn.close();
		 
		}catch (Exception e) {
			System.out.println("숫자만 입력해주세요!");
			sc.nextLine();
		}
	}
		return list;

	}

	@Override
	public void update() {
		int table = 0;
		boolean tcheck = true;
		boolean result = false;
		conn = jdbcTemplet.connDB();
		while (tcheck) {	
			
			System.out.println("테이블번호를 입력하세요");
			System.out.print("▶ ");
			table = sc.nextInt();sc.nextLine();
			result = tableSearch(table);
			if(result == true) {		
		try {
			String sql = "UPDATE orders SET table_no = ? WHERE table_no =?";

			pstmt = conn.prepareStatement(sql);

			
			pstmt.setInt(2, table);
			System.out.println("변경하실 테이블 번호를 입력하세요");
			System.out.print("▶ ");
			pstmt.setInt(1, sc.nextInt());sc.nextLine();

			pstmt.executeUpdate();
			System.out.println("테이블이 변경되었습니다.");
					tcheck = false;
		} catch (Exception e) {
			System.out.println("숫자만 입력해주세요!");
				sc.nextLine();
		}
		}else {System.out.println("존재하지 않는 테이블입니다");}
		}
	}

	@Override
	public void delete() {
		int table = 0;
		boolean result = false;
		conn = jdbcTemplet.connDB();
		String sql = "DELETE FROM orders WHERE table_no = ?";
	
			System.out.println("테이블번호를 입력하세요");
			System.out.print("▶ ");
			table = sc.nextInt();
			result = tableSearch(table);
			if(result == true) {
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, table);
				pstmt.executeUpdate();
				System.out.println("주문이 취소되었습니다");
				
				pstmt.close();
				conn.close();

			} catch (Exception e) {
				System.out.println("숫자만 입력해주세요");
				
			}
			}else {System.out.println("존재하지 않는 테이블입니다");}
		}

	
	
	
	public boolean tableSearch(int table) {
		conn = jdbcTemplet.connDB();
		try {
			int count = 0;			
			String sql = "SELECT COUNT(*) FROM orders WHERE table_no =?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, table);
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