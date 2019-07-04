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
import cc.hisens.hardboiled.patient.ui.activity.login.model.User;
import cc.hisens.hardboiled.patient.ui.activity.score.presenter.IIEFScorePresenter;
import cc.hisens.hardboiled.patient.utils.DateUtils;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.model
 * @fileName IIEF5Score
 * @date on 2017/6/6 11:48
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

public class IIEF5Score extends RealmObject implements Parcelable {

    @PrimaryKey
    private long msTimestamp;

    @SerializedName("time")
    public double timestamp;

    @SerializedName("scores")
    public byte[] scores;

    public IIEF5Score() {
    }

    protected IIEF5Score(Parcel in) {
        this.timestamp = in.readDouble();
        this.scores = in.createByteArray();
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getScores() {
        return scores;
    }

    public void setScores(byte[] scores) {
        this.scores = scores;
    }

    public long getMsTimestamp() {
        return msTimestamp;
    }

    public void setMsTimestamp() {
        this.msTimestamp = (long) (1000 * timestamp);
    }


    public int getTotalScore() {
        int totalScore = 0;
        byte[] scores = getScores();
        if (scores == null) {
            totalScore = -1;
        } else {
            for (int i = 0; i < scores.length; i++) {
                totalScore += scores[i];
            }
        }
        return totalScore;
    }

    public String getTimestampText() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(DateUtils.double2Date(timestamp));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.timestamp);
        dest.writeByteArray(this.scores);
    }

    public static final Creator<IIEF5Score> CREATOR = new Creator<IIEF5Score>() {
        @Override
        public IIEF5Score createFromParcel(Parcel source) {
            return new IIEF5Score(source);
        }

        @Override
        public IIEF5Score[] newArray(int size) {
            return new IIEF5Score[size];
        }
    };


    //上传分数
    public void UploadIIEFScore(Context context, int[] score, IIEFScorePresenter presenter) {
        HashMap<String, int[]> IIEFMap = new HashMap<>();
        IIEFMap.put("scores", score);
        RequestUtils.post(context, Url.uploadEhsScore, IIEFMap, new HashMap<>(), new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result != null) {

                    if (result.result == 0) {
                        Gson gson = new Gson();
                        IIEF5Score iief5Score = new Gson().fromJson(gson.toJson(result.getDatas()), IIEF5Score.class);
                        presenter.UploadSuccess(iief5Score);
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
