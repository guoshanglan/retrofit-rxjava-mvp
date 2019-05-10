package cc.hisens.hardboiled.patient.db;


import cc.hisens.hardboiled.patient.mvp.model.User;
import io.reactivex.Observable;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.data.database.repository
 * @fileName UserRepository
 * @date on 2017/6/14 11:32
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

public interface UserRepository {
    void saveUser(User user);

    Observable<User> getUser();
}
