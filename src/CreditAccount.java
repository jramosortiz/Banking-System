public class CreditAccount{

    private boolean has_Credit_Card = false;
    private double available_Credit;
    private double amountOwed;
    private double interesRate;
    private double minPayment;

    CreditAccount()
    {}

    CreditAccount(double available_Credit, double amountOwed, double interesRate, double minPayment )
    {
        this.available_Credit = available_Credit;
        this.amountOwed = amountOwed;
        this.interesRate = interesRate;
        this.minPayment =  minPayment;
        this.setHas_Credit_CardToTrue();
    }


    //Getters
    double getAmountOwed(){return amountOwed;}
    double getInteresRate(){return interesRate;}
    double getMinPayment(){return minPayment;}
    double getAvailable_Credit(){return  available_Credit;}
    boolean getHas_Credit_Card(){return has_Credit_Card;}

    //Setters
    void setAmountOwed(double newAmountOwed){this.amountOwed = newAmountOwed;}
    void setInteresRate(double newInterestRate){this.interesRate = newInterestRate;}
    void setMinPayment(double newMinPayment){this.minPayment = newMinPayment;}
    void setAvailable_Credit(int newAvailableCredit){this.available_Credit = newAvailableCredit;}
    //this will switch from false to true.
    void setHas_Credit_CardToTrue(){this.has_Credit_Card = true;}

    //General Fuctions
    void makeCreditCardPayment(double paymentAmount){
        this.amountOwed -= paymentAmount;
        this.available_Credit += paymentAmount;
    }

    void useCreditCard(double amountUsed){
        this.amountOwed += amountUsed;
        this.available_Credit -= amountUsed;
    }
}


