package DBMSparser;

import java.util.ArrayList;
import java.util.Arrays;

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

    public void InsertQuery(String query) {
        // sperate the query

        ArrayList<String>words=new ArrayList<String>(Arrays.asList(query.toLowerCase().split("\\s+")));
        if(words.get(0).equals(""))words.remove(0);

        switch ("1") {
        case "select":

            break;

        case "delete":

            break;
        case "drop":

            break;
        case "update":

            break;
        case "insert":

            break;
        case "create":
            //call create of database and table
        default:

            break;
        }

    }

    public void CreateDataBase(String name) {

    }

    public void CreateTable(String query) {

    }

    public void Insert(String query) {

    }

    public void DropDataBase(String name) {

    }

    public void DropTable(String name) {

    }

    public void Delete(String query) {

    }

    public void Select(String query) {

    }

    public void Update(String query) {

    }

    public void Where(String condition) {
        // check..0
    }
    public static void main(String[] args) {
        Parser a=new Parser();
        a.InsertQuery("         SELect enfdrlgfnj   esfdrngj ");
    }
}
