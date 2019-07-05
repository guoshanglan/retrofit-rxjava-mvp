package cc.hisens.hardboiled.patient.ui.activity.aboutus;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;


//关于我们
public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.tv_back)
    TextView tvBack;  //返回键
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.webview_userAgreement)
    WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWebView();
    }

    private void initWebView() {

        //不使用Android默认浏览器打开Web，就在App内部打开Web

        webview.setWebViewClient(new WebViewClient() {

            @Override

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);

                return true;

            }

        });

        //支持App内部javascript交互

        webview.getSettings().setJavaScriptEnabled(true);
        //自适应屏幕
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setLoadWithOverviewMode(true);
        //设置可以支持缩放
        webview.getSettings().setSupportZoom(true);
        //扩大比例的缩放
        webview.getSettings().setUseWideViewPort(true);
        //设置是否出现缩放工具
        webview.getSettings().setBuiltInZoomControls(true);

        webview.loadUrl("file:///android_asset/aboutUs.html");

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webview != null) {
            webview.destroy();
        }

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
