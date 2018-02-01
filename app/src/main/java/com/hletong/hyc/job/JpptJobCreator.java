package com.hletong.hyc.job;

import com.evernote.android.job.Job;

/**
 * Created by dongdaqing on 2017/7/28.
 */

public class JpptJobCreator implements com.evernote.android.job.JobCreator {
    @Override
    public Job create(String tag) {
        switch (tag){
            case UploadLocationJob.LOCATION:
                return new UploadLocationJob();
        }
        return null;
    }
}
