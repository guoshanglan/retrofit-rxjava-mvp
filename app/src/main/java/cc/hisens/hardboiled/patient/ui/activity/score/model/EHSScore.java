package cc.hisens.hardboiled.patient.ui.activity.score.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.score.presenter.EHSScorePresenter;
import cc.hisens.hardboiled.patient.ui.activity.score.presenter.IIEFScorePresenter;
import cc.hisens.hardboiled.patient.utils.DateUtils;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EHSScore extends RealmObject implements Parcelable {

    @PrimaryKey
    @SerializedName("time")
    public long timestamp;
    @SerializedName("score")
    public byte score;

    public EHSScore() {

    }

    protected EHSScore(Parcel in) {
        this.timestamp = in.readLong();
        this.score = in.readByte();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public byte getScore() {
        return score;
    }

    public void setScore(byte score) {
        this.score = score;
    }

    public String getTimestampText() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(DateUtils.double2Date(timestamp));
    }

    public static final Creator<EHSScore> CREATOR = new Creator<EHSScore>() {
        @Override
        public EHSScore createFromParcel(Parcel in) {
            return new EHSScore(in);
        }

        @Override
        public EHSScore[] newArray(int size) {
            return new EHSScore[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.timestamp);
        dest.writeByte(this.score);
    }


    //上传分数
    public void UploadEHSScore(Context context, int score, EHSScorePresenter presenter) {
        HashMap<String, Integer> ehsMap = new HashMap<>();
        ehsMap.put("score", score);
        RequestUtils.post(context, Url.uploadEhsScore, ehsMap, new HashMap<>(), new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result != null) {

                    if (result.result == 0) {
                        Gson gson = new Gson();
                        EHSScore ehsScore = new Gson().fromJson(gson.toJson(result.getDatas()), EHSScore.class);
                        presenter.UploadSuccess(ehsScore);
                    } else {

                        presenter.loginFailed(result.message);
                    }

                }

            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                presenter.loginFailed(errorMsg);
            }
        });


    }

}
