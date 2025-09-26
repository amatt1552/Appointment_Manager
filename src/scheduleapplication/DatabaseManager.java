package scheduleapplication;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * Helps with using databases.
 * <p>I started this in the last project and just made a few tweaks for this one.</p>
 * <p>Using stored Procedures would have worked better for most of these however, now knowing how they work.</p>
 * @author Austin Matthews
 */
public class DatabaseManager 
{
    private static final ObservableList<Statement> statements = FXCollections.observableArrayList();
    private static final ObservableList<CallableStatement> callableStatements = FXCollections.observableArrayList();
    private static Connection connection;
    
    /**
     * Only creates a database.
     * <p>If it already exists will not create another one.</p>
     * @param databaseName database name
     */
    @Deprecated
    protected static void createDatabase(String databaseName)
    {
        final String DB_URL = databaseName + ";create=true";
        openConnection();
        System.out.println("Database " + databaseName + " created or exists.");
        
    }
    
    /**
     * Creates a database and tables.
     * <p>Created tables have no check in this function if they exist but should still work fine.</p>
     * @param databaseName database name
     * @param command command for creating tables.
     */
    @Deprecated
    protected static void createDatabase(String databaseName, String command)
    {
        final String DB_URL = databaseName + ";create=true";
        openConnection();
        if(!command.equals(""))
        {
            createTable(command);
        }
        System.out.println("Database " + databaseName + " created.");
        
    }
    
    /**
     * Opens the database.
     * @return returns true if successful
     */
    protected static boolean openConnection()
    {
        //final String DB_URL = "jdbc:mysql:" + databaseName;
        DataSource dataSource = DatabaseSettings.getDataSourceMySQL();
        try
        {
            //Create or get a connection to the database.
            System.out.println("opening connection.");
            connection = dataSource.getConnection();
            return true;
            
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
            return false;
        }
    }
    
    
    /**
     * Checks if database exists.
     * @param databaseName database name
     * @return 
     */
    protected static Boolean checkForDatabase(String databaseName)
    {
        final String DB_URL = "jdbc:derby:" + databaseName; 
        try
        {
            // Get a connection to the database.
            DriverManager.getConnection(DB_URL);
            //if no error database exists
            return true;
        }
        catch(SQLException sqlE)
        {
             ExceptionManager.displayError(sqlE);
        }
        return false;
    }
    /**
     * Creates table with given name. 
     * <p>Has a check if table name exists.</p>  
     * <p>How it is formatted: "CREATE TABLE " + tableName + command</p> 
     * @param tableName table name
     * @param command SQL command
     * @return returns true if statement successful
     */
    protected static boolean createTable(String tableName, String command)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return false;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return false;
        }
        
        
        Statement statement = null;
        statements.add(statement);
        try
        {
            
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //gets database data
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet result = metadata.getTables(null, null, "%", null);
            //checks if table exists
            while (result.next()) 
            {
                //System.out.println(rs.getString(3));
                if(result.getString(3).equals(tableName.toUpperCase()))
                {
                    
                    System.out.println("Table with name " + tableName + " already created!");
                    closeResult(result);
                    return false;
                }
            }
            closeResult(result);
            // Create the table.
            System.out.println("Creating the " + tableName + " table...");
            statement.execute("CREATE TABLE " + tableName + command + ";");
            
            System.out.println("Table " + tableName + " created.");
            
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
            return false;
        }
        finally
        { 
            closeStatement(statement);
        }
        return true;
    }
    
    /**
     * Creates a table by only using a command. 
     * <p>Has no check if table exists.</p> 
     * @param command SQL command
     * @return returns true if statement successful
     */
    protected static boolean createTable(String command)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return false;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return false;
        }
        
        
        Statement statement = null;
        statements.add(statement);
        try
        {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            // Create the table.
            System.out.println("Creating the table...");
            boolean success = statement.execute(command);
            
            System.out.println("Table created.");
            return success;
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
            return false;
        }
        finally
        {
            closeStatement(statement);
        }
    }
    /**
     * Deletes a table.
     * <p>goes like: "DROP TABLE " + tableName + command</p> 
     * @param tableName table name
     * @param command sql command
     * @return returns true if statement successful
     */
    protected static boolean dropTable(String tableName, String command)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return false;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return false;
        }
        
        
        Statement statement = null;
        statements.add(statement);
        try
        {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //try to drop the table.
            System.out.println("Dropping the " + tableName + " table...");
            boolean success = statement.execute("DROP TABLE " + tableName + command);           
            System.out.println("Table " + tableName + " dropped.");
            return success;
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
            return false;
        }
        finally
        {
            closeStatement(statement);
        }
    }
    
    /**
     * Closes a Result Set.
     * @param result 
     */
    private static void closeResult(ResultSet result)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return;
        }
        
        
        try
        {
            if(result != null)
            {
                result.close();
            }
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
        }
                
    }
    
    //Closes the statement.
    private static void closeStatement(Statement statement)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return;
        }
        
        
        try
        {
            if(statement != null)
            {
                statement.close();
            }
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
        }
        statements.remove(statement);
    }
    
    //Closes the statement.
    private static void closeStatement(CallableStatement callableStatement)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return;
        }
        
        try
        {
            if(callableStatement != null)
            {
                callableStatement.close();
            }
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
        }
        statements.remove(callableStatement);
    }
    
    /**
     * Closes all statements.
     */
    protected static void closeAllStatements()
    {
        try
        {        
            //close all the statements
            for(Statement statement : statements)
            {
                if(statement != null)
                {
                    statement.close();
                }
            }
            statements.clear();
            
            //close all callable statements
            for(CallableStatement statement : callableStatements)
            {
                if(statement != null)
                {
                    statement.close();
                }
            }
            callableStatements.clear();
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
        }
    }
    /**
     * Closes the connection and all the leftover statements.
     */
    protected static void closeConnection()
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return;
        }
        
        
        closeAllStatements();
        try
        {      
            connection.close();
            System.out.println("Database closed.");
        }
        catch(SQLException sqlE)
        {
             ExceptionManager.displayError(sqlE);
        }
    }
    
    //time for some dml
    
    /**
     * Adds a row to a table.
     * @param tableName table name 
     * @param columns columns being modified
     * @param values values being inserted
     * @return returns true if statement successful
     */
    protected static boolean InsertRow(String tableName, String[] columns, String[] values)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return false;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return false;
        }
        
        
        //sets up the command
        //My rule of thumb with concat vs + with strings is if its a loop or complex then concat. 
        //Could get messy if there are a lot of iterations in my mind.
        //using concat too often is not as easy to read as + to me, so i dont really like to use it.
        String command = "INSERT INTO ".concat(tableName).concat("(");
        if(columns.length == values.length)
        {
            //adds column names
            for (int i = 0; i < columns.length;i++) 
            {
               
                command = command.concat(columns[i]);
                if(i != columns.length - 1)
                {
                    command = command.concat(", ");
                }
            }
            
            command = command.concat(") VALUES(");
                
            //adds values
            for (int i = 0; i < values.length; i++) 
            {
               
                command = command.concat(values[i]);
                if(i != values.length - 1)
                {
                    command = command.concat(", ");
                }
            }
            command = command.concat(")");
        }
        else
        {
            System.out.println("column length does not equal value length! Make sure they have the same length.");
            return false;
        }
        
        //excute command
        Statement statement = null;
        statements.add(statement);
        try
        { 
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("inserting into table " + tableName);
            System.out.println(command);
            int count = statement.executeUpdate(command);
            System.out.println("rows affected: " + count);
            return true;
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
            return false;
        }
        finally
        {
            closeStatement(statement);
        }
    }
    
    /**
     * Updates rows.
     * @param tableName
     * @param columns
     * @param values
     * @param whereClause 
     * @return returns true if statement successful
     */
    protected static boolean updateRows(String tableName, String[] columns, String[] values, String whereClause)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return false;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return false;
        }
        
        
        //setting up the command
        String command = "UPDATE ".concat(tableName).concat(" SET ");
        if(columns.length == values.length)
        {
            for(int i = 0; i < columns.length; i++)
            {
                command = command.concat(columns[i])
                .concat(" = ")
                .concat(values[i]);
                if(i != columns.length - 1)
                {
                    command = command.concat(", ");
                }
                
            }
            command = command.concat(" ").concat(whereClause);
        }
        else
        {
            System.out.println("column length does not equal value length! Make sure they have the same length.");
            return false;
        }
        
        //execute command
        Statement statement = null;
        statements.add(statement);
        try
        { 
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("updating table " + tableName);
            System.out.println(command);
            int count = statement.executeUpdate(command);
            System.out.println("rows affected: " + count);
            return count > 0;
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
            return false;
        }
        finally
        {
            closeStatement(statement);
        }
    }
    
    /**
     * Deletes rows. 
     * <p>looks like: "DELETE FROM " + tableName + " " + whereClause;</p> 
     * @param tableName
     * @param whereClause 
     * @return returns true if statement successful
     */
    protected static boolean deleteRows(String tableName, String whereClause)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return false;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return false;
        }
        
        
        //set up the command
        String command = "DELETE FROM "
                + tableName
                + " "
                + whereClause;
        
        //execute command
        Statement statement = null;
        statements.add(statement);
        try
        {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println(command);
            int rows = statement.executeUpdate(command);
            System.out.println("rows deleted: " + rows);
            return true;
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
            return false;
        }
        finally
        {
            closeStatement(statement);
        }
    }
    
    /**
     * Get rows from tables. 
     * <p>Can be used to do most queries.</p>
     * <p>looks like: "SELECT " + columns + " FROM " + tableNames + whereClause;</p>
     * <p>statement will not close if successful. call closeConnection or closeAllStatements when done if you use this.</p>
     * @param columns
     * @param tableNames
     * @param whereClause
     * @return returns a result set. Make sure to close it!
     */
    protected static ResultSet getRows(String columns, String tableNames, String whereClause)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return null;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return null;
        }
        
        
        Statement statement = null;
        statements.add(statement);
        //set up the command
        String command = "SELECT "
                + columns
                + " FROM "
                + tableNames
                + " "
                + whereClause;
        //execute command
        try
        { 
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println(command);
            ResultSet result = statement.executeQuery(command);
            return result;
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
        }
        closeStatement(statement);
        return null;
    }
    
    /**
     * A left outer join. 
     * <p>only works when foreign keys have the same name.</p>  
     * <p>How it will look: "SELECT " + columns + " FROM " + tables[0] + " LEFT OUTER JOIN " + table[1]</p> 
     * <p>+ "ON" + tables[0] + "." + comparedAtributes[0] + " = " + table[1] + "." + comparedAtributes[0]</p> 
     * <p>statement will not close if successful. call closeConnection or closeAllStatements when done if you use this.</p>    
     * @param columns
     * @param tables
     * @param comparedAttributes list of compared attributes. should be one less than tables
     * @param whereClause
     * @return 
     */
    protected static ResultSet getRowsLOJ(String columns, String[] tables, String[] comparedAttributes, String whereClause)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return null;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return null;
        }
        

        //set up the command
        String command;
        command = "SELECT "
                + columns
                + " FROM ";
        
        //check if you have the right amount of attributes
        if(comparedAttributes.length == tables.length - 1)
        {
            //start the left outer join 
            for(int i = 0; i < tables.length; i++)
            {
                if(i < 1)
                {
                    command = command.concat(tables[i]);
                }
                else
                {
                    command = command.concat(" LEFT OUTER JOIN ")
                            .concat(tables[i]);
                    //left side of on
                    command = command.concat(" ON ")
                            .concat(tables[i-1])
                            .concat(".")
                            .concat(comparedAttributes[i-1])
                            .concat(" = ");
                    //right side of on
                    command = command.concat(tables[i])
                            .concat(".")
                            .concat(comparedAttributes[i-1]);
                    
                }
                
            }
        }
        else
        {
            System.out.println("wrong amount of attributes! needs to be one less than the table length");
        }
        command += whereClause;
        
        //execute command
        Statement statement = null;
        statements.add(statement);
        try
        { 
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println(command);
            ResultSet result = statement.executeQuery(command);
            return result;
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
        }
        closeStatement(statement);
        return null;
    }
    
    /**
     * A right outer join. 
     * <p>Only works when foreign keys have the same name.</p>  
     * <p>How it looks:"SELECT " + columns + " FROM " + table[0] + " RIGHT OUTER JOIN " + table[1]</p>  
     * <p>+ "ON" + table[0] + "." + comparedAtributes[0] + " = " + table[1] + "." + comparedAtributes[0]</p>
     * <p>statement will not close if successful. call closeConnection or closeAllStatements when done if you use this.</p>
     * @param columns
     * @param tables
     * @param comparedAttributes list of compared attributes. should be one less than tables
     * @param whereClause
     * @return 
     */
    protected static ResultSet getRowsROJ(String columns, String[] tables, String[] comparedAttributes, String whereClause)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return null;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return null;
        }
        
        
        //setting up the command
        String command;
        command = "SELECT "
                + columns
                + " FROM ";
        
        //check if you have the right amount of attributes
        if(comparedAttributes.length == tables.length - 1)
        {
            //start the left outer join 
            for(int i = 0; i < tables.length; i++)
            {
                if(i < 1)
                {
                    command = command.concat(tables[i]);
                }
                else
                {
                    command = command.concat(" RIGHT OUTER JOIN ")
                            .concat(tables[i]);
                    //left side of on
                    command = command.concat(" ON ")
                            .concat(tables[i-1])
                            .concat(".")
                            .concat(comparedAttributes[i-1])
                            .concat(" = ");
                    //right side of on
                    command = command.concat(tables[i])
                            .concat(".")
                            .concat(comparedAttributes[i-1]);
                    
                }
                
            }
        }
        else
        {
            System.out.println("Wrong amount of attributes! needs to be one less than the table length");
        }
        command += whereClause;
        
        //execute command
        Statement statement = null;
        statements.add(statement);
        try
        { 
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println(command);
            ResultSet result = statement.executeQuery(command);
            return result;
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
        }
        closeStatement(statement);
        return null;
    }
    
    
    //--------------------------------computations-----------------------------------------
    
    /**
     * Uses a StoredProcedure in the database.
     * <p>statement will not close if successful. call closeConnection or closeAllStatements when done if you use this.</p>
     * @param procedureName
     * @return 
     */
    protected static ResultSet useProcedure(String procedureName)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return null;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return null;
        }
        
        CallableStatement cStatement = null;
        callableStatements.add(cStatement);
        String command = "{call " + procedureName + "()}";
        try
        {
           cStatement = connection.prepareCall(command);
           ResultSet rs = cStatement.executeQuery(command);
           return rs;
        }
        catch(SQLException | NullPointerException e)
        {
            ExceptionManager.displayError(e);
        }
        closeStatement(cStatement);
        return null;
    }
    
    /**
     * Uses a StoredProcedure in the database with a variable.
     * <p>statement will not close if successful. call closeConnection or closeAllStatements when done if you use this.</p>
     * @param procedureName
     * @param type variable passed
     * @return 
     */
    protected static <T> ResultSet useProcedure(String procedureName, T type)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return null;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return null;
        }
        
        CallableStatement cStatement = null;
        callableStatements.add(cStatement);
        String command;
        try
        {
            if(type instanceof String)
            {
                command = "{call " + procedureName + "('" + type + "')}";
            }
            else
            {
                command = "{call " + procedureName + "(" + type + ")}";
            }
        }
        catch(ClassCastException e)
        {
            ExceptionManager.displayError(e);
            return null;
        }
        try
        {
           cStatement = connection.prepareCall(command);
           ResultSet rs = cStatement.executeQuery(command);
           return rs;
        }
        catch(SQLException | NullPointerException e)
        {
            ExceptionManager.displayError(e);
        }
        closeStatement(cStatement);
        return null;
    }
    
    /**
     * Gets Count.
     * <p>Looks like this: "SELECT COUNT " + columns + " FROM " + tableNames + whereClause
     * @param columns
     * @param tableNames
     * @param whereClause
     * @return returns -1 when failed
     */
    protected static int getCount(String columns, String tableNames, String whereClause)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return -1;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return -1;
        }
        
        
        int count = -1;
        Statement statement = null;
        statements.add(statement);
        //set up the command
        String command = "SELECT COUNT("
                + columns
                +") FROM "
                + tableNames
                + " "
                + whereClause;
        //execute command
        try
        { 
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            System.out.println(command);
            ResultSet result = statement.executeQuery(command);
            if(result.next());
            {
                count = result.getInt(1);
            }
            closeResult(result);
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
        }
        finally
        {
            closeStatement(statement);
        }
        return count;
    }
    
    /**
     * Gets list of Counts.
     * <p>Looks like this: "SELECT COUNT " + columns + " FROM " + tableNames + whereClause
     * @param columns
     * @param tableNames
     * @param whereClause
     * @return returns -1 when failed
     */
    protected static List<Integer> getCountList(String columns, String tableNames, String whereClause)
    {
        List<Integer> intList = new ArrayList<>();
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return intList;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return intList;
        }
        
        
        Statement statement = null;
        statements.add(statement);
        //set up the command
        String command = "SELECT COUNT("
                + columns
                + ") FROM "
                + tableNames
                + " "
                + whereClause;
        //execute command
        try
        { 
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println(command);
            ResultSet result = statement.executeQuery(command);
            while(result.next())
            {
                intList.add(result.getInt(1));
            }
            result.close();
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
        }
        finally
        {
            closeStatement(statement);
        }
        return intList;
    }
    
    
    //---------------------------checks and display------------------------------------
    
    /**
     * Checks if a row exists. 
     * <p>Looks like this:"SELECT " + columns + " FROM " + tableName + " " + whereClause;</p> 
     * @param tableName
     * @param columns
     * @param whereClause
     * @return 
     */
    protected static Boolean checkForRow(String tableName, String columns, String whereClause)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return false;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return false;
        }
        
        
        // Create a Statement object.
        Statement statement = null;
        statements.add(statement);
        //set up the command
        String command = "SELECT "
                + columns
                + " FROM "
                + tableName
                + " "
                + whereClause;
        
        //execute command
        try
        { 
            statement = connection.createStatement();
            System.out.println(command);
            ResultSet result = statement.executeQuery(command);
            boolean foundRow = result.next();
            closeResult(result);
            return foundRow;
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
        }
        finally
        {
            closeStatement(statement);
        }
        return false;
    }
    
    /**
     * Displays a table's attribute names (column names) in console. 
     * <p>Best to use at the start of a result and only once for each table</p>
     * @param result 
     * @return returns true if statement successful
     */
    protected static boolean displayColumnNames(ResultSet result)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return false;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return false;
        }
        
        try
        {
            int currentColumnCount = 1;
            int columnCount;
            ResultSetMetaData metaData = result.getMetaData();
            columnCount = metaData.getColumnCount();
           
            while(currentColumnCount <= columnCount)
            {
                System.out.print(metaData.getColumnName(currentColumnCount) + ", ");
                currentColumnCount++;
                
            }
            System.out.println("");
            return true;
        }
        catch(SQLException sqlE)
        {
           //displayError(sqlE);
           return false;
        }
    }
    
    /**
     * Attempts to display your result in the console. 
     * @param result 
     * @return returns true if statement successful
     */
    protected static boolean displayResult(ResultSet result)
    {
        if(connection != null)
        {
            try 
            {
                if(!connection.isValid(1))
                {
                    openConnection();
                }
            } 
            catch (SQLException ex) 
            {
                ExceptionManager.displayError(ex);
                return false;
            }
        }
        else
        {
            System.out.println("No connection established!");
            return false;
        }
        
        
        try
        {
            int currentColumnCount = 1;
            int columnCount;
            ResultSetMetaData metaData = result.getMetaData();
            columnCount = metaData.getColumnCount();
            System.out.println("");
            
            //display names for columns one time at the start
            try
            {
                if(result.getRow() < 2)
                {
                    displayColumnNames(result);
                }
            }
            catch(SQLException sqlE)
            {
                //displayError(sqlE);
                System.out.println("\tMake sure statment allows ResultSet.TYPE_SCROLL_SENSITIVE");
            }
            
            //diplays values while they exist
            while(currentColumnCount <= columnCount)
            {
                System.out.print(result.getString(currentColumnCount) + ", ");
                currentColumnCount++;
                
            }
            System.out.println("");
            return true;
        }
        catch(SQLException sqlE)
        {
            ExceptionManager.displayError(sqlE);
            return false;
        }
    }
    
}
