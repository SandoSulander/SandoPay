package store.catsocket.sandopay;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

/* A Repository serves as an additional abstraction layer. With Repository,
   it is possible to mediate in between different DataSources therefore, it is
   considered as best practice. */

public class AccountTransactionRepository {

    private AccountTransactionDao accountTransactionDao;
    private LiveData<List<AccountTransaction>> allAccountTransactions;

    // DAO Interface & DataBase

    public AccountTransactionRepository(Application application){
        BankAccountDatabase bankAccountDatabase = BankAccountDatabase.getInstance(application);
        accountTransactionDao = bankAccountDatabase.accountTransactionDao();
        allAccountTransactions = accountTransactionDao.getAllAccountTransactions();
    }

    // Methods for inserting, updating and deleting data to/from Database through DAO using AsyncTasks

    public void insert(AccountTransaction accountTransaction) {
        new AccountTransactionRepository.InsertAccountTransactionAsyncTask(accountTransactionDao).execute(accountTransaction);
    }

    public void update(AccountTransaction accountTransaction) {
        new AccountTransactionRepository.UpdateAccountTransactionAsynTask(accountTransactionDao).execute(accountTransaction);
    }

    public void delete(AccountTransaction accountTransaction) {
        new AccountTransactionRepository.DeleteAccountTransactionAsyncTask(accountTransactionDao).execute(accountTransaction);
    }

    public void deleteAllAccountTransactions() {
        new AccountTransactionRepository.DeleteAllAccountTransactionsAsyncTask(accountTransactionDao).execute();
    }

    public LiveData<List<AccountTransaction>> getAllAccountTransactions(){
        return allAccountTransactions;
    }


    //  AsyncTasks to perform background operations and publish results on the UI thread without having to manipulate threads and/or handlers

    private static class InsertAccountTransactionAsyncTask extends AsyncTask<AccountTransaction, Void, Void> {
        private AccountTransactionDao accountTransactionDao;

        private InsertAccountTransactionAsyncTask(AccountTransactionDao accountTransactionDao) {
            this.accountTransactionDao = accountTransactionDao;
        }

        @Override
        protected Void doInBackground(AccountTransaction... accountTransactions) {
            accountTransactionDao.insert(accountTransactions[0]);
            return null;
        }
    }

    private static class UpdateAccountTransactionAsynTask extends AsyncTask<AccountTransaction, Void, Void> {
        private AccountTransactionDao accountTransactionDao;

        private UpdateAccountTransactionAsynTask(AccountTransactionDao accountTransactionDao) {
            this.accountTransactionDao = accountTransactionDao;
        }

        @Override
        protected Void doInBackground(AccountTransaction... accountTransactions) {
            accountTransactionDao.update(accountTransactions[0]);
            return null;
        }
    }

    private static class DeleteAccountTransactionAsyncTask extends AsyncTask<AccountTransaction, Void, Void> {
        private AccountTransactionDao accountTransactionDao;

        private DeleteAccountTransactionAsyncTask(AccountTransactionDao accountTransactionDao ) {
            this.accountTransactionDao = accountTransactionDao;
        }

        @Override
        protected Void doInBackground(AccountTransaction... accountTransactions) {
            accountTransactionDao.delete(accountTransactions[0]);
            return null;
        }
    }

    private static class DeleteAllAccountTransactionsAsyncTask extends AsyncTask<Void, Void, Void> {
        private AccountTransactionDao accountTransactionDao;

        private DeleteAllAccountTransactionsAsyncTask(AccountTransactionDao accountTransactionDao) {
            this.accountTransactionDao = accountTransactionDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            accountTransactionDao.getAllAccountTransactions();
            return null;
        }
    }


}
