package com.example.webservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.spreada.utils.chinese.ZHConverter;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class Change extends AppCompatActivity {
        EditText inputText;
        Button TChangeC,CChangeT;
        TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        this.setTitle("繁體與簡體轉換");
        tv = (TextView) findViewById(R.id.tv);
        inputText = (EditText) findViewById(R.id.inputText);
        TChangeC = (Button) findViewById(R.id.TtoC);
        CChangeT = (Button) findViewById(R.id.CtoT);

        TChangeC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
                String simplifiedStr = converter.convert(inputText.getText().toString());
                tv.setText(simplifiedStr);
            }
        });

        CChangeT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZHConverter converter = ZHConverter.getInstance(ZHConverter.TRADITIONAL);
                String simplifiedStr = converter.convert(inputText.getText().toString());
                tv.setText(simplifiedStr);
            }
        });
    }

}