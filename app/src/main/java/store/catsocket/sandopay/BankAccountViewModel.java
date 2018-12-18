package store.catsocket.sandopay;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class BankAccountViewModel extends AndroidViewModel {

    private DebitAccountRepository daRepository;
    private LiveData<List<DebitAccount>> allDebitAccounts;

    public BankAccountViewModel(@NonNull Application application) {
        super(application);
        daRepository = new DebitAccountRepository(application);
        allDebitAccounts = daRepository.getAllDebitAccounts();
    }

    public void insert(DebitAccount debitAccount) {
        daRepository.insert(debitAccount);
    }

    public void update(DebitAccount debitAccount) {
        daRepository.update(debitAccount);
    }

    public void delete(DebitAccount debitAccount) {
        daRepository.delete(debitAccount);
    }

    public void deleteAllDebitAccounts() {
        daRepository.deleteAllDebitAccounts();
    }

    public LiveData<List<DebitAccount>> getAllDebitAccounts() {
        return allDebitAccounts;
    }
}
