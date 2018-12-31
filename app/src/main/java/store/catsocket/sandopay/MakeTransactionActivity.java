package store.catsocket.sandopay;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* This Activity is for making a monetary transaction from Account A to B */

public class MakeTransactionActivity extends AppCompatActivity {

    public static final String EXTRA_FROM_ACCOUNT =
            "store.catsocket.sandopay.EXTRA_FROM_ACCOUNT";
    public static final String EXTRA_TO_ACCOUNT =
            "store.catsocket.sandopay.EXTRA_TO_ACCOUNT";
    public static final String EXTRA_MESSAGE =
            "store.catsocket.sandopay.EXTRA_MESSAGE";
    public static final String EXTRA_AMOUNT =
            "store.catsocket.sandopay.EXTRA_AMOUNT";
    public static final String EXTRA_TIME_STAMP =
            "store.catsocket.sandopay.EXTRA_TIME_STAMP";

    Spinner fromAccountSpinner;

    private BankAccountViewModel bankAccountViewModel;

    EditText editToAccountNumber, editTextMessage, editTextAmount;

    ArrayList<DebitAccount> debitAccountsList = new ArrayList<DebitAccount>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_transaction);

        editToAccountNumber = (EditText) findViewById(R.id.edit_to_account_number);
        editTextMessage = (EditText) findViewById(R.id.edit_text_message);
        editTextAmount = (EditText) findViewById(R.id.edit_text_amount);
        fromAccountSpinner = (Spinner) findViewById(R.id.from_account_spinner);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Transfer Money");

        // ViewModel for Getting DebitAccounts
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
                createAccountNumberDropDown(submitList);
            }

        });

    }

    // Gets DebitAccounts from ViewModel and Displays them as a Drop Down List
    public void createAccountNumberDropDown(List<DebitAccount> debitAccounts) {
        ArrayList<String> accountList = new ArrayList<String>();
        for (int i = 0; i < debitAccounts.size(); i++) {
            String accountNumber = debitAccounts.get(i).getAccountNumber() + " (" + debitAccounts.get(i).getBalance() + "€)";
            accountList.add(accountNumber);
            setAccountList(debitAccounts);
        }

        String[] debitAccountArray = accountList.toArray(new String[accountList.size()]);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, debitAccountArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromAccountSpinner.setAdapter(aa);
    }

    // Finalizes The Money Transaction Operation (Euro-sign Button in the Action-bar)
    private void transferMoney() {
        try {
            int balance = 0;
            Intent data = new Intent();

            XMLwriter xmlWriter = new XMLwriter();
            String id = "";

            String fromAccountLong = fromAccountSpinner.getSelectedItem().toString();
            String fromAccount = fromAccountLong.substring(0, 7);
            String toAccount = editToAccountNumber.getText().toString();
            String message = editTextMessage.getText().toString();
            String amount = editTextAmount.getText().toString(); // Transfer amount
            int amountInt = Integer.parseInt(amount);
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Date timeStamp = new Date();
            String timeStampString = dateFormat.format(timeStamp);
            String timeStampString2 = dateFormat2.format(timeStamp);


            System.out.println("##############" + fromAccountLong + "       " + fromAccount + "       " + toAccount + "       " + message + "       " + amount + "       " + timeStampString); //De-bug

            for (int i = 0; i < debitAccountsList.size(); i++) {
                if (debitAccountsList.get(i).getAccountNumber().equals(fromAccount)) {
                    balance = debitAccountsList.get(i).getBalance();
                    break;
                }
            }

            if (toAccount.trim().isEmpty() || message.trim().isEmpty() || amount.trim().isEmpty()) {
                Toast.makeText(this, "Fill empty fields.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED, data);
                finish();
            } else if (balance < amountInt) {
                setResult(RESULT_CANCELED, data);
                finish();
                Toast.makeText(this, "Not enough money on the account. Cannot proceed!", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < debitAccountsList.size(); i++) {
                    if (debitAccountsList.get(i).getAccountNumber().equals(fromAccount)) {
                        id = Integer.toString(debitAccountsList.get(i).getId());
                        break;
                    }
                }
                xmlWriter.saveToXML("transaction_accountId_" + id + "_" + timeStampString2 + ".xml", fromAccount, toAccount, message, timeStampString, amount);
                Toast.makeText(this, "XML-File created.", Toast.LENGTH_SHORT).show();

                data.putExtra(EXTRA_FROM_ACCOUNT, fromAccount);
                data.putExtra(EXTRA_TO_ACCOUNT, toAccount);
                data.putExtra(EXTRA_MESSAGE, message);
                data.putExtra(EXTRA_AMOUNT, amount);
                data.putExtra(EXTRA_TIME_STAMP, timeStampString);

                setResult(RESULT_OK, data);
                finish();
            }
        }
    catch(
    NumberFormatException nfe)
    {
        Toast.makeText(this, "Transaction not made! Transaction can't be that large!", Toast.LENGTH_SHORT).show();
    }

}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.transfer_money_menu, menu);
        return true;
    }


    // Finalizes The Money Transaction Operation (Euro-sign Button in the Action-bar)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.transfer_money:
                transferMoney();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public ArrayList setAccountList(List<DebitAccount> debitAccounts){
        this.debitAccountsList = (ArrayList<DebitAccount>) debitAccounts;
        return debitAccountsList;
    }
}
