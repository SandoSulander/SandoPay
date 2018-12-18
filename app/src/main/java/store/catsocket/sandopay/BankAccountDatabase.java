package store.catsocket.sandopay;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {DebitAccount.class}, version = 1) //TODO Migration strategy
public abstract class BankAccountDatabase extends RoomDatabase {

    private static BankAccountDatabase instance;

    public abstract DebitAccountDao debitAccountDao();

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

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private DebitAccountDao debitAccountDao;

        private PopulateDbAsyncTask(BankAccountDatabase db) {
            debitAccountDao = db.debitAccountDao();
        }

        // Example Accounts

        @Override
        protected Void doInBackground(Void... voids) {
            debitAccountDao.insert(new DebitAccount("111-111", 1000));
            debitAccountDao.insert(new DebitAccount("111-112", 40000));
            debitAccountDao.insert(new DebitAccount("111-113", 1000000));
            return null;
        }
    }
}
