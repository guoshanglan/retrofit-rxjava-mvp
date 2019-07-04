package cc.hisens.hardboiled.patient.eventbus;

import com.clj.fastble.data.BleDevice;

public class onDeviceMessage {

    public BleDevice device;

    public onDeviceMessage(BleDevice device) {
        this.device = device;
    }




}
