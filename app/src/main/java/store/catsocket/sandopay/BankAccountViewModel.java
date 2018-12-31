package store.catsocket.sandopay;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/* This Class stores and manages all the UI-related data in a lifecycle aware manner.
   The ViewModel classes purpose is to allow data to survive configuration changes such as screen rotations.
   This ViewModel Controls View of the all three (3) Entities (User, DebitAccount & AccountTransaction) of the SQLite DataBase. */

public class BankAccountViewModel extends AndroidViewModel {

    // DebitAccount related attributes
    private DebitAccountRepository daRepository;
    private LiveData<List<DebitAccount>> allDebitAccounts;

    // AccountTransaction related attributes
    private AccountTransactionRepository atRepository;
    private LiveData<List<AccountTransaction>> allAccountTransactions;

    // User related attributes
    private UserRepository uRepository;
    private LiveData<List<User>> allUsers;
    private LiveData<List<User>> user;

    public BankAccountViewModel(@NonNull Application application) {
        super(application);

        // DebitAccount
        daRepository = new DebitAccountRepository(application);
        allDebitAccounts = daRepository.getAllDebitAccounts();

        // AccountTransaction
        atRepository = new AccountTransactionRepository(application);
        allAccountTransactions = atRepository.getAllAccountTransactions();

        // User
        uRepository = new UserRepository(application);
        allUsers = uRepository.getAllUsers();
        user = uRepository.getUser();

    }

    // DebitAccount related methods
    public void insertDebitAccount(DebitAccount debitAccount) {
        daRepository.insert(debitAccount);
    }

    public void updateDebitAccount(DebitAccount debitAccount) {
        daRepository.update(debitAccount);
    }

    public void deleteDebitAccount(DebitAccount debitAccount) {
        daRepository.delete(debitAccount);
    }

    public void deleteAllDebitAccounts() {
        daRepository.deleteAllDebitAccounts();
    }

    public LiveData<List<DebitAccount>> getAllDebitAccounts() {
        return allDebitAccounts;
    }

    // AccountTransaction related methods
    public void insertAccountTransaction(AccountTransaction accountTransaction) {
        atRepository.insert(accountTransaction);
    }

    public void updateAccountTransaction(AccountTransaction accountTransaction) {
        atRepository.update(accountTransaction);
    }

    public void deleteAccountTransaction(AccountTransaction accountTransaction) {
        atRepository.delete(accountTransaction);
    }

    public void deleteAllAccountTransactions() {
        atRepository.deleteAllAccountTransactions();
    }

    public LiveData<List<AccountTransaction>> getAllAccountTransactions() {
        return allAccountTransactions;
    }

    // User related methods
    public void insertUser(User user) {
        uRepository.insertUser(user);
    }

    public void updateUser(User user) {
        uRepository.updateUser(user);
    }

    public void deleteUser(User user) {
        uRepository.deleteUser(user);
    }

    public void deleteAllUsers() {
        uRepository.deleteAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public LiveData<List<User>> getUser(){
        return user;
    }

}
