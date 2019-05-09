package cc.hisens.hardboiled.patient.db;

import java.util.List;

import androidx.annotation.NonNull;

import cc.hisens.hardboiled.patient.bean.Ed;
import io.reactivex.Observable;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.data.database.repository
 * @fileName EdRepository
 * @date on 2017/6/7 14:44
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

public interface EdRepository {

    Observable<Ed> saveEd(@NonNull Ed ed);

    Observable<List<Ed>> saveEdList(List<Ed> edList);

    Observable<List<Ed>> getNptEds(long startTime, long endTime);

    Observable<Ed> getNptEd(long startTime, long endTime);

    Observable<Ed> getLatestEd();

    Observable<Ed> getEd(long startSleep);

    Observable<Ed> getSpecificEd(long startSleep);

    Observable<List<Ed>> getSpecificEds();

    List<Ed> getNotSyncEd();

    void updateEdSyncState();

    Observable<Boolean> deleteEd(long startTime);
}
