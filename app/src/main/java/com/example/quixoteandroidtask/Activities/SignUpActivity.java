package com.example.quixoteandroidtask.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.quixoteandroidtask.Databases.UsersDatabaseHelper;
import com.example.quixoteandroidtask.R;
import com.example.quixoteandroidtask.databinding.ActivitySignUpBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.muddz.styleabletoast.StyleableToast;

public class SignUpActivity extends AppCompatActivity {

    private static final int MIN_NAME = 1;
    private static final int MIN_PASSWORD = 8;
    private static final int MIN_UPPER = 2;
    private static final int MIN_DIGIT = 2;
    private static final int MIN_SPECIAL_CHAR = 1;

    private ActivitySignUpBinding binding;
    private UsersDatabaseHelper usersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usersDatabase = new UsersDatabaseHelper(this);

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checks()) {
                    String name = binding.name.getText().toString();
                    String email = binding.emailId.getText().toString();
                    String phone = binding.phoneNum.getText().toString();
                    String password = binding.password.getText().toString();
                    if(usersDatabase.isNewUser(email) && usersDatabase.isNewUser(phone)) {
                        usersDatabase.addUser(email,password,name,phone);

                        SharedPreferences sharedPreferences = getSharedPreferences("usersSp",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("data",email);
                        editor.putBoolean("isLoggedIn",true);
                        editor.apply();

                        startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                    } else {
                        StyleableToast.makeText(SignUpActivity.this, "User already Exists! Login", Toast.LENGTH_LONG, R.style.errorToast).show();
                    }
                }
            }
        });

        binding.toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });
    }

    private boolean checks() {
        String name = binding.name.getText().toString().trim();
        String email = binding.emailId.getText().toString().trim();
        String phone = binding.phoneNum.getText().toString().trim();
        String password = binding.password.getText().toString().trim();

        boolean check1 = name.length() >= MIN_NAME;
        boolean check2 = !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();

        long specialChars = name.length() - password.chars().filter(Character::isLetter).count() - password.chars().filter(Character::isDigit).count() - password.chars().filter(Character::isWhitespace).count();
        boolean check3 = password.length() >= MIN_PASSWORD
                && !password.contains(name)
                && password.chars().filter(Character::isUpperCase).count() >= MIN_UPPER
                && password.chars().filter(Character::isDigit).count() >= MIN_DIGIT
                && specialChars >= MIN_SPECIAL_CHAR;
        boolean check4 = isPhoneValid(phone);

        if(!check1) {
            binding.name.requestFocus();
            binding.name.setError("Please enter more than "+MIN_NAME+" characters");
        }
        if(!check2) {
            binding.emailId.requestFocus();
            binding.emailId.setError("Please enter a valid Email Address");
        }
        if (!check3) {
            binding.password.requestFocus();
            String error_message = "";
            if(!(password.length() >= MIN_PASSWORD)) {
                error_message = "Please enter more than "+MIN_PASSWORD+" characters";
            } else if(password.contains(name)) {
                error_message = "Password cannot contain the name";
            } else if (!(password.chars().filter(Character::isUpperCase).count() >= MIN_UPPER)) {
                error_message = "Please enter more than "+MIN_UPPER+" Upper Case characters";
            } else if (!(password.chars().filter(Character::isDigit).count() >= MIN_DIGIT)) {
                error_message = "Please enter more than "+MIN_DIGIT+" digits";
            } else if(!(specialChars >= MIN_SPECIAL_CHAR)) {
                error_message = "Please enter more than "+MIN_SPECIAL_CHAR+" special case characters";
            }
            binding.password.setError(error_message);
        }
        if(!check4) {
            binding.phoneNum.requestFocus();
            binding.phoneNum.setError("Please enter a valid Phone Number");
        }
        return check1 && check2 && check3 && check4;
    }

    private boolean isPhoneValid(String phone) {
        Pattern p = Pattern.compile("(0|91)?[7-9][0-9]{9}");
        Matcher m = p.matcher(phone);
        return (m.find() && m.group().equals(phone));
    }
}