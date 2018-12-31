package store.catsocket.sandopay;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/* From this Activity, the user can make desired Changes on the User Information
   or Delete the Account. */

public class EditProfileActivity extends AppCompatActivity {

    // Intent Extra Variables for Passing them further to Insert/Update to the Database
    public static final String EXTRA_USER_ID =
            "store.catsocket.sandopay.EXTRA_USER_ID";
    public static final String EXTRA_NAME=
            "store.catsocket.sandopay.EXTRA_NEW_NAME";
    public static final String EXTRA_NEW_PASSWORD=
            "store.catsocket.sandopay.EXTRA_NEW_PASSWORD";
    public static final String EXTRA_NEW_EMAIL =
            "store.catsocket.sandopay.EXTRA_NEW_EMAIL";

    private ArrayList<User> users = new ArrayList<>();

    private EditText editOldPassword, editNewEmail, editNewPassword, editConfirmNewPassword;
    private TextView textOldPassword;
    private Button deleteAccountButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        // Disable DeleteButton
        deleteAccountButton = (Button) findViewById(R.id.delete_account_button);


        // Text Elements
        editOldPassword = (EditText) findViewById(R.id.edit_text_old_password);
        editNewEmail = (EditText) findViewById(R.id.edit_text_new_email);
        editNewPassword = (EditText) findViewById(R.id.edit_text_new_password);
        editConfirmNewPassword = (EditText) findViewById(R.id.edit_text_confirm_new_password);

        textOldPassword = (TextView) findViewById(R.id.text_old_password);

        setTitle("Edit User Information");

        // Getting right user from the DataBase for editing
        BankAccountViewModel bankAccountViewModel = ViewModelProviders.of(this).get(BankAccountViewModel.class);
        bankAccountViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                ArrayList<User> newUsers = (ArrayList<User>) users;
                ArrayList<User> submitList = new ArrayList<User>();
                LogIn logIn = LogIn.getInstance();
                int id = logIn.getLoginId();
                for (int i = 0; i < newUsers.size(); i++) {
                    if (newUsers.get(i).getId() == id) {
                        submitList.add(users.get(i));
                    }
                }
                setUserList(submitList);
            }
        });

    }

    // Method for Editing the User (Requires clicking the "Save" -button.
    private void saveUser () {

        String validatePassword = users.get(0).getPassword();
        System.out.println("##########################################################"+validatePassword);
        String oldEmail = users.get(0).getEmail();
        String name = users.get(0).getName();
        int id = users.get(0).getId();
        String idString = Integer.toString(id);

        String oldPassword = editOldPassword.getText().toString();
        String newEmail = editNewEmail.getText().toString();
        String newPassword = editNewPassword.getText().toString();
        String confirmNewPassword = editConfirmNewPassword.getText().toString();


        if (oldPassword.trim().isEmpty() || !oldPassword.equals(validatePassword)){
            Toast.makeText(this, "Wrong old password!", Toast.LENGTH_SHORT).show();
            Intent data = new Intent();
            setResult(RESULT_CANCELED, data);
            finish();
        }  else if (!newPassword.equals(confirmNewPassword)){
            Toast.makeText(this, "New passwords don't match!", Toast.LENGTH_SHORT).show();
            Intent data = new Intent();
            setResult(RESULT_CANCELED, data);
            finish();
        } else if (newEmail.trim().isEmpty() && !newPassword.trim().isEmpty() && !confirmNewPassword.trim().isEmpty()) {
            Intent data = new Intent();
            data.putExtra(EXTRA_USER_ID, idString);
            data.putExtra(EXTRA_NAME, name);
            data.putExtra(EXTRA_NEW_PASSWORD, newPassword);
            data.putExtra(EXTRA_NEW_EMAIL, oldEmail);
            setResult(RESULT_OK, data);
            finish();
        } else if (!newEmail.trim().isEmpty() && !newPassword.trim().isEmpty() && !confirmNewPassword.trim().isEmpty()){
            Intent data = new Intent();
            data.putExtra(EXTRA_USER_ID, idString);
            data.putExtra(EXTRA_NAME, name);
            data.putExtra(EXTRA_NEW_PASSWORD, newPassword);
            data.putExtra(EXTRA_NEW_EMAIL, newEmail);
            setResult(RESULT_OK, data);
            finish();
        } else if (!newEmail.trim().isEmpty() && newPassword.trim().isEmpty() && confirmNewPassword.trim().isEmpty()){
            Intent data = new Intent();
            data.putExtra(EXTRA_USER_ID, idString);
            data.putExtra(EXTRA_NAME, name);
            data.putExtra(EXTRA_NEW_PASSWORD, validatePassword);
            data.putExtra(EXTRA_NEW_EMAIL, newEmail);
            setResult(RESULT_OK, data);
            finish();
        }else{
            Intent data = new Intent();
            data.putExtra(EXTRA_USER_ID, idString);
            data.putExtra(EXTRA_NAME, name);
            data.putExtra(EXTRA_NEW_PASSWORD, newPassword);
            data.putExtra(EXTRA_NEW_EMAIL, newEmail);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    // Menu inflater
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_profile_menu, menu);
        return true;
    }

    // Item Select Listener --> saveUser() method activated on click.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_account:
                saveUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setUserList(List<User> userList){
        this.users = (ArrayList<User>) userList;
    }


    /* Deletes the User on Button Click if Old Password is Correct */

    public void deleteUser(View view){
        BankAccountViewModel bankAccountViewModel = ViewModelProviders.of(this).get(BankAccountViewModel.class);
        String validatePassword = users.get(0).getPassword();
        String oldPassword = editOldPassword.getText().toString();
        Intent intentLogout = new Intent(EditProfileActivity.this, MainActivity.class);
        if (oldPassword.equals(validatePassword)){
            User user = users.get(0);
            bankAccountViewModel.deleteUser(user);
            Toast.makeText(this, "User successfully deleted!", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();
            intentLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentLogout);
            MainActivity mainActivity = new MainActivity();
            mainActivity.setLogin();

        } else {
            Toast.makeText(this, "Insert correct password first.", Toast.LENGTH_SHORT).show();
        }
    }

}
