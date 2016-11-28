package dataBase.control;

import java.io.File;
import java.util.ArrayList;

import model.Printer;
import model.PrinterIF;

public class MainTest {

	public static void main(String[] args) {

		DataBaseControl controlTest = new DataBaseControlImpl();
		PrinterIF printerTest = new Printer();

		// test create
		ArrayList<String> coulmnNames = new ArrayList<String>();
		coulmnNames.add("ID");
		coulmnNames.add("Name");
		coulmnNames.add("Age");
		ArrayList<String> coulmnTypes = new ArrayList<String>();
		coulmnTypes.add("int");
		coulmnTypes.add("varchar");
		coulmnTypes.add("int");

		controlTest.createDataBase("mydb2");
		
		controlTest.createTable("students", coulmnNames, coulmnTypes);
		coulmnNames.clear();
		coulmnNames.add("password");
		coulmnNames.add("user");
		coulmnNames.add("Age");
		
		controlTest.createTable("clients", coulmnNames, coulmnTypes);
		
		
		
		// test insert
		ArrayList<String> values = new ArrayList<String>();
		values.add("1234");
		values.add("mohamed ahmed");
		values.add("1");
		controlTest.insertIntoTable(new ArrayList<String>(), values, "clients");
		
		values.set(1,"ahmed");
		values.set(2, "2");
		controlTest.insertIntoTable(new ArrayList<String>(), values, "clients");

		values.set(1,"ali");
		values.set(2, "3");
		controlTest.insertIntoTable(new ArrayList<String>(), values, "clients");

		coulmnNames.remove(2);
		values.remove(2);
		controlTest.insertIntoTable(coulmnNames, values, "clients");
		/*
		
		// update tests
		coulmnNames.clear();
		values.clear();
		coulmnNames.add("password");
		coulmnNames.add("Age");

		values.add("134");
		values.add("40");

		String[] conditions = { "Age", "<", "17" };
		controlTest.updateTable(coulmnNames, values, conditions, "clients");
		*/
		String[] conditions3 = { "Age", "<", "40" };
		ArrayList<String> cols = new ArrayList<String>();
		cols.add("user");
		cols.add("age");
		controlTest.selectFromTable(cols, conditions3, controlTest.getTableName(),null,null);
		controlTest.selectFromTable(cols, conditions3, controlTest.getTableName(),"Age","ASC");
		controlTest.selectFromTable(cols, conditions3, controlTest.getTableName(),"Age","Asc");
		controlTest.selectFromTable(cols, conditions3, controlTest.getTableName(),"Age","DESC");
		controlTest.selectFromTable(cols, conditions3, controlTest.getTableName(),"Age","DesC");

		/*
		// test delete.
		String[] conditions2 = { "Age", ">=", "20" };
		controlTest.deleteFromTable(conditions2, "clients");
		 */
		
		/*
		 * 
		 * // test drop controlTest.dropTable("clients");
		 * controlTest.createTable("clients", coulmnNames, coulmnTypes);
		 * controlTest.dropDataBase("mydb2");
		 * 
		 */
	}
}
