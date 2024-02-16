package com.keyanmreza.testscoutingprogram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    private Button goToTeleop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().setTitle("Match Auton Phase");

        goToTeleop = (Button) findViewById(R.id.goToTeleop);

        Intent intent = getIntent();
        int number = intent.getIntExtra(MainActivity.EXTRA_NUMBER, 0);
        String text = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        String text2 = intent.getStringExtra(MainActivity.EXTRA_TEXT2);

        Toast.makeText(this, "" + number, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, text , Toast.LENGTH_SHORT).show();
        Toast.makeText(this, text2 , Toast.LENGTH_SHORT).show();

        goToTeleop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });
    }

    public void openActivity3() {
        Intent intent2 = new Intent(this, MainActivity3.class);
        startActivity(intent2);
    }
}