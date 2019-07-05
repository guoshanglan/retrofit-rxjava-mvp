package cc.hisens.hardboiled.patient.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cc.hisens.hardboiled.patient.R;


/**
 * Created by Administrator on 2018/11/8.
 */

public class SyncDatadialog {
    public Dialog mDialog;
    public View view;
    public TextView tvCarema,tvXiangce,tvCancel;
    public Context mContext;
    public PhotoCallback photoCallback;


      public SyncDatadialog(Context context){
          this.mContext=context;

      }


      public void inintDialog(){
          view=View.inflate(mContext, R.layout.syncdata_dialog_view,null);

          mDialog=new Dialog(mContext,R.style.MyDialog);
          mDialog.setContentView(view);
          mDialog.setCanceledOnTouchOutside(true);
          mDialog.getWindow().setGravity(Gravity.TOP); //设置在底部
          mDialog.getWindow().setWindowAnimations(R.style.syncData); // 添加动画
          mDialog.show();

      }




     public void initCallback(PhotoCallback callback){
          this.photoCallback=callback;
     }

    public interface PhotoCallback{
          void onCarema(int type);

    }
}
