package dataBase.control;

import java.io.File;
import java.util.ArrayList;

import javax.management.RuntimeErrorException;

public class DataBaseControlImpl implements DataBaseControl {

	private String currentDataBaseName = null;
	private File currentDataBaseFile = null;

	@Override
	public void createDataBase(String dataBaseName) {
		// TODO Auto-generated method stub
		if (dataBaseName.length() != 0) {
			File x = new File("Data Bases");
			File newDataBase = new File(x.getAbsolutePath(), dataBaseName);
			newDataBase.mkdir();
		}
	}

	@Override
	public void createTable(String tableName, ArrayList<String> columnNames, ArrayList<String> types) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertIntoTable(ArrayList<String> columns, ArrayList<String> values, String tableName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFromTable(String[] conditions, String tableName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectFromTable(ArrayList<String> column, String[] conditions, String tableName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTable(ArrayList<String> columns, ArrayList<String> value, String[] conditions, String tableName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropDataBase(String dataBaseName) {
		File x = new File("Data Bases");
		File dataBasesFolder = new File(x.getAbsoluteFile(), dataBaseName);
		if (dataBasesFolder.exists()) {
			String[] databaseFiles = dataBasesFolder.list();
			for (String s : databaseFiles) {
				System.out.println(s);
				File file = new File(dataBasesFolder, s);
				System.out.println(file.getAbsolutePath());
				file.delete();
			}
			dataBasesFolder.delete();
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public void dropTable(String tableName) {
		// TODO Auto-generated method stub
		try {
			File data = new File(currentDataBaseFile.getAbsolutePath(), tableName + ".xml");
			File schema = new File(currentDataBaseFile.getAbsolutePath(), tableName + ".dtd");
			data.delete();
			schema.delete();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	@Override
	public void changeDataBase(String newDataBaseName) {
		// TODO Auto-generated method stub
		File x = new File("Data Bases");
		this.currentDataBaseName = newDataBaseName;
		this.currentDataBaseFile = new File(x.getAbsoluteFile(), newDataBaseName);
	}

}
