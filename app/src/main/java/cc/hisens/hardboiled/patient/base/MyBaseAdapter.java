package cc.hisens.hardboiled.patient.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;


/**
 * 自定义BaseAdapter的基类
 *
 * @author Ou Weibin
 * @version 1.0
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

    protected Context mContext;
    private List<T> mList;
    private LayoutInflater mLayoutInflater;



    public MyBaseAdapter(Context context, List<T> list) {
        mContext = context;
        mList = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    public Context getContext() {
        return mContext;
    }

    public List<T> getList() {
        return mList;
    }

    public void setList(List mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
