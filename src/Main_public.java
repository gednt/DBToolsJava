import DBTools.DBTools;
import Models.GenericObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import Utils.Utils;
public class Main_public {
    public static void main(String[] args) {
        DBTools db = new DBTools("3306");
        db.setHost("localhost");
        db.setDatabase("social_checker");
        db.setUid("user");
        db.setPassword("password");
        db.setQuery(Utils.select("*","opinions","idOpinion=46"));
        db.retrieveDataMySQL();
        List<List<GenericObject>> genericObjectList =db.getGenericObjectList();
        try{
            for (int row = 0;row<genericObjectList.size();row++) {
                System.out.println("ROW_____".concat(String.valueOf(row+1)));
                int size= genericObjectList.get(row).size();
                for(int columns =0;columns<genericObjectList.get(row).size();columns++){
                    System.out.println("Column Name:".concat(genericObjectList.get(row).get(columns).getColumn()));
                    try{
                        System.out.println("Value:".concat(genericObjectList.get(row).get(columns).getValue().toString()));
                    }catch (Exception e){

                    }

                }
                System.out.println("\n\n\n");

            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }



    }
}
