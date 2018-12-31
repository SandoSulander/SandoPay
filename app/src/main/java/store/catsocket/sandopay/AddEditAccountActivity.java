package store.catsocket.sandopay;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditAccountActivity extends AppCompatActivity {

    // Intent Extra Variables for Passing them further to Insert/Update to the Database
    //TODO Credit
    public static final String EXTRA_ID =
            "store.catsocket.sandopay.EXTRA_ID";
    public static final String EXTRA_ACCOUNT_NUMBER =
            "store.catsocket.sandopay.EXTRA_TITLE";
    public static final String EXTRA_BALANCE =
            "store.catsocket.sandopay.EXTRA_DESCRIPTION";

    //TODO Credit
    private EditText editAccountNumber;
    private EditText editBalance;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        editAccountNumber = findViewById(R.id.edit_account_number);
        editBalance = findViewById(R.id.edit_text_balance);

        // Close-button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        // Check if Edit or Add DebitAccount --> If there is an ID, there is an existing Account --> Edit
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit an Account");
            editAccountNumber.setText(intent.getStringExtra(EXTRA_ACCOUNT_NUMBER));
            int balance = intent.getIntExtra(EXTRA_BALANCE, 0);
            String balanceString = Integer.toString(balance);
            editBalance.setText(balanceString);
        }else{
            setTitle("Add an Account");
        }

    }
    // Method for Saving the Account (Requires clicking the "Save" -button.
    private void saveAccount () {
        String accountNumber = editAccountNumber.getText().toString();
        String balance = editBalance.getText().toString();
        int balanceInt = Integer.parseInt(balance);

        // Check for empty fields
        if(accountNumber.trim().isEmpty()||balance.trim().isEmpty()){
            Toast.makeText(this, "Empty fields. Please fill all the fields.", Toast.LENGTH_SHORT).show();
        }  else if (balanceInt < 0){
            Toast.makeText(this, "Can't add negative balance.", Toast.LENGTH_SHORT).show();
        } else {

            //TODO Credit
            Intent data = new Intent();
            data.putExtra(EXTRA_ACCOUNT_NUMBER, accountNumber);
            data.putExtra(EXTRA_BALANCE, balance);

            // Check if the ID needs to be updated (Edit DebitAccount situation)
            int id = getIntent().getIntExtra(EXTRA_ID, -1);
            if (id != -1) {
                data.putExtra(EXTRA_ID, id);
            }

            setResult(RESULT_OK, data);
            finish();
        }
    }

    // Menu inflater
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_account_menu, menu);
        return true;
    }

    // Item Select Listener --> saveAccount() method activated on click.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_account:
                saveAccount();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
