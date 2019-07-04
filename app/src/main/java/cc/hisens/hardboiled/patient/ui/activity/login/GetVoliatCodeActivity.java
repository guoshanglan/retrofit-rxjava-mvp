package cc.hisens.hardboiled.patient.ui.activity.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContentResolverCompat;
import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.ui.activity.UserAgreementActivity;
import cc.hisens.hardboiled.patient.ui.activity.login.present.GetVoliatCodePresenter;
import cc.hisens.hardboiled.patient.ui.activity.login.view.VoliateCodeView;
import cc.hisens.hardboiled.patient.utils.TimeUtils;
import cc.hisens.hardboiled.patient.utils.ToastUtils;
import cc.hisens.hardboiled.patient.wideview.ClearEditText;


//获取手机验证码
public class GetVoliatCodeActivity extends BaseActivity implements VoliateCodeView, TextWatcher {
    public GetVoliatCodePresenter getVoliatCodePresenter; //获取验证码的present
    @BindView(R.id.et_input_phone_num)
    public ClearEditText etPhoneNum;   //输入电话号码
    @BindView(R.id.btn_getverification_code)
    public Button btVerificatCode; //获取验证码
    @BindView(R.id.line_separator)
    public View lineView;
    @BindView(R.id.iv_argeement)
    public ImageView ivArgeement;

    private boolean isAdd=false,isDelet=false;  //判断手机号输入框内容是增加还是删除
    private boolean isArgeement=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

    }



    //点击事件
    @OnClick({R.id.btn_getverification_code,R.id.iv_argeement,R.id.tv_terms_and_agreement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_getverification_code:
                //Log.e("测试时间-----",TimeUtils.isMore6h("21:00-09:00",1562148028,2062180428)+"")   ;
                String number = etPhoneNum.getText().toString().trim().replace(" ", "");
                 if (isArgeement==false){
                     ShowToast("请先同意用户协议");
                     return;
                 }
                if (number.length()== 11) {
                    initProgressDialog("");
                    getVoliatCodePresenter.getVerificationCode();  //获取验证码
                } else if (TextUtils.isEmpty(number)) {
                    ShowToast(getString(R.string.login_number_empty));
                }else{
                    ShowToast(getString( R.string.login_error_hint));
                }

                break;
            case R.id.tv_terms_and_agreement:
                 startActivity(new Intent(this, UserAgreementActivity.class));
                break;

            case R.id.iv_argeement:  //是否同意用户协议
                isArgeement=!isArgeement;
                if (isArgeement){

                    if (etPhoneNum.getText().toString().replace(" ","").length()==11) {
                        btVerificatCode.setBackgroundResource(R.drawable.btn_getverification_code_input_shape);
                    }
                    ivArgeement.setBackgroundResource(R.drawable.login_btn_agreement);
                }else{

                    btVerificatCode.setBackgroundResource(R.drawable.btn_getverification_code_uninput_shape);
                    ivArgeement.setBackgroundResource(R.drawable.login_btn_agreemen_no);
                }

                break;
        }
    }


    //初始化所有控件的属性
    private void initView() {
        etPhoneNum.addTextChangedListener(this);

    }

    //获取布局id
    @Override
    protected int getLayoutId() {
        return R.layout.getvoliate_layout;
    }


    //返回GetVoliatCodePresenter对象
    @Override
    public BasePresenter getPresenter() {
        if (getVoliatCodePresenter == null) {
            getVoliatCodePresenter = new GetVoliatCodePresenter();
        }
        return getVoliatCodePresenter;
    }


    //得到用户输入的手机号
    @Override
    public String getNumber() {

        return etPhoneNum.getText().toString().replace(" ", "");
    }


    //上下文对象
    @Override
    public Context getContext() {
        return this;
    }



    //获取验证码成功，跳转到登录页面
    @Override
    public void GetSuccessful(String info) {
        dismissProgressDialog();
        if (info.equals("发送成功")) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("number", etPhoneNum.getText().toString().trim());
            startActivity(intent);
        }

    }

    //获取失败
    @Override
    public void GetFailedError(String str) {
        dismissProgressDialog();
        ShowToast(str);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (after == 1) {//after等于1的情况下，说明输入框里面开始增加了字符
            isAdd = true;
        } else {
            isAdd = false;
        }
        if (after==0){  //等于0说明是删除
            isDelet=true;
        }else{
            isDelet=false;
        }

    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String phoneNum = etPhoneNum.getText().toString().replace(" ", "");

        if(etPhoneNum.hasFoucs){
           etPhoneNum.setClearIconVisible(s.length() > 0);
           lineView.setBackgroundResource(R.color.hisens_blue);
        }else{
            if (s.length()==0){
                lineView.setBackgroundResource(R.color.separator_color);
            }
        }



        if (phoneNum.length() == 11) {
            btVerificatCode.setBackgroundResource(R.drawable.btn_getverification_code_input_shape);
            btVerificatCode.setClickable(true);
        } else {
            btVerificatCode.setBackgroundResource(R.drawable.btn_getverification_code_uninput_shape);
            btVerificatCode.setClickable(true);
        }
//        if (TextUtils.isEmpty(phoneNum)){
//            lineView.setBackgroundResource(R.color.separator_color);
//        }



    }

    @Override
    public void afterTextChanged(Editable s) {
        if (isAdd) {  //输入框增加文字
            if (null != etPhoneNum) {
                String str = s.toString();
                if (!str.endsWith(" ")) {
                    int length = s.length();
                    if (length == 3 || length == 8) {
                        String str1 = str + " ";//手动添加空格
                        etPhoneNum.setText(str1);
                        etPhoneNum.setSelection(str1.length());//光标移到最右边
                    }
                }
            }
        }
        if(isDelet){  //输入框删除文字,因为这边删除的时候需要空格一起删除
            if (null != etPhoneNum) {
                String str = s.toString();
                if (str.endsWith(" ")) {
                        String str1 = str.trim();//手动删除空格
                        etPhoneNum.setText(str1);
                        etPhoneNum.setSelection(str1.length());//光标移到最右边
                    }
                }
            }


    }



}
