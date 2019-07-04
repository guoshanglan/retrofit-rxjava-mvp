package cc.hisens.hardboiled.patient.db.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import cc.hisens.hardboiled.patient.db.IIEF5ScoreRepository;
import cc.hisens.hardboiled.patient.db.RealmHelper;
import cc.hisens.hardboiled.patient.ui.activity.score.model.IIEF5Score;
import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.data.database.repository.impl
 * @fileName IIEF5ScoreRepositoryImpl
 * @date on 2017/6/6 15:22
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

public class IIEF5ScoreRepositoryImpl implements IIEF5ScoreRepository {

    //存储IIEF_5分数
    @Override
    public IIEF5Score saveScore(IIEF5Score score) {
        Realm realm = RealmHelper.getRealm();
        realm.beginTransaction();
        score.setMsTimestamp();
        realm.insertOrUpdate(score);
        realm.commitTransaction();
        realm.close();
        return score;
    }


    //存储多条IIEF_5分数
    @Override
    public void saveScoreList(final List<IIEF5Score> scoreList) {
        for (IIEF5Score score : scoreList) {
            score.setMsTimestamp();
        }
        Realm realm = RealmHelper.getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(scoreList);
            }
        });
        realm.close();
    }

    @Override
    public void saveScores(IIEF5Score[] scores) {
        saveScoreList(new ArrayList(Arrays.asList(scores)));
    }


    //获取最后一条IIEF_5的分数
    @Override
    public Observable<IIEF5Score> getLatestScore() {
        Realm realm = RealmHelper.getRealm();
        IIEF5Score score = realm.where(IIEF5Score.class).findFirst();
        if (score != null) {
            score = realm.copyFromRealm(score);
        } else {
            score = new IIEF5Score();
            score.setTimestamp(System.currentTimeMillis());
        }
        realm.close();
        return Observable.just(score);
    }

    //获取所有的IIEF_5的分数
    @Override
    public Observable<List<IIEF5Score>> getAllScores() {
        Realm realm = RealmHelper.getRealm();
        RealmResults<IIEF5Score> results = realm.where(IIEF5Score.class).findAll();
        List<IIEF5Score> list = realm.copyFromRealm(results);
        realm.close();
        return Observable.just(list);
    }

    //获取所有的IIEF_5的分数数量
    @Override
    public Observable<Long> getIIEF5ScoreCount() {
        Realm realm = RealmHelper.getRealm();
        final Long[]counts = new Long[1];
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                counts[0] = realm.where(IIEF5Score.class).count();
            }
        });
        return Observable.just(counts[0]);
    }
}
