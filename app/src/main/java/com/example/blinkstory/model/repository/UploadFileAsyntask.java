package com.example.blinkstory.model.repository;

import android.os.AsyncTask;
import android.util.Log;

import com.example.blinkstory.constant.MainConstant;
import com.example.blinkstory.presenter.IUploadFilePresenter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UploadFileAsyntask implements IUploadFileAsyntask {
    private IUploadFilePresenter mIUploadFilePresenter;

    public UploadFileAsyntask(IUploadFilePresenter mIUploadFilePresenter) {
        this.mIUploadFilePresenter = mIUploadFilePresenter;
    }

    @Override
    public void onUploadFileAsyntask(final String pathFile) {
        new AsyncTask<String, Void, Integer>() {
            private  final Integer EXCPTION = 1;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mIUploadFilePresenter.onTurnOnProcessbar();
            }

            @Override
            protected Integer doInBackground(String... strings) {
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                DataInputStream inStream = null;
                String existingFileName = strings[0];
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                String responseFromServer = "";
                String urlString = MainConstant.URL_UPLOAD_DATA;

                try {

                    //------------------ CLIENT REQUEST
                    FileInputStream fileInputStream = new FileInputStream(new File(existingFileName));
                    // open a URL connection to the Servlet
                    URL url = new URL(urlString);
                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    // Allow Inputs
                    conn.setDoInput(true);
                    // Allow Outputs
                    conn.setDoOutput(true);
                    // Don't use a cached copy.
                    conn.setUseCaches(false);
                    // Use a post method.
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + existingFileName + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    // close streams
                    Log.e("Debug", "File is written");
                    fileInputStream.close();
                    dos.flush();
                    dos.close();


                    inStream = new DataInputStream(conn.getInputStream());
                    String str;

                    while ((str = inStream.readLine()) != null) {

                        Log.e("Debug", "Server Response " + str);

                    }

                    inStream.close();
                } catch (Exception ex) {
                    mIUploadFilePresenter.onDownloadFileFailed(ex.getMessage());
                    return EXCPTION;
                }
                finally {
                    mIUploadFilePresenter.onTurnOffProcessbar();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Integer resulf) {
                super.onPostExecute(resulf);
                if(resulf != EXCPTION){
                    mIUploadFilePresenter.onTurnOffProcessbar();
                    mIUploadFilePresenter.onUploadFileSuccess();
                }
            }
        }.execute(pathFile);
    }
}
