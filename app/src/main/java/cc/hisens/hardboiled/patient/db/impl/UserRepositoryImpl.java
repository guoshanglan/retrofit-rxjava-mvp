package cc.hisens.hardboiled.patient.db.impl;

import java.util.List;


import cc.hisens.hardboiled.patient.db.RealmHelper;
import cc.hisens.hardboiled.patient.db.UserRepository;
import cc.hisens.hardboiled.patient.mvp.model.User;
import io.reactivex.Observable;
import io.realm.Realm;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.data.database.repository.impl
 * @fileName UserRepositoryImpl
 * @date on 2017/6/14 11:33
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

public class UserRepositoryImpl implements UserRepository {
    @Override
    public void saveUser(final User user) {
        Realm realm = RealmHelper.getRealm();
        // DO NOT forget begin and commit the transaction.
//        realm.beginTransaction();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(user);
            }
        });
//        realm.copyToRealmOrUpdate(user);
//        realm.commitTransaction();
        realm.close();
    }

    @Override
    public Observable<User> getUser() {
        Realm realm = RealmHelper.getRealm();
         List<User> results = realm.copyFromRealm(realm.where(User.class).findAll());
        return Observable.just(results.size() > 0 ? results.get(0) : new User());
    }
}
