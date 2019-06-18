package cc.hisens.hardboiled.patient.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2019/6/3.
 * <p>
 * 解析字典型json数据，
 */

public class JsonUtils {


    /**
     * 解析字典型json数据，
     */

    public static List getDictionaryListObject(JSONObject jsonObject, String result) {

        List<String> datas = new ArrayList<String>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);
                Iterator<String> iterator = object.keys();
                String str = null;
                while (iterator.hasNext()) {
                    str = iterator.next();
                    if (!TextUtils.isEmpty(str)) {
                        datas.add(object.getString(str));
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return datas;

    }


}
