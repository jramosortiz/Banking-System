import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class Database {
    private static final String url = "jdbc:sqlite:bank.db";

    public static void Init()
    {
        Database.initilizeAccountTable();//initialize Table
        Database.initilizeDebitCardTable();//initialize debit_card Table
        Database.initilizeCreditCardTable();//init credit_card Table
    }


    public static Connection  connect() {
        try{

            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            System.out.println("^ SQLite connection successful!");
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("! Connection failed: " + e.getMessage());
            return null;
        }
    }

    //-------------init tables Area-------------//
    //Initialize the account Table
    public static void initilizeAccountTable() {

        String sqlCard = """
            CREATE TABLE IF NOT EXISTS accounts (
            account_id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT NOT NULL UNIQUE ,
            password TEXT NOT NULL ,
            Fname TEXT NOT NULL ,
            Lname TEXT NOT NULL
        );
    """;

        //Try to connect to sqlite and execute the table
        try ( Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCard);
            System.out.println("^ accounts table created succesfully!");

        } catch (SQLException e) {
            System.out.println("! Failed to create credit_cards table: " + e.getMessage());

        }
    }

    //Initialize the Debit Card Table
    public static void initilizeDebitCardTable(){

        String sqlCard = """
            CREATE TABLE IF NOT EXISTS debit_card (
            account_id INTEGER PRIMARY KEY,
            accountBalance REAL NOT NULL DEFAULT 0.0,
            lateFees REAL NOT NULL DEFAULT 30,
            FOREIGN KEY(account_id) REFERENCES accounts(account_id)
        );
    """;
        //try to connect to the sqlite and execute the debitCard table
        try(Connection conn = connect(); Statement stmt = conn.createStatement()){
            stmt.execute(sqlCard);
            System.out.println("^ DebitCard table created succesfully!");
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Initialize the Credit Cards Table
    public static void initilizeCreditCardTable(){

        String sqlCard = """
            CREATE TABLE IF NOT EXISTS credit_card (
            account_id INTEGER PRIMARY KEY,
            available_credit REAL NOT NULL DEFAULT 1000.00,
            credit_used REAL NOT NULL DEFAULT 0.0,
            interesRate REAL NOT NULL DEFAULT 22.78,
            minPayment REAL NOT NULL DEFAULT 0,
            FOREIGN KEY(account_id) REFERENCES accounts(account_id)
        );
    """;
        //try to connect to the sqlite and execute the debitCard table
        try(Connection conn = connect(); Statement stmt = conn.createStatement()){
            stmt.execute(sqlCard);
            System.out.println("^ CreditCard table created succesfully!");
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //--------------Create accounts area---------------//
    //Create accounts
    public static CheckingAccount CreateAccount( String username, String password, String Fname, String Lname) {
        String sql = "INSERT INTO accounts(username, password, Fname, Lname) VALUES (?, ?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1,username);//set the username to account
            pstmt.setString(2,password);//set the password to account
            pstmt.setString(3,Fname);//set the username to account
            pstmt.setString(4,Lname);//set the password to account
            pstmt.executeUpdate();//execute the update on account


            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) {

                createDebtCard(keys.getInt(1));
                return new CheckingAccount(keys.getInt(1),Fname,Lname,username,password, 0.0, 30);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());

        }

        return null;

    }
    //create debit card data
    public static void createDebtCard(int accountid) {
        String sql = "INSERT INTO debit_card(account_id) VALUES (?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setInt(1,accountid);//set the account_id to account
            pstmt.executeUpdate();//execute the update on account
            System.out.println("✅ DebitCard created succesfully!");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());

        }

    }
    //Create credit card data
    public static CreditAccount createCreditCard(int accountid) {
        String sql = "INSERT INTO credit_card(account_id) VALUES (?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setInt(1,accountid);//set the account_id to account
            pstmt.executeUpdate();//execute the update on account
            System.out.println("^ DebitCard created succesfully!");

            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) {


                return new CreditAccount(1000,0.0,22.78, 0.0);
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());

        }
        return null;
    }



    //----------------updates data Area--------------------//
    public static void updateDebitCardBalance(int account_id,double updateBalance) {
        String sql = "UPDATE debit_card SET accountBalance = ? WHERE account_id = ?";

        try(Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setDouble(1,updateBalance);
            pstmt.setInt(2, account_id);
            pstmt.executeUpdate();

        }catch (SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    public static void updateCreditCard(int account_id, double updateCreditBalance, double AvailableCredit)
    {
        String sql = "UPDATE credit_card SET available_credit = ?, credit_used = ? WHERE account_id = ?";

        try(Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setDouble(1,AvailableCredit);
            pstmt.setDouble(2,updateCreditBalance);
            pstmt.setInt(3, account_id);
            pstmt.executeUpdate();

        }catch (SQLException e){
            System.out.println("Error: " + e.getMessage());
        }

    }






    //-------------------log in Area------------------------//
    //return an account if username and password exist in the database
    public static CheckingAccount logInCredentials(String username, String password){
        String sql = "SELECT * FROM accounts JOIN debit_card ON accounts.account_id JOIN credit_card ON accounts.account_id = debit_card.account_id WHERE username = ?";
        CreditAccount hasCredit;
        try(Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1,username);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
            {
                hasCredit = CreditAccountVerification(rs.getInt("account_id"));
                System.out.println("User found.");
                if (rs.getString("password").equals(password))
                {
                    //if don't have a credit card just return the debit card info
                    if(hasCredit == null){
                        return new CheckingAccount(
                                rs.getInt("account_id"),
                                rs.getString("Fname"),
                                rs.getString("Lname"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getDouble("accountBalance"),
                                rs.getInt("lateFees")

                        );
                    }
                    //else return credit and debit card info
                    else{
                        return new CheckingAccount(
                                rs.getInt("account_id"),
                                rs.getString("Fname"),
                                rs.getString("Lname"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getDouble("accountBalance"),
                                rs.getInt("lateFees"),
                                hasCredit

                        );
                    }

                }
                else
                {
                    System.out.println("Incorrect Password...");
                    return null;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Accounts either does not exist or Username is wrong...");
        return null;
    }

    //--------------creditAccountVerification----------//
    public static CreditAccount CreditAccountVerification(int account_id)
    {
        String sql = "SELECT * FROM credit_card WHERE account_id = ?";


        try(Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1,account_id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
            {
                return new CreditAccount(
                    rs.getDouble("available_credit"),
                    rs.getDouble("credit_used"),
                    rs.getDouble("interesRate"),
                    rs.getDouble("minPayment"));
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    //--------------show Account-------------//
    public static void showAllAccount() {
    String sql = "SELECT * FROM accounts";

    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getInt("account_id") + ", " + rs.getString("username") + ", " + rs.getString("password"));
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
    //show all debit cards
    public static void showAllDebitCards() {
        String sql = "SELECT * FROM debit_card";

        try(Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                System.out.println(rs.getInt("account_id")+", "+rs.getInt("accountBalance"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }


}
    //show all debit cards
    public static void showAllCreditCards() {
        String sql = "SELECT * FROM credit_card";

        try(Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                System.out.println(rs.getInt("account_id")+", "+rs.getInt("available_credit")+
                        ", "+rs.getInt("credit_used"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }


    }


    //-------------delete Tables Area----------------//
    public static void deleteAccountsTable() {
        String sql = "DROP TABLE IF EXISTS accounts";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("^ 'accounts' table deleted.");
        } catch (SQLException e) {
            System.out.println("! Error deleting table: " + e.getMessage());
        }
    }

    public static void deleteDebitCardTable() {
        String sql = "DROP TABLE IF EXISTS debit_card";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("^ 'debit cards' table deleted.");
        } catch (SQLException e) {
            System.out.println("! Error deleting table: " + e.getMessage());
        }
    }

    public static void deleteCreditCardTable() {
        String sql = "DROP TABLE IF EXISTS credit_card";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("^ 'credit cards' table deleted.");
        } catch (SQLException e) {
            System.out.println("! Error deleting table: " + e.getMessage());
        }
    }


}

