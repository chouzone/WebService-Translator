package com.example.webservice;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import static android.os.Environment.MEDIA_MOUNTED;

public class AddFile {
    public static String getFilePath(Context context,String dir){
        String directoryPath="";
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){//判斷外部儲存是否可用
            directoryPath =context.getExternalFilesDir(dir).getAbsolutePath();
        }else{//沒外部儲存就使用內部儲存
            directoryPath=context.getFilesDir() + File.separator + dir;
        }
        //Log.e("log_tag","directoryPath="+directoryPath);
        File file = new File(directoryPath);
        if(!file.exists()){//判斷檔案目錄是否存在
            file.mkdirs();
        }
        return directoryPath;
    }
}
