package com.quys.utilslib;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.DecimalFormat;

/**
 * 文件目录工具
 */
public class FileUtil {
    private static final String TAG = "FileUtil";
    private static final String DEFAULT_ROOT_PATH = "FileUtil";
    private static String ROOT = DEFAULT_ROOT_PATH;
    /**
     * 图片缓存
     */
    public static final String IMAGE_PATH = Environment.DIRECTORY_PICTURES;
    /**
     * 相册
     */
    public static final String MICRO_IMAGE = "album";
    /**
     * 文件缓存路径
     */
    public static final String FILE_CACHE_PATH = "fileCaches";
    /**
     * 异常的日志
     */
    public static final String LOG = "log";

    public static final String DUMP = "dump";

    public static final String APK = "apk";

    public static void setRootName(String rootName) {
        if (TextUtils.equals(ROOT, DEFAULT_ROOT_PATH)) {
            if (!TextUtils.isEmpty(rootName)) {
                ROOT = rootName;
            }
        } else {
            throw new IllegalArgumentException("rootName can only set once!");
        }
    }

    public static String getFilePath(Context context) {
        return getFilePath(context, "", true);
    }

    /**
     * @param type The type of files directory to return. May be {@code null}
     *            for the root of the files directory or one of the following
     *            constants for a subdirectory:
     *            {@link Environment#DIRECTORY_MUSIC},
     *            {@link Environment#DIRECTORY_PODCASTS},
     *            {@link Environment#DIRECTORY_RINGTONES},
     *            {@link Environment#DIRECTORY_ALARMS},
     *            {@link Environment#DIRECTORY_NOTIFICATIONS},
     *            {@link Environment#DIRECTORY_PICTURES}, or
     *            {@link Environment#DIRECTORY_MOVIES}.
     * @param isUseAppRootPath 是否加入 {@link #ROOT} 目录，默认为true
     * @return
     */
    public static String getFilePath(Context context, String type, boolean isUseAppRootPath) {
        String savePath;
        String rootPath = "";
        if(hasSDCard()) {
            File rootFile = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                rootFile = context.getExternalFilesDir(TextUtils.isEmpty(type) ? "" : type);
            }
            if (null == rootFile) {
                rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            } else {
                rootPath = rootFile.getAbsolutePath();
            }
        } else {
            rootPath = context.getFilesDir().getAbsolutePath();
        }
        savePath = rootPath + File.separator;
        File file;
        if (isUseAppRootPath) {
            file = new File(savePath, ROOT);
        } else {
            file = new File(savePath);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    public static File createFileDir(Context context, String dirPath) {
        return createFileDir(context, "", dirPath);
    }

    public static File createFileDir(Context context, String type, String dirPath) {
        File file = new File(getFilePath(context, type, TextUtils.isEmpty(type)), dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 适配 Build.VERSION_CODES.Q
     * @return
     */
    public static String getPhotoPicTempPath(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File file = createFileDir(context, Environment.DIRECTORY_PICTURES, "");
            return file.getAbsolutePath();
        } else {
            File file = createFileDir(context, IMAGE_PATH);
            return file.getAbsolutePath();
        }
    }

    /**
     * 存储在 Android/data/com.xx.xxx包名下
     * @return
     */
    public static String getAndroidDataPhotoPicTempPath(Context context) {
        File file = createFileDir(context, Environment.DIRECTORY_PICTURES, "");
        return file.getAbsolutePath();
    }


    /**
     * 适配 Build.VERSION_CODES.Q
     * @return
     */
    public static String getExceptionPath(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File file = createFileDir(context, "", LOG);
            return file.getAbsolutePath();
        } else {
            return getAndroidDataExceptionPath(context);
        }
    }

    /**
     * 存储在 Android/data/com.xx.xxx包名下
     * @return
     */
    public static String getAndroidDataExceptionPath(Context context) {
        File file = createFileDir(context, "", LOG);
        return file.getAbsolutePath();
    }


    /**
     * 适配 Build.VERSION_CODES.Q
     * @return
     */
    public static String getApkPath(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File file = createFileDir(context, "", APK);
            return file.getAbsolutePath();
        } else {
            return getAndroidDataApkPath(context);
        }
    }

    /**
     * 存储在 Android/data/com.xx.xxx包名下
     * @return
     */
    public static String getAndroidDataApkPath(Context context) {
        File file = createFileDir(context, "", APK);
        return file.getAbsolutePath();
    }


    /**
     * 适配 Build.VERSION_CODES.Q
     * @return
     */
    public static String getFileCachePath(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File file = createFileDir(context, "", FILE_CACHE_PATH);
            return file.getAbsolutePath();
        } else {
            return getAndroidDataFileCachePath(context);
        }
    }

    /**
     * 存储在 Android/data/com.xx.xxx包名下
     * @return
     */
    public static String getAndroidDataFileCachePath(Context context) {
        File file = createFileDir(context, "", FILE_CACHE_PATH);
        return file.getAbsolutePath();
    }

    /**
     * 判断文件是否存在
     */
    public static boolean isFileExist(String path) {
        File file = new File(path);
        return isFileExist(file);
    }

    /**
     * 判断文件是否存在
     */
    public static boolean isFileExist(File file) {
        return file.exists();
    }

    /**
     * 创建目录
     */
    public static File getFolder(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!file.isDirectory()) {
            file.delete();
            file.mkdirs();
        }
        return file;
    }

    /**
     * 创建文件
     */
    public static void createFile(String filePath) {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            if (!file.isFile()) {
                file.delete();
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归删除文件夹下的数据
     */
    public static synchronized void deleteFile(String filePath){
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (null != files && files.length > 0) {
                for (File subFile : files) {
                    String path = subFile.getPath();
                    deleteFile(path);
                }
            }
        }
        //删除文件
        file.delete();
    }

    public static String getFilePathFromUri(Context ctx, Uri uri) {
        String filePath = "";
        LogUtils.i(TAG, "getFilePathFromUri----->uri: " + uri);
        if (null != uri) {
            filePath = uri.getPath();
            LogUtils.i(TAG, "getFilePathFromUri----->uri.getPath(): " + filePath);
            File file = new File(filePath);
            if (!file.exists()) {
                filePath = getRealPathFromURI(ctx, uri);
                LogUtils.i(TAG, "getFilePathFromUri----->getRealPathFromURI: " + filePath);
                file = new File(filePath);
                if (!file.exists()) {
                    LogUtils.e(TAG, "getFilePathFromUri----->get filePath failed");
                }
            }
        }
        LogUtils.i(TAG, "getFilePathFromUri----->filePath: " + filePath);
        return filePath;
    }

    public static String getRealPathFromURI(Context ctx, Uri uri) {
        String realPath = "";
        Cursor cursor = null;
        int columnIndex;
        String[] column = {MediaStore.Video.Media.DATA};
        ContentResolver contentResolver = ctx.getContentResolver();
        if (uri != null) {
            try {
                if (Build.VERSION.SDK_INT < 19) {
                    cursor = contentResolver.query(uri, column, null, null, null);
                    if (cursor != null) {
                        columnIndex = cursor.getColumnIndexOrThrow(column[0]);
                        cursor.moveToFirst();
                        realPath = cursor.getString(columnIndex);
                    }
                } else {
                    if (DocumentsContract.isDocumentUri(ctx, uri)) {
                        // 如果是document类型的Uri，则通过document id处理
                        String wholeID = DocumentsContract.getDocumentId(uri);
                        LogUtils.i(TAG, "getRealPathFromURI----->wholeID: " + wholeID);
                        if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                            // Split at colon, use second item in the array
                            String id = wholeID.split(":")[1]; // 解析出数字格式的id
                            // where id is equal to
                            String selection = MediaStore.Video.Media._ID + "=" + id;
                            realPath = getImagePath(contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    , selection);
                        } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                            Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(wholeID));
                            realPath = getImagePath(contentResolver, contentUri, null);
                        }
                    } else if("content".equalsIgnoreCase(uri.getScheme())) {
                        // 如果是content类型的Uri，则使用普通方式处理
                        realPath = getImagePath(contentResolver, uri, null);
//						realPath = GetImagePath.getPath(ctx, uri);

                        if(TextUtils.isEmpty(realPath)) {
                            realPath = handleCloudDeleteImage(uri, ctx);
                        }
                    }
                    else if("file".equalsIgnoreCase(uri.getScheme())) {
                        // 如果是file类型的Uri，直接获取图片路径即可
                        realPath = uri.getPath();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != cursor) {
                    cursor.close();
                }
            }
        }
        return realPath;
    }

    /**
     * 备注:进行该项处理的缘由:当软件选择照片时，如果是使用googlephoto或google云选择备份过，并在本地删除过的图片时，程序就会崩溃
     * 原因是使用googlephoto备份过并在本地删除的图片会在手机中存储一个缩略图，其他软件调用googlephoto选择图片时依然能看到删除过的图片
     * ，但是此时图片的url已经不是本地的url了，而是一个图片的下载链接，这时使用getPath(url)会得到一个null路径。
     * 不处理情况下打开如上所述开VPN下载好的图片会报错:content://com.google.android.apps.photos.contentprovider/0/1/mediakey%3A%2FAF1QipOcm5p-hk2Hr37qwRgRdt16LgVDU70KPOj1wiHT/ORIGINAL/NONE/1112993269
     * 图片如果不存在，不开VPN下载，图片会下载不了
     * @param uri
     * @return
     */
    private static String handleCloudDeleteImage(Uri uri, Context ctx) {
        Uri tempUri;
        String dir = getFilePath(ctx) + "pic/";
        getFolder(dir);
        String tempFilePath = dir + System.currentTimeMillis() + "photo_cloud_delete.jpg";
        File outputImage = new File(tempFilePath);
        try {
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream input = null;
        FileOutputStream output = null;
        try {
            outputImage.createNewFile();
            input = ctx.getContentResolver().openInputStream(uri);//可替换为任何路径何和文件名
            output = new FileOutputStream(outputImage);//可替换为任何路径何和文件名
            byte[] b = new byte[1024];
            int len;
            // 判断是否到文件结尾
            while ((len = input.read(b)) != -1) {
                Log.d(TAG, "handleCloudDeleteImage: read once");
                output.write(b, 0, len); // 复制旧文件的内容到新文件
                output.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //Now fetch the new URI
        tempUri = Uri.fromFile(outputImage);
        return tempUri.getPath();
    }

    private static String getImagePath(ContentResolver contentResolver, Uri uri, String selection) {
        LogUtils.d(TAG, "getImagePath: uri--->" + uri + " :::selection--->" + selection);
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = contentResolver.query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public static boolean writeFile(String filePath, byte[] content) {
        if(null == filePath || null == content) {
            LogUtils.e(TAG, "Invalid param. filePath: " + filePath + ", content: " + content);
            return false;
        }
        FileOutputStream fos = null;
        try{
            String pth = filePath.substring(0, filePath.lastIndexOf("/"));
            File pf = null;
            pf = new File(pth);
            if(pf.exists() && !pf.isDirectory()) {
                pf.delete();
            }
            pf = new File(filePath);
            if(pf.exists()) {
                if(pf.isDirectory()){
                    deleteFile(pf.getAbsolutePath());
                } else {
                    pf.delete();
                }
            }
            pf = new File(pth + File.separator);
            if (!pf.exists()) {
                if (!pf.mkdirs()) {
                    LogUtils.e(TAG, "Can't make dirs, path=" + pth);
                }
            }
            fos = new FileOutputStream(filePath);
            fos.write(content);
            fos.flush();
            fos.close();
            fos = null;
            pf.setLastModified(System.currentTimeMillis());
            return true;
        } catch(Exception ex) {
            LogUtils.e(TAG, "Exception---->" + ex);
        } finally {
            if(null != fos) {
                try {
                    fos.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return false;
    }

    //获取文件的编码格式
    public static CharsetEnum getCharset(String fileName) {
        BufferedInputStream bis = null;
        CharsetEnum charset = CharsetEnum.GBK;
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            bis = new BufferedInputStream(new FileInputStream(fileName));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                return charset;
            }
            if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = CharsetEnum.UTF8;
                checked = true;
            }
            /*
             * 不支持 UTF16LE 和 UTF16BE
            else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = Charset.UTF16LE;
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = Charset.UTF16BE;
                checked = true;
            } else */

            bis.mark(0);
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0) {
                        break;
                    }
                    if (0x80 <= read && read <= 0xBF) {
                        // 单独出现BF以下的，也算是GBK
                        break;
                    }
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            // 双字节 (0xC0 - 0xDF)
                            // (0x80 - 0xBF),也可能在GB编码内
                            continue;
                        }
                        else {
                            break;
                        }
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = CharsetEnum.UTF8;
                                break;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.INSTANCE.close(bis);
        }
        return charset;
    }

    /**
     * 本来是获取File的内容的。但是为了解决文本缩进、换行的问题
     * 这个方法就是专门用来获取书籍的...
     *
     * 应该放在BookRepository中。。。
     * @param file
     * @return
     */
    @Deprecated
    public static String getFileContent(File file){
        Reader reader = null;
        String str = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            while ((str = br.readLine()) != null){
                //过滤空语句
                if (!str.equals("")){
                    //由于sb会自动过滤\n,所以需要加上去
                    sb.append("    ").append(str).append("\n");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.INSTANCE.close(reader);
        }
        return sb.toString();
    }

    //获取文件
    public static synchronized File getFile(String filePath){
        File file = new File(filePath);
        try {
            if (!file.exists()){
                //创建父类文件夹
                getFolder(file.getParent());
                //创建文件
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static long getDirSize(File file){
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                long size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {
                return file.length();
            }
        } else {
            return 0;
        }
    }

    public static String getFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"b", "kb", "M", "G", "T"};
        //计算单位的，原理是利用lg,公式是 lg(1024^n) = nlg(1024)，最后 nlg(1024)/lg(1024) = n。
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        //计算原理是，size/单位值。单位值指的是:比如说b = 1024,KB = 1024^2
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}
