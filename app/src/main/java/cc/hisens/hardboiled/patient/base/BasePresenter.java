package cc.hisens.hardboiled.patient.base;


public abstract class BasePresenter<T> {

    public T mView;

    public void attach(T mView) {
        this.mView = mView;
    }

    public void detachView() {
        if (mView != null) {
            mView = null;
        }
    }
}
