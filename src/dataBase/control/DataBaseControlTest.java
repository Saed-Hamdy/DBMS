package dataBase.control;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class DataBaseControlTest {
	
	DataBaseControl dataBaseControlObj = new DataBaseControlImpl(); 
	@Test
	public void testCreateAndInsert() {
		dataBaseControlObj.createDataBase("db2");
		
		ArrayList<String> colNames = new ArrayList<String>();
		colNames.add("ID");
		colNames.add("Name");
		ArrayList<String> types = new ArrayList<String>();
		types.add("int");
		types.add("varchar");
		dataBaseControlObj.createTable("clients", colNames, types);
		
		assertEquals("clients", dataBaseControlObj.getTableName());
		assertArrayEquals(colNames.toArray(), dataBaseControlObj.getCoulmnNames().toArray());
		ArrayList<String> values = new ArrayList<String>();
		values.add("12");
		values.add("Mohamed Ahmed");
		/*
		dataBaseControlObj.insertIntoTable(colNames, values, "db2");
		ArrayList<ArrayList<String>> storedData = new ArrayList<ArrayList<String>>();
		storedData.add(colNames);
		storedData.add(values);
		//assertArrayEquals(storedData.toArray(), dataBaseControlObj.getWantedData().toArray());		
		*/
	}

}
