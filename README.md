�û�Ⱥ��:API 8(android2.2���ϵ��û�)
��lib��Ϊ4��ģ��,
1.����DownloadManager���أ������ȼ���
2����AsyncTask���أ����ݸ��㣩�������ȼ���
3.ͼƬ��ѹ��
4.�ļ��Ļ�������
/*********************���Ƿֽ���**********************/
����������ʹ�÷���
ģ��1˵��������DownloadManager���أ������ȼ���
�������У���private void download_API9Upper()����
Step1������Download_API9_Upperʵ����
���캯��ΪDownload_API9_Upper(Context context)
Step2���������صĲ�������getter��setter���Ի�ȡ�����ö��Ʋ���
1.FileName��String���������ص��ļ��������֣���get��set��Ĭ��download.apk��
2.SavePath��String����Ϊ��֤�ļ����ڣ��÷���ֻ�ܱ�����Ĭ��·���������ṩ���ļ������࣬��������ļ�������˵������get����
3.AllowScanningByMediaScanner��boolean�����Ƿ�����ϵͳMediaScannerɨ�赽����get��set������Ĭ��false
4.DownloadingNotifyWay��int��:����֪ͨ���ϵ�֪ͨ������£�֪ͨ��ʾ��ʽ����get��set������Ĭ��VISIBILITY_HIDDEN
5.DownloadingTitle��String��������֪ͨ���ϵ�֪ͨ������£���������֪ͨ�ı��⣬Ĭ�ϡ�title��
6.DownloadingDescription��String��������֪ͨ���ϵ�֪ͨ������£���������֪ͨ��������Ĭ�ϡ�description��
7.ShowDownloading��boolean�����Ƿ���ʾ֪ͨ���ϵ�֪ͨ
8.DownloadId��long��������id��ֻ���ڵ���getDownloadPercentage (long downloadId, Handler handler)ʱ��Ҫ
Step 3����ʼ���أ����÷�����
public void download(String url) throws Exception 
urlΪ�ļ����ص�ַ
Step: 4: ����Ҫ�����Զ���������ģ������
getDownloadPercentage (long downloadId, Handler handler)��
downloadId�ڵ��øú���֮ǰ����get������ȡ��
handlerΪ����UI��handler����handler�н���UI����
Step 5������������
isDownloadManagerAvailable()�����DownloadManager�Ƿ����

��ο������е�DownloadActivity��download_API9Upper()����
�÷���֧�ֶ��߳�����
/*********************���Ƿֽ���**********************/
ģ��2˵��������AsyncTask���أ����ݸ��㣩�������ȼ���
�õ�����ΪDownloadTask���÷������������캯����
DownloadTask(Context context)
DownloadTask(Context context, Handler uiHandler)
Step 1������DownloadTaskʵ����ʹ�õ�һ�����캯��Ϊ�������ȼ��������أ��ڶ������캯��Ϊ�����ȼ���������
Step 2���������أ�
ִ��downloadTask.execute(String... sUrl);
Step 3����Ҫ���ȼ����������handlerΪ����UI��handler
����ͨ��set��������downloadId
��downloadIdΪdownloadTask.execute(String... sUrl);��url����λ�ã���0��ʼ���㣬Ĭ�ϼ������ǵ�0��url���صĽ���

����ʹ����ο�DownloadActivity��download()����
/***********************���Ƿֽ���**********************/
ģ��3˵����ͼƬ��ѹ��
ʹ�õ�����ΪImageCompression
Step 1������ImageCompressionʵ�������캯����������
Ĭ��ѹ��֮���ļ���С���ֵ��
ImageCompression(Context context)
�Զ���ѹ��֮���ļ���С���ֵ
ImageCompression(Context context, int maxSize)
Step 2����ʼѹ��
compress(String srcPath)��srcPathΪ��Ҫѹ�����ļ���·��
Step 3����������
setFile(File file) ��������ļ�
getFile()��ȡ����ļ�
isCompressed()�Ƿ񱻳ɹ�ѹ��
getMaxSize()��ȡ�ļ�ѹ��֮���ļ���С���ֵ
getScreenWidth(Context context)��ȡ��Ļ���
getScreenHeight(Context context)��ȡ��Ļ�߶�

����ʹ����ο�DownloadActivity��downloadImage()����
/***********************���Ƿֽ���**********************/
ģ��4˵�����ļ��Ļ�������
ע�⣺�����Ҫ�����ļ�����Ŀ¼�������Լ��ĳ����������дȨ��
�÷����ṩ���ֲ�������
1.	ʹ�ô������ķ�����ֱ�Ӵ����Լ���Ҫ�������ļ����в���
2.	ͨ��get��set�������ú��ļ�λ��֮��ֱ��ʹ�ò��������ķ�������

����ʹ����ο�DownloadActivity��Listeners�ĸ�button����
