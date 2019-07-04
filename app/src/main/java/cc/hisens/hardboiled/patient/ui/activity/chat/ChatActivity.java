package cc.hisens.hardboiled.patient.ui.activity.chat;

import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;

//聊天Activity
public class ChatActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.chat_activity_layout;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
