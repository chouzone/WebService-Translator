package com.example.webservice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    EditText et;
    TextView content,vocabulary,meaning,MeanTitle;
    Button favority;
    String [] result;
    LoadingDialog loadingDialog;
    Boolean key = false;
    Context context;
    Intent intent;
    private static final String FILE_NAME = "VOCABULARY.txt";
    private String TAG ="Vik";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if(Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("單字查詢");
        et = (EditText)findViewById(R.id.et);
        content = (TextView)findViewById(R.id.textView);
        vocabulary = (TextView)findViewById(R.id.vocabulary);
        meaning = (TextView)findViewById(R.id.meaning);
        MeanTitle = (TextView)findViewById(R.id.textView6);
        favority = (Button)findViewById(R.id.favorites);
        loadingDialog = new LoadingDialog(MainActivity2.this);

        intent = getIntent();
        String v = intent.getStringExtra("v");
        context = getApplicationContext();
        AddFile.getFilePath(context,"dictionary");
        AsyncCallWS task = new AsyncCallWS();
        if (v!=null){
            loading(v);
            task.execute(v);
        }

        //限制最大輸入17字元
        et.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        et.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND ||
                        (event != null && event.getKeyCode()==KeyEvent.KEYCODE_ENTER)){
                    AsyncCallWS task = new AsyncCallWS();
                    String value = et.getText().toString();
                    //讀取檔案確認是否有收藏過此單字
                    loading(value);
                    task.execute(value); //連接webservice
                    return  true;
                }
                return false;
            }
        });

    }

    public void save(View view) {
        String read = ReadV();
        String vocabulary = et.getText().toString();
        Log.e("log_tag","read="+read);
        if (favority.getText().toString().equals("收藏單字")){
            if (read.length()>0){
                WriteV(read+','+vocabulary);
                favority.setText("取消收藏");
            }
            else {
                WriteV(vocabulary);
                favority.setText("取消收藏");
            }
        }
        else {
            read = read.replace(vocabulary,"");
            WriteV(read);
            favority.setText("收藏單字");
        }

    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return false;
    }

    private class AsyncCallWS extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String v = params[0];
            Log.e("tag","v="+v);
            Log.i(TAG, "doInBackground");
            word(v);
            sentence(v);
            return null;
        }
    }

    public void sentence(String value)
    {
        String m = "";
        result = new String[10];
        String SOAP_ACTION = "http://WebXml.com.cn/TranslatorSentenceString";
        String METHOD_NAME = "TranslatorSentenceString";
        String NAMESPACE = "http://WebXml.com.cn/";
        String URL = "http://fy.webxml.com.cn/webservices/EnglishChinese.asmx";
        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("wordKey", value);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport= new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);

            SoapObject resultString = (SoapObject)soapEnvelope.getResponse();
            Log.i("tag", "Result Fahrenheit: " + resultString);
            for (int i = 0; i < resultString.getPropertyCount(); i++){
                m +=resultString.getProperty(i).toString()+"\n";
            }
            MeanTitle.setVisibility(View.VISIBLE);
            content.setText(m+"\n");

        }
        catch(Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }

    public void word(String value)
    {
        String m = "";
        result = new String[10];
        String SOAP_ACTION = "http://WebXml.com.cn/getEnCnTwoWayTranslator";
        String METHOD_NAME = "getEnCnTwoWayTranslator";
        String NAMESPACE = "http://WebXml.com.cn/";
        String URL = "http://ws.webxml.com.cn/WebServices/TranslatorWebService.asmx";
        try {

            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("Word", value);

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport= new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);

            SoapObject resultString = (SoapObject)soapEnvelope.getResponse();
            Log.i("tag", "Result Fahrenheit: " + resultString.getProperty(0).toString());
            for (int i = 1; i < resultString.getPropertyCount(); i++){
                m +=resultString.getProperty(i).toString()+"\n";
            }
            meaning.setText(m.replace('|','\n'));

        }
        catch(Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }

    public void  loading(String value){
        if (ReadV().indexOf(value)<0){
            favority.setText("收藏單字");
        }
        else {
            favority.setText("取消收藏");
        }
        vocabulary.setText(value); //將輸入單字顯示在畫面上
        favority.setVisibility(View.VISIBLE); //將收藏按鈕顯示出來
    }

    public void WriteV(String m) {
        FileOutputStream fop = null;
        try {
            File file = new File(AddFile.getFilePath(context,"dictionary") + File.separator + FILE_NAME); //輸出檔案位置
            Log.e("log_tag", "file="+file + "");
            fop = new FileOutputStream(file);
            fop.write(m.getBytes());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fop != null) {
                try {
                    fop.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String ReadV() {
        FileInputStream fis = null;
        StringBuilder text = new StringBuilder();
        String mc = "";

        try {
            File file = new File(AddFile.getFilePath(context,"dictionary") + File.separator + FILE_NAME); //讀取檔案位置
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            mc = br.readLine();
            Log.e("log_tag","mc="+mc);
            br.close();

        }   catch (IOException e) {
            e.printStackTrace();
        }
        return mc;
    }

}