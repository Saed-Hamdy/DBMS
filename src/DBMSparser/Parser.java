package DBMSparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataBase.control.DataBaseControlImpl;

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
    DataBaseControlImpl Dpms = new DataBaseControlImpl();

    public boolean validName(final String name) {
        Pattern pattern;
        Matcher matcher;
        final String VALID_WORD = "^[A-Za-z][A-Za-z0-9_]*";
        // table "^[a-zA-Z\\d][\\w#@]{0,127}$";
        pattern = Pattern.compile(VALID_WORD);
        matcher = pattern.matcher(name);
        return matcher.matches();

    }

    private void print(String str) {

        System.out.println(str);
    }

    private void error() {

        System.out.println("error");
        return;
    }

    /**
     * 
     * @param query
     */
    public void InsertQuery(String query) {
        if (query == null) {
            error();
            return;
        }
        ArrayList<String> words = new ArrayList<String>(Arrays.asList(query.toLowerCase().split("\\s+")));
        ArrayList<String> queryArr = new ArrayList<String>(Arrays.asList(query.split("\\s+")));

        while (words.remove(""))
            ;
        while (queryArr.remove(""))
            ;

        switch (words.get(0).toLowerCase()) {
        case "select":
            Select(query);
            break;

        case "drop":
            if (words.get(1).equals("table") && words.size() == 3) {
                if (validName(words.get(2)))
                    DropTable(query);

            } else if (words.get(1).equals("database") && words.size() == 3) {
                if (validName(words.get(2)))
                    DropDataBase(query);
            } else {
                error();
                return;
            }

            break;
        case "delete":

            break;
        case "update":
            Update(query);

            break;
        case "insert":

            break;
        case "create":
            // call create of database and table
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

    public void DropDataBase(String query) {
        String DropPattern = "^\\s*((?i)drop)\\s+((?i)database)\\s+(\\w+)\\s*;?\\s*$";
        Pattern pat = Pattern.compile(DropPattern);
        Matcher ma = pat.matcher(query);
        if (!ma.find()) {
            error();
            return;
        }
        Dpms.dropTable(ma.group(3));

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
        // System.out.println(ma.find());

        if (!ma.find()) {
            error();
            return;
        } else {
            colomsName = new ArrayList<String>(Arrays.asList(ma.group(2).replaceAll("\\s+", "").split(",")));
            tableName = ma.group(10);
            String[] wherecondition = Where(ma.group(13));
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
        }
        return cndition;

    }

    public ArrayList<ArrayList<String>> colomValuesSpliter(String colomStateMent) {
        ArrayList<ArrayList<String>> clmAndVlu = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
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
        Scanner in = new Scanner(System.in);
        Parser a = new Parser();
        // for(int i=10;i>0;i++){
        // a.InsertQuery(in.nextLine());
        // }
        // Update sss set jjg=jj ,jj=58 where a=8 ;
        // select sss,kkf from wher ;
        // Drop table ggg ;
        // Drop database hjhjdf ;
        String DropPattern = "^\\s*((?i)drop)\\s+((?i)table)\\s+(\\w+)\\s*;?\\s*$";
        Pattern pat = Pattern.compile(DropPattern);
        Matcher ma = pat.matcher("drop table jjjff  ;  ");
        if (ma.find()) {
            System.out.println("k");
            for (int i = 1; i < 20; i++) {
                System.out.println(i + " " + ma.group(i));

            }
        }
    }

    // String DropPattern
    // ="^\\s*((?i)drop)\\s+((?i)table)\\s+(\\w+)\\s*;?\\s*$";
    // String selectPattern =
    // "^\\s*((?i)select\\s+)((\\*\\s*)|((\\s*(\\w+)\\s*,)*(\\s*(\\w+)\\s+)))\\s*((?i)from\\s+)(\\w+)"
    // +
    // "(\\s*((?i)where)\\s+((\\w+)(\\s*)(>|<|=|>=|<=|<>)\\s*(('[^']*')|(\\d+))))?\\s*;?\\s*$";
    // Pattern pat = Pattern.compile(selectPattern);
    // // Matcher ma = pat.matcher("select jjfj , jhh, hhhth, hf from ttt where
    // xx=''");
    // // System.out.println(ma.find());
    //
    // if (ma.find()) {
    // System.out.println("k");
    // for (int i = 1; i < 20; i++) {
    // System.out.println(i + " " + ma.group(i));
    //
    // }
    // String colom = ma.group(2);
    // String tableName = ma.group(10);
    //
    // }
    // }
}
