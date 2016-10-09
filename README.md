DownLoadFileNImage：基于DownloadManager并可以监听下载进度的下载
================================================
##### lib导入语句：compile 'com.example.downloadlibrary:downloadlibrary:1.0.2'

用户群体
-
API 8(android 2.2以上的用户，不包括android 2.2)

  
###### 请在manifast中申请如下权限   
```java
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

### Step1：创建Download实例
   **示例代码：** 
```java
        Download download = new Download(context);
```  
   **构造函数说明**  
```java
    /**
     * 下载方法说明
     * @param context 上下文
     */
    public Download(Context context)
```
### Step2：定制下载的参数，用getter和setter可以获取和设置定制参数
   **示例代码：**  
```java
    download.setDownloadingNotifyWay(Download.VISIBILITY_HIDDEN).setFileName("name")
    .setDownloadingTitle("test").setDownloadingDescription("this is a test");
```  
   **可定制参数如下：**  
        1.setFileName（String）：给下载的文件设置名字，有get和set（默认download.apk）  
        2.setSavePath（String）：为保证文件存在，该方法只能保存在默认路径，但是提供了文件操作类  
         详情请见文件操作类说明，有get方法  
        3.isAllowScanningByMediaScanner（boolean）：是否允许被系统MediaScanner扫描到，有get和set方法，默认false  
        4.setDownloadingNotifyWay（int）:在有通知栏上的通知的情况下，通知显示方式，有get和set方法，默认VISIBILITY_HIDDEN  
        5.setDownloadingTitle（String）：在有通知栏上的通知的情况下，正在下载通知的标题，默认“title”  
        6.setDownloadingDescription（String）：在有通知栏上的通知的情况下，正在下载通知的描述，默认”description”  
        7.DownloadId（long）：下载id，只有在调用getDownloadPercentage (long downloadId, Handler handler)时需要  
        8.isShowDownloading（boolean）：是否显示通知栏上的通知，若不在通知栏上显示通知，需要申请如下权限：  
```java  
        <uses-permission android:name="android.permission.DOWNLOAD_WITHOUT_NOTIFICATION" /> 
```
### Step 3：定制参数完毕之后，再调用下载方法
   **示例代码**  
```java
          download.setDownloadingNotifyWay(Download.VISIBILITY_HIDDEN).setFileName("name")
          .setDownloadingTitle("test").setDownloadingDescription("this is a test").download(url);
    /**
     * 下载方法说明
     * @param url 下载地址
     * @return this
     * @throws Exception 有错误参数时，将抛出异常
     */
    public Download download(String url) throws Exception
```

### Step 4: 若需要更新自定义进度条的，请在调用完download之后添加获取进度更新的方法
   **示例代码：**  
   _I.调用：_
```java
    //获取本次下载id
    downloadId = download.getDownloadId();
    //通过id获得下载进度，并实时更新UI
    download.getDownloadPercentage(downloadId, new UIHandler());
```
   _II.UIHandler的代码_
```java
    /**
     * 进度更新类,请将需要进度更新的UI写在handler中
     */
    private class UIHandler extends Handler {
            @Override
            public void handleMessage(final Message msg) {
                super.handleMessage(msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pb_progressbar.setProgress(msg.what);
                    }
                });
            }
        }
```
   **获取下载进度函数说明**
```java
    /**
     * 获取下载进度
     *
     * @param downloadId 需要获得进度的下载id，在调用该函数之前调用get方法获取
     * @param handler    activity中更新进度条的UIHandler
     */
    public void getDownloadPercentage(long downloadId, Handler handler)
```
  
### Step 5：其它方法
```java
    /**
     * 检查DownloadManager是否可用
     */
    public void isDownloadManagerAvailable()
```
  
###### 请参考例子中的DownloadActivity的download()方法  
###### 该方法支持多线程下载

为满足下载之后的一些后续要求，该下载还支持图片保持原比例压缩大小，打开apk和文件操作的功能
-
1.图片压缩功能使用
-
### 使用到的类为ImageCompression,该方法有两个构造函数
```java
    /**
     * 默认压缩，文件大小最大值为45KB
     *
     * @param context 上下文
     */
    public ImageCompression(Context context)
     
     /**
      * 自定义压缩，文件大小最大值为maxSize
      *
      * @param context 上下文
      * @param maxSize  文件大小的最大值
      */
    public ImageCompression(Context context, int maxSize)      
```

### Step 1：创建ImageCompression实例
   **示例代码：**
```java
    ImageCompression imageCompression = new ImageCompression(context);
```
### Step 2：开始压缩
  **示例代码：**
```java
    imageCompression.compress(yourOutputPath);
```
  **代码说明**
```java
     /**
      * 自定义压缩，文件大小最大值为maxSize
      *
      * @param srcPath srcPath为需要压缩的文件的路径
      */
     public void compress(String srcPath)      
```
### Step 3：其它方法
```java
    /**
     * @return 获取输出文件
     */
    public File getFile()

    /**
     * @return 获取被压缩到的最大大小
     */
    public int getMaxSize()

    /**
     * @return 是否被成功压缩
     */
    public boolean isCompressed()

    /**
     * 屏幕宽度
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context context)

    /**
     * 屏幕高度
     *
     * @param context 上下文
     * @return 屏幕高度
     */
    public static int getScreenHeight(Context context)
```
  
###### 具体使用请参考DownloadActivity中downloadImage()方法  
  
2.文件操作功能使用
-
###### 如果需要重设文件保存目录，请在自己的程序中对读写权限进行判断和申请

#### 该方法提供两种操作方法  
1.使用带参数的方法，直接传入自己需要操作的文件进行操作  
  
2.通过set方法设置好文件位置之后，直接使用不带参数的方法操作

 下面仅对操作方法1中的方法进行代码说明：
```java
    /**
     * 文件是否存在
     *
     * @param file 需要检查的文件
     * @return 存在为true，反之为false
     */
    public static boolean fileIsExists(File file)

    /**
     * 删除文件
     *
     * @param file 需要删除的文件
     * @return 文件是否被删除
     */
    public static boolean fileDelete(File file)

    /**
     * 移动文件
     *
     * @param destDirName 移动到的目录地址
     * @param srcFile     需要移动的文件
     * @return 是否被移动
     */
    public static boolean moveFile(String destDirName, File srcFile)

    /**
     * 获取文件大小
     *
     * @param file 需要获取大小的文件
     * @return 文件大小，单位B
     */
    public static long getFileSize(File file) {

    /**
     * 若为apk，可以调用该函数安装app
     * @param fileDir 文件保存目录
     * @param fileName  文件保存名称
     * @return 是否打开成功
     */
    public static boolean openAPK(String fileDir, String fileName)
```
###### 具体使用请参考DownloadActivity中Listeners的各button处理  
   
  
