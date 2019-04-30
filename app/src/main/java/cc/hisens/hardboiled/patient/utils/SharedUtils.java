package cc.hisens.hardboiled.patient.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * SharedPreferences写入工具
 * 
 * @author Andy_Wang
 * 
 */
public class SharedUtils {

	private Context context;

	private final String subTAG = getClass().getSimpleName();

	public SharedUtils(Context context) {
		this.context = context;
	}

	/**
	 *  写入数据
	 * @param map
	 */
	public void write(Map<String, Object> map) {
		SharedPreferences share = context.getSharedPreferences("data", Context.MODE_PRIVATE);
		if (share != null) {
			Editor editor = share.edit();
			Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> entry = (Entry<String, Object>) iterator.next();
				Object obj = entry.getValue();
				if (obj instanceof String) {
					editor.putString(entry.getKey().toString(), entry.getValue().toString());
				} else if (obj instanceof Integer) {
					editor.putInt(entry.getKey().toString(), Integer.parseInt(entry.getValue().toString()));
				} else if (obj instanceof Boolean) {
					editor.putBoolean(entry.getKey().toString(), Boolean.getBoolean(entry.getValue().toString()));
				} else if (obj instanceof Float) {
					editor.putFloat(entry.getKey().toString(), Float.parseFloat(entry.getValue().toString()));
				} else if (obj instanceof Long) {
					editor.putLong(entry.getKey().toString(), Long.parseLong(entry.getValue().toString()));
				}
			}
			editor.commit();
		}
	}

	/**
	 * 清除shared
	 */
	public void clear() {
		SharedPreferences share = context.getSharedPreferences("data", Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.clear();
		editor.commit();
	}

	public void writeString(String key, String value) {
		SharedPreferences share = context.getSharedPreferences("data", Context.MODE_PRIVATE);
		if (share != null) {
			Editor editor = share.edit();
			editor.putString(key, value);
			editor.commit();
		}
	}

	public void writeInt(String key, int value) {
		SharedPreferences share = context.getSharedPreferences("data", Context.MODE_PRIVATE);
		if (share != null) {
			Editor editor = share.edit();
			editor.putInt(key, value);
			editor.commit();
		}
	}


    public void writeLong(String key, long value) {
        SharedPreferences share = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        if (share != null) {
            Editor editor = share.edit();
            editor.putLong(key, value);
            editor.commit();
        }
    }


    public void writeBoolean(String key, Boolean value) {
		SharedPreferences share = context.getSharedPreferences("data", Context.MODE_PRIVATE);
		if (share != null) {
			Editor editor = share.edit();
			editor.putBoolean(key, value);
			editor.commit();
		}
	}

	public void delete(String key) {
		try {
			SharedPreferences share = context.getSharedPreferences("data", Context.MODE_PRIVATE);
			if (share != null) {
				Editor editor = share.edit();
				editor.remove(key);
				editor.commit();
			}
		} catch (Exception e) {
			Log.e(subTAG, e.getMessage());
		}
	}

	public String readString(String key) {
		SharedPreferences share = context.getSharedPreferences("data", Context.MODE_PRIVATE);
		if (share == null || !share.contains(key)) {
			return null;
		}
		return share.getString(key, null);
	}

	public Integer readInt(String key) {
		SharedPreferences share = context.getSharedPreferences("data", Context.MODE_PRIVATE);
		if (share == null || !share.contains(key)) {
			return 0;
		}
		return share.getInt(key, 0);
	}

	public Long readLong(String key) {
		SharedPreferences share = context.getSharedPreferences("data", Context.MODE_PRIVATE);
		if (share == null || !share.contains(key)) {
			return null;
		}
		return share.getLong(key, 0l);
	}

	public Boolean readBoolean(String key) {
		SharedPreferences share = context.getSharedPreferences("data", Context.MODE_PRIVATE);
		if (share == null || !share.contains(key)) {
			return false;
		}
		return share.getBoolean(key, false);
	}
	
	public Boolean readBoolean(String key, boolean defaultValue) {
		SharedPreferences share = context.getSharedPreferences("data", Context.MODE_PRIVATE);
		if (share == null || !share.contains(key)) {
			return defaultValue;
		}
		return share.getBoolean(key, defaultValue);
	}

	public Float readFloat(String key) {
		SharedPreferences share = context.getSharedPreferences("data", Context.MODE_PRIVATE);
		if (share == null || !share.contains(key)) {
			return null;
		}
		return share.getFloat(key, 0f);
	}

	public void writeString(String arg0, String key, String value) {
		SharedPreferences share = context.getSharedPreferences(arg0, Context.MODE_PRIVATE);
		if (share != null) {
			Editor editor = share.edit();
			editor.putString(key, value);
			editor.commit();
		}
	}

	public String readString(String arg0, String key) {
		SharedPreferences share = context.getSharedPreferences(arg0, Context.MODE_PRIVATE);
		if (share == null || !share.contains(key)) {
			return null;
		}
		return share.getString(key, null);
	}


	/**
	 * 保存List
	 * @param tag
	 * @param datalist
	 */
	public <T> void setDataList(String tag, List<T> datalist) {

		SharedPreferences share = context.getSharedPreferences(tag, Context.MODE_PRIVATE);
		if (share != null) {
			Editor editor = share.edit();
			if (null == datalist || datalist.size() <= 0)
				return;
			Gson gson = new Gson();
			//转换成json数据，再保存
			String strJson = gson.toJson(datalist);
			editor.clear();
			editor.putString(tag, strJson);
			editor.commit();

		}
	}

	/**
	 * 获取List
	 * @param tag
	 * @return
	 */
	public <T> ArrayList<T> getDataList(String tag) {
		SharedPreferences share = context.getSharedPreferences(tag, Context.MODE_PRIVATE);
		if (share == null) {
			return null;
		}
		ArrayList<T> datalist=new ArrayList<T>();
		String strJson = share.getString(tag, null);
		if (null == strJson) {
			return datalist;
		}
		Gson gson = new Gson();
		datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
		}.getType());
		return datalist;
	}
}