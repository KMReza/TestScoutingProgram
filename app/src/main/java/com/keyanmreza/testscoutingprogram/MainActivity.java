package com.keyanmreza.testscoutingprogram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static final String BROKER_URL = "tcp://192.168.1.140:1883";
    private static String CLIENT_ID = "Pixel";
    private MQTTHandler mqttHandler;
    private Button goToAuton;
    String[] allianceColor = {"Blue", "Red"};
    String[] fieldPosition = {"Amp Side", "Middle", "Source Side"};
    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView autoCompleteTextView2;
    ArrayAdapter<String> adapterItems;
    ArrayAdapter<String> adapterItems2;
    public static String teamColor;
    public static String teamPosition;
    public static final String EXTRA_NUMBER = "com.keyanmreza.testscoutingprogram.keyanmreza.EXTRA_NUMBER";
    public static final String EXTRA_TEXT = "com.keyanmreza.testscoutingprogram.keyanmreza.EXTRA_TEXT";
    public static final String EXTRA_TEXT2 = "com.keyanmreza.testscoutingprogram.keyanmreza.EXTRA_TEXT2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("BP-Scouter V1.2");
        mqttHandler = new MQTTHandler();
        mqttHandler.connect(BROKER_URL, CLIENT_ID);
        mqttHandler.waitForCompletion();
        autoCompleteTextView = findViewById(R.id.auto_complete_textview);
        autoCompleteTextView2 = findViewById(R.id.positionDropDown);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, allianceColor);
        adapterItems2 = new ArrayAdapter<String>(this, R.layout.list_item, fieldPosition);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView2.setAdapter(adapterItems2);
        goToAuton = (Button) findViewById(R.id.goToAuton);

        isEthOn();

        String testMessage = "TestMessage";
        publishMessage("match/testTeam", testMessage);


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                teamColor = adapterView.getItemAtPosition(position).toString();
            }
        });

        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                teamPosition = adapterView.getItemAtPosition(position).toString();
            }
        });

        goToAuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String testMessage = "TestMessage";
                publishMessage("match/testTeam", "TestMessage");
                openActivity2(teamColor, teamPosition);
            }
        });

        //MQTT Client Name Test
        Toast.makeText(this, CLIENT_ID, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        mqttHandler.disconnect();
        super.onDestroy();
    }

    private void publishMessage(String topic, String message) {
        mqttHandler.publish(topic, message);
    }

    private void subscribeToTopic(String topic) {
        Toast.makeText(this, "Subscribing to topic:" + topic, Toast.LENGTH_SHORT).show();
        mqttHandler.subscribe(topic);
    }

    public void openActivity2(String item, String item2) {
        EditText teamNumber = (EditText) findViewById(R.id.teamNumber);
        int teamNumberText = Integer.parseInt(teamNumber.getText().toString());
        if ((teamColor == null && teamColor.isEmpty()) || (Integer.toString(teamNumberText) == null && Integer.toString(teamNumberText).isEmpty())  || (teamPosition == null && teamPosition.isEmpty())) {
            Toast.makeText(this, "Please Enter Values!", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra(EXTRA_NUMBER, teamNumberText);
            intent.putExtra(EXTRA_TEXT, teamColor);
            intent.putExtra(EXTRA_TEXT2, teamPosition);
            startActivity(intent);
        }
    }

    public static boolean isEthOn() {

        try {

            String line;
            boolean r = false;

            Process p = Runtime.getRuntime().exec("netcfg");

            BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {

                if(line.contains("eth0")){
                    if(line.contains("UP")){
                        r=true;
                    }
                    else{
                        r=false;
                    }
                }
            }
            input.close();

            Log.e("OLE","isEthOn: "+r);
            return r;

        } catch (IOException e) {
            Log.e("OLE","Runtime Error: "+e.getMessage());
            e.printStackTrace();
            return false;
        }

    }
}