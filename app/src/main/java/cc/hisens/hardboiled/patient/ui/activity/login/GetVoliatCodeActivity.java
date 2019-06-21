package cc.hisens.hardboiled.patient.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.ui.activity.login.present.GetVoliatCodePresenter;
import cc.hisens.hardboiled.patient.ui.activity.login.view.VoliateCodeView;
import cc.hisens.hardboiled.patient.utils.ToastUtils;


//获取手机验证码
public class GetVoliatCodeActivity extends BaseActivity implements VoliateCodeView, TextWatcher {
    public GetVoliatCodePresenter getVoliatCodePresenter; //获取验证码的present
    @BindView(R.id.et_input_phone_num)
    public EditText etPhoneNum;   //输入电话号码
    @BindView(R.id.btn_getverification_code)
    public Button btVerificatCode; //获取验证码
    private boolean isAdd=false,isDelet=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    //点击事件
    @OnClick(R.id.btn_getverification_code)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_getverification_code:
                String number = etPhoneNum.getText().toString().trim().replace(" ", "");
                if (number.length()== 11) {
                    initProgressDialog("");
                    getVoliatCodePresenter.getVerificationCode();  //获取验证码
                } else {
                    ToastUtils.show(this, R.string.login_error_hint);
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

    //获取验证码成功
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
        ToastUtils.show(this, str);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (after == 1) {//after等于1的情况下，说明输入框里面开始增加了字符
            isAdd = true;
        } else {
            isAdd = false;
        }
        if (after==0){
            isDelet=true;
        }else{
            isDelet=false;
        }

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String phoneNum = etPhoneNum.getText().toString().replace(" ", "");

        if (phoneNum.length() == 11) {
            btVerificatCode.setBackgroundResource(R.drawable.btn_getverification_code_input_shape);
            btVerificatCode.setClickable(true);
        } else {
            btVerificatCode.setBackgroundResource(R.drawable.btn_getverification_code_uninput_shape);
            btVerificatCode.setClickable(true);
        }


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
                        String str1 = str.trim();//手动添加空格
                        etPhoneNum.setText(str1);
                        etPhoneNum.setSelection(str1.length());//光标移到最右边
                    }
                }
            }


    }



}
