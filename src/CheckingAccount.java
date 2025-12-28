public class CheckingAccount extends Bank{

    private CreditAccount creditData ;
    private double balance;
    private int lateFees;

    CheckingAccount(int accountID, String Fname, String Lname, String Username, String Password, Double balance, int lateFees){
        super(accountID, Fname, Lname, Username, Password);
        this.balance = balance;
        this.lateFees = lateFees;
        this.creditData = new CreditAccount();
    }

    CheckingAccount(int accountID, String Fname, String Lname, String Username, String Password, Double balance,
                    int lateFees, CreditAccount creditAccount){
        super(accountID, Fname, Lname, Username, Password);
        this.balance = balance;
        this.lateFees = lateFees;
        this.creditData = creditAccount;
        this.creditData.setHas_Credit_CardToTrue();
    }

    //Setters
    void setBalance(double balance) {this.balance = balance;}
    void setLateFees(int fees){this.lateFees = fees;}
    void setCreditData(CreditAccount creditData){this.creditData = creditData;}

    //Getters
    double getBalance(){return this.balance;}
    int getLateFees(){return this.lateFees;}
    CreditAccount getCreditData(){return this.creditData;}

    //Deposit money to account balance
    void deposit(double deposit){this.balance += deposit;}
    //Withdraw money from account balance
    void withdraw(double withdraw){this.balance -= withdraw;}
    //view account balance


}
