package dataBase.control;

import java.io.File;
import java.util.ArrayList;

public class MainTest {

	public static void main(String[] args) {

		DataBaseControl controlTest = new DataBaseControlImpl();

		// test create
		ArrayList<String> coulmnNames = new ArrayList<String>();
		coulmnNames.add("ID");
		coulmnNames.add("Name");
		coulmnNames.add("Age");
		ArrayList<String> coulmnTypes = new ArrayList<String>();
		coulmnTypes.add("int");
		coulmnTypes.add("string");
		coulmnTypes.add("int");

		controlTest.createDataBase("mydb2");
		controlTest.createTable("students", coulmnNames, coulmnTypes);
		coulmnNames.clear();
		coulmnNames.add("password");
		coulmnNames.add("user");
		coulmnNames.add("Age");
		controlTest.createTable("clients", coulmnNames, coulmnTypes);

		// test insert (OKKKKK)
		ArrayList<String> values = new ArrayList<String>();
		values.add("1234");
		values.add("mohamed ahmed");
		values.add("12");
		controlTest.insertIntoTable(coulmnNames, values, "clients");
		values.set(2, "18");
		controlTest.insertIntoTable(coulmnNames, values, "clients");
		values.set(2, "34");
		controlTest.insertIntoTable(coulmnNames, values, "clients");
		coulmnNames.remove(2);
		values.remove(2);
		controlTest.insertIntoTable(coulmnNames, values, "clients");

		// update tests
		coulmnNames.clear();
		values.clear();
		coulmnNames.add("password");
		coulmnNames.add("user");
		coulmnNames.add("Age");
		
		values.add("134");
		values.add("ahemd ahmed ghaly");
		values.add("40");
		
		String[] conditions = { "Age", ">", "34" };
		controlTest.updateTable(coulmnNames, values, conditions, "clients");
		// test delete.
		String[] conditions2 = { "Age", "=", "40" };
		controlTest.deleteFromTable(conditions2, "clients");
		String[] conditions3 = { "Age", "<", "12" };
		ArrayList<ArrayList<String>> x = controlTest.selectFromTable(coulmnNames, conditions3, "clients");
		
		// test drop
		// controlTest.dropTable("clients");
		// controlTest.createTable("clients", coulmnNames, coulmnTypes);
		 controlTest.dropDataBase("mydb2");

	}

}
