package dataBase.control;

import java.util.ArrayList;

public interface DataBaseControl {

	// arsanuos
	// create database OK
	// insertIntoTable OK
	// seletctFromTable
	// drop table OK
	// drop database OK

	// amr
	// create table
	// deleteFromTable
	// updateTable
	// changeDataBase OK
	/**
	 * create new data base
	 * 
	 * @param dataBaseName
	 */
	public void createDataBase(String dataBaseName);

	/**
	 * create table
	 * 
	 * @param tableName
	 * @param columnNames
	 * @param types
	 */
	public void createTable(String tableName, ArrayList<String> columnNames, ArrayList<String> types);

	/**
	 * insert into a specific row some data.
	 * 
	 * @param columns
	 * @param values
	 * @param tableName
	 */
	public void insertIntoTable(ArrayList<String> columns, ArrayList<String> values, String tableName);

	/**
	 * delete data form table
	 * 
	 * @param conditions
	 *            if condition is empty then delete all.
	 * @param tableName
	 */
	public void deleteFromTable(String[] conditions, String tableName);

	/**
	 * select data form table
	 * 
	 * @param column
	 *            columns names if (empty or *) then all
	 * @param conditions
	 *            2D array of size 3 (i.e column name,operation,value)
	 *            operations mean > < =
	 * @param tableName
	 */
	public ArrayList<ArrayList<String>> selectFromTable(ArrayList<String> column, String[] conditions,
			String tableName);

	/**
	 * update a specific row in table
	 * 
	 * @param columns
	 * @param conditions
	 * @param tableName
	 */
	public void updateTable(ArrayList<String> columns, ArrayList<String> value, String[] conditions, String tableName);

	/**
	 * delete dataBase
	 * 
	 * @param dataBaseName
	 */
	public void dropDataBase(String dataBaseName);

	/**
	 * delete dataBase.
	 * 
	 * @param tableName
	 */
	public void dropTable(String tableName);

	/**
	 * change dataBase name.
	 * 
	 * @param newDataBaseName
	 */
	public void changeDataBase(String newDataBaseName);
	
	public ArrayList<ArrayList<String>> getWantedData();
}
