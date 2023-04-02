package chinese.main;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chinese.employee.EmployeeDAO;
import chinese.menu.MenuDAO;
import chinese.order.OrderDAO;
import chinese.payment.PaymentDAO;
import chinese.sal.SalDAO;
//main
public class RealPosMain {

	public static void main(String[] args) {
		
		RealPOS empDao = new EmployeeDAO();
		RealPOS menuDao = new MenuDAO();
		RealPOS orderDao = new OrderDAO();
		RealPOS paymentDao = new PaymentDAO();
		RealPOS salDao = new SalDAO();
   
		int sel = 0;
		
		while(true) {
			try {
			Scanner sc = new Scanner(System.in);
			System.out.println("====================REALPOS====================");
			System.out.println("1)직원\t2)메뉴\t3)주문\t4)결제\t5)매출\t0)종료");
			System.out.println("===============================================");
			System.out.print("▶ ");
			sel = sc.nextInt();sc.nextLine();
			
			switch(sel) {
			case 1: empDao.run();break;
				
			case 2: menuDao.run();break;
				
			case 3: orderDao.run();break;
				
			case 4: paymentDao.run();break;
				
			case 5: salDao.run();break;
				
			case 0: System.out.println("시스템이 종료됩니다"); System.exit(0);
			
			default : System.out.println("번호를 다시 선택하세요\n");break;

			}
			}catch(InputMismatchException e) {
				System.out.println("숫자만 입력해주세요");
			}
			
		
		}//while
		
		
	}

}
