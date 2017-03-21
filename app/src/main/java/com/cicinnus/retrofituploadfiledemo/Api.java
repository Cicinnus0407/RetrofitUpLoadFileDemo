package com.cicinnus.retrofituploadfiledemo;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * 接口地址
 */

public interface Api {
    String BASE_URL = "http://192.168.191.1:8080/UploadFileServer/servlet/";

    //上传文件
    //没有使用@Post（“地址”）方法，使用了动态的url，方便封装
    @Multipart
    @POST
    Observable<ResponseBody> uploadFile(@Url String url, @Part MultipartBody.Part file);
}
