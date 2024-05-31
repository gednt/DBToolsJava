package Utils;

public class Utils {
    //MODULOS DE MANIPULAÇAO DE DADOS
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
}
