package cc.hisens.hardboiled.patient.db.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import cc.hisens.hardboiled.patient.db.EHSScoreRepository;
import cc.hisens.hardboiled.patient.db.RealmHelper;
import cc.hisens.hardboiled.patient.ui.activity.score.model.EHSScore;
import io.reactivex.Observable;
import io.realm.Realm;

public class EHSScoreRepositoryImpl implements EHSScoreRepository {


    //存储一条EHSScore
    @Override
    public void saveEHSScore(final EHSScore score) {
        Realm realm = RealmHelper.getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(score);
            }
        });
        realm.close();
    }

    //存储多条EHSScore评分记录
    @Override
    public void saveEHSScores(final EHSScore[] scores) {
        Realm realm = RealmHelper.getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(new ArrayList(Arrays.asList(scores)));
            }
        });
        realm.close();
    }

    //获取多条EHSScore评分记录
    @Override
    public Observable<List<EHSScore>> getEHSScoreList() {
        Realm realm = RealmHelper.getRealm();
        List<EHSScore> scoresList = realm.copyFromRealm(realm.where(EHSScore.class).findAll());
        return Observable.just(scoresList);
    }

    //获取最后一条EHSScore评分记录
    @Override
    public Observable<EHSScore> getLatestEHSScore() {
        Realm realm = RealmHelper.getRealm();
        EHSScore score = realm.where(EHSScore.class).findFirst();
        if (score != null) {
            score = realm.copyFromRealm(score);
        } else {
            score = new EHSScore();
        }
        realm.close();
        return Observable.just(score);
    }


    //获取EHSS评分记录的数量
    @Override
    public Observable<Long> getEHSScoreCount() {
        Realm realm = RealmHelper.getRealm();
        final Long[] counts = new Long[1];
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                counts[0] = realm.where(EHSScore.class).count();
            }
        });
        return Observable.just(counts[0]);
    }
}
