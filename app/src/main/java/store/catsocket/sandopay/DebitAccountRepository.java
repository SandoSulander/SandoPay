package store.catsocket.sandopay;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class DebitAccountRepository {

    private DebitAccountDao debitAccountDao;
    private LiveData<List<DebitAccount>> allDebitAccounts;

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

        private DeleteDebitAccountAsyncTask(DebitAccountDao debitAccountDaoDao) {
            this.debitAccountDao = debitAccountDaoDao;
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
