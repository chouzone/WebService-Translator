package com.example.webservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity{
    private static final String NAMESPACE = "https://WebXml.com.cn/";

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("英漢字典");

    }

    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener()
        { public boolean onTouch(View v, MotionEvent event)
            {
            switch (event.getAction())
                { case MotionEvent.ACTION_DOWN:
                    { v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP); v.invalidate(); break; }
                    case MotionEvent.ACTION_UP: { v.getBackground().clearColorFilter(); v.invalidate(); break; } } return false; } }); }

    public void search(View view) {
        intent = new Intent(MainActivity.this,MainActivity2.class);
        startActivity(intent);
    }

    public void favority(View view) {
        intent = new Intent(MainActivity.this,History.class);
        startActivity(intent);
    }

    public void translate(View view) {
        intent = new Intent(MainActivity.this,translate.class);
        startActivity(intent);
    }

    public void change(View view) {
        intent = new Intent(MainActivity.this,Change.class);
        startActivity(intent);
    }
}