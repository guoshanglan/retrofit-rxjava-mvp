package cc.hisens.hardboiled.patient.utils;


import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


//读取js文件内容
public class ReadFileUtil {


    public static String fileRead(String fileName) throws Exception {

        File file = new File(Environment.getExternalStorageDirectory().getPath(), fileName);//定义一个file对象，用来初始化FileReader
        if (file == null || !file.exists()) {

            return null;
        }

        FileReader reader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader

        BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存

        StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
        String s = "";
        while ((s = bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
            sb.append(s);//将读取的字符串添加换行符后累加存放在缓存中
        }
        bReader.close();

        return sb.toString();

    }


    public boolean isSDcard() {

        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

    }


}
