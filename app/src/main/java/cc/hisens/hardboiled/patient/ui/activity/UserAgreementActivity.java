package cc.hisens.hardboiled.patient.ui.activity;


import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import com.github.barteksc.pdfviewer.PDFView;
import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;


/**
 *
 * 用户协议Activity
 */

public class UserAgreementActivity extends BaseActivity {

    @BindView(R.id.tv_back)
    TextView tvBack;  //返回键
    @BindView(R.id.pdf_view)
    PDFView pdfView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTermsFromPdf();

    }


    //点击事件
    @OnClick({R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:    //返回
                 finish();
                break;

        }

    }


    //加载pdf文件,文件放在asset文件夹下
    private void loadTermsFromPdf() {
        pdfView.fromAsset("userprotocol.pdf")
                .defaultPage(0)
                .enableSwipe(true)
                .load();

    }



    @Override
    protected int getLayoutId() {
        return R.layout.useragreement_activity;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


}
