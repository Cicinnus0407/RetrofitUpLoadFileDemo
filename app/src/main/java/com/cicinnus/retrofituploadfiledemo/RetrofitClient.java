package com.cicinnus.retrofituploadfiledemo;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * 构建基本的RetrofitClient，链式调用
 */

public class RetrofitClient {
    private static RetrofitClient mInstance;
    private static Retrofit retrofit;


    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(OkHttpManager.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }

    static RetrofitClient getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitClient.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitClient();
                }
            }
        }
        return mInstance;
    }

    private <T> T create(Class<T> clz) {
        return retrofit.create(clz);
    }

    Api api() {
        return RetrofitClient.getInstance().create(Api.class);
    }

    /**
     * 单上传文件的封装
     *
     * @param url                完整的接口地址
     * @param file               需要上传的文件
     * @param fileUploadObserver 上传回调
     */
    void upLoadFile(String url, File file, FileUploadObserver<ResponseBody> fileUploadObserver) {
        UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(file, fileUploadObserver);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), uploadFileRequestBody);
        create(UpLoadFileApi.class)
                .uploadFile(url, part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileUploadObserver);
    }
    //上传文件的interface
    interface UpLoadFileApi {
        @Multipart
        @POST
        Observable<ResponseBody> uploadFile(@Url String url, @Part MultipartBody.Part file);
    }


}
