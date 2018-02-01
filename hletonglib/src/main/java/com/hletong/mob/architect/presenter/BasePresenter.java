package com.hletong.mob.architect.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.model.DataSource;
import com.hletong.mob.architect.model.repository.DataCallback;
import com.hletong.mob.architect.model.repository.DataRepository;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.http.AnimCallBack;
import com.hletong.mob.util.FinishOptions;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;
import com.xcheng.okhttp.util.Platform;

/**
 * Created by ddq on 2016/12/9.
 * presenter基类
 */
public abstract class BasePresenter<V extends IBaseView> implements IBasePresenter {
    private V mView;
    private DataSource mDataRepository;

    public BasePresenter(@NonNull V baseView) {
        this(DataRepository.getInstance(), baseView);
    }

    public BasePresenter(DataSource dataRepository, @NonNull V baseView) {
        this.mDataRepository = dataRepository;
        this.mView = baseView;
    }

    public DataSource getDataRepository() {
        return mDataRepository;
    }

    public void start() {
    }

    public void stop() {

    }

    /**
     * 获取mView的tag
     *
     * @return tag
     */
    protected int tag() {
        return mView.hashCode();
    }

    public V getView() {
        return mView;
    }

    /**
     * 处理参数的错误信息
     *
     * @param message 错误消息
     * @return 固定返回false
     */
    public final boolean handleMessage(final String message) {
        if (Platform.isOnMainThread()) {
            mView.handleError(ErrorFactory.getParamError(message));
        } else {
            Platform.get().execute(new Runnable() {
                @Override
                public void run() {
                    handleMessage(message);
                }
            });
        }
        return false;
    }

    public boolean isEmpty(CharSequence charSequence) {
        return TextUtils.isEmpty(charSequence);
    }

    protected final void simpleSubmit(String url, ParamsHelper params) {
        OkRequest request = EasyOkHttp.get(url).params(params.getMaps()).tag(tag()).build();
        new ExecutorCall<CommonResult>(request).enqueue(new AnimCallBack<CommonResult>(getView()) {
            @Override
            public void onError(OkCall<CommonResult> okCall, EasyError error) {
                simpleSubmitFailed((BaseError) error);
            }

            @Override
            public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                simpleSubmitSucceed(response);
            }
        });
    }

    protected final void simpleSubmit(ItemRequestValue<CommonResult> requestValue) {
        getDataRepository().loadItem(requestValue, new DataCallback<CommonResult>(getView()) {
            @Override
            public void onSuccess(@NonNull CommonResult response) {
                simpleSubmitSucceed(response);
            }

            @Override
            public void onError(@NonNull BaseError error) {
                simpleSubmitFailed(error);
            }
        });
    }

    protected void simpleSubmitFailed(BaseError error) {
        easySubmitFailed(error);
    }

    protected void simpleSubmitSucceed(CommonResult cr) {
        easySubmitSucceed(cr);
    }

    protected final void easySubmit(OkRequest okRequest) {
        easySubmit(okRequest, false);
    }

    protected final void easySubmit(OkRequest okRequest, final boolean withOutProgress) {
        new ExecutorCall<CommonResult>(okRequest).enqueue(new AnimCallBack<CommonResult>(withOutProgress ? null : getView()) {
            @Override
            public void onError(OkCall<CommonResult> okCall, EasyError error) {
                easySubmitFailed((BaseError) error);
            }

            @Override
            public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                easySubmitSucceed(response);
            }
        });

    }

    protected void easySubmitFailed(BaseError error) {
        getView().handleError(error);
    }

    protected void easySubmitSucceed(CommonResult cr) {
        if (mView instanceof ICommitSuccessView) {
            ICommitSuccessView s = (ICommitSuccessView) mView;
            s.success(cr.getErrorInfo());
        }
    }

    /**
     * app内部大多数表单提交请求都可以使用这个callback
     * 很多表单提交之后都是两部：
     * 1.展示提示(成功)消息
     * 2.返回上一级页面，并返回操作成功
     */
    public static class SubmitForwardResultCallback extends AnimCallBack<CommonResult> {
        private IBaseView mBaseView;
        private ICommitSuccessView mSuccessView;
        private ITransactionView mTransactionView;
        private Intent mIntent;//提交成功之后要传递的结果

        public SubmitForwardResultCallback(IBaseView baseView, ICommitSuccessView successView, ITransactionView transactionView) {
            super(baseView);
            mBaseView = baseView;
            mSuccessView = successView;
            mTransactionView = transactionView;
        }

        public SubmitForwardResultCallback(IBaseView baseView, ICommitSuccessView successView, ITransactionView transactionView, Intent intent) {
            super(baseView);
            mBaseView = baseView;
            mSuccessView = successView;
            mTransactionView = transactionView;
            mIntent = intent;
        }

        @Override
        public void onError(OkCall<CommonResult> okCall, EasyError error) {
            mBaseView.handleError((BaseError) error);
        }

        @Override
        public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
            //弹出提示消息
            mSuccessView.success(response.getErrorInfo());
            //回到上一级页面，并返回提交成功结果
            mTransactionView.finishWithOptions(FinishOptions.FORWARD_RESULT(mIntent));
        }
    }
}
