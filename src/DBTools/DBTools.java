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
        retrieveDataMySQL(null);
    }

    /**
     * Retrieve data from MySQL using parameterized query to prevent SQL injection.
     * @param parameters Array of parameter values to bind to the query. Use null for queries without parameters.
     * @throws IllegalArgumentException if parameter count doesn't match placeholders in query
     */
    public void retrieveDataMySQL(Object[] parameters){
        try {
            //Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://"+this.host+":"+this.port+"/"+this.database,this.uid,this.password);
            PreparedStatement stmt = conn.prepareStatement(this.query, ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            // Bind parameters if provided
            if (parameters != null) {
                // Validate parameter count matches placeholders
                int expectedParams = stmt.getParameterMetaData().getParameterCount();
                if (parameters.length != expectedParams) {
                    throw new IllegalArgumentException(
                        String.format("Parameter count mismatch: expected %d, got %d", expectedParams, parameters.length)
                    );
                }
                for (int i = 0; i < parameters.length; i++) {
                    stmt.setObject(i + 1, parameters[i]);
                }
            }
            
            this.resultSet = stmt.executeQuery();
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
        return executeQuery(null);
    }

    /**
     * Execute a query (INSERT, UPDATE, DELETE, etc.) using parameterized query to prevent SQL injection.
     * @param parameters Array of parameter values to bind to the query. Use null for queries without parameters.
     * @return true if the first result is a ResultSet object; false if it is an update count or there are no results
     * @throws IllegalArgumentException if parameter count doesn't match placeholders in query
     */
    public boolean executeQuery(Object[] parameters) {
        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.uid, this.password);
            PreparedStatement stmt = conn.prepareStatement(this.query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            // Bind parameters if provided
            if (parameters != null) {
                // Validate parameter count matches placeholders
                int expectedParams = stmt.getParameterMetaData().getParameterCount();
                if (parameters.length != expectedParams) {
                    throw new IllegalArgumentException(
                        String.format("Parameter count mismatch: expected %d, got %d", expectedParams, parameters.length)
                    );
                }
                for (int i = 0; i < parameters.length; i++) {
                    stmt.setObject(i + 1, parameters[i]);
                }
            }
            
            boolean executed = stmt.execute();
            conn.close();
            return executed;
        }catch (Exception e){
            this.setError(e.toString());
            return false;
        }

    }
    //endregion
}
