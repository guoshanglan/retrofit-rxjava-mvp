# retrofit-rxjava-mvp
这个项目是mvp开发模式和retrofit+rxjava的开发框架

在retrofit这个文件夹中封装retrofit的网络请求框架

 apiservice：定义请求方式
 
   //  下面的是rxjava与retrofit的结合封装的apiservice

    //get请求
    @GET
    Observable<String> obget(@Url String url, @QueryMap Map<String, String> params, @HeaderMap Map<String, String> headers);

    //post请求，一般会带这两个注解  参数说明： url ：接口地址  map：参数集合 ，header：需要添加的请求头
    @FormUrlEncoded
    @POST()
    Observable<String> obpost(@Url String url, @FieldMap Map<String, String> params, @HeaderMap Map<String, String> headers);



    //文件上传
    @POST()
    @Multipart
    Observable<BaseModel> uploadFiles(@Url String url, @Part MultipartBody.Part[] parts, @HeaderMap() Map<String, String> header);
    
    
    
    2：interceptor:定义的基本的网络请求拦截方式
    
    
    
    3：Httputil:对网络请求进行实例化： 在myAlication中的oncrea进行实例化然后才可以调用
    
          //初始化网络框架
        new HttpUtil.SingletonBuilder(this, Url.localhost)
                .build();
     
     4：httpbuild进行构建： 
       
                  //rxjava封装的请求（get）
    public void get(Observer<String> observable) {
        if (!allready()) {
            return;
        }
        HttpUtil.getService().obget(checkUrl(this.url), checkParams(params), checkHeaders(headers))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Observer) observable);
    }

    //rxjava封装的请求（post）
    public void post2(Observer<String> observable) {
        if (!allready()) {
            return;
        }
        HttpUtil.getService().obpost(checkUrl(this.url), checkParams(params), checkHeaders(headers))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observable);
    }
    
    
    


  //rxjava封装的文件上传
     public  void UploadFile(Observer<BaseModel> observable){
         if (this.files == null) {
             return ;
         }

         MultipartBody.Part[] parts = new MultipartBody.Part[files.size()];
         int cnt = 0;
         for (String m : files) {
             File file = new File(m);
             RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
             MultipartBody.Part filePart = MultipartBody.Part.createFormData("headimg[]", file.getName(), requestFile);
             parts[cnt] = filePart;
             cnt++;
         }

         HttpUtil.getService().uploadFiles(checkUrl(this.url), parts, checkHeaders(headers))
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(observable);

     }
     
     
     使用：
   
   public void loginNet(final Context context, String number, String password , final LoginPresenter listener){
       new HttpBuilder(Url.LOGIN)
               .params("username",number)
               .params("password",password)
               .tag(context)
               .post2(new Observer<String>() {
                   @Override
                   public void onSubscribe(Disposable d) {

                   }

                   @Override
                   public void onNext(String value) {
                       if (value != null) {
                           loginBean = new Gson().fromJson(value, LoginBean.class);
                           listener.loginSuccess(loginBean);
                       }
                   }

                   @Override
                   public void onError(Throwable e) {
                       listener.loginFailed(e.toString());
                   }

                   @Override
                   public void onComplete() {

                   }
               });
     
     
     
       
