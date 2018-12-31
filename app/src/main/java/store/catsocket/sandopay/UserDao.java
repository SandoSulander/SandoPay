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
public interface UserDao {

    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM user_table")
    void deleteAllUsers();

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM user_table WHERE email ='Admin' and password = '1234'")
    LiveData<List<User>> getUser();

}
