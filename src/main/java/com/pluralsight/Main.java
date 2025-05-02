package com.pluralsight;


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    // Make static variables for DateTime, Scanners, File path, etc.
    static String filePath = "src/main/resources/transactions.csv";
    static File transactionFile = new File(filePath);

    // Create an Array List for holding all the different Transactions that will be added to the file.
    // Then make a hashmap for the data we will pull from each transaction when we search for a vendor.
    static ArrayList<Transaction> transactionArrayInfo = new ArrayList<>();
    static HashMap<String, ArrayList<Transaction>> transactionHashMap = new HashMap<>();

    // Create a LocalDateTime variable to get the current date and time.
    // Make a DateTime formatter to create the format of the Date and the time.
    static LocalDateTime dateVariable = LocalDateTime.now();
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    // I am going to prepare to split the date format by the date and the time.
    // First, I make a string variable to connect the dateVariable and dateFormatter so the date can be read as a string.
    static String changeDateFormatToString = dateVariable.format(dateFormatter);

    // I create a string variable to split the date string I just made.
    static String[] splitDate = changeDateFormatToString.split(" ");

    // Then, I establish each part of the split DateTime variable.
    static String yearMonthDay = splitDate[0];
    static String hourMinuteSecond = splitDate[1];


    // I make a variable for the scanner named userInput.
    static Scanner userInput = new Scanner(System.in);


    // This function sets the double for the account balance.
    static double accountBalance = 0.0;


    // NOW WE START WITH THE MAIN METHOD
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

    // THIS METHOD BRINGS THE USER TO THE MAIN MENU.
    public static void mainMenu(){

        while(true) {

            System.out.println("------------MAIN MENU------------\n");
            System.out.println("D. Add Deposit.");
            System.out.println("P. Make a payment.");
            System.out.println("L. Ledger.");
            System.out.println("B. Show current account balance");
            System.out.println("X. Exit.");
            divider();
            System.out.print("Enter: ");

            // Use new tool for the menu choice.
            String mainMenuChoice = userInput.nextLine().toUpperCase().trim();

            switch (mainMenuChoice) {
                case "D":
                    addDeposit();
                    break;
                case "P":
                    makePayment();
                    break;
                case "L":
                    ledgerMenu();
                    break;
                case "B":
                    showTotalBalance(transactionArrayInfo);
                    break;

                case "X":
                    userInput.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Please Enter a Valid option.\n");
                    waiting();
                    break;
            }
        }
    }








    // THIS IS THE READ FILE METHOD
    public static void readFileFirst(){
        try {
            // Create a Buffered reader and Writer
            BufferedReader readTheFile = new BufferedReader(new FileReader(transactionFile));
            // Make sure to skip the header row with this function.
            // This function is saying to read the first line of the file before moving forward.
            readTheFile.readLine();

            String theLineWeRead;
            while((theLineWeRead = readTheFile.readLine()) != null ){
                String[] transactionInfoParts = theLineWeRead.split("\\|");
                if (transactionInfoParts.length == 5){
                    yearMonthDay = transactionInfoParts[0].trim();
                    hourMinuteSecond = transactionInfoParts[1].trim();
                    String description = transactionInfoParts[2].trim();
                    String vendor = transactionInfoParts[3].trim();
                    double amount = Double.parseDouble(transactionInfoParts[4].trim().replace("$", ""));
                    Transaction splitTransaction = new Transaction(yearMonthDay, hourMinuteSecond, description, vendor, amount);
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

    // THIS METHOD WILL READ THE FILE.
    public static void writeOnTheFile(String theLog){
        try (BufferedWriter writer1 = new BufferedWriter(new FileWriter(transactionFile, true))) {

            writer1.write(theLog);
            writer1.newLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }







    // THIS METHOD FINDS AND CALCULATES THE ACCOUNT BALANCE
    public static void previousBalance(){
        for (Transaction money : transactionArrayInfo){
            accountBalance += money.getAmount();
        }
    }
    // THIS METHOD WILL SHOW THE CURRENT ACCOUNT BALANCE
    public static void showTotalBalance(ArrayList<Transaction> transactions){
        double balance = 0.0;

        for(Transaction totalBalance : transactions){
            balance += totalBalance.getAmount();
        }
        divider();
        System.out.printf("\nCURRENT BALANCE: $%.2f\n", balance);
        waiting();
        doSomethingElseMain();
    }









    // THESE METHODS WILL ALLOW THE USER TO ADD MONEY AND MAKE PAYMENTS.
    public static void addDeposit() {

        try {
            divider();
            System.out.println("How much would you like to add?");
            System.out.print("Enter:  ");
            double[]amount = new double[1];
            amount[0] = Double.parseDouble(userInput.nextLine().trim());

            confirmDeposit(amount);

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


            Transaction deposit = new Transaction(yearMonthDay, hourMinuteSecond, "Personal Deposit", "David Lisk", amount[0]);

            transactionArrayInfo.add(deposit);

            writeOnTheFile(String.valueOf(deposit));
            divider();
            System.out.printf("Deposit Completed.\nNew Balance: $%.2f\n", accountBalance);


        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }


        doSomethingElseMain();

    }

    public static void makePayment(){

        try {
            divider();
            System.out.println("WHo's the payment for?");
            System.out.print("Enter:  ");
            // Make a new string array that holds only one item.
            String[] vendor = new String[1];
            // The vendor will be in index '0' of that string array.
            vendor[0] = capitalizedwords(userInput.nextLine().trim());
            divider();
            System.out.println("\nHow much are you paying?");
            System.out.print("Enter:   ");
            // create a double array that holds only one item
            double[] amount = new double[1];
            // the amount will be the first index position of that array.
            amount[0] = Double.parseDouble(userInput.nextLine());
            divider();
            confirmPayment(vendor, amount);

            if (amount[0] > accountBalance){
                System.out.println("Insufficient funds. Payment Cancelled.");
                waiting();
                doSomethingElse();
            }
                accountBalance -= amount[0];

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String[] splitDate = now.format(formatter).split(" ");
            String yearMonthDay = splitDate[0];
            String hourMinuteSecond = splitDate[1];

            Transaction paymentMade = new Transaction(yearMonthDay, hourMinuteSecond, "Payment", vendor[0], -amount[0]);
            transactionArrayInfo.add(paymentMade);

            writeOnTheFile(String.valueOf(paymentMade));
            System.out.println("Payment processing...");
            waiting();
            divider();
            System.out.printf("Payment completed.\nCurrent Balance: $%.2f\n", accountBalance);



        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        doSomethingElseMain();

    }




    // THESE METHODS WILL ASK THE USER TO CONFIRM A DEPOSIT OR A PAYMENT.
    public static boolean confirmDeposit(double [] holdAmount){
        while(true){
            divider();
            System.out.printf("\nYou entered:\nAmount: $%.2f ", holdAmount[0]);
            System.out.print("\n\nIs this correct? Y or N:  ");
            String confirmInput = userInput.nextLine().trim().toUpperCase();

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

    public static boolean confirmPayment(String[] holdVendor, double[] holdAmount){
        while(true){
            divider();
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


                    holdVendor[0] = capitalizedwords(cancelName);


                } else if (userChoiceForPaymentConfirmation.equals("2")){
                    System.out.println("Enter Amount: ");
                    String cancelAmount = userInput.nextLine().trim();

                    if(cancelAmount.equalsIgnoreCase("X")){
                        continue;
                    }



                    try {
                        holdAmount[0] = Double.parseDouble(cancelAmount);
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






    // THIS METHOD BRINGS THE USER TO THE LEDGER MENU
    public static void ledgerMenu(){

        boolean loop = true;

        while(loop) {
                divider();
                System.out.println("----------  LEDGER MENU  ----------\n");
                System.out.println("A) Display All.");
                System.out.println("D) Deposits.");
                System.out.println("P) Payments.");
                // The reports choice will take user to the reports ledger menu
                System.out.println("R) Reports.");
                System.out.println("\nPRESS 0 TO FOR MAIN MENU");
                divider();
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
                        divider();
                        System.out.println("Not an option. Please try again");
                }
            }
    }








    // THESE METHODS BRING THE USER FROM THE LEDGER MENU OPTIONS
    public static void seeAllTransactions(){
        divider();
        System.out.println("\nLoading all transactions.....\n");
        waiting();
        System.out.println("ALL TRANSACTIONS\n");
        for (Transaction viewTransactions : transactionArrayInfo){
            System.out.println(viewTransactions);
        }

        System.out.printf("\nCurrent Balance:  $%.2f%n", accountBalance);

        doSomethingElse();
    }

    public static void depositLedger(){
        divider();
        System.out.println("\nLoading all deposits.....\n");
        waiting();
        System.out.println("DEPOSITS\n");

        for (Transaction viewDeposits : transactionArrayInfo){
            if (viewDeposits.getAmount() > 0){
                System.out.println(viewDeposits);
            }
        }
        doSomethingElse();
    }

    public static void paymentLedger(){
        divider();
        System.out.println("\nLoading all payments.....\n");
        waiting();
        System.out.println("PAYMENTS\n");

        for (Transaction viewPayments : transactionArrayInfo){
            if (viewPayments.getAmount() < 0){
                System.out.println(viewPayments);
            }
        }
        doSomethingElse();
    }

    // THIS METHOD IS A MENU FOR SEEING THE DIFFERENT TYPES OF REPORTS FROM THE LEDGER.
    public static void reportsLedgerMenu() {

        boolean loop = true;

        while (loop) {
            divider();
            System.out.println("\nLoading reports menu.....\n");
            waiting();
            System.out.println("----------  VIEW REPORTS  ----------\n");
            System.out.println("1) Current Month");
            System.out.println("2) Previous Month");
            System.out.println("3) Current Year");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Go Back");
            System.out.println("H) Main Menu");
            divider();
            System.out.print("Enter:   ");

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
                    System.out.println("\nPlease choose a valid option.");
            }
        }
    }


    // THIS IS A METHOD FOR CUSTOM SEARCHES
    public static void customSearch(){
        divider();
            System.out.println("\nCUSTOM SEARCH\n");

            System.out.print("Enter a date: ");
            String dateInput = userInput.nextLine().trim();
            divider();
            System.out.print("\nDescription: ");
            String descriptionInput = userInput.nextLine().trim().toLowerCase();
            divider();
            System.out.print("Enter a name: ");
            String vendorInput = userInput.nextLine().toLowerCase();


            if(dateInput.isEmpty() && descriptionInput.isEmpty() && vendorInput.isEmpty()){
                System.out.println("\nPlease enter something in at least 1 field.");
                waiting();
                return;
            }
            System.out.println("Results loading...");
            waiting();
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
                System.out.println("\nNothing was found.");
            }

            reportsLedgerMenu();
        }










    // THIS METHOD ASKS THE USER IF THEY WOULD LIKE TO DO SOMETHING ELSE.
    public static void doSomethingElse(){
        System.out.println("\nWould you like to do something else?");
        divider();
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
                    divider();
                    waiting();
                    userInput.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input....");
            }
        }
    }
    // THIS IS A SECOND DO SOMETHING ELSE METHOD THAT BRINGS USER TO MAIN MENU.
    public static void doSomethingElseMain(){
        System.out.println("\nWould you like to do something else?");
        divider();
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
                    divider();
                    waiting();
                    userInput.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input....");
            }
        }
    }










    // THESE ARE ALL THE METHODS FOR SHOWING MORE SPECIFIC REPORTS
    public static void showTransactionsForCurrentMonth(){
        divider();
        System.out.println("Results loading...\n");
        waiting();
        System.out.println("CURRENT MONTH TRANSACTIONS\n");

        LocalDateTime current1 = LocalDateTime.now();

        String currentYear = String.valueOf(current1.getYear());
        String currentMonth = String.format("%02d", current1.getMonthValue());

        for (Transaction allCurrentMonth : transactionArrayInfo){
            if (allCurrentMonth.getDate().startsWith(currentYear + "-" + currentMonth)) {
                System.out.println(allCurrentMonth);
            }
        }
}

    public static void showTransactionsForPreviousMonth(){
        divider();
        System.out.println("Results loading...\n");
        waiting();
        System.out.println("PREVIOUS MONTH TRANSACTIONS\n");

        LocalDateTime previous1 = LocalDateTime.now().minusMonths(1);

        String previousYear = String.valueOf(previous1.getYear());
        String previousMonth = String.format("%02d", previous1.getMonthValue());

       for (Transaction allPreviousMonth : transactionArrayInfo){
           if (allPreviousMonth.getDate().startsWith(previousYear + "-" + previousMonth)){
               System.out.println(allPreviousMonth);
           }
       }


    }

    public static void showTransactionsForCurrentYear(){
        divider();
        System.out.println("Results loading...\n");
        waiting();
        System.out.println("CURRENT YEAR TRANSACTIONS\n");

        String currentYear = String.valueOf(LocalDateTime.now().getYear());

        for (Transaction allCurrentYear : transactionArrayInfo){
            if (allCurrentYear.getDate().startsWith(currentYear)){
                System.out.println(allCurrentYear);
            }
        }

}

    public static void showTransactionsForPreviousYear(){
        divider();
        System.out.println("Results loading...\n");
        waiting();
        System.out.println("PREVIOUS YEAR TRANSACTIONS\n");

        String currentYear = String.valueOf(LocalDateTime.now().getYear() - 1);

        for (Transaction allPreviousYear : transactionArrayInfo){
            if (allPreviousYear.getDate().startsWith(currentYear)){
                System.out.println(allPreviousYear);
            }
        }

    }

    public static void searchByVendor(){
        divider();
        waiting();

        System.out.print("ENTER A NAME: ");

        String name = userInput.nextLine().trim().toLowerCase();

      ArrayList<Transaction> transactionsUnderThisVendor = transactionHashMap.get(name);

        System.out.println("Results loading...\n");
        waiting();

      if (transactionsUnderThisVendor != null && !transactionsUnderThisVendor.isEmpty()){
          for (Transaction viewVendors : transactionsUnderThisVendor){

              System.out.println(viewVendors);
          }
      } else {
          System.out.println("\nNo transactions found under " + name);
      }
        waiting();
        reportsLedgerMenu();
    }





















    public static void waiting () {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Something went wrong while loading...");
        }
    }

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


    public static void divider(){

        System.out.println("◼".repeat(30));
    }


}
