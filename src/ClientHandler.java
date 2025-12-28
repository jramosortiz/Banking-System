import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private CheckingAccount account;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            while (account == null) {
                account = LogInMenu(in,out,socket);
            }

            String input;
            double amount;

            do {
                BankMenu(account,out);

                out.println("Please select an input: ");
                out.println("END");
                input = in.readLine();

                switch (input) {
                    case "1":
                        out.println("Deposit Amount: ");
                        out.println("END");
                        amount = Double.parseDouble(in.readLine());
                        account.deposit(amount);
                        Database.updateDebitCardBalance(account.getAccountID(), account.getBalance());
                        break;

                    case "2":
                        out.println("Withdraw Amount: ");
                        out.println("END");
                        amount = Double.parseDouble(in.readLine());
                        account.withdraw(amount);
                        Database.updateDebitCardBalance(account.getAccountID(), account.getBalance());
                        break;

                    case "3":
                        out.println("Payment Amount: ");
                        out.println("END");
                        amount = Double.parseDouble(in.readLine());
                        account.getCreditData().makeCreditCardPayment(amount);
                        Database.updateCreditCard(account.getAccountID(),
                                account.getCreditData().getAmountOwed(),
                                account.getCreditData().getAvailable_Credit());
                        break;

                    case "4":
                        out.println("Amount used: ");
                        out.println("END");
                        amount = Double.parseDouble(in.readLine());
                        account.getCreditData().useCreditCard(amount);
                        Database.updateCreditCard(account.getAccountID(),
                                account.getCreditData().getAmountOwed(),
                                account.getCreditData().getAvailable_Credit());
                        break;
                }

            } while (!input.equals("0"));

            socket.close();
            System.out.println("🔴 Client disconnected.");

        } catch (IOException e) {
            System.err.println("❌ I/O error: " + e.getMessage());
        }

    }
    private CheckingAccount LogInMenu(BufferedReader in, PrintWriter out, Socket clientSocket) throws IOException {
        out.println("\t\t ---Log In MENU---");
        out.println("1.Log In");
        out.println("2.Create Account");
        out.println("3.Exit");

        out.println("Select: ");
        out.println("END");

        String input = in.readLine();

        if (input.equals("1"))
        {
            //Log in
            //Get Username
            out.println("Username: ");
            out.println("END");
            String username = in.readLine();

            //get Password
            out.println("Password: ");
            out.println("END");
            String password = in.readLine();

            //Search in sqlite for account
            return Database.logInCredentials(username,password);

        }
        else if(input.equals("2"))
        {
            // Create an account
            //Get First Name
            out.println("Name: ");
            out.println("END");
            String Fname = in.readLine();

            //create a paswword
            out.println("Last name: ");
            out.println("END");
            String Lname = in.readLine();

            //create a username
            out.println("Username: ");
            out.println("END");
            String username = in.readLine();

            //create a password
            out.println("Password: ");
            out.println("END");
            String password = in.readLine();

            //ask if would like to apply to credit card
            out.println("Would you like a to apply for a credit card?");
            out.println("1.Yes");
            out.println("2.No");
            out.println("END");
            input = in.readLine();

            if(input.equals("1"))
            {
                out.println("Congratulations! You been accepted with a credit line of $1,000 with a 22.78 interest.");
                // if yes create a debit account but add a credit account to it
                CheckingAccount createdAccount = Database.CreateAccount(username,password,Fname,Lname);
                createdAccount.setCreditData(Database.createCreditCard(createdAccount.getAccountID()));


            }
            else{
                //else only create the debit account
                //send to sqlite to create an account so it can create a accountID
                CheckingAccount createdAccount = Database.CreateAccount(username,password,Fname,Lname);
                if(createdAccount != null)
                {
                    out.println("Account Created Successfully");
                    return createdAccount;
                }
                else
                {   out.println("Account error....");
                    out.println("END");
                }
            }


            return null;//return
        }
        else{
            // stop the program
            clientSocket.close();
            System.out.println("Client disconnected.");
            return null;
        }

    }

    private void BankMenu (CheckingAccount account ,PrintWriter out) {
        if(!account.getCreditData().getHas_Credit_Card())
        {
            out.println("\t\tMenu");
            out.println("1.Deposit");
            out.println("2.Withdraw");
            out.println("0.Exit");
            out.println("Balance: "+ account.getBalance());
        }
        else{
            out.println("\t\tMenu");
            out.println("1.Deposit");
            out.println("2.Withdraw");
            out.println("3.Make a credit card payment");
            out.println("4.Use credit card");
            out.println("0.Exit");
            out.println("Debit Account Balance: "+ account.getBalance());
            out.println("Available credit: " + (account.getCreditData().getAvailable_Credit()));
            out.println("Credit card Balance: " + account.getCreditData().getAmountOwed());
        }


    }

}



