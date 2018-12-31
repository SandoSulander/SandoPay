package store.catsocket.sandopay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static final int EDIT_USER_REQUEST = 5;

    private BankAccountViewModel bankAccountViewModel;
    private DrawerLayout drawer;
    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Floating Action Button on the Home Screen

        FloatingActionButton buttonAddAccount = findViewById(R.id.button_add_account);
        buttonAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("########################################################################Clicked");
                Intent intent1 = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivityForResult(intent1, EDIT_USER_REQUEST);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_profile);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final UserAdapter userAdapter = new UserAdapter();
        recyclerView.setAdapter(userAdapter);

        bankAccountViewModel = ViewModelProviders.of(this).get(BankAccountViewModel.class);
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
                userAdapter.submitList(submitList);
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

        Intent intentHome = new Intent(ProfileActivity.this, AccountActivity.class);
        Intent intentProfile = new Intent (ProfileActivity.this, ProfileActivity.class);
        Intent intentTransfer = new Intent(ProfileActivity.this, TransactionActivity.class);
        Intent intentLogout = new Intent(ProfileActivity.this, MainActivity.class);

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

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setThisIntent(data);
        if (requestCode == EDIT_USER_REQUEST && resultCode == RESULT_OK) {
            updateUser();
        } else {
        }
    }

    // Update to DataBase
    public void updateUser() {

        String name = intent.getStringExtra(EditProfileActivity.EXTRA_NAME);
        String newEmail = intent.getStringExtra(EditProfileActivity.EXTRA_NEW_EMAIL);
        String newPassword = intent.getStringExtra(EditProfileActivity.EXTRA_NEW_PASSWORD);
        String idString = intent.getStringExtra(EditProfileActivity.EXTRA_USER_ID);
        int id = Integer.parseInt(idString);

        User user = new User(name, newEmail, newPassword);
        user.setId(id);
        bankAccountViewModel.updateUser(user);
        Toast.makeText(this, "User information updated!", Toast.LENGTH_SHORT).show();

    }

    public Intent setThisIntent(Intent data) {
        this.intent = data;
        return intent;
    }

}
