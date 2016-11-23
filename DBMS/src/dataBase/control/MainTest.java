package dataBase.control;

public class MainTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataBaseControl con = new DataBaseControlImpl();
		
		con.dropDataBase("mydb");
		con.changeDataBase("mydb");
		con.dropTable("clients");
		con.createDataBase("mydbArsanuos");
		System.out.println("herooor");
	}

}
