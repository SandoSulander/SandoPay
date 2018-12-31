package store.catsocket.sandopay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* From this Activity the user can view all his/hers account transactions and Request to make one.*/

public class TransactionActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {
    public static final int TRANSFER_MONEY_REQUEST = 4;

    private BankAccountViewModel bankAccountViewModel;
    private DrawerLayout drawer;

    private Intent intent = new Intent();
    private ArrayList<DebitAccount> debitAccountsList = new ArrayList<DebitAccount>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        // Floating Action Button on the Home Screen
        // Starts an Money Transfer Process
        FloatingActionButton buttonTransferMoney = findViewById(R.id.button_transfer_money);
        buttonTransferMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("########################################################################Clicked");
                Intent intent1 = new Intent(TransactionActivity.this, MakeTransactionActivity.class);
                startActivityForResult(intent1, TRANSFER_MONEY_REQUEST);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // RecyclerView

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_transaction);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final TransactionAdapter transactionAdapter = new TransactionAdapter();
        recyclerView.setAdapter(transactionAdapter);

        // ViewModel for all Account Transactions
        bankAccountViewModel = ViewModelProviders.of(this).get(BankAccountViewModel.class);
        bankAccountViewModel.getAllAccountTransactions().observe(this, new Observer<List<AccountTransaction>>() {
            @Override
            public void onChanged(@Nullable List<AccountTransaction> accountTransactions) {
                ArrayList<AccountTransaction> newAccountTransactions = (ArrayList<AccountTransaction>) accountTransactions;
                ArrayList<AccountTransaction> submitList = new ArrayList<AccountTransaction>();
                LogIn logIn = LogIn.getInstance();
                int id = logIn.getLoginId();
                for (int i = 0; i < newAccountTransactions.size(); i++) {
                    if (newAccountTransactions.get(i).getUserId() == id) {
                        submitList.add(accountTransactions.get(i));
                    }
                }
                transactionAdapter.setAccountTransactions(submitList);
            }
        });
        // ViewModel for all DebitAccounts
        bankAccountViewModel.getAllDebitAccounts().observe(this, new Observer<List<DebitAccount>>() {
            @Override
            public void onChanged(@Nullable List<DebitAccount> debitAccounts) {
                setAccountList(debitAccounts);
            }
        });

        //Side Menu Drawer
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    public void setAccountList(List<DebitAccount> debitAccounts){
        this.debitAccountsList = (ArrayList<DebitAccount>) debitAccounts;
    }

    // Choose Instances of Activities from the Menu Drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intentHome = new Intent(TransactionActivity.this, AccountActivity.class);
        Intent intentProfile = new Intent (TransactionActivity.this, ProfileActivity.class);
        Intent intentTransfer = new Intent(TransactionActivity.this, TransactionActivity.class);
        Intent intentLogout = new Intent(TransactionActivity.this, MainActivity.class);

        switch (item.getItemId()) {
            case R.id.nav_my_accounts:
                startActivity(intentHome);
                this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.nav_profile:
                startActivity(intentProfile);
                this.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            case R.id.nav_transfer:
                startActivity(intentTransfer);
                this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.nav_logout:
                SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();
                intentLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentLogout);
                MainActivity mainActivity = new MainActivity();
                mainActivity.setLogin();

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Back Button Behavior
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Checks weather Money Transfer Request, was successful or not
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setThisIntent(data);
        if (bankAccountViewModel != null && requestCode == TRANSFER_MONEY_REQUEST && resultCode == RESULT_OK) {
            insertTransaction();
        } else {

        }
    }

    // Insert Transaction to DataBase
    public void insertTransaction() {

        LogIn logIn = LogIn.getInstance();

        int loginId = logIn.getLoginId();

        int balanceTo = 0;
        int balanceFrom = 0;
        int idTo = 0;
        int idFrom = 0;
        int userIdFrom = 0;
        int userIdTo = 0;

        ArrayList<DebitAccount> list = debitAccountsList;

        String fromAccount = intent.getStringExtra(MakeTransactionActivity.EXTRA_FROM_ACCOUNT);
        String toAccount = intent.getStringExtra(MakeTransactionActivity.EXTRA_TO_ACCOUNT);

        int in = 0;
        while (in < list.size()) {
            if (list.get(in).getAccountNumber().equals(toAccount)) {

                System.out.println("##########################################################################   " + list.get(0).getId()); // De-bug

                System.out.println("##########################################################################   " + intent.getStringExtra(MakeTransactionActivity.EXTRA_FROM_ACCOUNT)); // De-bug


                // Get fromAccount id & balance
                for (int i2 = 0; i2 < list.size(); i2++) {
                    if (list.get(i2).getAccountNumber().equals(fromAccount)) {
                        idFrom = list.get(i2).getId();
                        balanceFrom = list.get(i2).getBalance();
                        userIdFrom = list.get(i2).getUserId();
                    }
                }

                // Get toAccount id & balance
                for (int i3 = 0; i3 < list.size(); i3++) {
                    if (list.get(i3).getAccountNumber().equals(toAccount)) {
                        idTo = list.get(i3).getId();
                        balanceTo = list.get(i3).getBalance();
                        userIdTo = list.get(i3).getUserId();
                    }
                }


                String message = intent.getStringExtra(MakeTransactionActivity.EXTRA_MESSAGE);
                String amount = intent.getStringExtra(MakeTransactionActivity.EXTRA_AMOUNT);
                int transaction = Integer.parseInt(amount);
                String timeStamp = intent.getStringExtra(MakeTransactionActivity.EXTRA_TIME_STAMP);

                System.out.println(fromAccount + "     " + transaction + "     " + toAccount + "     " + message + "     " + amount + "     " + timeStamp + "     " + idFrom); // De-bug

                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(timeStamp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Remove Transaction Amount from fromAccount Balance (Update Account Balance)
                int newBalanceFrom = balanceFrom - transaction;

                if (newBalanceFrom < 0) {
                    Toast.makeText(this, "Not enough money on the account. Cannot proceed!", Toast.LENGTH_SHORT).show();
                    break;
                } else {

                    DebitAccount debitAccountFrom = new DebitAccount(fromAccount, newBalanceFrom, userIdFrom);
                    debitAccountFrom.setId(idFrom);
                    bankAccountViewModel.updateDebitAccount(debitAccountFrom);

                    // Add Transaction Amount to toAccount Balance (Update Account Balance)
                    int newBalanceTo = balanceTo + transaction;

                    DebitAccount debitAccountTo = new DebitAccount(toAccount, newBalanceTo, userIdTo);
                    debitAccountTo.setId(idTo);
                    bankAccountViewModel.updateDebitAccount(debitAccountTo);

                    // Check if sent from own account --> If yes make only one AccountTransaction Record

                    if (loginId == userIdTo) {
                        // Create Account Transaction for fromAccount
                        AccountTransaction accountTransactionFrom = new AccountTransaction(date, transaction, fromAccount, toAccount, message, idFrom, userIdFrom);
                        bankAccountViewModel.insertAccountTransaction(accountTransactionFrom);
                    } else {
                        // Create Account Transaction for fromAccount
                        AccountTransaction accountTransactionFrom = new AccountTransaction(date, transaction, fromAccount, toAccount, message, idFrom, userIdFrom);
                        bankAccountViewModel.insertAccountTransaction(accountTransactionFrom);

                        // Create Account Transaction for toAccount
                        AccountTransaction accountTransactionTo = new AccountTransaction(date, transaction, fromAccount, toAccount, message, idTo, userIdTo);
                        bankAccountViewModel.insertAccountTransaction(accountTransactionTo);
                    }


                    Toast.makeText(TransactionActivity.this, transaction + "€ sent to " + toAccount, Toast.LENGTH_LONG).show();
                    break;
                }
            } else{
                Toast.makeText(this, "Account Number doesn't exist.", Toast.LENGTH_SHORT).show();
            }
            in++;
        }
    }

    public Intent setThisIntent(Intent data) {
        this.intent = data;
        return intent;
    }

}




