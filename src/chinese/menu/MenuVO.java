package chinese.menu;

public class MenuVO {
	
	private String menuName;
	private int    menuPrice;
	private String menuType;
	
	
	public MenuVO() {}
 
	public MenuVO(String menuName, int menuPrice, String menuType) {
		super();
		this.menuName = menuName;
		this.menuPrice = menuPrice;
		this.menuType = menuType;
	}

	

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public int getMenuPrice() {
		return menuPrice;
	}

	public void setMenuPrice(int menuPrice) {
		this.menuPrice = menuPrice;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	@Override
	public String toString() {
		return "메뉴이름" + menuName + " 메뉴가격" + menuPrice + " 메뉴종류" + menuType;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
