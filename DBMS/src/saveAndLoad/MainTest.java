package saveAndLoad;

import java.io.File;
import java.util.ArrayList;

public class MainTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SaveAndLoad saveTest = new SaveAndLoadImpl();
		File dataBasesFolder = new File("Data Bases");
		// this line to make empty folder for all data bases
		dataBasesFolder.mkdir();
		String dataBaseName = "mydb";
		File dataBaseFolder = new File(dataBasesFolder.getAbsolutePath() + File.separator + dataBaseName);
		dataBaseFolder.mkdir();
		String tableName = "clients";
		String path = dataBaseFolder.getAbsolutePath() + File.separator + tableName + ".xml";
		System.out.println(path);
		File filee = new File(path);

		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		ArrayList<String> row = new ArrayList<String>();
		row.add("1");
		row.add("ahmed");
		row.add("12");
		data.add(row);
		data.add(row);

		ArrayList<String> coulmnNames = new ArrayList<String>();
		coulmnNames.add("ID");
		coulmnNames.add("Name");
		coulmnNames.add("Age");
		saveTest.save(filee, data, coulmnNames, tableName);
		
		ArrayList<ArrayList<String>> list = saveTest.load(filee);
		System.out.println("hello");
		
	}

}
