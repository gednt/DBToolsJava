import DBTools.DBTools;
import Models.GenericObject;
import Utils.Utils;

import java.util.List;

/**
 * Example demonstrating secure parameterized queries to prevent SQL injection.
 * This example shows how to use the new parameterized methods.
 */
public class Main_secure_example {
    public static void main(String[] args) {
        DBTools db = new DBTools("3306");
        db.setHost("localhost");
        db.setDatabase("social_checker");
        db.setUid("user");
        db.setPassword("password");
        
        // Example 1: Secure SELECT with parameterized query
        // Old way (vulnerable to SQL injection):
        // db.setQuery(Utils.select("*","opinions","idOpinion=46"));
        
        // New secure way with parameterized query:
        String[] whereFields = {"idOpinion"};
        db.setQuery(Utils.selectParameterized("*", "opinions", whereFields));
        Object[] selectParams = {46};
        db.retrieveDataMySQL(selectParams);
        
        List<List<GenericObject>> genericObjectList = db.getGenericObjectList();
        
        System.out.println("=== SELECT Example with Parameterized Query ===");
        for (int row = 0; row < genericObjectList.size(); row++) {
            System.out.println("ROW_____" + (row + 1));
            for (int columns = 0; columns < genericObjectList.get(row).size(); columns++) {
                System.out.println("Column Name: " + genericObjectList.get(row).get(columns).getColumn());
                try {
                    System.out.println("Value: " + genericObjectList.get(row).get(columns).getValue().toString());
                } catch (Exception e) {
                    System.out.println("Value: null");
                }
            }
            System.out.println("\n");
        }
        
        // Example 2: Secure INSERT with parameterized query
        System.out.println("=== INSERT Example with Parameterized Query ===");
        String[] insertFields = {"title", "content", "userId"};
        db.setQuery(Utils.insertParameterized(insertFields, "opinions"));
        Object[] insertParams = {"Test Opinion", "This is a secure test", 123};
        // db.executeQuery(insertParams);  // Uncomment to actually execute
        System.out.println("Query: " + db.getQuery());
        System.out.println("Parameters: " + java.util.Arrays.toString(insertParams));
        
        // Example 3: Secure UPDATE with parameterized query
        System.out.println("\n=== UPDATE Example with Parameterized Query ===");
        String[] updateFields = {"title", "content"};
        String[] updateWhereFields = {"idOpinion"};
        db.setQuery(Utils.updateParameterized(updateFields, "opinions", updateWhereFields));
        Object[] updateParams = {"Updated Title", "Updated Content", 46};
        // db.executeQuery(updateParams);  // Uncomment to actually execute
        System.out.println("Query: " + db.getQuery());
        System.out.println("Parameters: " + java.util.Arrays.toString(updateParams));
        
        // Example 4: Secure DELETE with parameterized query
        System.out.println("\n=== DELETE Example with Parameterized Query ===");
        String[] deleteWhereFields = {"idOpinion"};
        db.setQuery(Utils.deleteParameterized("opinions", deleteWhereFields));
        Object[] deleteParams = {99};
        // db.executeQuery(deleteParams);  // Uncomment to actually execute
        System.out.println("Query: " + db.getQuery());
        System.out.println("Parameters: " + java.util.Arrays.toString(deleteParams));
        
        System.out.println("\n=== Security Note ===");
        System.out.println("All user-provided values are now passed as parameters,");
        System.out.println("preventing SQL injection attacks. Table and column names");
        System.out.println("are escaped with backticks for additional security.");
    }
}
