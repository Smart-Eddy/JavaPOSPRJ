package chinese.main;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;



public interface RealPOS {
	
	public static final String POSNAME = "RealPOS";
	public static final Scanner sc = new Scanner(System.in);
	
	
	
	
	public void run();
	//C
	public void insert();
	//R
	public List select();
	//U
	public void update();
	//D
	public void delete();
}
