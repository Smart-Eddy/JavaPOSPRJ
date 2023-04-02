//결제
package chinese.payment;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import chinese.main.JdbcTemplet;
import chinese.main.RealPOS;
import chinese.order.OrderDAO;
import chinese.order.OrderVO;
import chinese.sal.SalDAO;

public class PaymentDAO implements RealPOS{
	
	private JdbcTemplet jdbcTemplet = new JdbcTemplet();
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	
	@Override
	public void run() {
		int run = 1;
		while (run == 1) {
			System.out.println("======================<결제관리>=======================");
			System.out.println("1)결제하기\t2)결제내역\t3)결제취소\t0)이전\t");
			System.out.println("=======================================================");
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
	
	
	
	
	//결제하기
	@Override
	public void insert() {
		conn = jdbcTemplet.connDB();
		List<OrderVO> list = new ArrayList<>();
		int supplyPrice = 0;
		int vat = 0;
		int totalPrice = 0;
	 	boolean result = false;
	 	int table = 0;
	 	int totalSearch =0;
	 	String paymentMethod = null;
		OrderDAO orderDao = new OrderDAO();
		
		
		System.out.println(" 결제할 테이블번호를 입력하세요");
		System.out.print("▶ ");
		table = sc.nextInt();sc.nextLine();
		result = orderDao.tableSearch(table);
		if(result == true) {		
		
		
		 try {
			
			 conn.setAutoCommit(false);
			 Savepoint  savePoint1 = conn.setSavepoint();
		
			 String sql = "insert into  payment(pay_no,pay_money,pay_method,pay_day) values(SEQ_PAYMENT.nextval,(\r\n" + 
				"                                                                                          select sum(order_price)  \r\n" + 
				"                                                                                          from  orders  \r\n" + 
				"                                                                                          where  table_no = ? \r\n" + 
				"                                                                                          ), ?, sysdate)";
		 		    
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,table);
			System.out.println("결제방식 입력하세요(카드/현금)");
			System.out.print("▶ ");
			paymentMethod = sc.nextLine(); 
		    if(paymentMethod.equals("카드")||paymentMethod.equals("현금")) {
			pstmt.setString(2,paymentMethod);
		    
			pstmt.executeUpdate();
			pstmt.clearParameters();
			
			
			String sql2 = "select menu_name as 메뉴명,\r\n" + 
					"        (order_price/order_qty) as 단가,\r\n" + 
					"        order_qty 수량,\r\n" + 
					"        order_price\r\n" + 
						   "from orders\r\n" + 
						   "where table_no = ?";
	
			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1, table);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				OrderVO vo = new OrderVO();
     			String menuName   = rs.getString(1);
     			int    menuPrice  = rs.getInt(2);
     			int    orderQty   = rs.getInt(3);
     			int    orderPrice = rs.getInt(4);
     			
     			vo.setMenuName(menuName);
     			vo.setMenuPrice(menuPrice);
     			vo.setOrderQty(orderQty);
     			vo.setOrderPrice(orderPrice);
      			    		
     			list.add(vo);	
     			for(int i=0; i<list.size(); i++) {
     			totalSearch += (list.get(i).getMenuPrice() * list.get(i).getOrderQty());
     			}
			}
			
			if(totalSearch > 0) {
				
				System.out.println("<영수증>-------------------------");
				System.out.println("품명\t단가\t수량\t금액");
				System.out.println("---------------------------------");
				
				for(int i=0; i<list.size(); i++) {
				System.out.printf("%s\t%,6d\t%3d\t%,6d원\n",
				list.get(i).getMenuName(), list.get(i).getMenuPrice(),
				list.get(i).getOrderQty(), list.get(i).getOrderPrice());		
				
				totalPrice += (list.get(i).getMenuPrice() * list.get(i).getOrderQty());
				supplyPrice = (int)(list.get(i).getOrderPrice() / 1.1);
				vat = list.get(i).getOrderPrice() - supplyPrice;
				
				} 
				System.out.println("---------------------------------");
				System.out.printf("공급가액\t\t%,6d원\n", supplyPrice);
				System.out.printf("부가세액\t\t%,6d원\n", vat);
				System.out.println("---------------------------------");
				System.out.printf("청구금액\t\t%,6d원\n", totalPrice);	
				System.out.println();
				
				SalDAO salDao = new SalDAO();
				salDao.autoSalInsert(table);
				conn.commit();conn.setAutoCommit(true);		
			
				}else if(totalSearch == 0) {
				conn.rollback(savePoint1);
				System.out.println("결제 승인거부 되었습니다");
				}
			
			
			pstmt.clearParameters();
		    String sql3 = "DELETE FROM orders WHERE table_no = ?";
		    pstmt = conn.prepareStatement(sql3);
		    pstmt.setInt(1, table); 
			pstmt.executeUpdate();
        
			pstmt.close();
			conn.close();
	    }else {System.out.println("결제는 카드/현금 만 가능합니다");}	
		} catch (SQLException e) {
			e.printStackTrace();
			
		} catch(InputMismatchException e) {
			System.out.println("테이블 번호로 입력해주세요");
			
		}
		}else {System.out.println("존재하지 않는 테이블입니다");}
		
	}
	

	//출력 결제
	@Override
		public List<PaymentVO> select() {
			conn = jdbcTemplet.connDB();
			List<PaymentVO> list = new ArrayList<PaymentVO>();
			String sql = "select * from payment order by pay_no desc";
			try {
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while(rs.next()) {
				PaymentVO vo = new PaymentVO();						 
							
				int payNo = rs.getInt("pay_no");
				int payMoney = rs.getInt("pay_money");
				String payMethod = rs.getString("pay_method");
				Date payDay = rs.getDate("pay_day");
					
				vo.setPayNo(payNo);
				vo.setPayMoney(payMoney);
				vo.setPayMethod(payMethod);
				vo.setPayDay(payDay);
					
				list.add(vo);
				}
				
				System.out.println("----------------------------------------------------------");
				System.out.println("결제번호\t결제금액\t결제수단\t결제일시");
				System.out.println("----------------------------------------------------------");
				
				for(int i=0; i<list.size(); i++) {
				System.out.printf("%8d\t%,8d\t%5s\t%17s\n",
				list.get(i).getPayNo(), list.get(i).getPayMoney(),
				list.get(i).getPayMethod(), list.get(i).getPayDay());		
				
				} 
				System.out.println("----------------------------------------------------------");
				
				
				

				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch(InputMismatchException e) {
				System.out.println("입력 형식예외 입니다.");
			}
			
			return list;
		}

	//결제취소
	@Override
	public void delete() {
		int result = 0;
		conn = jdbcTemplet.connDB();
		String sql = "delete from payment where pay_no = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			System.out.println("결제번호를 입력하세요");
			System.out.print("▶ ");
			pstmt.setInt(1, sc.nextInt());sc.nextLine();
			result = pstmt.executeUpdate();
			if(result >= 1)
			System.out.println("결제가 취소되었습니다.");
			else {System.out.println("존재하지 않는 결제번호 입니다");}
			
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}  catch(InputMismatchException e) {
			System.out.println("입력 형식예외 입니다.");
		}
		
	}
	//
	@Override
	public void update() {}
	

}