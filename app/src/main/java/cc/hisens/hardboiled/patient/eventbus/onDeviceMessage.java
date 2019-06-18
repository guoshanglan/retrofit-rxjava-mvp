package cc.hisens.hardboiled.patient.eventbus;

public class onDeviceMessage {

    private String onBattery;   //蓝牙设备电量信息

    public onDeviceMessage(String onBattery) {
        this.onBattery = onBattery;
    }

    public String getMessage() {
        return onBattery;
    }

    @Override
    public String toString() {
        return onBattery;
    }


}
