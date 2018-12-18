package store.catsocket.sandopay;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DebitAccountDao {

    @Insert
    void insert(DebitAccount debitAccount);

    @Update
    void update(DebitAccount debitAccount);

    @Delete
    void delete(DebitAccount debitAccount);

    @Query("DELETE FROM debit_account_table")
    void deleteAllDebitAccounts();

    @Query("SELECT * FROM debit_account_table ORDER BY accountNumber ASC") //TODO how to order?
    LiveData<List<DebitAccount>> getAllDebitAccounts();
}
