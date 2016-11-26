package dataBase.control;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import model.Printer;
import model.PrinterIF;
import saveAndLoad.SaveAndLoad;
import saveAndLoad.SaveAndLoadImpl;

public class DataBaseControlImpl implements DataBaseControl {
	String currentDataBase, currentTableName;
	ArrayList<ArrayList<String>> currentTableData, wantedData;
	ArrayList<String> coulmnNames, coulmnTypes;
	SaveAndLoad saveAndLoadObj;
	File file;
	PrinterIF printerObj;

	public DataBaseControlImpl() {
		currentDataBase = "";
		currentTableName = "";
		currentTableData = new ArrayList<ArrayList<String>>();
		coulmnNames = new ArrayList<String>();
		coulmnTypes = new ArrayList<String>();
		saveAndLoadObj = new SaveAndLoadImpl();
		wantedData = new ArrayList<ArrayList<String>>();
		printerObj = new Printer();
	}

	@Override
	public void createDataBase(String dataBaseName) {
		if (dataBaseName.length() != 0) {
			File x = new File("Data Bases");
			File newDataBase = new File(x.getAbsolutePath(), dataBaseName);
			if (newDataBase.exists()) {
				currentDataBase = "";
				throw new RuntimeException();
			} else {
				newDataBase.mkdir();
				wantedData = new ArrayList<ArrayList<String>>();
				currentDataBase = dataBaseName;
				coulmnNames = new ArrayList<String>();
				coulmnTypes = new ArrayList<String>();
				currentTableName = "";
			}
		}
	}

	@Override
	public void createTable(String tableName, ArrayList<String> columnNames, ArrayList<String> types) {
		this.currentTableName = tableName;
		File file = makeFile(currentDataBase, tableName, ".xml");
		if (file.exists()) {
			currentTableName = "";
			throw new RuntimeException();
		} else {
			this.coulmnNames = columnNames;
			this.currentTableName = tableName;
			this.coulmnTypes = types;
			this.currentTableData = new ArrayList<ArrayList<String>>();
			saveAndLoadObj.save(file, currentTableData, columnNames, types, tableName);
			setWantedData(this.coulmnNames, this.currentTableData);
			printerObj.printTable(getCoulmnNames(), getWantedData(), getTableName());
		}
	}

	@Override
	public void insertIntoTable(ArrayList<String> columns, ArrayList<String> values, String tableName) {
		ready(tableName);
		if ((columns.size() == 0 && values.size() != coulmnNames.size())
				|| (columns.size() != 0 && columns.size() != values.size())) {
			throw new RuntimeException();
		}
		if (columns.size() == 0) {
			columns = (ArrayList<String>) this.coulmnNames.clone();
		}
		if (!validateCoulmnNames(columns) || !validateDataTypes(columns, values) || !validTableName(tableName)) {
			throw new RuntimeException();
		}
		ArrayList<String> row = new ArrayList<String>();
		for (int i = 0; i < coulmnNames.size(); i++) {
			row.add(new String(" "));
		}
		for (int i = 0; i < coulmnNames.size(); i++) {
			for (int j = 0; j < columns.size(); j++) {
				if (coulmnNames.get(i).equals(columns.get(j))) {
					row.set(i, values.get(j));
					break;
				} else {
					row.set(i, new String(" "));
				}
			}
		}
		currentTableData.add(row);
		saveAndLoadObj.save(file, currentTableData, coulmnNames, coulmnTypes, currentTableName);
		setWantedData(this.coulmnNames, this.currentTableData);
		printerObj.printTable(getCoulmnNames(), getWantedData(), getTableName());
	}

	@Override
	public void deleteFromTable(String[] conditions, String tableName) {
		ready(tableName);
		if (conditions.length == 3) {
			validCondition(conditions);
		}
		if (!validTableName(tableName)) {
			throw new RuntimeException();
		}

		ArrayList<Integer> indexes = makeConditions(conditions);
		for (int i = indexes.size() - 1; i >= 0; i--) {
			currentTableData.remove((int) indexes.get(i));
		}
		saveAndLoadObj.save(file, currentTableData, coulmnNames, coulmnTypes, currentTableName);
		setWantedData(this.coulmnNames, this.currentTableData);
		printerObj.printTable(getCoulmnNames(), getWantedData(), getTableName());
	}

	@Override
	public void selectFromTable(ArrayList<String> column, String[] conditions, String tableName) {

		ready(tableName);
		if (conditions.length == 3) {
			validCondition(conditions);
		}
		if (column.size() == 0) {
			column = (ArrayList<String>) this.coulmnNames.clone();
		}
		if (!validateCoulmnNames(column) || !validTableName(tableName)) {
			throw new RuntimeException();
		}

		ArrayList<Integer> colIndex = getColIndex(column);
		ArrayList<Integer> indexes = makeConditions(conditions);
		ArrayList<ArrayList<String>> selectedData = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < indexes.size(); i++) {
			ArrayList<String> row = currentTableData.get(indexes.get(i));
			ArrayList<String> rowSelectedData = new ArrayList<String>();
			for (int j = 0; j < colIndex.size(); j++) {
				rowSelectedData.add(row.get(colIndex.get(j)));
			}
			selectedData.add(rowSelectedData);
		}
		setWantedData(column, selectedData);
		printerObj.printTable(getCoulmnNames(), getWantedData(), getTableName());
	}

	@Override
	public void updateTable(ArrayList<String> columns, ArrayList<String> value, String[] conditions, String tableName) {
		ready(tableName);
		if (conditions.length == 3) {
			validCondition(conditions);
		}

		if ((columns.size() != value.size()) || !validateCoulmnNames(columns) || !validateDataTypes(columns, value)
				|| !validTableName(tableName)) {
			throw new RuntimeException();
		}
		ArrayList<Integer> indexes = makeConditions(conditions);
		for (int k = 0; k < indexes.size(); k++) {
			ArrayList<String> row = currentTableData.get(indexes.get(k));
			for (int i = 0; i < coulmnNames.size(); i++) {
				for (int j = 0; j < columns.size(); j++) {
					if (coulmnNames.get(i).equals(columns.get(j))) {
						row.set(i, value.get(j));
						break;
					}
				}
			}
			currentTableData.set(indexes.get(k), row);
		}
		saveAndLoadObj.save(file, currentTableData, coulmnNames, coulmnTypes, currentTableName);
		setWantedData(this.coulmnNames, this.currentTableData);
		printerObj.printTable(getCoulmnNames(), getWantedData(), getTableName());

	}

	@Override
	public void dropDataBase(String dataBaseName) {
		File x = new File("Data Bases");
		File dataBasesFolder = new File(x.getAbsoluteFile(), dataBaseName);
		if (dataBasesFolder.exists()) {
			String[] databaseFiles = dataBasesFolder.list();
			for (String s : databaseFiles) {
				File file = new File(dataBasesFolder, s);
				file.delete();
			}
			dataBasesFolder.delete();
			wantedData = new ArrayList<ArrayList<String>>();
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public void dropTable(String tableName) {
		try {
			File data = makeFile(currentDataBase, tableName, ".xml");
			data.delete();
			File schema = makeFile(currentDataBase, tableName, ".dtd");
			schema.delete();
			wantedData = new ArrayList<ArrayList<String>>();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	@Override
	public void changeDataBase(String newDataBaseName) {
		File x = new File("Data Bases");
		File dataBaseFolder = new File(x.getAbsoluteFile(), newDataBaseName);
		if (dataBaseFolder.exists()) {
			currentDataBase = newDataBaseName;
			wantedData = new ArrayList<ArrayList<String>>();
		} else {
			throw new RuntimeException();
		}
	}

	private File makeFile(String dataBaseName, String tableName, String extension) {
		if (this.currentDataBase == "" || this.currentTableName == "") {
			throw new RuntimeException();
		}
		File file = new File("Data Bases");
		String path = file.getAbsolutePath() + File.separator + dataBaseName + File.separator + tableName + extension;
		File filee = new File(path);
		return filee;
	}

	private ArrayList<Integer> makeConditions(String[] conditions) {
		ArrayList<Integer> indexes = new ArrayList<Integer>();

		// handle if no conditions is existing then retun all the rows
		if (conditions.length == 0) {
			for (int i = 0; i < currentTableData.size(); i++) {
				indexes.add(i);
			}
			return indexes;
		}
		String coulmn = conditions[0];
		coulmn = coulmn.toLowerCase();
		ArrayList<String> myCoulmnNames = toLow((ArrayList<String>) this.coulmnNames.clone());
		int indexOfCoulmn = myCoulmnNames.indexOf(coulmn);
		String operator = conditions[1];
		String value = conditions[2];
		for (int i = 0; i < currentTableData.size(); i++) {
			if (isMatch(currentTableData.get(i).get(indexOfCoulmn), operator, value)) {
				indexes.add(i);
			}
		}
		return indexes;
	}

	private boolean isMatch(String data, String operator, String value) {
		String[] strings = new String[2];
		strings[0] = data;
		strings[1] = value;
		Arrays.sort(strings);
		if (operator == "=") {
			return data.equals(value);
		} else if (operator == "<>" || operator == "!=") {
			return (!data.equals(value));
		} else if (operator == ">") {
			return (strings[0].equals(value) && !value.equals(data));
		} else if (operator == "<") {
			return (strings[1].equals(value) && !value.equals(data));
		} else if (operator == ">=") {
			return (data.equals(value) || strings[0].equals(value));
		} else if (operator == "<=") {
			return (data.equals(value) || strings[1].equals(value));
		}
		return false;
	}

	private boolean validateCoulmnNames(ArrayList<String> coulmnNames) {
		ArrayList<String> coulmnNames1 = toLow((ArrayList<String>) coulmnNames.clone());
		ArrayList<String> mycoulmnNames = toLow((ArrayList<String>) this.coulmnNames.clone());
		for (int i = 0; i < coulmnNames1.size(); i++) {
			if (!mycoulmnNames.contains(coulmnNames1.get(i))) {
				return false;
			}
		}
		return true;
	}

	private boolean validateDataTypes(ArrayList<String> coulmnNames, ArrayList<String> coulmnValues) {
		ArrayList<String> coulmnNames1 = toLow((ArrayList<String>) coulmnNames.clone());
		ArrayList<String> mycoulmnNames = toLow((ArrayList<String>) this.coulmnNames.clone());
		if (coulmnNames1.size() == 0) {
			coulmnNames1 = (ArrayList<String>) mycoulmnNames.clone();
		}
		for (int i = 0; i < coulmnValues.size(); i++) {
			if (coulmnTypes.get(mycoulmnNames.indexOf(coulmnNames1.get(i))).equals("varchar")) {
				continue;
			}
			else {
				String str = coulmnValues.get(i);
				for (int j = 0; j < str.length(); j++) {
					if (!Character.isDigit(str.charAt(j))) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean validTableName(String tableName) {
		tableName = tableName.toLowerCase();
		File file = new File("Data Bases");
		File folderOfDataBase = new File(file.getAbsolutePath(), currentDataBase);
		folderOfDataBase.mkdir();
		String[] tables = folderOfDataBase.list();
		for (int i = 0; i < tables.length; i++) {
			tables[i] = tables[i].toLowerCase();
			if (tables[i].indexOf(tableName) == 0) {
				return true;
			}
		}
		return false;
	}

	private void ready(String tableName) {
		if (!validTableName(tableName)) {
			throw new RuntimeException();
		}
		currentTableName = tableName;
		file = makeFile(currentDataBase, currentTableName, ".xml");
		currentTableData = saveAndLoadObj.load(file);
		coulmnNames = saveAndLoadObj.getCoulmnNames();
		coulmnTypes = saveAndLoadObj.getCoulmnTypes();
	}

	private ArrayList<Integer> getColIndex(ArrayList<String> columns) {
		ArrayList<Integer> colIndex = new ArrayList<Integer>();
		ArrayList<String> temp1 = toLow(this.coulmnNames);
		ArrayList<String> temp2 = toLow(columns);
		for (int i = 0; i < columns.size(); i++) {
			colIndex.add(temp1.indexOf(temp2.get(i)));
		}
		return colIndex;
	}

	private void validCondition(String[] conditions) {
		ArrayList<String> str1 = new ArrayList<String>();
		str1.add(conditions[0]);
		ArrayList<String> str2 = new ArrayList<String>();
		str2.add(conditions[2]);

		str1 = toLow(str1);
		// str2 = toLow(str2);
		if (!validateCoulmnNames(str1) || !validateDataTypes(str1, str2)) {
			throw new RuntimeException();
		}
	}

	@Override
	public ArrayList<ArrayList<String>> getWantedData() {
		return wantedData;
	}

	public void setWantedData(ArrayList<String> coulmnNames, ArrayList<ArrayList<String>> tableData) {
		wantedData = new ArrayList<ArrayList<String>>();
		wantedData.add((ArrayList<String>) this.coulmnNames.clone());
		for (int i = 0; i < tableData.size(); i++) {
			wantedData.add(tableData.get(i));
		}
	}

	public ArrayList<String> getCoulmnNames() {
		return coulmnNames;
	}

	public String getTableName() {
		return currentTableName;
	}

	private ArrayList<String> toLow(ArrayList<String> x) {
		ArrayList<String> x2 = (ArrayList<String>) x.clone();
		for (int i = 0; i < x.size(); i++) {
			x2.set(i, x2.get(i).toLowerCase());
		}
		return (ArrayList<String>) x2.clone();
	}

}
