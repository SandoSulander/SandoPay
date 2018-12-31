package store.catsocket.sandopay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.UnknownServiceException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/*This Activity serves as Login Activity for the app. Furthermore, from this Activity,
  it is possible to start a user creation process.*/

public class MainActivity extends AppCompatActivity {
    public static final int CREATE_USER_REQUEST= 5;

    private EditText editUsername, editPassword;
    private TextView attemptsView;
    private Button logInButton;

    private Intent intent = new Intent();

    private BankAccountViewModel bankAccountViewModel;
    private ArrayList<User> users = new ArrayList<>();

    public boolean login;

    private int loginCounter = 5; // This loginCounter should have been in "Security" class and saved to DataBase. Now it resets on Activity Creation.

    // Preference File Constant Variables for Login --> Preferences could have been done in "Preferences" class.
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String USERNAME = "usernameKey";
    public static final String PASSWORD = "passwordKey";

    SharedPreferences sharedpreferences;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Floating Action Button on the Login Screen
        // Starts an User Creation Process
        FloatingActionButton buttonTransferMoney = findViewById(R.id.button_transfer_money);
        buttonTransferMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("########################################################################Clicked");
                Intent intent = new Intent(MainActivity.this, CreateUserActivity.class);
                startActivityForResult(intent, CREATE_USER_REQUEST);
            }
        });

        // ViewModel for UserList from the Database.
        bankAccountViewModel = ViewModelProviders.of(this).get(BankAccountViewModel.class);
        bankAccountViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                setUserList(users);
            }
        });

        login = false;

        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);

        // Set number of Login attempts
        attemptsView = (TextView) findViewById(R.id.attemptsView);
        attemptsView.setText("Number of attempts remaining: 5");

        // Login and Saves User Login Preferences (Username & Password) --> In Order to Remember the Login Details IF not Logged out (Log Out Deletes Preferences)
        logInButton = (Button) findViewById(R.id.logInButton);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (!Objects.equals(sharedpreferences.getString(USERNAME, "usernameKey"), "usernameKey")
                && login == false){
            editUsername.setText(sharedpreferences.getString(USERNAME, "usernameKey"));
            editPassword.setText(sharedpreferences.getString(PASSWORD, "passwordKey"));
        }

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName  = editUsername.getText().toString();
                String passWord  = editPassword.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(USERNAME, userName);
                editor.putString(PASSWORD, passWord);
                editor.apply();

                validate(userName,passWord,users);
        }
        });
    }

    // Checks weather Create User Request, was successful or not
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setThisIntent(data);
        if (requestCode == CREATE_USER_REQUEST && resultCode == RESULT_OK) {
            insertUser();
        } else {
            Toast.makeText(MainActivity.this, "User creation canceled!", Toast.LENGTH_SHORT).show();
        }
    }

    // Validate Application Login --> Login is missing "LogInLog" class for tracking the logins. Furthermore, Security class is missing (Login restrictions, password restrictions etc.)
    @SuppressLint("SetTextI18n")
    private void validate(String userName, String userPassword, ArrayList<User> user) {
        LogIn logIn = LogIn.getInstance();
        ArrayList<User> list = (ArrayList<User>)user;
        if(loginCounter!=0){
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i).getEmail());
                if (list.get(i).getEmail().equals(userName)) {
                    String validUser = list.get(i).getEmail();
                    String validPassWord = list.get(i).getPassword();
                    String name = list.get(i).getName();
                    int id = list.get(i).getId();
                    logIn.setLoginId(id); // Sets the Login id as the user id for further use (Display correct user data/information in the activities and saves new data to correct user.
                    if (userName.equals(validUser) && userPassword.equals(validPassWord)) {
                        Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Toast.makeText(this, "Welcome " + name + "!", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        loginCounter--;
                        attemptsView.setText("Number of attempts remaining: " + String.valueOf(loginCounter));
                        Toast.makeText(this, "Invalid password.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                } else{
                    Toast.makeText(this, "Invalid e-mail.", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            logInButton.setEnabled(false);
            Toast.makeText(this, "Too many login attempts. Login locked.", Toast.LENGTH_SHORT).show();
        }

    }

    // Insert New User to the DataBase
    public void insertUser() {

        String name = intent.getStringExtra(CreateUserActivity.EXTRA_NAME);
        String email = intent.getStringExtra(CreateUserActivity.EXTRA_EMAIL);
        String password = intent.getStringExtra(CreateUserActivity.EXTRA_PASSWORD);

        System.out.println(name + "     " + email + "     " + password); // De-bug

        User user = new User(name, email, password);
        bankAccountViewModel.insertUser(user);

        Toast.makeText(MainActivity.this, "Your user has been created " + name+"!", Toast.LENGTH_LONG).show();
    }

    // This is used for setting Login True for saving Login preferences of a user
    // Log out will erase them, while only closing the application won't.
    public void setLogin(){
        login = true;
    }

    public Intent setThisIntent(Intent data) {
        this.intent = data;
        return intent;
    }

    public void setUserList(List<User> userList){
        this.users = (ArrayList<User>) userList;
    }

}
