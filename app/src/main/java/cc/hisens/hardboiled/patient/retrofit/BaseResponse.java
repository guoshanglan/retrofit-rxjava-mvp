package cc.hisens.hardboiled.patient.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * 统一响应
 * @param <T>
 */
public class BaseResponse<T> {
    @SerializedName("message")
    public String message;
    @SerializedName("result")
    public int result;
    @SerializedName("datas")
    public T datas;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "message='" + message + '\'' +
                ", result=" + result +
                ", datas=" + datas +
                '}';
    }
}
