package store.catsocket.sandopay;

import android.content.Context;
import android.os.AsyncTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

/* This class is the DataBase of the Bank (misleadingly named as a BankAccountDataBase)
   The Class Extends From RoomDatabase. Room is a persistence library that provides an abstraction
   layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.*/

@Database(entities = {User.class, DebitAccount.class, AccountTransaction.class}, version = 29)  //If DataBase gets Corrupted OR Changes on the Schema are made --> increment version +1
@TypeConverters({Converters.class}) // Converter for Date
public abstract class BankAccountDatabase extends RoomDatabase {

    private static BankAccountDatabase instance;
    // DAOs
    public abstract DebitAccountDao debitAccountDao();
    public abstract AccountTransactionDao accountTransactionDao();
    public abstract UserDao userDao();
    // Singleton
    public static synchronized BankAccountDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BankAccountDatabase.class, "bank_account_database")
                    .addCallback(roomCallback)
                    .fallbackToDestructiveMigration() //TODO Migration strategy
                    .build();
        }
        return instance;
    }

    // Methods for "Pre-populating the DataBase with desired Items.

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        // Change onCreate --> onOpen to make the pre-population every time on Open (onCreate only when the version of the DataBase is created)
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private DebitAccountDao debitAccountDao;
        private AccountTransactionDao accountTransactionDao;
        private UserDao userDao;

        private PopulateDbAsyncTask(BankAccountDatabase db) {
            debitAccountDao = db.debitAccountDao();
            accountTransactionDao = db.accountTransactionDao();
            userDao = db.userDao();
        }

        // Example (Pre-populated) User(s). Account(s) and Account Transaction(s)

        @Override
        protected Void doInBackground(Void... voids) {

            // Pre-populated AdminUser for Testing
            userDao.insertUser(new User("SandoPay", "sando@sandopay.fi", "S4nd0P4y123!"));

            // Pre-populated AdminAccount for Testing
            debitAccountDao.insert(new DebitAccount("SandoPay", 999999999, 1));

            return null;
        }
    }
}
