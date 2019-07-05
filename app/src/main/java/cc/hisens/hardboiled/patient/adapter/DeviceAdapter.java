package cc.hisens.hardboiled.patient.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.clj.fastble.data.BleDevice;

import java.util.List;

import cc.hisens.hardboiled.patient.base.MyBaseAdapter;
import cc.hisens.hardboiled.patient.ui.activity.login.model.User;
import cc.hisens.hardboiled.patient.ui.activity.searchdevice.SearchDeviceActivity;

public class DeviceAdapter extends MyBaseAdapter<BleDevice> {


    public DeviceAdapter(Context context, List<BleDevice> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
