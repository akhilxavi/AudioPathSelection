package com.example.audiopathselection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
//import android.os.SystemProperties;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static java.lang.Compiler.command;

public class MainActivity extends AppCompatActivity {

    Spinner mySpinner1, mySpinner2;
    ArrayList<String> audioPaths;
    String outSelection = "default";
    String inSelection = "default";

    String outCmd = "persist.audio.output.sel ";
    String inCmd = "persist.audio.input.sel ";

    String inPropValue, outPropValue;

    public void updateAudioPath(ArrayList<String> strings){
        switch (strings.get(0)){
            case "Hdmi Out":
                outPropValue = "hdmi-out";
                break;
            case "3.5 MM":
                outPropValue = "3.5mm";
                break;
            case "USB":
                outPropValue = "usb";
                break;
            default:
                outPropValue = "default";
                break;
        }

        switch (strings.get(1)){
            case "Hdmi In":
                inPropValue = "hdmi-in";
                break;
            case "3.5 MM":
                inPropValue = "3.5mm";
                break;
            case "USB":
                inPropValue = "usb";
                break;
            default:
                inPropValue = "default";
                break;
        }
    }

    public void playNow(View view){
        Intent intent = new Intent(getApplicationContext(), myPlayer.class);

        intent.putExtra("output", outSelection);
        intent.putExtra("input", inSelection);

        startActivity(intent);
    }

    public void updateVarSetting(final String command){
        Process process;
        try {
            process = Runtime.getRuntime().exec("shell-tunnel --client");

            DataOutputStream stdin = new DataOutputStream(process.getOutputStream());
            stdin.writeBytes("setprop " + command + "\n");
            stdin.flush();
            stdin.writeBytes("exit\n");
            stdin.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void applyNow(View view){
        outSelection = mySpinner1.getSelectedItem().toString();
        inSelection = mySpinner2.getSelectedItem().toString();

        audioPaths = new ArrayList<String>();

        audioPaths.add(outSelection);
        audioPaths.add(inSelection);

        updateAudioPath(audioPaths);

        Log.i("output path = ",outPropValue);
        Log.i(" input path = ",inPropValue);

        outCmd += outPropValue;
        inCmd += inPropValue;

        updateVarSetting(outCmd);
        updateVarSetting(inCmd);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySpinner1 = (Spinner) findViewById(R.id.spinner1);
        mySpinner2 = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<String> myArrayAdaptor1 = new ArrayAdapter<String>(MainActivity.this,
                R.layout.my_spinner, getResources().getStringArray(R.array.outAudioPaths));

        ArrayAdapter<String> myArrayAdaptor2 = new ArrayAdapter<String>(MainActivity.this,
                R.layout.my_spinner, getResources().getStringArray(R.array.inAudioPaths));

        //myArrayAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mySpinner1.setAdapter(myArrayAdaptor1);
        mySpinner2.setAdapter(myArrayAdaptor2);
    }
}