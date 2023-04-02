package chinese.sal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import chinese.main.JdbcTemplet;
import chinese.main.RealPOS;
import chinese.menu.MenuDAO;

public class SalDAO implements RealPOS{

	
	private JdbcTemplet jdbcTemplet = new JdbcTemplet();
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	
	
		@Override
		public void run() {
			int run = 1;
			while (run==1) {
				System.out.println("======================<매출관리>=======================");
				System.out.println("\t1)매출내역\t2)매출조회\t0)이전");
				System.out.println("=======================================================");
				System.out.print("▶ ");
	
				try {
					int choice = 0;
					int minCho = 0;
					choice = sc.nextInt();sc.nextLine();
					switch (choice) {
					
					case 1:	select(); break;	
					case 2: System.out.println("1)현재매출\t2)총매출\t3)날짜별\t4)메뉴별");
							System.out.print("▶ ");
							minCho = sc.nextInt();sc.nextLine();
							switch(minCho) {
							case 1:nowSalSelect(); break;
							case 2:totalSalSelect(); break;
							case 3:daySalSelect(); break;
							case 4:menuSalSelect(); break;		
							}
					break;
					case 0: System.out.println("이전 메뉴로 돌아갑니다");
						run--;
						break;
					default: System.out.println("번호를 다시 선택하세요\n");break;
					
					}
				} catch(InputMismatchException e) {
					System.out.println("숫자만 입력해주세요");
					sc.nextLine();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("숫자만 입력해주세요");
					sc.nextLine();
				}
			}
		}
		
		
	//결제시 매출에 자동 삽입
	public void autoSalInsert(int table) { 
		conn = jdbcTemplet.connDB();
		String sql = "insert into sales select SEQ_SALES.nextval, order_no, order_price, table_no, menu_name, order_qty, order_date  from orders  where table_no = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, table);
			pstmt.executeUpdate();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	//전체 매출 출력
	@Override
	public List<SalVO> select() {
		
		conn = jdbcTemplet.connDB();
		List<SalVO> list = new ArrayList<>();
		String sql = "select sales_no,order_price,order_date from sales order by sales_no desc";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
			int salesNo    =    rs.getInt(1);
			int salesPrice =	rs.getInt(2);
			Date salesDate =	rs.getDate(3);
			SalVO vo = new SalVO();
			vo.setSalesNo(salesNo);
			vo.setOrderPrice(salesPrice);
			vo.setOrderDate(salesDate);
			list.add(vo);		
			}
			
			System.out.println("--------------------------------------------");
			System.out.println("매출번호\t매출금액\t매출일시");
			System.out.println("--------------------------------------------");
			for(int i=0; i<list.size(); i++) {
				System.out.printf("%7d\t%,16d\t%10s\t\n",
				list.get(i).getSalesNo(), list.get(i).getOrderPrice(), list.get(i).getOrderDate());	
				}
	
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//현재 매출 조회
	public void nowSalSelect() {
		conn = jdbcTemplet.connDB();
		String sql = "select sum(order_price),sysdate \r\n" + 
					 "from sales\r\n" + 
					 "where to_char(order_date,'YY/MM/DD') = to_char(sysdate,('YY/MM/DD'))";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
			int orderPrice = rs.getInt(1);
			Date orderDate = rs.getDate(2);
			System.out.println("----------------------------");
			System.out.println("매출일시\t매출금액\t");
			System.out.println("----------------------------");	
			System.out.printf("%s\t%,8d\t\n",orderDate,orderPrice);
			}	
			System.out.println("----------------------------");			
					
			
			
			
			
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//총매출조회
	public void totalSalSelect() {
		conn = jdbcTemplet.connDB();
		String sql = "select sum(order_price) from sales";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
			int totalSal = rs.getInt(1);	
			System.out.println("----------------------------");
			System.out.println("\t총매출금액\t");
			System.out.println("----------------------------");	
			System.out.printf("%,18d\n",totalSal);
			}
			System.out.println("----------------------------");	
			
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//날짜별
	public void daySalSelect() {
		conn = jdbcTemplet.connDB();
		String sql = "select  sum(order_price)  from sales  where  to_date(order_date,'YY/MM/DD') = to_date(?,'YY/MM/DD')";
		try {
			System.out.println("날짜를 입력해주세요 (YY/MM/DD)");
			System.out.print("▶ ");
			String getDate = sc.nextLine();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, getDate);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
			int daySal = rs.getInt(1);
			System.out.println("----------------------------");
			System.out.println("매출일시\t매출금액\t");
			System.out.println("----------------------------");	
			System.out.printf("%s\t%,8d\t\n",getDate,daySal);
			}
			System.out.println("----------------------------");
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLDataException e) {
			System.out.println("입력형식을 지켜주세요");
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public void menuSalSelect() {
		conn = jdbcTemplet.connDB();
		String sql = "select menu_name, sum(order_qty), sum(order_price)  from sales\r\n" + 
					 "group by menu_name\r\n" + 
					 "having menu_name = ?";
		
			MenuDAO menuDao = new MenuDAO();
			String menu    =null;
			boolean result = false;
	
			System.out.println("메뉴명을 입력하세요");
			System.out.print("▶ ");
			menu = sc.nextLine();
			result = menuDao.MenuSearch(menu);
			if(result == true) {
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, menu);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String menuName = rs.getString(1);
				int    orderQty = rs.getInt(2);
				int  menuPrice = rs.getInt(3);
				System.out.println("---------------------------------");
				System.out.println("메뉴명\t  판매수량\t매출금액");
				System.out.println("---------------------------------");	
				System.out.printf("%s\t%,8d\t%d\n",menuName,orderQty,menuPrice);
			}
				System.out.println("---------------------------------");	
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
			}else {System.out.println("존재하지 않는 메뉴입니다");}
	}

	
	//XX
	@Override
	public void insert() {}
	//X
	@Override
	public void update() {}
	//X
	@Override
	public void delete() {}

}//
