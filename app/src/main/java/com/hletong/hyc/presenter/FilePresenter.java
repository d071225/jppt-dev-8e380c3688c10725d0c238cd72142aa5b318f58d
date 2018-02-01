package com.hletong.hyc.presenter;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.SimpleArrayMap;

import com.hletong.hyc.contract.FileContract;
import com.hletong.hyc.model.FileInfo;
import com.hletong.hyc.model.FileResult;
import com.hletong.hyc.model.Images;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Executors;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.error.ErrorState;
import com.hletong.mob.architect.model.DataSource;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.architect.view.IProgress;
import com.hletong.mob.http.AnimCallBack;
import com.hletong.mob.security.EncryptUtils;
import com.hletong.mob.util.BitmapUtils;
import com.orhanobut.logger.Logger;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.UICallback;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;
import com.xcheng.okhttp.util.ParamUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import okhttp3.MediaType;

/**
 * Created by ddq on 2017/1/4.
 */

public class FilePresenter<T extends FileContract.View> extends BasePresenter<T> implements FileContract.Presenter {
    private static final HashMap<String, String> CompressedImageCache = new HashMap<>();
    /**
     * 成功上传的历史
     * 由于图片是单独上传的，而提交的过程可能分几步走，
     * 比如先提交图片然后提交其他表单数据，但是在其他提交过程中
     * 可能会失败，但是图片上传已经成功，用户修改其他数据之后（图片内容和顺序均未发生变化）
     * 再次进行提交，可以不必再次上传已经成功上传的图片
     */
    private SimpleArrayMap<String, List<FileBundle>> mSuccessfulHistory = new ArrayMap<>();
    //当次上传成功的图片的服务端返回信息
    private List<FileBundle> successfulUploadedImage;
    //等待上传的图片
    private Stack<String> imagesWaitForUpload = new Stack<>();
    private String mUrl;//图片上传的接口
    private String mode;//图片上传的模式：single ：multi
    private int totalImage;
    private IProgress mProgress;

    public FilePresenter(T view) {
        super(view);
        mProgress = view;
    }

    public FilePresenter(DataSource dataSource, T view) {
        super(dataSource, view);
        mProgress = view;
    }

    @Override
    public void upload(List<String> images, String url, boolean isPublic) {
        //检查一下参数，如果子类有需求一定要有文件被上传，可以覆盖这个函数
        if (!isParamsValid(images))
            return;

        if (ParamUtil.isEmpty(images)) {
            dispatch(0, false, null, null);
            return;
        }

        //判断这一组图片是不是上传成功过，如果成功过，直接返回
        List<FileBundle> history = mSuccessfulHistory.get(getKeyForImages(images, url));
        if (history != null) {
            dispatch(0, false, history.get(0).fileInfo, null);
            return;
        }

        imagesWaitForUpload.clear();//清空待上传列表
        totalImage = images.size();


        getView().uploadStart(totalImage);
        //开始动画
        mProgress.showLoading();

        //将新的待上传数据入栈
        for (int i = images.size() - 1; i >= 0; i--) {
            if (images.get(i) == null)
                continue;
            imagesWaitForUpload.push(images.get(i));//反序入栈，上传的时候就会按照原images里面的顺序上传
        }

        //重置已上传成功的列表
        if (successfulUploadedImage == null) {
            successfulUploadedImage = new ArrayList<>(images.size());
        }
        successfulUploadedImage.clear();

        mUrl = url;

        if (imagesWaitForUpload.size() > 1) {
            mode = "multi";
        } else {
            mode = "single";
        }

        //取栈顶数据上传，压缩图片然后上传
        Executors.getExecutors().execute(new ImagesCompress(new FileBundle(imagesWaitForUpload.pop()), isPublic));
    }

    protected boolean isParamsValid(List<String> images) {
        return true;
    }

    @Override
    public void download(String id) {
        OkRequest request = EasyOkHttp.get(String.format(Constant.FETCH_GROUP_PICTURES_URL, id)).build();
        new ExecutorCall<Images>(request).enqueue(new UICallback<Images>() {
            @Override
            public void onError(OkCall<Images> okCall, EasyError error) {

            }

            @Override
            public void onSuccess(OkCall<Images> okCall, Images response) {
                if (response.empty())
                    return;

                List<String> paths = new ArrayList<>(response.getList().size());
                for (int index = 0; index < response.getList().size(); index++) {
                    paths.add(response.get(index).getFileDownloadUrl());
                }
                getView().showFiles(paths);
            }
        });
    }

    /**
     * 上传图片
     * 如果是多张图片，上传第一张的时候，服务端返回的数据里面包含groupid
     * 上传第二张及其后续图片的时候要传递之前返回的groupid（上传第一张返回的）
     *
     * @param fileBundle
     */
    private synchronized void upload(final FileBundle fileBundle, final boolean isPublic) {
        if (fileBundle.compressed == null) {
            handleMessage("图片压缩失败");
            return;
        }

        OkRequest.Builder builder = EasyOkHttp.form(mUrl)
                .addFileInput("file", MediaType.parse("image/*"), new File(fileBundle.compressed))//上传的是压缩后的图片
                .param("file_upload_mode", mode)
                .param("file_pre", isPublic ? "$public$/android/" : "android/")
                .inProgress();

        String groupId = getGroupId();
        if (groupId != null) {
            builder.param("file_group_id", groupId);
        }

        new ExecutorCall<FileResult>(builder.build()).enqueue(new AnimCallBack<FileResult>(null) {
            @Override
            public void onError(OkCall<FileResult> okCall, EasyError error) {
                dispatch(imagesWaitForUpload.size(), true, null, (BaseError) error);
            }

            @Override
            public void onSuccess(OkCall<FileResult> okCall, FileResult response) {
                if (response.isSuccess()) {
                    //记录成功上传的图片
                    Logger.d("upload succeed => " + fileBundle.toString());
                    fileBundle.fileInfo = response.getFile_info();
                    successfulUploadedImage.add(fileBundle);
                    dispatch(imagesWaitForUpload.size(), false, response.getFile_info(), null);
                    if (imagesWaitForUpload.size() > 0) {
                        Executors.getExecutors().execute(new ImagesCompress(new FileBundle(imagesWaitForUpload.pop()), isPublic));
                    } else {//全部文件上传完毕，把他们放到成功历史里面
                        mSuccessfulHistory.put(getKeyForImages(successfulUploadedImage), successfulUploadedImage);
                    }
                } else {
                    dispatch(imagesWaitForUpload.size(), true, null, new BaseError(ErrorState.BUSINESS_ERROR, "上传失败"));
                }
            }

            @Override
            public void inProgress(OkCall<FileResult> okCall, float progress, long total, boolean done) {
                getView().progress(progress, total, done, fileBundle.path, totalImage - imagesWaitForUpload.size(), totalImage);
            }
        });
    }

    private void dispatch(int remain, boolean error, FileInfo fileInfo, BaseError baseError) {
        uploadFinished(remain, error, fileInfo);
        if (remain == 0 && !error) {
            mProgress.hideLoading();
            getView().uploadFinished(true);
        }
        if (baseError != null) {
            mProgress.hideLoading();
            getView().uploadFinished(false);
            getView().handleError(baseError);
        }
    }

    /**
     * Presenter中做做业务处理
     */
    protected void uploadFinished(int remain, boolean error, FileInfo fileInfo) {

    }

    private String getKeyForImages(List<FileBundle> images) {
        ArrayList<String> as = new ArrayList<>(images.size());
        for (int i = 0; i < images.size(); i++) {
            as.add(images.get(i).path);
        }
        return getKeyForImages(as, mUrl);
    }

    private String getKeyForImages(List<String> images, String url) {
        String s = url;
        for (int i = 0; i < images.size(); i++) {
//            Logger.d(s);
            s += images.get(i);
        }
//        Logger.d(s);
        String key;
        try {
            key = EncryptUtils.md5Encrypt(s);
        } catch (UnsupportedEncodingException e) {
            key = s;
            e.printStackTrace();
        }
        return key;
    }

    public String getGroupId() {
        if (!ParamUtil.isEmpty(successfulUploadedImage))
            return successfulUploadedImage.get(0).fileInfo.getFile_group_id();
        return null;
    }

    private class FileBundle {
        String path;//原图地址
        String compressed;//压缩后图片的地址
        FileInfo fileInfo;//图片上传成功之后的返回信息

        public FileBundle(String path) {
            this.path = path;
        }

        @Override
        public String toString() {
            return path;
        }
    }

    private class ImagesCompress implements Runnable {
        private FileBundle fileBundle;
        public boolean isPublic;

        public ImagesCompress(FileBundle fileBundle, boolean isPublic) {
            this.fileBundle = fileBundle;
            this.isPublic = isPublic;
        }

        @Override
        public void run() {
            final String local = CompressedImageCache.get(fileBundle.path);
            if (local != null) {
                File file = new File(local);
                if (file.exists()) {
                    fileBundle.compressed = local;
                    upload(fileBundle, isPublic);
                    return;
                }
            }
            fileBundle.compressed = BitmapUtils.compressImage(fileBundle.path, System.currentTimeMillis() + "_upload.jpg", 50);
            CompressedImageCache.put(fileBundle.path, fileBundle.compressed);//把压缩成功的图片地址放到缓存里面，下次再用就不用再次压缩了
            upload(fileBundle, isPublic);
        }
    }
}
