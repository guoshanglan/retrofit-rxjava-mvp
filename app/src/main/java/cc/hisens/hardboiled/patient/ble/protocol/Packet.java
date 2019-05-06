package cc.hisens.hardboiled.patient.ble.protocol;

/**
 * @author Ou Weibin
 * @version 1.0
 */
public class Packet {
    private byte[] data;
    private Retransmission retransmission;

    public Packet(byte[] data, Retransmission retransmission) {
        this.data = data;
        this.retransmission = retransmission;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Retransmission getRetransmission() {
        return retransmission;
    }

    public void setRetransmission(Retransmission retransmission) {
        this.retransmission = retransmission;
    }
}
