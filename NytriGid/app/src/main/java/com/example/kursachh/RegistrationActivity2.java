package com.example.kursachh;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class RegistrationActivity2 extends AppCompatActivity {
    String[] lifestules = { "Активный", "Сидячий", "Лежачий", "Смешанный"};
    private String login, password, name, surname;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        Intent intent = getIntent();
        login = intent.getStringExtra("login");
        password = intent.getStringExtra("password");
        name = intent.getStringExtra("name");
        surname = intent.getStringExtra("surname");

        ImageButton selectAgeButton = findViewById(R.id.idSelectAgeButton);
        SeekBar seekBarHeight = findViewById(R.id.seekBarHeight);
        SeekBar seekBarWeight = findViewById(R.id.seekBarWeight);
        TextView ageValueText = findViewById(R.id.idAgeValueText);
        TextView textViewValueHeight = findViewById(R.id.seekBarHeightValue);
        TextView textViewValueWeight = findViewById(R.id.seekBarWeightValue);

        Spinner spinnerLifestyle = findViewById(R.id.spinnerLifestyle);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lifestules);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLifestyle.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle item selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle nothing selected
            }
        };
        spinnerLifestyle.setOnItemSelectedListener(itemSelectedListener);

        final Calendar defaultCalendar = Calendar.getInstance();
        defaultCalendar.set(1950, Calendar.JANUARY, 1);

        int defaultYear = defaultCalendar.get(Calendar.YEAR);
        int defaultMonth = defaultCalendar.get(Calendar.MONTH);
        int defaultDay = defaultCalendar.get(Calendar.DAY_OF_MONTH);

        ageValueText.setText(defaultDay + "-" + (defaultMonth + 1) + "-" + defaultYear);

        selectAgeButton.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Получить текущую дату для ограничения
            c.set(year, month, day);
            long today = c.getTimeInMillis();

            @SuppressLint("SetTextI18n") DatePickerDialog datePickerDialog = new DatePickerDialog
                    (RegistrationActivity2.this, R.style.CustomDatePickerDialog,
                            (view, year1, monthOfYear, dayOfMonth) -> ageValueText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1),
                            year, month, day);
            // Установить максимальную дату для выбора
            datePickerDialog.getDatePicker().setMaxDate(today);

            datePickerDialog.show();
        });

        seekBarHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewValueHeight.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Handle start tracking touch
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Handle stop tracking touch
            }
        });

        seekBarWeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewValueWeight.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Handle start tracking touch
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Handle stop tracking touch
            }
        });
    }

    public void RegistrationBack1(View v) {
        Intent intent = new Intent(RegistrationActivity2.this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void RegistrationNextPage3(View v) {
        TextView ageValueText = findViewById(R.id.idAgeValueText);
        TextView textViewValueHeight = findViewById(R.id.seekBarHeightValue);
        TextView textViewValueWeight = findViewById(R.id.seekBarWeightValue);
        Spinner spinnerLifestyle = findViewById(R.id.spinnerLifestyle);
        RadioGroup genderRadioGroup = findViewById(R.id.radioGroupGender);

        String age = ageValueText.getText().toString().trim();
        String height = textViewValueHeight.getText().toString().trim();
        String weight = textViewValueWeight.getText().toString().trim();
        String lifestyle = spinnerLifestyle.getSelectedItem().toString().trim();

        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        boolean isGenderSelected = selectedGenderId != -1;

        final Calendar defaultCalendar = Calendar.getInstance();
        defaultCalendar.set(1950, Calendar.JANUARY, 1);
        int defaultYear = defaultCalendar.get(Calendar.YEAR);
        int defaultMonth = defaultCalendar.get(Calendar.MONTH);
        int defaultDay = defaultCalendar.get(Calendar.DAY_OF_MONTH);

        String[] dateParts = age.split("-");
        if (dateParts.length == 3) {
            int selectedDay = Integer.parseInt(dateParts[0]);
            int selectedMonth = Integer.parseInt(dateParts[1]) - 1;
            int selectedYear = Integer.parseInt(dateParts[2]);

            boolean isDefaultDate = (selectedDay == defaultDay && selectedMonth == defaultMonth && selectedYear == defaultYear);

            if (age.isEmpty() || height.isEmpty() || weight.isEmpty() || lifestyle.isEmpty() || !isGenderSelected || isDefaultDate) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(RegistrationActivity2.this, RegistrationActivity3.class);
                intent.putExtra("login", login);
                intent.putExtra("password", password);
                intent.putExtra("name", name);
                intent.putExtra("surname", surname);
                intent.putExtra("age", age);
                intent.putExtra("height", height);
                intent.putExtra("weight", weight);
                intent.putExtra("lifestyle", lifestyle);
                intent.putExtra("gender", isGenderSelected);
                startActivity(intent);
            }
        }
    }
}
