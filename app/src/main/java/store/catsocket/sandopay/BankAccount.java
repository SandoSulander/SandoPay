package store.catsocket.sandopay;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

public abstract class BankAccount {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int balance;

    public String accountNumber;

    public String getAccountNumber() {

        return accountNumber;
    }

    public int getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

}

@Entity(tableName = "debit_account_table")
class DebitAccount extends BankAccount {

    public DebitAccount (String accountNumber, int balance){

        this.accountNumber = accountNumber;
        this.balance = balance;

    }

}

@Entity(tableName = "credit_account_table")
class CreditAccount extends BankAccount {

    private int credit;

    public CreditAccount (String an, int b, int c){

        accountNumber = an;
        balance = b;
        credit = c;
    }

    public int getCredit(){

        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
