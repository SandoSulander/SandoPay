package store.catsocket.sandopay;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

// "@"-Notations are for Room SQLite Database. This Class/Entity Holds information on Bank-, Debit- and CreditAccounts.

public abstract class BankAccount {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    public int userId;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

@Entity(tableName = "debit_account_table",foreignKeys =
@ForeignKey(entity=User.class,parentColumns = "id",childColumns = "user_id", onDelete = CASCADE))
class DebitAccount extends BankAccount {

    public DebitAccount (String accountNumber, int balance, int userId){

        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;

    }

}

@Entity(tableName = "credit_account_table")
class CreditAccount extends BankAccount {

    private int credit;

    public CreditAccount (String accountNumber, int balance, int credit, int userId){

        this.accountNumber = accountNumber;
        this.balance = balance;
        this.credit = credit;
        this.userId = userId;
    }

    public int getCredit(){

        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
