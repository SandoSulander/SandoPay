package store.catsocket.sandopay;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    // Request Code For Add and Edit Account Intent
    public static final int ADD_ACCOUNT_REQUEST = 1;
    public static final int EDIT_ACCOUNT_REQUEST = 2;

    private BankAccountViewModel bankAccountViewModel;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Floating Action Button on the Accounts View
        // Starts an Account Creation Process
        FloatingActionButton buttonAddAccount = findViewById(R.id.button_add_account);
        buttonAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("########################################################################Clicked");
                Intent intent =  new Intent(AccountActivity.this, AddEditAccountActivity.class);
                startActivityForResult(intent, ADD_ACCOUNT_REQUEST);
            }
        });

        // Support Action Toolbar

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // RecyclerView

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final BankAccountAdapter bankAccountAdapter = new BankAccountAdapter();
        recyclerView.setAdapter(bankAccountAdapter);

        // Get the right List of Accounts for Display from DataBase (Through Repository using LogIn, BankAccountViewModel & Adapter)
        bankAccountViewModel = ViewModelProviders.of(this).get(BankAccountViewModel.class);
        bankAccountViewModel.getAllDebitAccounts().observe(this, new Observer<List<DebitAccount>>() {
            @Override
            public void onChanged(@Nullable List<DebitAccount> debitAccounts) {
                ArrayList<DebitAccount> newDebitAccounts = (ArrayList<DebitAccount>) debitAccounts;
                ArrayList<DebitAccount> submitList = new ArrayList<DebitAccount>();
                LogIn logIn = LogIn.getInstance();
                int id = logIn.getLoginId();
                for (int i = 0; i < newDebitAccounts.size(); i++) {
                    if (newDebitAccounts.get(i).getUserId() == id) {
                        submitList.add(debitAccounts.get(i));
                    }
                }
                bankAccountAdapter.submitList(submitList);
            }
        });


        // Delete Debit Accounts with Swipe
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                bankAccountViewModel.deleteDebitAccount(bankAccountAdapter.getDebitAccountAt(viewHolder.getAdapterPosition()));
                Toast.makeText(AccountActivity.this, "Debit Account deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        // Click on the RecyclerView Items (Accounts) in order to Edit edit them.
        bankAccountAdapter.setOnItemClickListener(new BankAccountAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DebitAccount debitAccount) {
                Intent intent = new Intent(AccountActivity.this, AddEditAccountActivity.class);
                intent.putExtra(AddEditAccountActivity.EXTRA_ID, debitAccount.getId());
                intent.putExtra(AddEditAccountActivity.EXTRA_ACCOUNT_NUMBER, debitAccount.getAccountNumber());
                intent.putExtra(AddEditAccountActivity.EXTRA_BALANCE, debitAccount.getBalance());
                startActivityForResult(intent, EDIT_ACCOUNT_REQUEST);
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

    // Choose Instances of Activities from the Menu Drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intentHome = new Intent(AccountActivity.this, AccountActivity.class);
        Intent intentProfile = new Intent (AccountActivity.this, ProfileActivity.class);
        Intent intentTransfer = new Intent(AccountActivity.this, TransactionActivity.class);
        Intent intentLogout = new Intent(AccountActivity.this, MainActivity.class);

        switch (item.getItemId()) {
            case R.id.nav_my_accounts:
                startActivity(intentHome);
                this.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            case R.id.nav_profile:
                startActivity(intentProfile);
                this.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            case R.id.nav_transfer:
                startActivity(intentTransfer);
                this.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
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

    // Checks if Account details are correct and inserts/updates Account to the Database
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogIn logIn = LogIn.getInstance();
        int accountId = logIn.getLoginId();
        try {
            if (requestCode == ADD_ACCOUNT_REQUEST && resultCode == RESULT_OK) {
                String accountNumber = data.getStringExtra(AddEditAccountActivity.EXTRA_ACCOUNT_NUMBER);
                String balance = data.getStringExtra(AddEditAccountActivity.EXTRA_BALANCE);
                int balanceInt = Integer.parseInt(balance);

                BankAccount debitAccount = new DebitAccount(accountNumber, balanceInt, accountId); //TODO USERID
                bankAccountViewModel.insertDebitAccount((DebitAccount) debitAccount);

                Toast.makeText(this, "Account saved", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException nfe) {
            Toast.makeText(this, "Account not saved! Balance can't be that large!", Toast.LENGTH_SHORT).show();
        }
        try {
            if (requestCode == EDIT_ACCOUNT_REQUEST && resultCode == RESULT_OK) {
                int id = 0;
                if (data != null) {
                    id = data.getIntExtra(AddEditAccountActivity.EXTRA_ID, -1);
                }

                if (id == -1) {
                    Toast.makeText(this, "Account can't be updated.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String accountNumber = data.getStringExtra(AddEditAccountActivity.EXTRA_ACCOUNT_NUMBER);
                String balance = data.getStringExtra(AddEditAccountActivity.EXTRA_BALANCE);
                int balanceInt = Integer.parseInt(balance);


                DebitAccount debitAccount = new DebitAccount(accountNumber, balanceInt, accountId);
                debitAccount.setId(id);
                bankAccountViewModel.updateDebitAccount(debitAccount);

                Date date = new Date();
                AccountTransaction accountTransactionSandoPay = new AccountTransaction(date, balanceInt, "SandoPay", accountNumber, "Balance fixed.", 1, accountId);
                bankAccountViewModel.insertAccountTransaction(accountTransactionSandoPay);

                Toast.makeText(this, "Debit Account updated!", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException nfe) {
            Toast.makeText(this, "Account not saved! Balance can't be that large!", Toast.LENGTH_SHORT).show();
        }
    }

}
