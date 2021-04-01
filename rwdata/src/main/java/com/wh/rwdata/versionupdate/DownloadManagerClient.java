package com.wh.rwdata.versionupdate;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

/**
 * 2021/2/23
 * wh
 * desc：
 */
public class DownloadManagerClient {

    private DownloadManagerClient mClient;
    private long mId;

    public DownloadManagerClient getInstance() {
        if (mClient == null) {
            synchronized (DownloadManagerClient.class) {
                if (mClient == null) {
                    mClient = new DownloadManagerClient();
                }
            }
        }
        return mClient;
    }

    public void download(Context context) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(""))
                .setTitle("版本更新")
                .setDescription("正在下载...")
                .setAllowedOverRoaming(true)
                .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_NOTIFICATIONS, "version.apk");

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        mId = downloadManager.enqueue(request);
    }

    public void cancelDownLodd(Context context) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.remove(mId);
    }
}
