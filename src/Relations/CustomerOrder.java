package Relations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * CustomerOrder class represents the relation between the customers' and their
 * orders
 * 
 * @version 26 January 2022
 * @author Aseel Sabri
 *
 */
public class CustomerOrder {


	private int ID;
	private LocalDate date;
	private double price;
	private double discount;
	private int employeeID;

	/**
	 * Allocates a {@code CustomerOrder} object and initializes it to represent the
	 * specified parameters.
	 * 
	 * @param iD         The ID of the order.
	 * @param date       The date of the order.
	 * @param price      The price of orders' drugs.
	 * @param discount   The discount of the order.
	 * @param employeeID The employee who sold the order.
	 */
	public CustomerOrder(int iD, LocalDate date, double price, double discount, int employeeID) {
		super();
		ID = iD;
		this.date = date;
		this.price = price;
		this.discount = discount;
		this.employeeID = employeeID;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public static int getMaxID() {
		return Integer.parseInt(Queries.queryResult("select ifnull(max(order_ID),0) from c_order;", null).get(0).get(0));
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}

	/**
	 * Read from data base and fill the ArrayList
	 * 
	 */


	public static void insertCustomerOrder(String date, double price, double discount, double paid, int employeeID,
			String customerNID) {
		Queries.queryUpdate("Insert into C_order values (? ,?, ?, ?, ?, ?); ", new ArrayList<>(
				Arrays.asList((getMaxID() + 1) + "", date, price + "", discount + "", employeeID + "", customerNID)));

		Queries.queryUpdate("update Customer set Customer_Debt=customer_Debt+ ? where customer_nid=? ;",
				new ArrayList<>(Arrays.asList((price - discount - paid) + "", customerNID)));
	}

	public static void insertCustomerOrderBatch(String oID, String pID, String productionDate, String expiryDate,
			int amount) {
		Queries.queryUpdate("Insert into C_order_batch values (? ,?, ?, ?, ?);",
				new ArrayList<>(Arrays.asList(oID, pID, productionDate, expiryDate, amount + "")));

		Queries.queryUpdate(
				"update batch set Batch_Amount=Batch_Amount- ? where Product_ID=? and Batch_Production_Date=? "
						+ " and Batch_Expiry_Date=? ;",
				new ArrayList<>(Arrays.asList(amount + "", pID + "", productionDate, expiryDate)));
	}

	/**
	 * Report Customer selling informations on csv file
	 * 
	 * @param path The path of file
	 */
	public static void report(String path) {
		Queries.reportQuery(
				"select C.Customer_Name as 'Customer Name',CO.Order_Date as 'Order Date',E.Employee_Name As 'Employee name', p.product_name as 'Product Name'\r\n"
						+ "from Customer C, C_Order CO, C_Order_Batch COB, Product P, Employee E \r\n"
						+ "where CO.Employee_ID=E.Employee_ID and C.Customer_NID=CO.customer_NID "
						+ " and CO.Order_ID=COB.Order_ID and P.Product_ID=COB.Product_ID;",
				path);
	}
}
