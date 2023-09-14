import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

abstract class Account {
    //abstract class for bank account
    private int accountID;
    private BigDecimal balance;

    public Account(int accountID, BigDecimal balance) {
        this.accountID = accountID;
        this.balance = balance;
    }

    public int getID() {
        return accountID;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public abstract BigDecimal calculateBenefit();//abstract method to benefit for different accounts

    public void deposit(BigDecimal cash) {
        //deposit fund
        balance = balance.add(cash);
        System.out.println("Deposited " + cash + " TL to the account. New balance: " + balance + " TL");
    }

    public void withdraw(BigDecimal cash) {
        //withdraw fund
        if (balance.compareTo(cash) >= 0) {
            balance = balance.subtract(cash);
            System.out.println("Withdrawn " + cash + " TL from the account. New balance: " + balance + " TL");
        } else {
            System.out.println("Insufficient balance! Transaction failed.");
        }
    }
}

class ShortTerm extends Account {
    //short term account class
    public ShortTerm(int accountID, BigDecimal balance) {
        super(accountID, balance);
    }

    @Override
    public BigDecimal calculateBenefit() {
        //calculate daily benefit balance and for account creation
        LocalDate today = LocalDate.now();
        LocalDate startDate = LocalDate.of(2023, 5, 5); //first date 5/05/2023
        int days = today.getDayOfYear() - startDate.getDayOfYear();
        BigDecimal dailyBenefit = getBalance().multiply(new BigDecimal("0.17")).divide(new BigDecimal("365"), 2, BigDecimal.ROUND_HALF_UP);
        return dailyBenefit.multiply(new BigDecimal(days));
    }
}

class LongTerm extends Account {
    public LongTerm(int accountID, BigDecimal balance) {
        super(accountID, balance);
    }

    @Override
    public BigDecimal calculateBenefit() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = LocalDate.of(2023, 5, 5); // Start date: 05.05.2023
        int days = today.getDayOfYear() - startDate.getDayOfYear();
        BigDecimal dailyBenefit = getBalance().multiply(new BigDecimal("0.24")).divide(new BigDecimal("365"), 2, BigDecimal.ROUND_HALF_UP);
        return dailyBenefit.multiply(new BigDecimal(days));
    }
}

class Special extends Account {
    //special account
    public Special(int accountID, BigDecimal balance) {
        super(accountID, balance);
    }

    @Override
    public BigDecimal calculateBenefit() {
        //calculate daily benefit
        LocalDate today = LocalDate.now();
        LocalDate startDate = LocalDate.of(2023, 5, 5); // Start date: 05.05.2023
        int days = today.getDayOfYear() - startDate.getDayOfYear();
        BigDecimal dailyBenefit = getBalance().multiply(new BigDecimal("0.12")).divide(new BigDecimal("365"), 2, BigDecimal.ROUND_HALF_UP);
        return dailyBenefit.multiply(new BigDecimal(days));
    }
}

class Current extends Account {
    //current account class
    public Current(int accountID, BigDecimal balance) {
        super(accountID, balance);
    }

    @Override
    public BigDecimal calculateBenefit() {
        //current account dont earn any benefits!
        return BigDecimal.ZERO;
    }

    @Override
    public void withdraw(BigDecimal cash) {
        if (getBalance().compareTo(cash) >= 0) {
            super.withdraw(cash);
        } else {
            System.out.println("Insufficient balance! Transaction failed.");
        }
    }
}

class Bank {
    //bank class
    private List<Account> accounts;
    private LocalDate currentDate;
    private Random random;

    public Bank() {
        accounts = new ArrayList<>();
        currentDate = LocalDate.of(2023, 5, 5); // Start date: 05.05.2023
        random = new Random();
    }

    public void setDDMMYY(int day, int month, int year) {
        //set date
        currentDate = LocalDate.of(year, month, day);
    }

    public LocalDate getDate() {
        //current date
        return currentDate;
    }

    public void createShortTerm(int accountID, BigDecimal balance) {
        //short term account if the balance requirement is met
        if (balance.compareTo(new BigDecimal("1000")) >= 0) {
            ShortTerm shortTerm = new ShortTerm(accountID, balance);
            accounts.add(shortTerm);
            System.out.println("Short term account created. Account number: " + accountID);
        } else {
            System.out.println("Minimum balance requirement not met for short-term account.");
        }
    }

    public void createLongTerm(int accountID, BigDecimal balance) {
        //long-term account if the balance requirement is met
        if (balance.compareTo(new BigDecimal("1500")) >= 0) {
            LongTerm longTerm = new LongTerm(accountID, balance);
            accounts.add(longTerm);
            System.out.println("Long term account created. Account number: " + accountID);
        } else {
            System.out.println("Minimum balance requirement not met for long-term account.");
        }
    }

    public void createSpecial(int accountID, BigDecimal balance) {
        //create new special account
        Special special = new Special(accountID, balance);
        accounts.add(special);
        System.out.println("Special account created. Account number: " + accountID);
    }

    public void createCurrent(int accountID, BigDecimal balance) {
        //create new current account
        Current current = new Current(accountID, balance);
        accounts.add(current);
        System.out.println("Current account created. Account number: " + accountID);
    }

    public void deposit(int accountID, BigDecimal cash) {
        //deposit to account
        Account account = findAccount(accountID);
        if (account != null) {
            account.deposit(cash);
        } else {
            System.out.println("Account not found!");
        }
    }

    public void withdraw(int accountID, BigDecimal cash) {
        //withdraw from account
        Account account = findAccount(accountID);
        if (account != null) {
            account.withdraw(cash);
        } else {
            System.out.println("Account not found!");
        }
    }

    public void showAccount(int accountID) {
        //details about the account
        Account account = findAccount(accountID);
        if (account != null) {
            System.out.println("Account number: " + accountID);
            System.out.println("Balance: " + account.getBalance() + " TL");
            System.out.println("Benefit: " + account.calculateBenefit() + " TL");
            BigDecimal totalBalance = account.getBalance().add(account.calculateBenefit());
            System.out.println("Balance after interest: " + totalBalance + " TL");
            System.out.println("------------------------");
        } else {
            System.out.println("Account not found!");
        }
    }

    public void sortition() {
        List<Account> specialAccounts = new ArrayList<>();
        for (Account account : accounts) {
            if (account instanceof Special) {
                specialAccounts.add(account);
            }
        }

        if (specialAccounts.isEmpty()) {
            System.out.println("Not enough special accounts for a lottery!");
        } else {
            int randomIndex = random.nextInt(specialAccounts.size());
            Account winner = specialAccounts.get(randomIndex);
            System.out.println("Lottery result: Winning account number is " + winner.getID());
        }
    }

    public void showAccountIDs() {
        //display all acount numbers
        System.out.println("All account numbers in the system:");
        for (Account account : accounts) {
            System.out.println(account.getID());
        }
    }

    private Account findAccount(int accountID) {
        for (Account account : accounts) {
            if (account.getID() == accountID) {
                return account;
            }
        }
        return null;
    }

    public void getAllAccounts() {
        //display all of details about all of the bank accounts
        for (Account account : accounts) {
            System.out.println("Account number: " + account.getID());
            System.out.println("Balance: " + account.getBalance() + " TL");
            System.out.println("Benefit: " + account.calculateBenefit() + " TL");
            BigDecimal totalBalance = account.getBalance().add(account.calculateBenefit());
            System.out.println("Balance after interest: " + totalBalance + " TL");
            System.out.println("------------------------");
        }
    }
}

public class Main {
    //main method for run the code
    public static void main(String[] args) {
        Bank bank = new Bank();

        // sample operations
        bank.createShortTerm(1, new BigDecimal("2000"));
        bank.createLongTerm(2, new BigDecimal("3000"));
        bank.createSpecial(3, new BigDecimal("4000"));
        bank.createCurrent(4, new BigDecimal("5000"));

        bank.deposit(1, new BigDecimal("1000"));
        bank.deposit(2, new BigDecimal("2000"));
        bank.withdraw(3, new BigDecimal("1500"));
        bank.withdraw(4, new BigDecimal("2000"));

        bank.showAccount(1);
        bank.showAccount(2);
        bank.showAccount(3);
        bank.showAccount(4);

        bank.sortition();
        bank.showAccountIDs();
    }
}