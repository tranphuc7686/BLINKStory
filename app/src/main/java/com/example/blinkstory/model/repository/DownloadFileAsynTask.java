package com.example.blinkstory.model.repository;

import android.os.AsyncTask;
import android.os.Environment;

import com.example.blinkstory.presenter.IDownloadFilePresenter;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class DownloadFileAsynTask implements IDownloadAsynTask {
    private IDownloadFilePresenter mIDownloadFilePresenter;

    public DownloadFileAsynTask(IDownloadFilePresenter mIDownloadFilePresenter) {
        this.mIDownloadFilePresenter = mIDownloadFilePresenter;
    }


    @Override
    public void onStartDownloadFile(String id,String urlDownload) {
        new AsyncTask<String, Void, File>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mIDownloadFilePresenter.onTurnOnProcessbar();

            }

            @Override
            protected File doInBackground(String... url) {
                File file = null;
                try {
                    URL u = new URL(url[0]);
                    InputStream is = u.openStream();

                    DataInputStream dis = new DataInputStream(is);

                    byte[] buffer = new byte[1024];
                    int length;
                    file = new File(Environment.getExternalStorageDirectory() + "/" + url[1]+".mp4");
                    FileOutputStream fos = new FileOutputStream(file);
                    while ((length = dis.read(buffer))>0) {
                        fos.write(buffer, 0, length);
                    }

                    fos.flush();
                    // closing streams
                    fos.close();
                    dis.close();
                }
                catch (Exception ex) {
                    mIDownloadFilePresenter.onDownloadFileFailed(ex.getMessage());
                }
                return file;

            }

            @Override
            protected void onPostExecute(File file) {

                mIDownloadFilePresenter.onDownloadFileSuccess(file);
                mIDownloadFilePresenter.onTurnOffProcessbar();
            }

        }.execute(urlDownload,id);
    }


}
