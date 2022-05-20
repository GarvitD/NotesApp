package com.example.quixoteandroidtask.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.quixoteandroidtask.Databases.UsersDatabaseHelper;
import com.example.quixoteandroidtask.R;
import com.example.quixoteandroidtask.databinding.ActivityLoginBinding;

import io.github.muddz.styleabletoast.StyleableToast;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UsersDatabaseHelper usersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usersDatabase = new UsersDatabaseHelper(this);

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString().trim();
                String password = binding.password.getText().toString().trim();
                if(email.length()>0) {
                    if(usersDatabase.isNewUser(email)) {
                        StyleableToast.makeText(LoginActivity.this, "User does not Exist! Signup", Toast.LENGTH_LONG, R.style.errorToast).show();
                    } else {
                        if(password.length() > 0) {
                            if(usersDatabase.isPassCorrect(email,password)) {

                                SharedPreferences sharedPreferences = getSharedPreferences("usersSp",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("id",usersDatabase.getRowNum(email));
                                editor.putBoolean("isLoggedIn",true);
                                editor.apply();

                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            } else {
                                binding.password.requestFocus();
                                binding.password.setError("Password entered is incorrect!");
                            }
                        } else {
                            binding.password.requestFocus();
                            binding.password.setError("Password cannot be empty!");
                        }

                    }
                } else {
                    binding.email.requestFocus();
                    binding.email.setError("Email cannot be empty!");
                }
            }
        });

        binding.toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });
    }
}