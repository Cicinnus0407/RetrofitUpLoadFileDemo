package com.cicinnus.retrofituploadfiledemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {


    private ProgressDialog dialog;
    String url = "http://192.168.191.1:8080/UploadFileServer/servlet/UploadHandleServlet";
    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ddmsrec.mp4");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMax(100);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMessage("上传文件中");
    }

    /**
     * 未封装的方法
     * @param view
     */
    public void upload(View view) {
        dialog.show();
        FileUploadObserver<ResponseBody> fileUploadObserver = new FileUploadObserver<ResponseBody>() {
            @Override
            public void onUpLoadSuccess(ResponseBody responseBody) {
                //上传成功
                dialog.dismiss();

            }

            @Override
            public void onUpLoadFail(Throwable e) {
                //上传失败
                dialog.dismiss();
            }

            @Override
            public void onProgress(int progress) {
                //progress 0-100
                dialog.setProgress(progress);
            }
        };

        UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(file,fileUploadObserver);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file",file.getName(),uploadFileRequestBody);
        RetrofitClient
                .getInstance()
                .api()
                .uploadFile(url,part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileUploadObserver);
    }

    /**
     * 封装后的单文件上传方法
     * @param view
     */
    public void upload2(View view) {
        dialog.show();
        RetrofitClient
                .getInstance()
                .upLoadFile(url, file, new FileUploadObserver<ResponseBody>() {
                    @Override
                    public void onUpLoadSuccess(ResponseBody responseBody) {
                        Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        try {
                            Log.d("上传进度",responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onUpLoadFail(Throwable e) {
                        Toast.makeText(MainActivity.this, "上传失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    @Override
                    public void onProgress(int progress) {
                        dialog.setProgress(progress);
                    }
                });
    }
}
