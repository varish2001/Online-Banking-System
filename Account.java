import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Create a scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Get account details from user
        System.out.print("Enter Account Number: ");
        String accNo = scanner.nextLine();

        System.out.print("Enter Account Holder Name: ");
        String accHolder = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Initial Balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine(); // consume leftover newline

        System.out.print("Enter Account Type (e.g., Savings/Current): ");
        String accType = scanner.nextLine();

        // Create account
        Account account = new Account(accNo, accHolder, email, balance, accType);

        // Menu loop
        int choice;
        do {
            System.out.println("\n----- Safe Cash Banking -----");
            System.out.println("1. Display Account Details");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    account.displayDetails();
                    break;
                case 2:
                    System.out.print("Enter amount to deposit: ");
                    double depAmount = scanner.nextDouble();
                    account.deposit(depAmount);
                    break;
                case 3:
                    System.out.print("Enter amount to withdraw: ");
                    double witAmount = scanner.nextDouble();
                    account.withdraw(witAmount);
                    break;
                case 4:
                    System.out.println("Thank you for using Safe Cash Banking!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 4);

        scanner.close();
    }
}
