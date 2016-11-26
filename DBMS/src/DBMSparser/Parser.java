package DBMSparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataBase.control.DataBaseControlImpl;
import model.DBMS;

public class Parser implements IParser {
	/*
	 * INSERT INTO table_name VALUES (value1,value2,value3,...);
	 */
	/*
	 * DROP DATABASE database_name
	 *
	 */

	/*
	 * DELETE FROM table_name WHERE some_column=some_value;
	 */

	/*
	 * SELECT column_name,column_name FROM table_name;
	 */

	/*
	 * UPDATE table_name SET column1=value1,column2=value2,... WHERE
	 * some_column=some_value;
	 */
	private DataBaseControlImpl Dpms = new DataBaseControlImpl();

	private void print(String str) {

		System.out.println(str);
	}

	private void error() {

		print("error");
		return;
	}

	/**
	 * take a Query and check if it is a valid operation then call the operation
	 * method.
	 * 
	 * @param query
	 */

	@Override
	public void InsertQuery(String query) {

		if (query == null || query.replaceAll("^\\s*", "").equals("")) {
			error();
			return;
		}
		ArrayList<String> words = new ArrayList<String>(Arrays.asList(query.toLowerCase().split("\\s+")));
		while (words.remove(""))
			;
		switch (words.get(0).toLowerCase()) {
		case "select":
			Select(query);
			break;

		case "drop":
			if (words.get(1).equals("table")) {
				DropTable(query);
			} else if (words.get(1).equals("database")) {
				DropDataBase(query);
			} else {
				error();
			}

			break;
		case "delete":
			Delete(query);
			;
			break;
		case "update":
			Update(query);

			break;
		case "use":
			useDatabase(query);
			break;
		case "insert":
			Insert(query);
			break;
		case "create":
			if (words.get(1).equals("table")) {
				CreateTable(query);
			} else if (words.get(1).equals("database")) {
				CreateDataBase(query);
			} else {
				error();
				return;
			}
			break;
		default:
			error();

			break;
		}
	}

	public void CreateDataBase(String query) {
		// String database_name = new String();
		Pattern pat = Pattern.compile("^(\\s*)((?i)create)(\\s+)((?i)database)(\\s+)(\\w+)(\\s*);?(\\s*)$");

		Matcher ma = pat.matcher(query);

		if (ma.matches()) {
			// database_name = ma.group(6);
			Dpms.createDataBase(ma.group(6));

		} else {
			error();
		}

	}

	public void CreateTable(String query) {
		String table_name = new String();
		ArrayList<String> coloums = new ArrayList<String>();
		ArrayList<String> value = new ArrayList<String>();
		Pattern pat = Pattern.compile("^(\\s*)((?i)create)(\\s+)((?i)table)(\\s+)(\\w+)(\\s*)"
				+ "\\((((\\s*)(\\w+)(\\s+)((?i)varchar|(?i)int)(\\s*),)*((\\s*)(\\w+)(\\s+)((?i)varchar|(?i)int)"
				+ "(\\s*))\\)(\\s*))(\\s*);?(\\s*)$");

		Matcher ma = pat.matcher(query);
		if (ma.matches()) {
			table_name = ma.group(6);
			ArrayList<String> NameAndType = new ArrayList<String>(
					Arrays.asList(ma.group(8).replaceAll("[()]", "").split(",")));
			int size = NameAndType.size();
			for (int i = 0; i < size; i++) {
				ArrayList<String> temp = new ArrayList<String>(Arrays.asList(NameAndType.get(i).split("\\s+")));
				if (temp.get(0).equals(""))
					temp.remove(0);
				coloums.add(temp.get(0));
				value.add(temp.get(1));

			}
			Dpms.createTable(table_name, coloums, value);
		}

	}

	public void Insert(String query) {
		String table_name = new String();
		ArrayList<String> coloums = new ArrayList<String>();
		ArrayList<String> value = new ArrayList<String>();
		Pattern pat = Pattern.compile("^(\\s*)((?i)insert)(\\s+)((?i)into)(\\s+)(\\w+)"
				+ "((\\s*)\\(((\\s*)(\\w+)(\\s*),)*((\\s*)(\\w+)(\\s*)\\)(\\s*)))?" + "((\\s+)((?i)values))(\\s*)\\("
				+ "(((\\s*)(('[^']*')|(\\d+))(\\s*),)*((\\s*)(('[^']*')|(\\d+))))(\\s*)\\)(\\s*)(\\s*);?(\\s*)$");
		/*
		 * INSERT INTO TABLE_NAME (column1, column2, column3,...columnN) VALUES
		 * (value1, value2, value3,...valueN);
		 */
		Matcher ma = pat.matcher(query.replaceAll("\\)(?i)values", "\\) values"));
		if (ma.matches()) {
			// print("mat");
			table_name = ma.group(6);
			if (ma.group(7) == null)
				coloums = new ArrayList<String>();
			else
				coloums = new ArrayList<String>(
						Arrays.asList(ma.group(7).replaceAll("\\s+", "").replaceAll("[()]", "").split(",")));
			String trim = ma.group(22) + ",";
			while (!trim.replaceAll("\\s+", "").replaceAll("[,]", "").equals("")) {
				Pattern patt = Pattern.compile("('(\\s*[^']+\\s*)')|(\\s*(\\d+)\\s*,)");
				Matcher matc = patt.matcher(trim);

				if (matc.find()) {

					if (trim.charAt(matc.start()) == '\'') {
						value.add(trim.substring(matc.start() + 1, matc.end() - 1));
					} else {
						value.add(trim.substring(matc.start(), matc.end() - 1));

					}
					trim = trim.substring(matc.end());
				}

			}
			Dpms.insertIntoTable(coloums, value, table_name);
		} else {
			error();
		}

	}

	public void useDatabase(String query) {
		String usePattern = "^\\s*((?i)use)\\s+(\\w+)\\s*;?\\s*$";
		Pattern pat = Pattern.compile(usePattern);
		Matcher ma = pat.matcher(query);
		if (!ma.matches()) {
			error();
			return;
		}
		 Dpms.changeDataBase(ma.group(2));

	}

	public void DropDataBase(String query) {
		String usePattern = "^\\s*((?i)drop)\\s+((?i)database)\\s+(\\w+)\\s*;?\\s*$";
		Pattern pat = Pattern.compile(usePattern);
		Matcher ma = pat.matcher(query);
		if (!ma.find()) {
			error();
			return;
		}
		Dpms.dropDataBase(ma.group(3));

	}

	public void DropTable(String query) {
		String DropPattern = "^\\s*((?i)drop)\\s+((?i)table)\\s+(\\w+)\\s*;?\\s*$";
		Pattern pat = Pattern.compile(DropPattern);
		Matcher ma = pat.matcher(query);
		if (!ma.find()) {
			error();
			return;
		}
		Dpms.dropTable(ma.group(3));

	}

	public void Delete(String query) {
		// String table_name = new String();
		Pattern pat = Pattern.compile("^(\\s*)((?i)delete)(\\s+)((?i)from)(\\s+)(\\w+)"
				+ "(\\s+((?i)where)\\s+((\\w+)(\\s*)(>|<|=|>=|<=|<>)\\s*(('[^']*')|(\\d+))))?\\s*;?$");
		Matcher ma = pat.matcher(query);
		if (ma.matches()) {

			Dpms.deleteFromTable(Where((ma.group(9))), ma.group(6));
		} else {
			error();
		}

	}

	public void Select(String query) {
		/*
		 * SELECT column_name,column_name FROM table_name;
		 */
		query = query.replaceFirst("^\\s*((?i)select\\s*\\*)", "select * ");
		ArrayList<String> colomsName = new ArrayList<>();
		String tableName;
		String selectPattern = "^\\s*((?i)select\\s+)"
				+ "((\\*\\s*)|((\\s*(\\w+)\\s*,)*(\\s*(\\w+)\\s+)))\\s*((?i)from\\s+)(\\w+)"
				+ "(\\s*((?i)where)\\s+((\\w+)(\\s*)(>|<|=|>=|<=|<>)\\s*(('[^']*')|(\\d+))))?\\s*;?\\s*$";

		Pattern pat = Pattern.compile(selectPattern);
		Matcher ma = pat.matcher(query);
		// print(ma.find());

		if (!ma.find()) {
			error();
			return;
		} else {
			colomsName = new ArrayList<String>(Arrays.asList(ma.group(2).replaceAll("\\s+", "").split(",")));
			tableName = ma.group(10);
			String[] wherecondition = Where(ma.group(13));
			if (colomsName.get(0).equals("*"))
				colomsName = new ArrayList<>();
			Dpms.selectFromTable(colomsName, wherecondition, tableName);
		}

	}

	public void Update(String query) {

		String updatepattern = "^\\s*((?i)update)\\s+(\\w+)\\s+((?i)set)\\s+"
				+ "((\\s*(\\w+)\\s*=\\s*(('[^']*')|(\\d+))\\s*,)*(\\s*(\\w+)\\s*=\\s*(('[^']*')|(\\d+))))"
				+ "(\\s*((?i)where)\\s+((\\w+)(\\s*)(>|<|=|>=|<=|<>)\\s*(('[^']*')|(\\d+))))?\\s*;?\\s*$";
		Pattern pat = Pattern.compile(updatepattern);
		Matcher ma = pat.matcher(query);
		if (!ma.find()) {
			error();
			return;
		}
		String tableName = ma.group(2);
		ArrayList<ArrayList<String>> clmAndVlu = colomValuesSpliter(ma.group(4));
		String[] wherecondition = Where(ma.group(17));
		ArrayList<String> columns = new ArrayList<>();

		ArrayList<String> value = new ArrayList<>();
		for (ArrayList<String> st : clmAndVlu) {
			columns.add(st.get(0));
			value.add(st.get(1));

		}

		Dpms.updateTable(columns, value, wherecondition, tableName);
	}

	public String[] Where(String condition) {
		if (condition == null) {
			return new String[1];
		}

		String WhereCondtionPattern = "(\\w+)\\s*(>|<|=|>=|<=|<>)\\s*(('[^']*')|(\\d+))";
		Pattern pat = Pattern.compile(WhereCondtionPattern);
		Matcher ma = pat.matcher(condition);
		String[] cndition = new String[3];
		if (ma.find()) {
			cndition[0] = ma.group(1);
			cndition[1] = ma.group(2);
			cndition[2] = ma.group(3);
			if (cndition[2].endsWith("'")) {
				cndition[2] = cndition[2].substring(1, cndition[2].length() - 1);
			}
		}
		return cndition;

	}

	public ArrayList<ArrayList<String>> colomValuesSpliter(String colomStateMent) {
		ArrayList<ArrayList<String>> clmAndVlu = new ArrayList<>();
		ArrayList<String> temp = new ArrayList<>();
		// if (colomStateMent.endsWith("'"))
		colomStateMent = colomStateMent.replaceAll("'\\s*$", "");

		colomStateMent = colomStateMent.replaceAll("\\s*=\\s*'?", "=");
		colomStateMent = colomStateMent.replaceAll("'\\s*", "'");
		colomStateMent = colomStateMent.replaceAll("(\\d+)\\s*,\\s*", "$1',");
		String oneClm = "\\s*(\\w+)\\s*=\\s*(.*)\\s*";
		Pattern pa = Pattern.compile(oneClm);
		ArrayList<String> coloms = new ArrayList<String>(Arrays.asList(colomStateMent.split("((('))\\s*,\\s*)")));
		for (String ss : coloms) {
			Matcher ma = pa.matcher(ss);
			if (ma.find()) {
				temp = new ArrayList<>();
				temp.add(ma.group(1));
				temp.add(ma.group(2));
				clmAndVlu.add(temp);
			}

		}
		return clmAndVlu;
	}

	public static void main(String[] args) {
		// Scanner in = new Scanner(System.in);
		Parser a = new Parser();
		// a.InsertQuery("select name ,state from testTable1 where id ='2' ;");

		// a.Insert("INSERT INTO Customers(CustomerName, ContactName, Address,
		// City, PostalCode, Country)"
		// + "VALUES ('Cardinal','Tom B. Erichsen','Skagen
		// 21','Stavanger','4006','Norway');");
		ArrayList<String> query = new ArrayList<>();

		query.add("drop database test1 ;");
		query.add("drop database test ;");

		query.add("create Database test1 ;");
		query.add("create Database test ;");
		query.add("create table testTable (" + "id int ,name varchar, state varchar) ;");
		query.add("create table testTable1 (" + "id int ,name varchar, state varchar) ;");

		query.add("insert into testTable " + "values (1, 'hendy' , 'da7e7')");
		query.add("insert into testTable " + "values (2, 'arsanuse' , 'da7e72')");
		query.add("insert into testTable (id,name,state )" + "values (3, '7osam' , 'da7e7')");
		query.add("insert into testTable (id,name )" + "values (4, 'saed' )");

		query.add("insert into testTable1 " + "values (1, 'arsanuse' , 'da7e   7(2)')");
		query.add("insert into testTable1 (id,name,state )" + "values (2, '7osam' , 'da7e   7')");
		query.add("insert into testTable1 (id,name )" + "values (3, 'saed' )");
		query.add("insert into testTable1 " + "values (4, 'hendy' , 'da7e   7')");

		query.add("select * from testTable ;");
		query.add("select name ,state from testTable ;");
		query.add("select id ,state from testTable ;");
		query.add("select state from testTable ;");
		// query.add("select * from testTable ;");

		query.add("select name ,state from testTable where id ='2' ;");
		query.add("select id ,state from testTable where name ='hendy' ;");
		query.add("select state from testTable where name ='7osam' ;");

		query.add("select * from testTable1 ;");
		query.add("select name ,state from testTable1 ;");
		query.add("select id ,state from testTable1 ;");
		query.add("select state from testTable1 ;");
		// query.add("select * from testTable1 ;");
		query.add("select name ,state from testTable1 where id ='2' ;");
		query.add("select id ,state from testTable1 where name ='hendy' ;");
		query.add("select state from testTable1 where name ='7osam' ;");

		query.add("update testTable set name='saeeeed',state = 'hahaha' where name ='hendy' ;");
		query.add("update testTable set name='saeeeed' ;");

		query.add("update testTable set name='sa3sa3',id =10 where id ='2'");
		query.add("update testTable set name='saeeeed' where id ='4'");

		query.add("update testTable1 set name='7sammmm',state = 'hahaha' where name ='hendy'");
		query.add("update testTable1 set name='7osam' ;");
		query.add("update testTable1 set name='7oos',id =10 where id ='2'");
		query.add("update testTable1 set name='7osam' where id ='4'");

		query.add("select name ,state from testTable1 where id ='10' ;");
		query.add("select id ,state from testTable1 where name ='7osam' ;");
		query.add("select state from testTable1 where name ='7oos' ;");

		query.add("select name ,state from testTable where id >'1' ;");
		query.add("select id ,state from testTable where name ='saeed' ;");
		query.add("select state from testTable where state ='hahaha' ;");

		query.add("drop table testTable1  ;");
		/**
		 * exception
		 */
		// query.add("select name ,state from testTable1 where id ='10' ;");

		query.add("select * from testTable ;");
		query.add("delete from testTable where id ='10';");
		// query.add("select * from testTable ;");
		//
		// query.add("drop database test1 ;");
		// query.add("select * from testTable ;");
		// query.add("select state,id from testTable where id>0 ;");
		//
		// /**
		// * exceptions
		// */
		// query.add("create Database test1 ;");
		// //query.add("create Database test ;");
		// query.add("select * from testTable ;");
		Scanner in = new Scanner(System.in);
		for (String s : query) {
			try{
			a.InsertQuery(s);
			System.out.println(s);
			//press enter here.
			in.nextLine();
			}catch(Exception e)
			{
				System.out.println("Error in DBMS");
			}
		}

		a.InsertQuery("select name ,state from testTable where id >'2' ");

		/*
		 * for (int i = 0; i < 5; i++) { System.out.println("jjj"); String s =
		 * in.nextLine(); a.InsertQuery(s); }
		 */
		//
		// a.InsertQuery("update hhh set cc='hhg' ,ggf=25,hhh='nn' Where
		// jjj='fgg' ;");
	}

}
