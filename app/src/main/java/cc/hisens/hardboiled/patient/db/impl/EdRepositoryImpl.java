package cc.hisens.hardboiled.patient.db.impl;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import cc.hisens.hardboiled.patient.bean.Ed;
import cc.hisens.hardboiled.patient.ble.algorithm.EdAnalyze;
import cc.hisens.hardboiled.patient.db.EdRepository;
import cc.hisens.hardboiled.patient.db.RealmHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * @author Waiban
 * @package cc.hisens.hardboiled.data.database.repository.impl
 * @fileName EdRepositoryImpl
 * @date on 2017/6/7 15:52
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *
 *
 *
 *
 * 蓝牙监测数据的存储数据库操作
 */

public class EdRepositoryImpl implements EdRepository {
    @Override
    public Observable<Ed> saveEd(@NonNull Ed ed) {
        Realm realm = RealmHelper.getRealm();
        // DO NOT forget begin and commit the transaction.
        realm.beginTransaction();
        final Ed edNew = realm.copyFromRealm(realm.copyToRealmOrUpdate(ed));
        realm.commitTransaction();
        realm.close();
        return Observable.just(edNew);
    }

    @Override
    public Observable<List<Ed>> saveEdList(List<Ed> edList) {
        Realm realm = RealmHelper.getRealm();
        realm.beginTransaction();
        List<Ed> list =realm.copyFromRealm(realm.copyToRealmOrUpdate(edList));
        realm.commitTransaction();
        realm.close();
        return Observable.just(list);
    }

    @Override
    public Observable<List<Ed>> getNptEds(long startTime, long endTime) {
        Realm realm = RealmHelper.getRealm();
        List<Ed> edList = realm.copyFromRealm(
                realm.where(Ed.class)
                .between(Ed.START_SLEEP, startTime, endTime)
                        .findAll().sort(Ed.START_SLEEP, Sort.ASCENDING));
        realm.close();
        List<Ed> list = new ArrayList<>();
        for (Ed ed : edList) {
            if (EdAnalyze.isNormalNPT(ed.getStartTimestamp(), ed.getEndTimestamp()) && TextUtils.isEmpty(ed.getInterferenceFactor())) {
                list.add(ed);
            }
        }
        realm.close();
        return Observable.just(list);
    }

    @Override
    public Observable<Ed> getNptEd(long startTime, long endTime) {
        Realm realm = RealmHelper.getRealm();
        List<Ed> edList = realm.copyFromRealm(realm.where(Ed.class)
                .between(Ed.START_SLEEP, startTime, endTime)
                .findAll().sort(Ed.START_SLEEP, Sort.DESCENDING));
        realm.close();
        Ed resEd = null;
        if (edList.size() > 0) {
            for (Ed ed : edList) {
                if (EdAnalyze.isNormalNPT(ed.getStartTimestamp(), ed.getEndTimestamp()) && TextUtils.isEmpty(ed.getInterferenceFactor())) {
                    resEd = ed;
                    break;
                }
            }
        }

        return Observable.just(resEd == null ? new Ed() : resEd);
    }

    @Override
    public Observable<Ed> getLatestEd() {
        Realm realm = RealmHelper.getRealm();
        List<Ed> results = realm.copyFromRealm(realm.where(Ed.class).findAll().sort(Ed.START_SLEEP, Sort.DESCENDING));
        realm.close();
        return Observable.just(results.size() > 0 ? results.get(0) : new Ed());
    }

    @Override
    public Observable<Ed> getEd(long startSleep) {
        Realm realm = RealmHelper.getRealm();
        List<Ed> results = realm.copyFromRealm(realm.where(Ed.class)
                .equalTo(Ed.START_SLEEP, startSleep).findAll().sort(Ed.START_SLEEP, Sort.DESCENDING));
        realm.close();
        if (results.size() == 0) {
            return Observable.create(new ObservableOnSubscribe<Ed>() {
                @Override
                public void subscribe(ObservableEmitter<Ed> e) {
                    e.onComplete();
                }
            });
        } else {
            return Observable.just(results.get(0));
        }
    }

    @Override
    public Observable<Ed> getSpecificEd(long startSleep) {
        Realm realm = RealmHelper.getRealm();
        List<Ed> results = realm.copyFromRealm(realm.where(Ed.class)
                .equalTo(Ed.START_SLEEP, startSleep)
                .findAll());
        realm.close();
        if (results.size() == 0) {
            return Observable.create(new ObservableOnSubscribe<Ed>() {
                @Override
                public void subscribe(ObservableEmitter<Ed> e) {
                    e.onComplete();
                }
            });
        } else {
            return Observable.just(results.get(0));
        }
    }

    @Override
    public Observable<List<Ed>> getSpecificEds() {
        Realm realm = RealmHelper.getRealm();
        List<Ed> edList = realm.copyFromRealm(realm.where(Ed.class)
                .findAll().sort(Ed.START_SLEEP, Sort.DESCENDING));
        List<Ed> list = new ArrayList<>();
        realm.close();
        for (Ed ed : edList) {
            if (!EdAnalyze.isNormalNPT(ed.getStartTimestamp(), ed.getEndTimestamp()) || !TextUtils.isEmpty(ed.getInterferenceFactor())) {
                list.add(ed);
            }
        }
        return Observable.just(list);
    }

    @Override
    public List<Ed> getNotSyncEd() {

        Realm realm = RealmHelper.getRealm();
        realm.beginTransaction();
        List<Ed> list = realm.copyFromRealm(
                realm.where(Ed.class).equalTo("isSync",false).findAll());
        realm.commitTransaction();
        realm.close();

        return list;
    }

    @Override
    public void updateEdSyncState() {

        Realm realm = RealmHelper.getRealm();
        realm.beginTransaction();
        RealmResults<Ed> realmList = realm.where(Ed.class)
                .equalTo("isSync",false).findAll();

        for (Ed ed:realmList) {
            ed.setSync(true);
        }

        realm.commitTransaction();
        realm.close();

    }

    @Override
    public Observable<Boolean> deleteEd(long startTime) {
        Realm realm = RealmHelper.getRealm();
        realm.beginTransaction();
        boolean res = realm.where(Ed.class).equalTo(Ed.START_SLEEP, startTime).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
        return Observable.just(res);
    }
}
