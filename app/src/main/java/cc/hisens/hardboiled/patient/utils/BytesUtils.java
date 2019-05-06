package cc.hisens.hardboiled.patient.utils;

import com.socks.library.KLog;

import java.util.Arrays;
import java.util.Locale;

/**
 * @author Ou Weibin
 * @version 1.0
 */
public class BytesUtils {

    public static byte[] intToBytes(int data) {
        byte[] res = new byte[4];
        for (int i = 0; i < res.length; i++) {
            res[i] = (byte) ((data >> (i * 8)) & 0xFF);
        }
        return res;
    }

    public static byte[] intToBytes(int data, byte length) {
        byte[] res = new byte[length];
        for (int i = 0; i < res.length; i++) {
            res[i] = (byte) ((data >> (i * 8)) & 0xFF);
        }
        return res;
    }

    public static int bytesToInt(byte[] data) {
        int length = data.length;
        int res = 0;
        for (int i = 0; i < length; i++) {
            res |= (data[i] & 0xFF) << (i * 8);
        }
        return res;
    }

    /**
     * 大端模式转换
     *
     * @param data
     * @return
     */
    public static int bytesToIntBig(byte[] data) {
        int length = data.length;
        int res = 0;
        for (int i = 0; i < length; i++) {
            res |= (data[i] & 0xFF) << ((length - 1 - i) * 8);
        }
        return res;
    }

    public static byte[] shortToBytes(short data) {
        byte[] res = new byte[2];
        for (int i = 0; i < res.length; i++) {
            res[i] = (byte) ((data >> (i * 8)) & 0xFF);
        }
        return res;
    }

    public static short bytesToShort(byte[] data) {
        int length = data.length > 2 ? 2 : data.length;
        short res = 0;
        for (int i = 0; i < length; i++) {
            res |= (data[i] & 0xFF) << (i * 8);
        }
        return res;
    }

    public static byte[] concat(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public static byte[] concatHeaderAndPayload(byte[] header, byte[]... payload) {
        int totalLength = header.length;
        int payloadLength = 0;
        for (byte[] array : payload) {
            payloadLength += array.length;
        }
        totalLength += payloadLength;

        byte[] result = Arrays.copyOf(header, totalLength);
        result[1] = (byte) payloadLength;
        int offset = header.length;

        for (byte[] array : payload) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }

    public static void printHex(String name, byte[] data) {
        String str = "[" + name + "] : ";
        for (int i = 0; i < data.length; i++) {
            str += String.format(Locale.getDefault(), "%02X ", (int) (data[i] & 0xFF));
        }
        KLog.i("-->>" + str);
    }


    public static byte[] hexStrToByteArray(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new byte[0];
        }
        byte[] byteArray = new byte[str.length() / 2];
        for (int i = 0; i < byteArray.length; i++) {
            String subStr = str.substring(2 * i, 2 * i + 2);
            byteArray[i] = ((byte) Integer.parseInt(subStr, 16));
        }
        return byteArray;
    }

    public static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
