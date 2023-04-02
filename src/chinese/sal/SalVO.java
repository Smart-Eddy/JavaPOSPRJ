package chinese.sal;

import java.sql.Date;

public class SalVO {
	private int salesNo;
	private int orderNo;
	private int orderPrice;
	private int tableNo;
	private String menuName;
	private int orderQty;
	private Date orderDate;
	
	
	
	public SalVO() {}
	public SalVO(int salesNo, int orderNo, int orderPrice, int tableNo, String menuName, int orderQty, Date orderDate) {
		super();
		this.salesNo = salesNo;
		this.orderNo = orderNo;
		this.orderPrice = orderPrice;
		this.tableNo = tableNo;
		this.menuName = menuName;
		this.orderQty = orderQty;
		this.orderDate = orderDate;
		
		
		
		
		
	}
	public int getSalesNo() {
		return salesNo;
	}
	public void setSalesNo(int salesNo) {
		this.salesNo = salesNo;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public int getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(int orderPrice) {
		this.orderPrice = orderPrice;
	}
	public int getTableNo() {
		return tableNo;
	}
	public void setTableNo(int tableNo) {
		this.tableNo = tableNo;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public int getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(int orderQty) {
		this.orderQty = orderQty;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	@Override
	public String toString() {
		return "SalVO [salesNo=" + salesNo + ", orderNo=" + orderNo + ", orderPrice=" + orderPrice + ", tableNo="
				+ tableNo + ", menuName=" + menuName + ", orderQty=" + orderQty + ", orderDate=" + orderDate + "]";
	}
	
	
	
	
}
