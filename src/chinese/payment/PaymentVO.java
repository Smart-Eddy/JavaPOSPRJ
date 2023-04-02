package chinese.payment;

import java.sql.Date;

public class PaymentVO {
	private int payNo;
	private int payMoney;
	private String payMethod;
	private Date  payDay;
	
	
	
	public PaymentVO() {}
	public PaymentVO(int payNo, int payMoney, String payMethod, Date payDay) {
		super();
		this.payNo = payNo;
		this.payMoney = payMoney;
		this.payMethod = payMethod;
		this.payDay = payDay;
	}
	
	public int getPayNo() {
		return payNo;
	}
	public void setPayNo(int payNo) {
		this.payNo = payNo;
	}

	public int getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(int payMoney) {
		this.payMoney = payMoney;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public Date getPayDay() {
		return payDay;
	}
	public void setPayDay(Date payDay) {
		this.payDay = payDay;
	}
	@Override
	public String toString() {
		return "결제번호:"+ payNo + "  결제금액: " +payMoney+ "  결제수단:"
				+ payMethod + "  결제일시:" + payDay ;
	}
	
	
	
	
	
	
}
