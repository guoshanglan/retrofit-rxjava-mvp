package cc.hisens.hardboiled.patient.db;

import java.util.List;

import cc.hisens.hardboiled.patient.ui.activity.score.model.EHSScore;
import io.reactivex.Observable;

public interface EHSScoreRepository {

    void saveEHSScore(EHSScore score);
    void saveEHSScores(EHSScore[] scores);
    Observable<List<EHSScore>> getEHSScoreList();
    Observable<EHSScore> getLatestEHSScore();

    Observable<Long> getEHSScoreCount();

}
