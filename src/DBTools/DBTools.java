package DBTools;
import Models.GenericObject;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DBTools is a Mysql Library to manipulate data in MYSQL Databases.
 */
public class DBTools {
//region private_variables

    private ResultSet resultSet = null;
    private String host;
    private String uid;

    private String password;

    private String database;

    private String query;

    private String error;

    private String table;
    private String port;
    private List<List<GenericObject>> genericObjectList = new ArrayList<List<GenericObject>>();
    private String _connectionString;

    public int count;
    
    //endregion

    //region getters and setters

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<List<GenericObject>> getGenericObjectList() {
        return genericObjectList;
    }

    //end region


    //region public_methods

    public DBTools()
    {
        //STANDARD TCP/IP Port
        this.port = "3306";
    }

    public DBTools(String port)
    {
        this.port = port;
    }

    public void retrieveDataMySQL(){
        try {
            //Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://"+this.host+":"+this.port+"/"+this.database,this.uid,this.password);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            this.resultSet = stmt.executeQuery(this.query);
            ResultSetMetaData rsmt = this.resultSet.getMetaData();
            this.resultSet.first();
            while(!this.resultSet.isAfterLast()){
                List<GenericObject> objRow = new ArrayList<GenericObject>();
                for(int column=1;column<=rsmt.getColumnCount();column++){
                    GenericObject obj = new GenericObject();
                    obj.setColumn(rsmt.getColumnName(column));
                    obj.setType(rsmt.getColumnTypeName(column));
                    obj.setValue(this.resultSet.getObject(column));
                    objRow.add(obj);
                }
                genericObjectList.add(objRow);
               this.resultSet.next();
            }
            conn.close();
        } catch (Exception e) {
            this.setError(e.toString());
        }
    }

    public boolean executeQuery() {
        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.uid, this.password);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            boolean executed = stmt.execute(this.query);
            conn.close();
            return executed;
        }catch (Exception e){
            this.setError(e.toString());
            return false;
        }

    }
    //endregion
}
