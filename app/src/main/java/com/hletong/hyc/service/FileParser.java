package com.hletong.hyc.service;

import android.support.annotation.NonNull;

import com.hletong.hyc.http.parse.JpptParse;
import com.hletong.hyc.util.IOUtil;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

/**
 * Created by dongdaqing on 2017/7/31.
 */

public class FileParser extends JpptParse<File> {
    public static final FileParser INSTANCE=new FileParser();

    @NonNull
    @Override
    public OkResponse<File> handleResponse(OkCall<File> okCall, Response response) throws IOException {
        String path = okCall.request().extra("path");
        File file = new File(path);
        if (file.exists() && !file.delete()) {
            throw new IOException("failed to delete file:" + file.getPath());
        }

        File tmp = new File(file.getPath() + ".tmp");

        if (tmp.exists() && !tmp.delete()) {
            throw new IOException("failed to delete tmp file:" + tmp.getPath());
        }
        InputStream is = response.body().byteStream();
        FileOutputStream fos = null;
        try {
            if (!tmp.createNewFile()) {
                throw new IOException("failed to create file:" + tmp.getPath());
            }

            fos = new FileOutputStream(tmp);
            byte[] buffer = new byte[8096];
            int last = 0, c, ct = 0;
            while ((c = is.read(buffer)) != -1) {
                ct += c;
                fos.write(buffer, 0, c);
            }
            if (!tmp.renameTo(file)) {
                throw new IOException("failed to rename file:" + tmp.getPath());
            }
            return OkResponse.success(file);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } finally {
            IOUtil.close(fos);
        }
    }
}
