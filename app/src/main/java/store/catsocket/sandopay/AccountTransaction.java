package store.catsocket.sandopay;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

// "@"-Notations are for Room SQLite Database. This Class/Entity Holds information on Account Transactions

@Entity(tableName = "account_transaction_table",foreignKeys =
@ForeignKey(entity=DebitAccount.class,parentColumns = "id",childColumns = "account_id", onDelete = CASCADE))
public class AccountTransaction {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date date;
    private int transaction;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String message;
    @ColumnInfo(name="account_id")
    private int accountId;
    private int userId;


    public AccountTransaction(Date date, int transaction, String fromAccountNumber, String toAccountNumber, String message, int accountId, int userId) {
        this.date = date;
        this.transaction = transaction;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.message = message;
        this.accountId = accountId;
        this.userId = userId;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTransaction() {
        return transaction;
    }

    public void setTransaction(int transaction) {
        this.transaction = transaction;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public String getDateString(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }
}

