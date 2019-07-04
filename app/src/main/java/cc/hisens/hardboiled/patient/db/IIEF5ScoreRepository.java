package cc.hisens.hardboiled.patient.db;

import java.util.List;


import cc.hisens.hardboiled.patient.ui.activity.score.model.IIEF5Score;
import io.reactivex.Observable;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.data.database.repository
 * @fileName IIEF5ScoreRepository
 * @date on 2017/6/6 15:20
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

public interface IIEF5ScoreRepository {
    IIEF5Score saveScore(IIEF5Score score);

    void saveScoreList(List<IIEF5Score> scoreList);

    void saveScores(IIEF5Score[] scores);

    Observable<IIEF5Score> getLatestScore();

    Observable<List<IIEF5Score>> getAllScores();

    Observable<Long> getIIEF5ScoreCount();

}
