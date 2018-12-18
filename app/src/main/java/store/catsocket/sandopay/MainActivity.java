package store.catsocket.sandopay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editUsername, editPassword;
    private TextView attemptsView;
    private Button logInButton;

    private int counter = 5;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);

        attemptsView = (TextView) findViewById(R.id.attemptsView);

        attemptsView.setText("Number of attempts remaining: 5");

        logInButton = (Button) findViewById(R.id.logInButton);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(editUsername.getText().toString(),editPassword.getText().toString());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void validate(String userName, String userPassword){
        if (userName.equals("Admin") && userPassword.equals("1234")){
            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(intent);
        } else{
            counter--;

            attemptsView.setText("Number of attempts remaining: " + String.valueOf(counter));

            if(counter == 0){
                logInButton.setEnabled(false);
            }
        }
    }
}
