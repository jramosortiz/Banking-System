  abstract class  Bank {

    private int accountID;
    private String Fname;
    private String Lname;
    private  String userName;
    private final String password ;


    Bank(int accountID, String Fname, String Lname, String Username, String Password)
    {
        this.accountID = accountID;
        this.Fname =Fname;
        this.Lname = Lname;
        this.userName = Username;
        this.password = Password;
    }


      //Setter and Getters
      public void setFname(String fname) {
          Fname = fname;
      }

      public void setLname(String lname) {
          Lname = lname;
      }


      int getAccountID(){return this.accountID;}
      String getFname(){return this.Fname;}
      String getLname(){return this.Lname;}
      String getUserName(){return  this.userName;}
      String getPassword(){return this.password;}


  }
