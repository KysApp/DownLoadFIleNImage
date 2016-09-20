DownLoadFileNImage使用说明
=
##### lib导入语句：compile 'com.example.downloadlibrary:downloadlibrary:1.0.1'  

用户群体
-
API 8(android2.2以上的用户)  
  
该lib分为4个模块
-
#### 1.基于DownloadManager下载，带进度监听  
#### 2.基于AsyncTask下载（兼容更广），带进度监听  
#### 3.图片的压缩  
#### 4.文件的基本操作  
  
###### 请在manifast中申请如下权限  

    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
  
模块1说明：基于DownloadManager下载，带进度监听  
-
#### 使用到的类为Download_API9_Upper和DownloadObserver
  
#### Step1：创建Download_API9_Upper实例   
    构造函数为Download_API9_Upper(Context context)  
  
#### Step2：定制下载的参数，用getter和setter可以获取和设置定制参数  
    1.FileName（String）：给下载的文件设置名字，有get和set（默认download.apk） 
    2.SavePath（String）：为保证文件存在，该方法只能保存在默认路径，但是提供了文件操作类  
    详情请见文件操作类说明，有get方法   
    3.AllowScanningByMediaScanner（boolean）：是否允许被系统MediaScanner扫描到，有get和set方法，默认false  
    4.DownloadingNotifyWay（int）:在有通知栏上的通知的情况下，通知显示方式，有get和set方法，默认VISIBILITY_HIDDEN  
    5.DownloadingTitle（String）：在有通知栏上的通知的情况下，正在下载通知的标题，默认“title”  
    6.DownloadingDescription（String）：在有通知栏上的通知的情况下，正在下载通知的描述，默认”description”  
    7.ShowDownloading（boolean）：是否显示通知栏上的通知
      若不在通知栏上显示通知，需要申请如下权限：
        <uses-permission android:name="android.permission.DOWNLOAD_WITHOUT_NOTIFICATION" />
    8.DownloadId（long）：下载id，只有在调用getDownloadPercentage (long downloadId, Handler handler)时需要   
  
#### Step 3：开始下载，调用方法   
    public void download(String url) throws Exception   
    url为文件下载地址  
      
#### Step: 4: 若需要更新自定义进度条的，请调用 getDownloadPercentage (long downloadId, Handler handler)   
    downloadId在调用该函数之前调用get方法获取  
    handler为更新UI的handler，该handler中进行UI更新  
  
#### Step 5：其它方法   
    isDownloadManagerAvailable()：检查DownloadManager是否可用  
  
###### 请参考例子中的DownloadActivity的download_API9Upper()方法  
###### 该方法支持多线程下载
  
模块2说明：基于AsyncTask下载（兼容更广），带进度监听  
-
#### 用到的类为DownloadTask，该方法有两个构造函数
    不带进度监听的下载
      DownloadTask(Context context)  
      
    带进度监听的下载
      DownloadTask(Context context, Handler uiHandler)  
      
    本模块需要权限：
      <uses-permission android:name="android.permission.WAKE_LOCK" />
  
#### Step 1：创建DownloadTask实例
  
#### Step 2：进行下载    
    执行downloadTask.execute(String... sUrl);  
    可输入多个url，用逗号隔开，每个下载的id为传参的位置  
    如:downloadTask.execute(url0,url1,url2,...,urln);  
    url0的下载id为0，url1的下载id为1，url2的下载id为2，...，urln的下载id为n
  
#### Step 3：若要进度监听，传入的handler为更新UI的handler  
    通过set方法传入downloadId  
    若不知道怎么获取downloadId，请看step2
  
###### 具体使用请参考DownloadActivity中download()方法  
###### 该方法支持多线程下载

模块3说明：图片的压缩  
-
#### 使用到的类为ImageCompression,该方法有两个构造函数
    默认压缩，文件大小最大值为45KB：
      ImageCompression(Context context)

    自定义压缩，文件大小最大值为maxSize：
      ImageCompression(Context context, int maxSize)
  
#### Step 1：创建ImageCompression实例，构造函数有两个    

#### Step 2：开始压缩  
    compress(String srcPath)，srcPath为需要压缩的文件的路径  
  
#### Step 3：其它方法  
    1.setFile(File file) 设置输出文件  
    2.getFile()获取输出文件  
    3.isCompressed()是否被成功压缩  
    4.getMaxSize()获取文件压缩之后，文件大小最大值  
    5.getScreenWidth(Context context)获取屏幕宽度  
    6.getScreenHeight(Context context)获取屏幕高度  
  
###### 具体使用请参考DownloadActivity中downloadImage()方法  
  
模块4说明：文件的基本操作  
-
###### 如果需要重设文件保存目录，请在自己的程序中对读写权限进行判断和申请
#### 使用到的类为FileOperation

#### 该方法提供两种操作方法  
    1.使用带参数的方法，直接传入自己需要操作的文件进行操作  
    
    2.通过get和set方法设置好文件位置之后，直接使用不带参数的方法操作  
  
###### 具体使用请参考DownloadActivity中Listeners的各button处理  
   
  
