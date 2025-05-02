# **Accounting Ledger Capstone**


# Most interesting part of the code

I was able to figure out how to allow the user to keep track of their total balance on the account.

```java

// static double to use in other methods when finding the account balance.
 static double accountBalance = 0.0;


    // THIS METHOD WILL SHOW THE CURRENT ACCOUNT BALANCE

    public static void showTotalBalance(ArrayList<Transaction> transactions){
        // the account balance will start at 0.
        double balance = 0.0;

        // for each transaction in the transactions array that we will temporarily call 'totalBalance'.
        for(Transaction totalBalance : transactions){
            // then get the amount from each transaction and add it to the balance. 
            balance += totalBalance.getAmount();
        }
        divider();
        // format the print statement. 
        System.out.printf("\nCURRENT BALANCE: $%.2f\n", balance);
        waiting();
        doSomethingElseMain();
    }

```




# PART 1
### Construct the transactions. What pieces of information will each transaction contain? 

- I created a class called "Transaction" to define what a transaction will contain inside of it.<br/>
- Kind of like building a house. Before we build the more technical parts of a house, we determine what we want inside of the house and create the foundation.


  
```java
public class Transaction {

    private String date;
    private String time;
    private String description;
    private String vendor;
    private double amount;

    public Transaction(String date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

```


- I automtically set the getters and setter, but we most likely will not use the setters for this project.

  

```java
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

```


- I add a method that is helpful for converting the information that will be written onto the file as a string.
  

```java
    public String toString(){
        return String.format("%s | %s | %s | %s | $%.2f", date, time, description, vendor, amount);
    }
}
```



# PART 2
### Initialize `static` variables

- Identify the path of the file and name a variable for the file itself.

  
```java
    static String filePath = "src/main/resources/transactions.csv";
    static File transactionFile = new File(filePath);
```


- I made an `ArrayList` and a `HashMap` that pulls String variables from the Array List.

  

```java
    static ArrayList<Transaction> transactionArrayInfo = new ArrayList<>();
    static HashMap<String, ArrayList<Transaction>> transactionHashMap = new HashMap<>();
```

   

- Variables for Date and Time along with a formatter to correctly formatting the date and time.
  

  

```java
    static LocalDateTime dateVariable = LocalDateTime.now();
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
```



- Pull the LocalDateTime variable and the formatter together into a String.
- Then use a splitter to separate the date and time from each other by the `" "`.
  

  ```java
      static String changeDateFormatToString = dateVariable.format(dateFormatter);
      static String[] splitDate = changeDateFormatToString.split(" ");
      static String yearMonthDay = splitDate[0];
      static String hourMinuteSecond = splitDate[1];
  ```


  

- Import a new Scanner for the users input.

  ```java
    static Scanner userInput = new Scanner(System.in);
  ```


- Make a variable ofr the account balaance. It will be used later to calculate the total balance on the account.


```java
 static double accountBalance = 0.0;
```



# Part 3 
## FUNCTIONALITY


- The main method will read the file and calculate the current balance of the account.
- The very first thing that happens when we run the code is file reading and welcoming the user.
  

```java

    // MAIN METHOD

    public static void main(String[] args) {
        // I know that I want the file to be read first.
        // So, I make a method.
        readFileFirst();
        previousBalance();

        System.out.println("WELCOME...");
        waiting();
        divider();
        mainMenu();
    }

// ----------------------------------------------------------------------------

// THIS METHOD WILL READ THE FILE.

 public static void readFileFirst(){
        try {
            // Create a Buffered reader and Writer
            BufferedReader readTheFile = new BufferedReader(new FileReader(transactionFile));
            // Make sure to skip the header row with this function.
            // This function is saying to read the first line of the file before moving forward.
            readTheFile.readLine();

              // String varibale for the lines that are being read. 
              String theLineWeRead;

            // We are saying that while the String variable we initialized above contain the lines that are being read,
            while((theLineWeRead = readTheFile.readLine()) != null ){

                // then split the lines by the pipe.
                String[] transactionInfoParts = theLineWeRead.split("\\|");

                // and if those parts are equal to 5
                if (transactionInfoParts.length == 5){

                    // then here is what each of those 5 parts are.
                    yearMonthDay = transactionInfoParts[0].trim();
                    hourMinuteSecond = transactionInfoParts[1].trim();
                    String description = transactionInfoParts[2].trim();
                    String vendor = transactionInfoParts[3].trim();
                    double amount = Double.parseDouble(transactionInfoParts[4].trim().replace("$", ""));

                    // Now put all the parts we dfined back in a new box, so we can use that box as the reference to the transaction.
                    Transaction splitTransaction = new Transaction(yearMonthDay, hourMinuteSecond, description, vendor, amount);

                    // Add the box we just closed to the storage which is the main transaction Array List 
                    transactionArrayInfo.add(splitTransaction);

                    String grabTheVendor = vendor.toLowerCase();
                    if (!transactionHashMap.containsKey(grabTheVendor)){
                        transactionHashMap.put(grabTheVendor, new ArrayList<>());
                    }
                    transactionHashMap.get(grabTheVendor).add(splitTransaction);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

// -------------------------------------------------------------------------------------------


    // THIS METHOD CALCULATES THE CURRENT BALANCE OF THE ACCOUNT.

    public static void previousBalance(){

        // for the Transaction class we will refer to as `money` inside the Array List, keep adding the positive amounts of each transaction and add the total to the account balance. 
        for (Transaction money : transactionArrayInfo){
            accountBalance += money.getAmount();
        }
    }



    // THIS METHOD IS TO USE AS A TIMER IN BETWEEN PROMPTS.

  public static void waiting () {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Something went wrong while loading...");
        }
    }
```





- The user will be taken to the main menu where they will be able to either Add a deposit, make a payment, show current account balance, or see the Ledger menu. 
- They can also Exit the application if they want.
- Make sure there is also a way to write on the file, so the users deposits and methods can be documented.




```java
// METHOD FOR WRITING ON THE FILE.

   public static void writeOnTheFile(String theLog){

    // Initialize a file writer inside a buffered writer. 
        try (BufferedWriter writer1 = new BufferedWriter(new FileWriter(transactionFile, true))) {

    // tell the buffered writer to write everything that's stored in 'theLog` when we call it. 
            writer1.write(theLog);

            // Tell the buffered writer to write on a new line. 
            writer1.newLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

// ------------------------------------------------------------------------------

// THIS METHOD WILL PROMPT THE USER TO ADD A DEPOSIT.

  public static void addDeposit() {
        // 
        try {
            System.out.println("How much would you like to add?");

            // Make an array for the amount so we can efficiently modify the amount value.
            // What this is doing is making the amount variable an object that can be passed to and changed in different methods.
            // Since a regular double is a primitive data type, it cannot be passed of and altered 
            double[]amount = new double[1];   //<------- Make sure to let the array know that there will be one object inside of it. 

            // Convert the string that the file is reading to a double.
            amount[0] = Double.parseDouble(userInput.nextLine().trim());

            // Confirm if the deposit if the deposit is correct. 
            confirmDeposit(amount);


            // Tell the account balance to add the amount that the user confirmed. 
            accountBalance += amount[0];


            // Create a LocalDateTime variable to get the current date and time.
            // Make a DateTime formatter to create the format of the Date and the time.
            LocalDateTime dateVariable = LocalDateTime.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

            // I am going to prepare to split the date format by the date and the time.
            // First, I make a string variable to connect the dateVariable and dateFormatter so the date can be read as a string.
            String changeDateFormatToString = dateVariable.format(dateFormatter);

            // I create a string variable to split the date string I just made.
            String[] splitDate = changeDateFormatToString.split(" ");

            // Then, I establish each part of the split DateTime variable.
            String yearMonthDay = splitDate[0];
            String hourMinuteSecond = splitDate[1];


            // Now use the transaction class to make a new varibale that will hold the new input from the user after making a deposit. 
            Transaction deposit = new Transaction(yearMonthDay, hourMinuteSecond, "Personal Deposit", "David Lisk", amount[0]);

            // Make sure the deposit variable with this specific transaction gets placed in the Array List. 
            transactionArrayInfo.add(deposit);

            // Now call the method that writes to the file
            writeOnTheFile(String.valueOf(deposit));

            System.out.printf("Deposit Completed.\nNew Balance: $%.2f\n", accountBalance);


        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }


        doSomethingElseMain();

    }


// ---------------------------------------------------------------------------------------

// THIS METHOD WILL PROMPT THE USER TO MAKE A PAYMENT

    public static void makePayment(){

        try {
            System.out.println("WHo's the payment for?");
            System.out.print("Enter:  ");
            // Make a new string array that holds only one item.
            String[] vendor = new String[1];
            // The vendor will be in index '0' of that string array.
            vendor[0] = capitalizedwords(userInput.nextLine().trim());

            System.out.println("\nHow much are you paying?");
            System.out.print("Enter:   ");
            // create a double array that holds only one item
            double[] amount = new double[1];
            // the amount will be the first index position of that array.
            amount[0] = Double.parseDouble(userInput.nextLine());

            confirmPayment(vendor, amount);

            if (amount[0] > accountBalance){
                System.out.println("Insufficient funds. Payment Cancelled.");
                waiting();
                doSomethingElse();
            }

                // Since the user is making a payment, this time the amount that they confirmed will be subtracted from the total balance. 
                accountBalance -= amount[0];


    LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String[] splitDate = now.format(formatter).split(" ");
            String yearMonthDay = splitDate[0];
            String hourMinuteSecond = splitDate[1];


            // Make a variable to hold the new payment that will be logged onto the file.
            Transaction paymentMade = new Transaction(yearMonthDay, hourMinuteSecond, "Payment", vendor[0], -amount[0]);
            transactionArrayInfo.add(paymentMade);


            // Bring the `paymentMade` variable to the method that writes on the file. 
            writeOnTheFile(String.valueOf(paymentMade));

            // Print a statement that confirms that the payment was made and also show the updated account balance.
            System.out.printf("Payment completed.\nCurrent Balance: $%.2f", accountBalance);



        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        doSomethingElseMain();

    }
```




- ***Methods for confirming the users deposit and payment***



  ```java
    // THESE METHODS WILL ASK THE USER TO CONFIRM A DEPOSIT OR A PAYMENT.
    // We use a boolean initializer for the method because we are returning a value after we check to see if that value is true. 
  
    public static boolean confirmDeposit(double [] holdAmount){

        // As far as we know, the users input is true so far. So we wrap the prompts in a while statement to determine if the users input needs to be changed or not.  
        while(true){

            // Ask if the users held amount is correct.
            System.out.printf("\nYou entered:\nAmount: $%.2f ", holdAmount[0]);
            System.out.print("\n\nIs this correct? Y or N:  ");
            String confirmInput = userInput.nextLine().trim().toUpperCase();

            // If yes, then return the statement that was already true. If no, then return the statement sd false.
            if(confirmInput.equals("Y")){
                return true;
            } else if(confirmInput.equals("N")) {
                System.out.print("Enter the correct amount or X to cancel: ");

                String cancelInput = userInput.nextLine().trim();

                if(cancelInput.equalsIgnoreCase("X")){
                    doSomethingElseMain();
                    return false;
                }
                try{
                    holdAmount[0] = Double.parseDouble(cancelInput);
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }

            } else {
                System.out.println("Invalid option. Please enter Y or N.");
            }
        }
    }

  // -------------------------------------------------------------------------------
  // METHOD FOR PAYMENT CONFIRMATION

  // We write this the same way as the `confirmDeposit` method except now add an 

    public static boolean confirmPayment(String[] holdVendor, double[] holdAmount){
        while(true){
            System.out.printf("\nName: %s\nAmount $%.2f\n", holdVendor[0], holdAmount[0]);
            System.out.println("\n\nIs this correct? Y or N: ");
            String confirmInput = userInput.nextLine().trim().toUpperCase();

            if(confirmInput.equals("Y")){
                return true;
            } else if (confirmInput.equals("N")){
                System.out.println("What would you like to change?");
                System.out.println("1) Name");
                System.out.println("2) Amount");
                System.out.println("3) Cancel (go back)");

                String userChoiceForPaymentConfirmation = userInput.nextLine().trim();

                if(userChoiceForPaymentConfirmation.equals("1")){
                    System.out.println("Enter Name or X to cancel:  ");

                    String cancelName = userInput.nextLine().trim();

                    if(cancelName.equalsIgnoreCase("X")){
                        continue;
                    }


                    // This function gives the vendor input to the capitalized words method and brings it back.
                    holdVendor[0] = capitalizedwords(userInput.nextLine().trim());


                } else if (userChoiceForPaymentConfirmation.equals("2")){
                    System.out.println("Enter Amount: ");
                    String cancelAmount = userInput.nextLine().trim();

                    if(cancelAmount.equalsIgnoreCase("X")){
                        continue;
                    }



                    try {
                        holdAmount[0] = Double.parseDouble(userInput.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount");
                        throw new RuntimeException(e);
                    }
                    }
                else if (userChoiceForPaymentConfirmation.equals("3")){
                    System.out.println("Cancelling...");
                    doSomethingElseMain();
                    return false;

                } else {
                    System.out.println("Invalid Choice.");
                }
            } else {
                System.out.print("Invalid input. Please enter Y or N: ");
            }
        }
    }

  // ------------------------------------------------------------------------------------
  // THIS METHOD ENSURES THAT THE SEPAARATE WORDS ARE CAPITALIZED FOR A SMOOTHER SEARCH

      public static String capitalizedwords(String input){
        String[] nameParts = input.trim().split("\\s+");
        StringBuilder capitalized = new StringBuilder();
        for (String name : nameParts){
            if (!name.isEmpty()){
                char firstLetter = Character.toUpperCase(name.charAt(0));
                String restOfName = name.substring(1).toLowerCase();
                capitalized.append(firstLetter).append(restOfName).append(" ");
            }
        }
        return capitalized.toString().trim();
    }
  ```


### Ledger Menu 


- The ledger menu will allow the user to show all transactions, show all deposits or payments only, or to be taken to shoose more specific report options.


```java
 // THIS METHOD BRINGS THE USER TO THE LEDGER MENU
    public static void ledgerMenu(){

        boolean loop = true;

        while(loop) {

                System.out.println("A) Display All.");
                System.out.println("D) Deposits.");
                System.out.println("P) Payments.");
                // The reports choice will take user to the reports ledger menu
                System.out.println("R) Reports.");
                System.out.println("\nPRESS 0 TO EXIT");
                System.out.print("\nEnter:    ");

                String ledgerChoice = userInput.nextLine().trim().toUpperCase();


                switch (ledgerChoice){
                    case "A":
                        seeAllTransactions();
                        break;

                    case "D":
                        depositLedger();
                        break;

                    case "P":
                        paymentLedger();
                        break;

                    case "R":
                        reportsLedgerMenu();
                        break;


                    case "0":
                        mainMenu();
                        loop = false;
                        break;

                    default:
                        System.out.println("Not an option. Please try again");
                }
            }
    }
  ```


- Here are all the methods for each option from the ledger menu.
- Reports will be skipped for now since that option provides a separate sub-menu.


```java
    //----------------------- THESE METHODS BRING THE USER FROM THE LEDGER MENU OPTIONS ------------------------------


// METHOD THAT SHOWS ALL TRANSACTIONS IN THE FILE

    public static void seeAllTransactions(){
        System.out.println("ALL TRANSACTIONS\n");
        for (Transaction viewTransactions : transactionArrayInfo){
            System.out.println(viewTransactions);
        }

        System.out.printf("\nCurrent Balance:  $%.2f%n", accountBalance);

        doSomethingElse();
    }

//------------------------------------------------------------------------------
// METHOD FOR SHOWING ALL DEPOSITS

    public static void depositLedger(){
        System.out.println("DEPOSITS\n");

        for (Transaction viewDeposits : transactionArrayInfo){
            if (viewDeposits.getAmount() > 0){
                System.out.println(viewDeposits);
            }
        }
        doSomethingElse();
    }

// -----------------------------------------------------------------------
// METHOD FOR SHOWING ALL PAYMENTS

    public static void paymentLedger(){
        System.out.println("PAYMENTS\n");

        for (Transaction viewPayments : transactionArrayInfo){
            if (viewPayments.getAmount() < 0){
                System.out.println(viewPayments);
            }
        }
        doSomethingElse();
    }

```



- If the user chooses the reports option in the `ledgerMenu`, then they will be taken to a sub-menu.


```java
   // THIS METHOD IS A MENU FOR SEEING THE DIFFERENT TYPES OF REPORTS FROM THE LEDGER.
    public static void reportsLedgerMenu() {

        boolean loop = true;

        while (loop) {
            System.out.println("1) Current Month");
            System.out.println("2) Previous Month");
            System.out.println("3) Current Year");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Go Back");
            System.out.println("H) Main Menu");

            String reportLedgerChoice = userInput.nextLine().trim().toUpperCase();

            switch (reportLedgerChoice){
                case "1":
                    showTransactionsForCurrentMonth();
                    break;
                case "2":
                    showTransactionsForPreviousMonth();
                    break;
                case "3":
                    showTransactionsForCurrentYear();
                    break;
                case "4":
                    showTransactionsForPreviousYear();
                    break;
                case "5":
                    searchByVendor();
                    break;
                case "6":
                    customSearch();
                    break;
                case "0":
                    ledgerMenu();
                    break;
                case "H":
                    mainMenu();
                    loop = false;
                    break;

                default:
                    System.out.println("Please choose a valid option.");
            }
        }
    }

```


- Let's quickly go through what each option does


```java
    // ---------------------------  THESE ARE ALL THE METHODS FOR SHOWING MORE SPECIFIC REPORTS  --------------------------------


    // METHOD FOR SHOWING TRANSACTIONS FOR THE CURRENT MONTH.

    public static void showTransactionsForCurrentMonth(){
        System.out.println("CURRENT MONTH TRANSACTIONS\n");

        LocalDateTime current1 = LocalDateTime.now();

        // Initialize string variables for the current month and year.
        String currentYear = String.valueOf(current1.getYear());

        // Get the month value from the string format of the LocalDateTime variable 
        String currentMonth = String.format("%02d", current1.getMonthValue());

        
        // For the Transaction class we will temporarily use as 'allCurrentMonth' from the Transaction Array List, 
        for (Transaction allCurrentMonth : transactionArrayInfo){

            // if we get the a date that starts with the current year - current month, 
            if (allCurrentMonth.getDate().startsWith(currentYear + " - " + currentMonth)) {

                // then print the results. 
                System.out.println(allCurrentMonth);
            }
        }
}


// --------------------------------------------------------------------------------------
// THE SAME PROCESS WITH SHOWING RESULTS FROM THE PREVIOUS MONTH

    public static void showTransactionsForPreviousMonth(){
        System.out.println("PREVIOUS MONTH TRANSACTIONS\n");

        // This time the main thing we do differently is subtract a month inside the LocalDateTime function using '.minusMonths' 
        LocalDateTime previous1 = LocalDateTime.now().minusMonths(1);

        String previousYear = String.valueOf(previous1.getYear());
        String previousMonth = String.format("%02d", previous1.getMonthValue());

       for (Transaction allPreviousMonth : transactionArrayInfo){
           if (allPreviousMonth.getDate().startsWith(previousYear + " - " + previousMonth)){
               System.out.println(allPreviousMonth);
           }
       }


    }

// ----------------------------------------------------------------------
// SAME FORMULA FOR SEARCHING FOR PREVIOUS YEAR TRANSACTIONS

    public static void showTransactionsForCurrentYear(){
        System.out.println("CURRENT YEAR TRANSACTIONS\n");

        String currentYear = String.valueOf(LocalDateTime.now().getYear());

        for (Transaction allCurrentYear : transactionArrayInfo){
            if (allCurrentYear.getDate().startsWith(currentYear)){
                System.out.println(allCurrentYear);
            }
        }

}


// -----------------------------------------------------------------------
// I DID THE PREVIOUS YEAR FUNCTIONS A LITTLE DIFFERENT JUST TO FIND ANOTHER WAY OF PERFORMING THE ACTION. 


    public static void showTransactionsForPreviousYear(){
        System.out.println("PREVIOUS YEAR TRANSACTIONS\n");

        // Stuff the localDateTime function inside a string variable and ensure that we grab the current year and subtract 1 from that. 
        String currentYear = String.valueOf(LocalDateTime.now().getYear() - 1);

        for (Transaction allPreviousYear : transactionArrayInfo){
            if (allPreviousYear.getDate().startsWith(currentYear)){
                System.out.println(allPreviousYear);
            }
        }

    }

// ------------------------------------------------------------------------------------------
// THIS METHOD ALLOWS THE USER TO SEARCH BY VENDOR. 

    public static void searchByVendor(){
        System.out.println("ENTER A NAME: ");

        String name = userInput.nextLine().trim().toLowerCase();

      // Make a new Array List as a placeholder for whichever vendor names are pulled from the transactions Array List.
      // In this case, I am using a HashMap to pull that information from the file. 
      ArrayList<Transaction> transactionsUnderThisVendor = transactionHashMap.get(name);


      // If there are still transactions to read under this vendors name 
      if (transactionsUnderThisVendor != null && !transactionsUnderThisVendor.isEmpty()){

          // then for the Transaction class we will refer to as 'viewVendors' in the vendor placeholder Array List from above, 
          for (Transaction viewVendors : transactionsUnderThisVendor){

              // Show the results we pulled 
              System.out.println(viewVendors);
          }
      } else {
          // If there are no lines to read with this vendors name, then we will print the following statement. 
          System.out.println("No transactions found under " + name);
      }

        doSomethingElse();
    }
  ```


# PART 4

## Other methods and functions for user experience 


### DO SOMETHING ELSE

- I used a couple of methods to ensure that the application does not immediately shut down on the user after they finish a task
- I am sure there was a more efficient way of doing this, but the code works smoothly.


```java
   // THIS METHOD ASKS THE USER IF THEY WOULD LIKE TO DO SOMETHING ELSE.

    public static void doSomethingElse(){
        System.out.println("\nWould you like to do something else?");
        System.out.print("Please enter Y or N: ");

       

        while(true){
         String doSomething = userInput.nextLine().trim().toUpperCase();
            switch (doSomething){
                case "Y":
                    System.out.println("\nOkay, one moment...");
                    waiting();
                    ledgerMenu();
                    return;
                case "N":
                    System.out.println("\nOkay, have a good day!");
                    waiting();
                    userInput.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input....");
            }
        }
    }

// --------------------------------------------------------------------------------------
    // THIS IS A SECOND DO SOMETHING ELSE METHOD THAT BRINGS USER TO MAIN MENU.

    public static void doSomethingElseMain(){
        System.out.println("\nWould you like to do something else?");
        System.out.print("Please enter Y or N: ");

        

        while(true){
          String doSomething = userInput.nextLine().trim().toUpperCase();
            switch (doSomething){
                case "Y":
                    System.out.println("\nOkay, one moment...\n");
                    waiting();
                    mainMenu();
                    return;
                case "N":
                    System.out.println("\nOkay, have a good day!");
                    waiting();
                    userInput.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input....");
            }
        }
    }
```


### CUSTOM SEARCH


- Here is a method that allows the user to do a custom search.
- The user will be prompted to input a date, a description and a name.
- The user is able to leave no more than two out of three of the prompts blank in order for the search to work.
- The description may be an uneccessary prompt since all the descriptions are either Personal Deposits or Payments, but it is still nice to have and practice working in the   code. 


```java
    // THIS IS A METHOD FOR CUSTOM SEARCHES

    public static void customSearch(){
            System.out.println("\nCUSTOM SEARCH\n");

            System.out.print("Enter a date: ");
            String dateInput = userInput.nextLine().trim();

            System.out.print("\nDescription: ");
            String descriptionInput = userInput.nextLine().trim().toLowerCase();

            System.out.print("Enter a name: ");
            String vendorInput = userInput.nextLine().toLowerCase();


            if(dateInput.isEmpty() && descriptionInput.isEmpty() && vendorInput.isEmpty()){
                System.out.println("\nPlease enter something in at least 1 field.");
                waiting();
                return;
            }

            System.out.println("\nRESULTS\n");

            boolean foundAnythingAtAll = false;

            for (Transaction searchResults : transactionArrayInfo) {

                boolean resultsAreFound = true;
                if (!dateInput.isEmpty() && !searchResults.getDate().startsWith(dateInput)){
                    resultsAreFound = true;
                }
                if (!descriptionInput.isEmpty() && !searchResults.getDescription().toLowerCase().contains(descriptionInput)){
                    resultsAreFound = false;
                }
                if (!vendorInput.isEmpty() && !searchResults.getVendor().toLowerCase().contains(vendorInput)){
                    resultsAreFound = false;
                }

                if (resultsAreFound){
                    System.out.println(searchResults);
                    foundAnythingAtAll = true;
                }


            }
            if (!foundAnythingAtAll){
                System.out.println("Nothing was found.");
            }

            doSomethingElse();
        }
```



## TOTAL ACCOUNT BALANCE

- I added an extra method to allow the user to see their total account balance which will also display when the user makes a deposit or a payment.


```java
 // THIS METHOD WILL SHOW THE CURRENT ACCOUNT BALANCE

    // Brig the Transaction Array List in the method. 
    public static void showTotalBalance(ArrayList<Transaction> transactions){

        // The balance starts at 0 before it starts calculating 
        double balance = 0.0;

        
        // We will count each amount from each transaction from the Array List in the the Transactions class 
        for(Transaction totalBalance : transactions){
            balance += totalBalance.getAmount();
        }

        System.out.printf("\nCURRENT BALANCE: $%.2f\n", balance);
        waiting();
        doSomethingElseMain();
    }
```


## DIVIDERS 

- Added dividers in between prompts and menus.

```java

    public static void divider(){

        System.out.println("◼".repeat(30));
    }


```

## Some minor changes were made after the making of this README file, such as adding loading screens and dividers. 








