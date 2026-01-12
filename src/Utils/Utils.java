package Utils;

public class Utils {
    //MODULOS DE MANIPULAÇAO DE DADOS
    
    /**
     * Escapes SQL identifiers (table names, column names) by wrapping them in backticks
     * to prevent SQL injection on identifiers.
     * @param identifier The identifier to escape
     * @return The escaped identifier
     * @throws IllegalArgumentException if identifier is null or empty
     */
    private static String escapeIdentifier(String identifier) {
        if (identifier == null || identifier.trim().isEmpty()) {
            throw new IllegalArgumentException("Identifier cannot be null or empty");
        }
        // Remove any existing backticks and wrap in backticks
        // This prevents SQL injection on table/column names
        return "`" + identifier.replace("`", "``") + "`";
    }
    
    /// <summary>
    /// Returns a String based on the parameters given<br/>
    /// </summary>
    /// <param name="_fields"></param>
    /// <param name="_table"></param>
    /// <param name="_conditions"></param>
    /// <returns></returns>
    public static String select(String _fields, String _table, String _conditions) {

        String query = "";
        if (_conditions != "") {
            query = String.format("SELECT %s FROM %s WHERE %s", _fields, _table, _conditions);
        } else {
            query = String.format("SELECT %s FROM %s", _fields, _table);
        }

        return query;


    }

    /**
     * Returns a parameterized SELECT query using ? placeholders for values.
     * Use this with DBTools.retrieveDataMySQL(parameters) to prevent SQL injection.
     * @param _fields Comma-separated field names (will be escaped)
     * @param _table Table name (will be escaped)
     * @param _whereFields Array of field names for WHERE clause (will be escaped), can be null for no WHERE clause
     * @return Parameterized SELECT query with ? placeholders
     * @throws IllegalArgumentException if _fields or _table are null or empty
     */
    public static String selectParameterized(String _fields, String _table, String[] _whereFields) {
        if (_fields == null || _fields.trim().isEmpty()) {
            throw new IllegalArgumentException("Fields cannot be null or empty");
        }
        String tableName = escapeIdentifier(_table);
        
        // Parse and escape field names
        String[] fields = _fields.split(",");
        StringBuilder escapedFields = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i].trim();
            if (field.equals("*")) {
                escapedFields.append("*");
            } else {
                escapedFields.append(escapeIdentifier(field));
            }
            if (i < fields.length - 1) {
                escapedFields.append(", ");
            }
        }
        
        if (_whereFields == null || _whereFields.length == 0) {
            return String.format("SELECT %s FROM %s", escapedFields.toString(), tableName);
        }
        
        // Build WHERE clause with placeholders
        StringBuilder whereClause = new StringBuilder();
        for (int i = 0; i < _whereFields.length; i++) {
            whereClause.append(escapeIdentifier(_whereFields[i])).append(" = ?");
            if (i < _whereFields.length - 1) {
                whereClause.append(" AND ");
            }
        }
        
        return String.format("SELECT %s FROM %s WHERE %s", escapedFields.toString(), tableName, whereClause.toString());
    }

    /// <summary>
    /// Returns an insert query based on the parameters given<br/>
    /// </summary>
    /// <param name="_fields"></param>
    /// <param name="_table"></param>
    /// <param name="_conditions"></param>
    /// <returns></returns>
    public static String insert(String[] _fields, String _table, String[] _values) {
        String fields = "", values = "";
        //MONTA OS CAMPOS
        String query = "INSERT INTO " + _table + "(";

        for (int cont = 0; cont < _fields.length; cont++) {
            fields += _fields[cont] + ",";
        }
        fields = fields.substring(0, fields.length() - 1);
        fields += ") VALUES(";
        //VALORES
        for (int cont = 0; cont < _fields.length; cont++) {
            double numero;

            try {
                Double.valueOf(_values[cont]);
            } catch (Exception e) {

                if (_values[cont] != "''" && _values[cont] != null) {
                    if (_values[cont].length() > 0) {
                        if (_values[cont].substring(0, 1) != "'") {
                            _values[cont] = "'" + _values[cont] + "'";
                        } else {
                            _values[cont] = _values[cont] + "";
                        }

                    } else {
                        if (_values[cont].length() == 0)
                            _values[cont] = "null";
                    }

                } else {
                    if (_values[cont] == null) {
                        _values[cont] = "null";
                    } else {
                        if (_values[cont].length() > 0) {
                            if (_values[cont].substring(0, 1) != "'") {
                                _values[cont] = _values[cont];
                            }

                        } else {
                            _values[cont] = "null";
                        }
                    }

                }

            }


            values+=_values[cont]+",";
        }
        values = values.substring(0, values.length() - 1);

        //FINALIZA A QUERY
        query += fields;
        query += values;

        query += ")";
        //RETORNA A QUERY
        return query;

    }

    /**
     * Returns a parameterized INSERT query using ? placeholders for values.
     * Use this with DBTools.executeQuery(parameters) to prevent SQL injection.
     * @param _fields Array of field names (will be escaped)
     * @param _table Table name (will be escaped)
     * @return Parameterized INSERT query with ? placeholders
     * @throws IllegalArgumentException if _fields is null or empty
     */
    public static String insertParameterized(String[] _fields, String _table) {
        if (_fields == null || _fields.length == 0) {
            throw new IllegalArgumentException("Fields array cannot be null or empty");
        }
        String tableName = escapeIdentifier(_table);
        StringBuilder fields = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        
        for (int i = 0; i < _fields.length; i++) {
            fields.append(escapeIdentifier(_fields[i]));
            placeholders.append("?");
            if (i < _fields.length - 1) {
                fields.append(", ");
                placeholders.append(", ");
            }
        }
        
        return String.format("INSERT INTO %s(%s) VALUES(%s)", tableName, fields.toString(), placeholders.toString());
    }

    /// <summary>
    /// Returns an Update query
    /// </summary>
    /// <param name="_fields"></param>
    /// <param name="_table"></param>
    /// <param name="_values"></param>
    /// <returns></returns>
    public static String update(String[] _fields, String _table, String[] _values, String condition)
    {
        String fields = "", values = "";
        //MONTA OS CAMPOS
        String query = "UPDATE  " + _table + " SET ";
        //VALORES
        for (int cont = 0; cont < _fields.length; cont++)
        {
            double numero;
            try
            {
                Double.valueOf(_values[cont]);
            }catch (Exception e){

                    if (values != "''" && _values[cont] != null)
                    {
                        if (_values[cont].length() > 0)
                        {
                            if (_values[cont].substring(0, 1) != "'")
                            {
                                _values[cont] = "'" + _values[cont] + "'";
                            }
                            else
                            {
                                _values[cont] = _values[cont] + "";
                            }

                        }
                        else
                        {
                            if (_values[cont].length() == 0)
                                _values[cont] = "null";
                        }

                    }
                    else
                    {
                        if(_values[cont]==null)
                        {
                            _values[cont] = "null";
                        }
                        else
                        {
                            if (_values[cont].length() > 0)
                            {
                                if (_values[cont].substring(0, 1) != "'")
                                {
                                    _values[cont] = _values[cont];
                                }

                            }
                            else
                            {
                                _values[cont] = "null";
                            }
                        }

                    }

                }



        }
        for (int cont = 0; cont < _fields.length; cont++)
        {
            fields += _fields[cont] + "=" + _values[cont] + ",";
        }
        //fields = fields.Remove(fields.length - 1, 1);
        fields = fields.substring(0, fields.length() - 1);
        fields += " WHERE " + condition;
        query += fields;

        //Retorna a query
        return query;


    }
    
    /**
     * Returns a parameterized UPDATE query using ? placeholders for values.
     * Use this with DBTools.executeQuery(parameters) to prevent SQL injection.
     * @param _fields Array of field names to update (will be escaped)
     * @param _table Table name (will be escaped)
     * @param _whereFields Array of field names for WHERE clause (will be escaped)
     * @return Parameterized UPDATE query with ? placeholders
     * @throws IllegalArgumentException if _fields or _whereFields are null or empty
     */
    public static String updateParameterized(String[] _fields, String _table, String[] _whereFields) {
        if (_fields == null || _fields.length == 0) {
            throw new IllegalArgumentException("Fields array cannot be null or empty");
        }
        if (_whereFields == null || _whereFields.length == 0) {
            throw new IllegalArgumentException("WHERE fields array cannot be null or empty");
        }
        String tableName = escapeIdentifier(_table);
        StringBuilder setClause = new StringBuilder();
        
        // Build SET clause with placeholders
        for (int i = 0; i < _fields.length; i++) {
            setClause.append(escapeIdentifier(_fields[i])).append(" = ?");
            if (i < _fields.length - 1) {
                setClause.append(", ");
            }
        }
        
        // Build WHERE clause with placeholders
        StringBuilder whereClause = new StringBuilder();
        for (int i = 0; i < _whereFields.length; i++) {
            whereClause.append(escapeIdentifier(_whereFields[i])).append(" = ?");
            if (i < _whereFields.length - 1) {
                whereClause.append(" AND ");
            }
        }
        
        return String.format("UPDATE %s SET %s WHERE %s", tableName, setClause.toString(), whereClause.toString());
    }
    /// <summary>
    /// Returns a Delete query<br/>
    /// For security reasons, the use of a condition is mandatory.
    /// </summary>
    /// <param name="_table"></param>
    /// <param name="condition"></param>
    /// <returns></returns>
    public static String delete(String _table, String condition)
    {

        //MONTA OS CAMPOS
        String query = "DELETE  FROM " + _table + " WHERE " + condition;
        //RETORNA A QUERY

        return query;


    }
    
    /**
     * Returns a parameterized DELETE query using ? placeholders for values.
     * Use this with DBTools.executeQuery(parameters) to prevent SQL injection.
     * For security reasons, the use of a WHERE clause is mandatory.
     * @param _table Table name (will be escaped)
     * @param _whereFields Array of field names for WHERE clause (will be escaped)
     * @return Parameterized DELETE query with ? placeholders
     * @throws IllegalArgumentException if _whereFields is null or empty
     */
    public static String deleteParameterized(String _table, String[] _whereFields) {
        if (_whereFields == null || _whereFields.length == 0) {
            throw new IllegalArgumentException("WHERE fields array cannot be null or empty - WHERE clause is mandatory for DELETE");
        }
        String tableName = escapeIdentifier(_table);
        
        // Build WHERE clause with placeholders
        StringBuilder whereClause = new StringBuilder();
        for (int i = 0; i < _whereFields.length; i++) {
            whereClause.append(escapeIdentifier(_whereFields[i])).append(" = ?");
            if (i < _whereFields.length - 1) {
                whereClause.append(" AND ");
            }
        }
        
        return String.format("DELETE FROM %s WHERE %s", tableName, whereClause.toString());
    }
}
