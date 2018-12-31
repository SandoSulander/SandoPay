package store.catsocket.sandopay;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

/* A Repository serves as an additional abstraction layer. With Repository,
   it is possible to mediate in between different DataSources therefore, it is
   considered as best practice. */

public class DebitAccountRepository {

    private DebitAccountDao debitAccountDao;

    private LiveData<List<DebitAccount>> allDebitAccounts;

    // DAO Interface & DataBase

    public DebitAccountRepository(Application application) {
        BankAccountDatabase database = BankAccountDatabase.getInstance(application);
        debitAccountDao = database.debitAccountDao();
        allDebitAccounts = debitAccountDao.getAllDebitAccounts();
    }

    // API of the View Model

    public void insert(DebitAccount debitAccount) {
        new InsertDebitAccountAsyncTask(debitAccountDao).execute(debitAccount);
    }

    public void update(DebitAccount debitAccount) {
        new UpdateDebitAccountAsyncTask(debitAccountDao).execute(debitAccount);
    }

    public void delete(DebitAccount debitAccount) {
        new DeleteDebitAccountAsyncTask(debitAccountDao).execute(debitAccount);
    }

    public void deleteAllDebitAccounts() {
        new DeleteDebitAccountAsyncTask(debitAccountDao).execute();
    }

    public LiveData<List<DebitAccount>> getAllDebitAccounts(){
        return allDebitAccounts;
    }


    //  AsyncTasks to perform background operations and publish results on the UI thread without having to manipulate threads and/or handlers

    private static class InsertDebitAccountAsyncTask extends AsyncTask<DebitAccount, Void, Void> {
        private DebitAccountDao debitAccountDao;

        private InsertDebitAccountAsyncTask(DebitAccountDao debitAccountDao) {
            this.debitAccountDao = debitAccountDao;
        }

        @Override
        protected Void doInBackground(DebitAccount... debitAccounts) {
            debitAccountDao.insert(debitAccounts[0]);
            return null;
        }
    }

    private static class UpdateDebitAccountAsyncTask extends AsyncTask<DebitAccount, Void, Void> {
        private DebitAccountDao debitAccountDao;

        private UpdateDebitAccountAsyncTask(DebitAccountDao debitAccountDao) {
            this.debitAccountDao = debitAccountDao;
        }

        @Override
        protected Void doInBackground(DebitAccount... debitAccounts) {
            debitAccountDao.update(debitAccounts[0]);
            return null;
        }
    }

    private static class DeleteDebitAccountAsyncTask extends AsyncTask<DebitAccount, Void, Void> {
        private DebitAccountDao debitAccountDao;

        private DeleteDebitAccountAsyncTask(DebitAccountDao debitAccountDao) {
            this.debitAccountDao = debitAccountDao;
        }

        @Override
        protected Void doInBackground(DebitAccount... debitAccounts) {
            debitAccountDao.delete(debitAccounts[0]);
            return null;
        }
    }

    private static class DeleteAllDebitAccountAsyncTask extends AsyncTask<Void, Void, Void> {
        private DebitAccountDao debitAccountDao;

        private DeleteAllDebitAccountAsyncTask(DebitAccountDao debitAccountDao) {
            this.debitAccountDao = debitAccountDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            debitAccountDao.deleteAllDebitAccounts();
            return null;
        }
    }

}
