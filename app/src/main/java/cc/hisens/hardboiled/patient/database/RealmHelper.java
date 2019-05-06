package cc.hisens.hardboiled.patient.database;


import android.text.TextUtils;

import cc.hisens.hardboiled.patient.bean.UserConfig;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author Ou Weibin
 * @version 1.0
 */
public class RealmHelper {
    private static final int CURRENT_VERSION = 0;

    public static Realm getRealm() {
        String name;
        if (!TextUtils.isEmpty(UserConfig.UserInfo.getUid())) {
            name = UserConfig.UserInfo.getUid() + ".realm";
        } else {
            name = "myrealm.realm";
        }
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                // 自定义数据库名
                .name(name)
                .schemaVersion(CURRENT_VERSION)
                /*.migration(new MyRealmMigration())*/
                .build();
        return Realm.getInstance(config);
    }

}
