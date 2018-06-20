package com.sunian.baselib.util;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;

public class DownLoadService extends Service {

    private DownloadManager manager;
    private DownCompleteBroadCast receiver;
    private String url;
    private String DOWNLOADPATH = "/download/";

    public static final String TYPE_DOWN = "type_down";

    private void initDownManager() {
        try {
            manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            receiver = new DownCompleteBroadCast();
            DownloadManager.Request down = new DownloadManager.Request(Uri.parse(url));
            down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                    | DownloadManager.Request.NETWORK_WIFI);
            down.setAllowedOverRoaming(false);
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
            down.setMimeType(mimeString);
            down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            down.setVisibleInDownloadsUi(true);
            down.setDestinationInExternalPublicDir(DOWNLOADPATH, "智慧宝贝.apk");
            down.setTitle("智慧宝贝");
            manager.enqueue(down);
            registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } catch (Exception e) {
          //  ToasUtil.show("下载地址出错");
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        url = intent.getStringExtra(TYPE_DOWN);
        File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DOWNLOADPATH);
        if (!file1.exists())
            file1.mkdirs();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + DOWNLOADPATH + "智慧宝贝.apk";
        File file = new File(path);
        if (file.exists()) {
            deleteFileWithPath(path);
        }
        try {
            initDownManager();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent0 = new Intent(Intent.ACTION_VIEW, uri);
                intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent0);
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
            }
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (receiver != null)
            unregisterReceiver(receiver);
        super.onDestroy();
    }

    public boolean deleteFileWithPath(String filePath) {
        SecurityManager checker = new SecurityManager();
        File f = new File(filePath);
        checker.checkDelete(filePath);
        if (f.isFile()) {
            f.delete();
            return true;
        }
        return false;
    }

    public class DownCompleteBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //判断是否下载完成的广播
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

                DownLoadService.this.stopSelf();
                //获取下载的文件id
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                //自动安装apk
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                    Uri uriForDownloadedFile = ((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE)).getUriForDownloadedFile(downId);
                    installApkNew(uriForDownloadedFile, context);
                }
                /*Process.(Process.myPid());*/
                //   Process.killProcess(Process.myPid());
            } else {
             //   ToasUtil.show("下载失败！");
            }
        }


        private String getRealFile(Context context, Uri uri) {
            String path = null;
            if (null == uri) {
              // ToasUtil.show("下载失败！");
                path = null;
            } else {
                String scheme = uri.getScheme();
                if (scheme == null || ContentResolver.SCHEME_FILE.equals(scheme))
                    path = uri.getPath();
                else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
                    Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                    if (null != cursor) {
                        if (cursor.moveToFirst()) {
                            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                            if (index > -1) {
                                path = cursor.getString(index);
                            }
                        }
                        cursor.close();
                    }

                }

            }

            return path;
        }


        //安装apk
        protected void installApkNew(Uri uri, Context context) {
            String path = getRealFile(context, uri);
            if (path == null || !new File(path).exists()) {
             //   ToasUtil.show("下载失败！");
                return;
            }
            String cmd = "chmod 777 " + path;
            File file = new File(path);
            try {
                Runtime runtime = Runtime.getRuntime();
                Process proc = runtime.exec(cmd);
            } catch (Exception e) {
                e.printStackTrace();
            }


            Intent intent = new Intent();
            //执行动作
            intent.setAction(Intent.ACTION_VIEW);
            //执行的数据类型
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String packageName = context.getPackageName();
                Log.i("infos------packageName",packageName);
                Uri uriForFile = FileProvider.getUriForFile(context, packageName + ".provider", file);
                intent.setDataAndType(uriForFile, context.getContentResolver().getType(uri));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                intent.setDataAndType(Uri.fromFile(file), getMiMeType(file));
            }


            //   intent.setDataAndType(uri, "application/vnd.android.package-archive");
            //不加下面这句话是可以的，查考的里面说如果不加上这句的话在apk安装完成之后点击单开会崩溃
            // android.os.Process.killProcess(android.os.Process.myPid());
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
            }


        }

        private String getMiMeType(File file) {
            String var1 = "";
            String var2 = file.getName();
            String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
            var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
            return var1;
        }
    }

}