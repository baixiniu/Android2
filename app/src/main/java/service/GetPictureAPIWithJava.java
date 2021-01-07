package service;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface GetPictureAPIWithJava {
    @GET
    Observable<ResponseBody> downloadPicFromNet(@Url String fileUrl);
}
