package store.catsocket.sandopay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/* This Activity is for User Creation Information input purposes. */

public class CreateUserActivity extends AppCompatActivity {

    // Intent Extra Variables for Passing them further to Insert/Update to the Database
    public static final String EXTRA_NAME =
            "store.catsocket.sandopay.EXTRA_NAME";
    public static final String EXTRA_EMAIL =
            "store.catsocket.sandopay.EXTRA_EMAIL";
    public static final String EXTRA_PASSWORD =
            "store.catsocket.sandopay.EXTRA_PASSWORD";

    EditText editName, editEmail, editNewPassword, editConfirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        editName = (EditText) findViewById(R.id.edit_text_name);
        editEmail = (EditText) findViewById(R.id.edit_text_email);
        editNewPassword = (EditText) findViewById(R.id.edit_text_password);
        editConfirmNewPassword = (EditText) findViewById(R.id.edit_text_confirm_password);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Create New User");

    }
    // Method for Creating the User (Requires clicking the "Save" -button.
    private void createUser(){

        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String newPassword = editNewPassword.getText().toString();
        String confirmNewPassword = editConfirmNewPassword.getText().toString();


        if (name.trim().isEmpty() || email.trim().isEmpty() || newPassword.trim().isEmpty() || confirmNewPassword.trim().isEmpty()) {
            Toast.makeText(this, "Fill empty fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword.equals(confirmNewPassword)){
            Toast.makeText(this, "Passwords don't match.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_EMAIL, email);
        data.putExtra(EXTRA_PASSWORD, newPassword);

        setResult(RESULT_OK, data);
        finish();

    }
    // Menu inflater
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.create_user_menu, menu);
        return true;
    }
    // Item Select Listener --> createUser() method activated on click.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.create_user:
                createUser();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
