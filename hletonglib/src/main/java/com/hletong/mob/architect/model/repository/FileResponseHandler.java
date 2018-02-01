package com.hletong.mob.architect.model.repository;

import com.hletong.mob.architect.error.DataError;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by ddq on 2016/12/27.
 */

class FileResponseHandler extends ResponseHandler<File> {
    private String mFilePath;

    public FileResponseHandler(int tag, DataCallback<File> mCallback, String mFilePath) {
        super(tag, mCallback);
        this.mFilePath = mFilePath;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        clean();//清空临时数据
        super.onFailure(call, e);
    }

    @Override
    protected File handleResponse(Response response) throws IOException {
        Logger.d("start downloading file => " + mFilePath);
        File tmp = new File(mFilePath + ".tmp");

        if (!tmp.exists()) {
            if (!tmp.createNewFile()) {
                setError(new DataError("文件创建失败"));
            }
        }

        if (!tmp.isFile()) {
            setError(new DataError("文件错误"));
        }

        if (checkErrorNull()) {
            FileOutputStream fos = new FileOutputStream(tmp);
            InputStream mInputStream = response.body().byteStream();
            try {
                byte[] buffer = new byte[1024];
                int c, ct = 0;
                while ((c = mInputStream.read(buffer)) != -1) {
                    ct += c;
                    fos.write(buffer, 0, c);
                }
            } catch (Exception e) {
                handleException(e);
            } finally {
                try {
                    mInputStream.close();
                } catch (IOException e) {
                    handleException(e);
                }

                try {
                    fos.close();
                } catch (IOException e) {
                    handleException(e);
                }
            }
        } else {
            //下载文件出错
            clean();//删除可能存在的缓存文件
            return null;
        }
        File mFile = new File(mFilePath);
        if (!tmp.renameTo(mFile)) {
            clean();
            setError(new DataError("文件重命名失败"));
            return null;
        }
        return mFile;
    }

    private void clean() {//下载出错时，清空缓存文件
        Logger.d("error occur while downloading, clean cache data");
        File mFile = new File(mFilePath + ".tmp");
        if (mFile.exists()) {
            if (!mFile.delete()) {
                Logger.d("failed to clean dirty file => " + mFile.getAbsolutePath());
            }
        }
    }
}
