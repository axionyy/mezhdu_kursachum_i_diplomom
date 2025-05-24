package com.example.kursachh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void Autorization(View v) {
        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void RegistrationNextPage2(View v) {
        EditText loginRegistration = findViewById(R.id.loginInputRegistration);
        EditText passwordRegistration = findViewById(R.id.passwordInputRegistration);
        EditText nameRegistration = findViewById(R.id.nameInputRegistration);
        EditText surnameRegistration = findViewById(R.id.surnameInputRegistration);

        String login = loginRegistration.getText().toString().trim();
        String password = passwordRegistration.getText().toString().trim();
        String name = nameRegistration.getText().toString().trim();
        String surname = surnameRegistration.getText().toString().trim();

        if (login.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(RegistrationActivity.this, RegistrationActivity2.class);
            intent.putExtra("login", login);
            intent.putExtra("password", password);
            intent.putExtra("name", name);
            intent.putExtra("surname", surname);
            startActivity(intent);
        }
    }
}
