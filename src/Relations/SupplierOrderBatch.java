package Relations;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * SupplierOrderBatch represents the details of each supplier's order
 * 
 * @version 12 January 2022
 * @author Loor Sawalhi
 */
public class SupplierOrderBatch {
	private static ArrayList<SupplierOrderBatch> data = new ArrayList<SupplierOrderBatch>();
	private static ObservableList<SupplierOrderBatch> dataList;
	private int OID;
	private int PID;
	private LocalDate productionDate;
	private LocalDate expiryDate;
	private int amount;

	/**
	 * SupplierOrderBatch Constructor
	 * 
	 * @param oID
	 * @param pID
	 * @param productionDate
	 * @param expiryDate
	 * @param amount
	 */
	public SupplierOrderBatch(int oID, int pID, LocalDate productionDate, LocalDate expiryDate, int amount) {
		super();
		OID = oID;
		PID = pID;
		this.productionDate = productionDate;
		this.expiryDate = expiryDate;
		this.amount = amount;
	}

	public int getOID() {
		return OID;
	}

	public void setOID(int oID) {
		OID = oID;
	}

	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}

	public LocalDate getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(LocalDate productionDate) {
		this.productionDate = productionDate;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public static ObservableList<SupplierOrderBatch> getDataList() {
		return dataList;
	}

	public static void setDataList(ObservableList<SupplierOrderBatch> dataList) {
		SupplierOrderBatch.dataList = dataList;
	}

	public static ArrayList<SupplierOrderBatch> getData() {
		return data;
	}

	public static void setData(ArrayList<SupplierOrderBatch> data) {
		SupplierOrderBatch.data = data;
	}

	public static void getSupplierOrderBatchData() throws ClassNotFoundException, SQLException, ParseException {

		data.clear();
		ArrayList<ArrayList<String>> table = Queries.queryResult("select * from S_order_batch;", null);

		for (int i = 0; i < table.size(); i++) {

			LocalDate writingDate = LocalDate.parse(table.get(i).get(2));
			LocalDate cashingDate = LocalDate.parse(table.get(i).get(3));
			
			SupplierOrderBatch temp = new SupplierOrderBatch(Integer.parseInt(table.get(i).get(0)), 
					Integer.parseInt(table.get(i).get(1)), writingDate,
					cashingDate, Integer.parseInt(table.get(i).get(4)));

			data.add(temp);
		}

		dataList = FXCollections.observableArrayList(data);
	}

	public static ArrayList<SupplierOrderBatch> getSupplierOrderBatchData(ArrayList<ArrayList<String>> table)
			throws ClassNotFoundException, SQLException, ParseException {

		ArrayList<SupplierOrderBatch> tempData = new ArrayList<SupplierOrderBatch>();
		Connection getPhoneConnection = Queries.dataBaseConnection();

		for (int i = 0; i < table.size(); i++) {
			LocalDate writingDate = LocalDate.parse(table.get(i).get(2));
			LocalDate cashingDate = LocalDate.parse(table.get(i).get(3));
			SupplierOrderBatch temp = new SupplierOrderBatch(Integer.parseInt(table.get(i).get(0)), 
					Integer.parseInt(table.get(i).get(1)), writingDate,
					cashingDate, Integer.parseInt(table.get(i).get(4)));

			tempData.add(temp);
		}

		getPhoneConnection.close();
		return tempData;
	}

	public static void insertSupplierOrderBatch(String oID, String pID, LocalDate productionDate, LocalDate expiryDate,
			int amount) {
		try {
			Queries.queryUpdate("Insert into S_order_batch values (?, ?, ?, ?, ?);", new ArrayList<>(
					Arrays.asList(oID, pID, productionDate.toString(), expiryDate.toString(), amount + "")));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}