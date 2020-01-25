package com.github.katavasija.bricklink;
import java.sql.*;

public class SqLiteItemKeeper implements IItemKeeper {
	private Connection connection;
	private final String WARNING_HEADER = "SqLiteItemKeeper warning:";
	private final String QUERY_ERROR =  WARNING_HEADER + " error on querying db. ";
	private final String INIT_WARNING = WARNING_HEADER + " error on connecting db. ";
	private final String REFUSAL_SAVING_ID_WARNING = WARNING_HEADER + " refusal to save item - empty itemNo or id";
	private final String NO_SAVE_ID_WARNING = WARNING_HEADER + " error on saving item in db. ";
	private final String EMPTY_ID_WARNING = WARNING_HEADER + " can't find id by itemNo ";


	public SqLiteItemKeeper() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:bricklink.db");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@Override
	public String getItemId(Item item) {
		String query = "SELECT itemId " +
						"FROM item " +
						"WHERE itemNo = '" + item.getItemNo()  + "'";
		int id = 0;

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
			while (resultSet.next()) {
				id = resultSet.getInt("itemId");
				break;
			}
			resultSet.close();
			statement.close();

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			item.appendOperationString(QUERY_ERROR + ex.getMessage());
		}

		if (id > 0) {
			return Integer.toString(id);
		} else {
			return "";
		}
		
	};

	@Override
	public void saveItem(Item item) {
		
		int itemId = item.getItemId();
		String itemNo = item.getItemNo();
		if(itemId <= 0 || StringUtils.IsBlank(itemNo)) {
			item.appendOperationString(REFUSAL_SAVING_ID_WARNING);
		}

		String query = "INSERT INTO item(itemId, itemNo) " + 
						"VALUES (" + item.getItemId() + ",'" + item.getItemNo() +"')";
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			statement.close();
		} catch (Exception ex) {
			// todo logger
			System.out.println(ex.getMessage());
			item.appendOperationString(NO_SAVE_ID_WARNING + ex.getMessage());
		}
	}

	@Override
	public void close() {
		if (this.connection != null) {
			try {
				this.connection.close();
			}
			catch (Exception ex) {
				// todo logger
				System.out.println(ex.getMessage());
			}
		}
	}
}
