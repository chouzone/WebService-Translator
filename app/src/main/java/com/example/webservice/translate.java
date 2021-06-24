package com.example.webservice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;


public class translate extends AppCompatActivity {
    EditText et;
    TextView tv;
    Button bt;
    String sourceText;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        this.setTitle("中翻英");
        bt = (Button)findViewById(R.id.button);
        et = (EditText)findViewById(R.id.et);
        tv = (TextView)findViewById(R.id.tv);


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                translator();
            }
        });
    }

    public void translator(){
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.CHINESE)
                .setTargetLanguage(TranslateLanguage.ENGLISH)
                .build();
        final Translator englishChineseTranslator = Translation.getClient(options);

        String text = String.valueOf(et.getText());
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        englishChineseTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        (OnSuccessListener) v -> {
                            englishChineseTranslator.translate(text)
                                    .addOnSuccessListener(
                                            (OnSuccessListener) translatedText -> {
                                                tv.setText("");
                                                tv.append((String) translatedText);

                                                Log.i("TAG", "Translation is "+(String) translatedText);
                                            })
                                    .addOnFailureListener(
                                            new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Error.
                                                    Log.e("Error","Translation faliled "+e);
                                                }
                                            });
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be downloaded or other internal error.
                                Log.e("Error","Model could n’t be downloaded "+e);

                            }
                        });
    }

}