package com.example.webservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class History extends AppCompatActivity {
    String [] favority;
    Context context;
    ListView lv;
    Intent intent;
    ArrayAdapter adapter;
    private static final String FILE_NAME = "VOCABULARY.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        this.setTitle("我的收藏");

        lv = (ListView)findViewById(R.id.listview1);
        context = getApplicationContext();
        AddFile.getFilePath(context,"dictionary");
        String read = ReadV();
        favority = read.split(",");
        List<String> list = new ArrayList<>(Arrays.asList(favority));
        list.remove("");
        Log.e("log_tag","list="+list);
        adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String v = list.get(position);
                Log.e("log_tag","index="+position);
                intent = new Intent(History.this,MainActivity2.class);
                intent.putExtra("v",v);
                startActivity(intent);
            }
        });
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