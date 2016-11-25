package DBMSparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser implements IParser {
    String table_name = new String();
    String database_name = new String();
    static ArrayList<String> coloums = new ArrayList<String>();
    static ArrayList<String> value = new ArrayList<String>();
    /*
     * INSERT INTO table_name VALUES value1,value2,value3,... where hdegsh;
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
     * UPDATE table_name SET column1 = value1 , column2=value2,... WHERE
     * some_column=some_value;
     */

    public void InsertQuery(String query) {
        // sperate the query

        ArrayList<String> words = new ArrayList<String>(Arrays.asList(query.toLowerCase().split("\\s+")));
        if (words.get(0).equals(""))
            words.remove(0);

        switch (words.get(0)) {
        case "select":

            break;

        case "delete":
            Delete(query);

            break;
        case "drop":

            break;
        case "update":

            break;
        case "insert":
            Insert(query);
            break;
        case "create":
            if (words.get(1).equals("table")) {
                CreateTable(query);
            } else if (words.get(1).equals("database")) {
                CreateDataBase(query);
            }
        default:

            break;
        }

    }

    public void CreateDataBase(String query) {
        Pattern pat = Pattern.compile("^(\\s*)((?i)create)(\\s+)((?i)database)(\\s+)(\\w+)(\\s*);?(\\s*)$");

        Matcher ma = pat.matcher(query);

        if (ma.matches()) {
            database_name = ma.group(6);

        }

    }

    public void CreateTable(String query) {
        Pattern pat = Pattern.compile(
                "^(\\s*)((?i)create)(\\s+)((?i)table)(\\s+)(\\w+)(\\s*)\\((((\\s*)(\\w+)(\\s+)(\\w+)(\\s*),)*((\\s*)(\\w+)(\\s+)(\\w+)(\\s*))\\)(\\s*))(\\s*);?(\\s*)$");

        Matcher ma = pat.matcher(query);
        if (ma.matches()) {
            table_name = ma.group(6);
            // for(int i=0;i<23;i++)
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
        }

    }

    public void Insert(String query) {
        Pattern pat = Pattern.compile("^(\\s*)((?i)insert)(\\s+)((?i)into)(\\s+)(\\w+)"
                + "((\\s*)\\(((\\s*)(\\w+)(\\s*),)*((\\s*)(\\w+)(\\s*)\\)(\\s*)))?" + "((\\s+)((?i)values))(\\s*)\\("
                + "(((\\s*)(('[^']*')|(\\d+))(\\s*),)*((\\s*)(('[^']*')|(\\d+))))(\\s*)\\)(\\s*)(\\s*);?(\\s*)$");
        /*
         * INSERT INTO TABLE_NAME (column1, column2, column3,...columnN) VALUES
         * (value1, value2, value3,...valueN);
         */
        Matcher ma = pat.matcher(query);
        if (ma.matches()) {
            System.out.println("mat");
            table_name = ma.group(6);
            if (ma.group(7) == null)
                coloums = new ArrayList<String>();
            else
                coloums = new ArrayList<String>(
                        Arrays.asList(ma.group(7).replaceAll("\\s+", "").replaceAll("[()]", "").split(",")));
            String trim = ma.group(22) + ",";
            while (!trim.replaceAll("\\s+", "").replaceAll("[,]","").equals("")){
            Pattern patt = Pattern.compile("('(\\s*[^']+\\s*)')|(\\s*(\\d+)\\s*,)");
            Matcher matc = patt.matcher(trim.replaceAll("\\)","\\) "));

            if (matc.find()) {

                if (trim.charAt(matc.start()) == '\'') {
                    value.add(trim.substring(matc.start() + 1, matc.end() - 1));
                } else {
                    value.add(trim.substring(matc.start(), matc.end() - 1));

                }
                trim=trim.substring(matc.end());
            }

            }
        }

    }

    public void DropDataBase(String name) {

    }

    public void DropTable(String name) {

    }

    public void Delete(String query) {
        Pattern pat = Pattern.compile(
                "^(\\s*)((?i)delete)(\\s+)((?i)from)(\\s+)(\\w+)(\\s+((?i)where)\\s+((\\w+)(\\s*)(>|<|=|>=|<=|<>)\\s*(('[^']*')|(\\d+))))?\\s*;?$");
        Matcher ma = pat.matcher(query);
        if (ma.matches()) {
            table_name = ma.group(6);
            Where((ma.group(9)));

        }

    }

    public void Select(String query) {

    }

    public void Update(String query) {

    }

    public void Where(String condition) {
        // check..0
    }

    public static void main(String[] args) {
        Parser a = new Parser();
        a.InsertQuery("INSERT INTO Customers (CustomerName, ContactName, Address, City, PostalCode, Country) VALUES('Cardinal','Tom B. Erichsen','Skagen 21','Stavanger', 4006 ,'Norway');");
        for(int i=0;i<value.size();i++){
            System.out.println(value.get(i));
        }

    }

    public void useDatabase(String name) {

    }

    public boolean validName(final String username) {
        Pattern pattern;
        Matcher matcher;
        final String VALID_WORD = "^[A-Za-z][A-Za-z0-9_]*";
        // table "^[a-zA-Z\\d][\\w#@]{0,127}$";
        pattern = Pattern.compile(VALID_WORD);
        matcher = pattern.matcher(username);
        return matcher.matches();

    }
}
