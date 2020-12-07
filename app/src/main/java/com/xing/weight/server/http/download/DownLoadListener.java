package com.xing.weight.server.http.download;

public interface DownLoadListener {

    void onDownLoadSuccess(String filePath);

    void onDownloadProgress(int progress);

    void onDownLoadError(String message);
}
