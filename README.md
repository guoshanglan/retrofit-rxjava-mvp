# retrofit-rxjava-mvp
这个项目是mvp开发模式和retrofit+rxjava的开发框架


  需要引入的jar包：
         implementation 'com.squareup.retrofit2:retrofit:2.1.0'
         implementation 'com.squareup.okhttp3:okhttp:3.5.0'
         implementation 'com.squareup.retrofit2:converter-gson:2.1.0'
         implementation 'io.reactivex.rxjava2:rxjava:2.0.1'
         implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'
         implementation 'com.android.support:support-v4:28.0.0'
         implementation 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
         implementation 'com.squareup.retrofit2:converter-scalars:2.3.0'



在retrofit这个文件夹中封装retrofit的网络请求框架

 apiservice：定义请求方式
 
   //  下面的是rxjava与retrofit的结合封装的apiservice

     /**
         * TODO Get请求
         */
        @GET
        Observable<BaseResponse> getUser(@Url String url,@QueryMap Map<String, String> info); //简洁方式   直接获取所需数据


        /**
         * TODO POST请求
         */
        @POST
        @FormUrlEncoded
        //多个参数
        Observable<BaseResponse> postUser(@Url String url,@FieldMap Map<String, String> map,@HeaderMap Map<String,String>headsMap);

        /**
         * TODO DELETE
         */
        @DELETE
        Observable<BaseResponse> delete(@Url String url,@QueryMap Map<String, String> map,@HeaderMap Map<String,String>headsMap);

        /**
         * TODO PUT
         */
        @PUT()
        Observable<BaseResponse> put(@Url String url,@FieldMap Map<String, String> map,@HeaderMap Map<String,String>headsMap);

        /**
         * TODO 文件上传
         */
        //亲测可用
        @Multipart
        @POST
        Observable<BaseResponse> uploadImage(@Url String url,@HeaderMap Map<String, String> headers, @Part MultipartBody.Part file);

        /**
         * 多文件上传
         */


        @Multipart
        @POST
        Observable<BaseResponse> uploadImages(@Url String url,@HeaderMap Map<String, String> headers, @Part List<MultipartBody.Part> files);

        /**
         * 来自https://blog.csdn.net/impure/article/details/79658098
         * @Streaming 这个注解必须添加，否则文件全部写入内存，文件过大会造成内存溢出
         */
        @Streaming
        @GET
        Observable<BaseResponse> download(@Header("RANGE") String start, @Url String url);
    
    
    
    2：LogInterceptor:定义的基本的网络请求拦截方式
    
    
    
    3：RetrofitUtils:对网络请求进行实例化

     /**
         * 初始化Retrofit
         */
        @NonNull
        private Retrofit initRetrofit(OkHttpClient client) {
            return new Retrofit.Builder()
                        .client(client)
                        .baseUrl(Url.BaseUrl)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
        }

        /**
         * 初始化okhttp
         */
        @NonNull
        private OkHttpClient initOkHttp() {
            return new OkHttpClient().newBuilder()
                        .readTimeout(Url.DEFAULT_TIME, TimeUnit.SECONDS)//设置读取超时时间
                        .connectTimeout(Url.DEFAULT_TIME, TimeUnit.SECONDS)//设置请求超时时间
                        .writeTimeout(Url.DEFAULT_TIME,TimeUnit.SECONDS)//设置写入超时时间
                        .addInterceptor(new LogInterceptor())//添加打印拦截器
                        .retryOnConnectionFailure(true)//设置出现错误进行重新连接。
                        .build();
        }



    /**
     * Get 请求
     * @param context
     * @param
     */
    public static void get(Context context, String url, Map<String,String>params, MyObserver<BaseResponse> observer){
        RetrofitUtils.getApiUrl()
                .getUser(url,params).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * Post 请求
     * @param context
     * @param
     */
    public static void post(Context context, String url, Map<String,String>params, Map<String,String>headsMap, MyObserver<BaseResponse> observer){
        RetrofitUtils.getApiUrl()
                .postUser(url,params,headsMap).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                         observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    /**
     * Put 请求
     * @param context
     * @param
     */
    @SuppressLint("CheckResult")
    public static void put(Context context, String url, Map<String,String>params, Map<String,String>headsMap, MyObserver<BaseResponse> observer){
        Map<String, String> headers = new HashMap<String, String>();
        RetrofitUtils.getApiUrl()
                .put(url,params,headsMap).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
    /**
     * Delete 请求demo
     * @param context  上下文对象
     * @param observer   观察者
     * @param params  查询参数map集合
     * @param headsMap  请求头参数
     * @param observer   观察者
     */
    @SuppressLint("CheckResult")
    public static void delete(Context context, String url, Map<String,String>params, Map<String,String>headsMap, MyObserver<BaseResponse> observer){
        RetrofitUtils.getApiUrl()
                .delete(url,params,headsMap).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 上传图片，文件
     * @param context
     * @param observer
     */
    public static void upImage(Context context, String  url, String pathName, MyObserver<BaseResponse>  observer){
        File file = new File(pathName);
//        File file = new File(imgPath);
        Map<String,String> header = new HashMap<String, String>();
        header.put("Accept","application/json");

//        File file =new File(filePath);
        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        RequestBody requestFile =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), reqFile);
        RetrofitUtils.getApiUrl().uploadImage(url,header,body).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 上传多张图片，多份文件
     * @param files
     */
    public static void upLoadImgs(Context context, String url, List<File> files, MyObserver<BaseResponse>  observer){
        Map<String,String> header = new HashMap<String, String>();
        header.put("Accept","application/json");
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("file", file.getName(), photoRequestBody);
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitUtils.getApiUrl().uploadImages(url,header,parts).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



     //使用

      //登录的网络请求
         public void login(final Context context, String number, String VerificationCode, final LoginPresenter listener) {
             Map<String, String> params = new HashMap<>();
             params.put("phone", number);
             params.put("code", VerificationCode);

             RequestUtils.post(context, Url.paientLogin, params, new HashMap<>(), new MyObserver<BaseResponse>(context) {
                 @Override
                 public void onSuccess(BaseResponse result) {
                     if (result != null) {

                         if (result.result == 0) {
                             Gson gson = new Gson();
                             User paientUser = new Gson().fromJson(gson.toJson(result.getDatas()), User.class);
                             listener.loginSuccess(paientUser);
                         } else {

                             listener.loginFailed(result.message);
                         }

                     }
                 }

                 @Override
                 public void onFailure(Throwable e, String errorMsg) {
                     listener.loginFailed(errorMsg);
                 }
             });


         }
