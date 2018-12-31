package store.catsocket.sandopay;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/* An interface in between the DataBase and the Repository.
   DAO stands for Data Access Object is this specific DAO
   a part of Room library created by Google Android */
@Dao
public interface AccountTransactionDao {

    @Insert
    void insert(AccountTransaction accountTransaction);

    @Update
    void update(AccountTransaction accountTransaction);

    @Delete
    void delete(AccountTransaction accountTransaction);

    @Query("DELETE FROM account_transaction_table")
    void deleteAllAccountTransactions();

    @Query("SELECT * FROM account_transaction_table ORDER BY date DESC") //TODO how to order?
    LiveData<List<AccountTransaction>> getAllAccountTransactions();
}
