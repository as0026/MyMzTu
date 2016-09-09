package com.hjs.mymztu.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

/**
 * @描述 文件工具类
 * @作者 郝炯淞
 * @创建时间 2014-4-29 下午4:15:23
 */
public class FileUtils {
	
	private static String cacheFilePath,filesPath;
	
	/**
	 * @描述: 获取默认的缓存目录,需要 android.permission.WRITE_EXTERNAL_STORAGE 权限
	 * @作者：  郝炯淞
	 * @创建时间：  2014-6-3 下午4:11:40
	 * @param context
	 * @return
	 * String
	 */
	public static final String getCacheFilePath(Context context){
		if(cacheFilePath == null || cacheFilePath.length() <= 0){
			cacheFilePath = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED || !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();
		}
		return cacheFilePath;
	}
	/**
	 * @描述: 获取默认的文件保存目录,需要 android.permission.WRITE_EXTERNAL_STORAGE 权限
	 * @作者：  郝炯淞
	 * @创建时间：  2014-6-3 下午4:11:40
	 * @param context
	 * @return
	 * String
	 */
	public static final String getFilesPath(Context context){
		if(filesPath == null || filesPath.length() <= 0){
			filesPath = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED || !isExternalStorageRemovable() ? getExternalFilesDir(context).getPath() : context.getFilesDir().getPath();
		}
		return filesPath;
	}
	
	/**
	 * @描述: Check if external storage is built-in or removable.
	 * @作者：  郝炯淞
	 * @创建时间：  2014-6-3 下午4:04:28
	 * @return True if external storage is removable (like an SD card), false otherwise.
	 */
	@SuppressLint("NewApi")
	public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }
	
	/**
	 * @描述: Get the external app cache directory.
	 * @作者：  郝炯淞
	 * @创建时间：  2014-6-3 下午4:10:49
	 * @param context
	 * @return
	 * File
	 */
    @SuppressLint("NewApi")
    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }
    /**
	 * @描述: Get the external app files directory.
	 * @作者：  郝炯淞
	 * @创建时间：  2014-6-3 下午4:10:49
	 * @param context
	 * @return
	 * File
	 */
    @SuppressLint("NewApi")
    public static File getExternalFilesDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalFilesDir(null);
        }
        final String filesDir = "/Android/data/" + context.getPackageName() + "/files/";
        return new File(Environment.getExternalStorageDirectory().getPath() + filesDir);
    }
    
    /**
     * @描述: Check if OS version has built-in external cache dir method.
     * @作者：  郝炯淞
     * @创建时间：  2014-6-3 下午4:10:25
     * @return
     * boolean
     */
    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }
    
    
    
    
    /**
     * @描述: 判断外部存储设备的文件是否存在,需要 android.permission.WRITE_EXTERNAL_STORAGE 权限
     * @作者：  郝炯淞
     * @创建时间：  2014-6-3 下午5:17:07
     * @param filePath	外部存储的文件路径
     * @return
     * boolean
     */
    public static boolean judgeExternalFile(String filePath){
    	if(filePath == null || filePath.length() <= 0){
    		return false;
    	}
    	File file = new File(filePath);
    	if(!file.exists()){
    		return false;
    	}
    	return true;
    }

	/**
	 * @描述: 判断/data/data/<包名>/files/目录中的fileName文件是否存在
	 * @作者：  郝炯淞
	 * @创建时间：  2014-4-29 下午4:18:05
	 * @param context
	 * @param fileName
	 * @return
	 * boolean
	 */
	public static boolean judgeDataFiles(Context context,String fileName){
		if(fileName == null || fileName.length() <= 0){
    		return false;
    	}
		String[] files = context.fileList();
		for(String str:files){
			if(str.equals(fileName)){
				return true;
			}
		}
		return false;
	}
	/**
	 * @描述: 判断/data/data/<包名>/databases/目录中的数据库文件是否存在
	 * @作者：  郝炯淞
	 * @创建时间：  2014-4-29 下午4:18:05
	 * @param context
	 * @param fileName
	 * @return
	 * boolean
	 */
	public static boolean judgeDataBasesFiles(Context context,String fileName){
		if(fileName == null || fileName.length() <= 0){
    		return false;
    	}
		String[] files = context.databaseList();
		for(String str:files){
			if(str.equals(fileName)){
				return true;
			}
		}
		return false;
	}
	/**
	 * @描述: 判断/data/data/<包名>/cache/目录中的fileName文件是否存在
	 * @作者：  郝炯淞
	 * @创建时间：  2014-4-29 下午4:18:05
	 * @param context
	 * @param fileName
	 * @return
	 * boolean
	 */
	public static boolean judgeDataCache(Context context,String fileName){
		if(fileName == null || fileName.length() <= 0){
    		return false;
    	}
		File cacheFile = context.getCacheDir();
		if(!cacheFile.exists()){
			return false;
		}
		File file = new File(cacheFile.getPath() + File.separator + fileName);
		if(!file.exists()){
			return false;
		}
		return true;
	}
	
	/**
     * @描述: 判断Assets文件是否存在
     * @作者：  郝炯淞
     * @创建时间：  2014-6-3 下午5:17:07
     * @param filePath	外部存储的文件路径
     * @return
     * boolean
     */
    public static boolean judgeAssetsFile(Context context,String filePath){
    	if(filePath == null || filePath.length() <= 0){
    		return false;
    	}
    	try {
			String[] files = context.getAssets().list(filePath);
			if(files != null && files.length > 0){
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return false;
    }
	
	/**
	 * @描述: 删除外部存储设备的文件,需要 android.permission.WRITE_EXTERNAL_STORAGE 权限
	 * @作者：  郝炯淞
	 * @创建时间：  2014-4-30 下午3:55:49
	 * @param filePath
	 * @return
	 * boolean
	 */
	public static boolean deleteExternalFile(String filePath){
		if(filePath == null || filePath.length() <= 0){
			return true;
		}
		try{
			File file = new File(filePath);
			if(file.exists()){
				return file.delete();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @描述: 删除/data/data/<包名>/cache/目录中的fileName文件
	 * @作者：  郝炯淞
	 * @创建时间：  2014-6-3 下午6:09:36
	 * @param context
	 * @param fileName
	 * @return
	 * boolean
	 */
	public static boolean deleteCacheFile(Context context,String fileName){
		if(fileName == null || fileName.length() <= 0){
			return true;
		}
		File cacheFile = context.getCacheDir();
		if(!cacheFile.exists()){
			return true;
		}
		File file = new File(cacheFile.getPath() + File.separator + fileName);
		if(file.exists()){
			return file.delete();
		}
		return false;
	}
	/**
	 * @描述: 删除/data/data/<包名>/files/目录中的fileName文件
	 * @作者：  郝炯淞
	 * @创建时间：  2014-6-3 下午6:09:36
	 * @param context
	 * @param fileName
	 * @return
	 * boolean
	 */
	public static boolean deleteFilesFile(Context context,String fileName){
		if(fileName == null || fileName.length() <= 0){
			return true;
		}
		return context.deleteFile(fileName);
	}
	/**
	 * @描述: 删除/data/data/<包名>/database/目录中的数据库文件
	 * @作者：  郝炯淞
	 * @创建时间：  2014-6-3 下午6:09:36
	 * @param context
	 * @param fileName
	 * @return
	 * boolean
	 */
	public static boolean deleteDataBasesFile(Context context,String fileName){
		if(fileName == null || fileName.length() <= 0){
			return true;
		}
		return context.deleteDatabase(fileName);
	}
	
	/**
	 * @描述: 获取/data/data/<包名>/cache/的fileName文件InputStream
	 * @作者：  郝炯淞
	 * @创建时间：  2014-6-3 下午6:29:11
	 * @param _context
	 * @param _fileName
	 * @return
	 * String
	 */
	public static FileInputStream getInputStreamFromCache(Context _context,String _fileName){
		if(_fileName == null || _fileName.length() <= 0){
			return null;
		}
		FileInputStream is = null;
		try{
			is = new FileInputStream(_context.getCacheDir() + File.separator + _fileName);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return is;
	}
	/**
	 * @描述: 获取/data/data/<包名>/databases/的数据库文件InputStream
	 * @作者：  郝炯淞
	 * @创建时间：  2014-6-3 下午6:29:11
	 * @param _context
	 * @param _fileName
	 * @return
	 * String
	 */
	public static FileInputStream getInputStreamFromDataBases(Context _context,String _fileName){
		if(_fileName == null || _fileName.length() <= 0){
			return null;
		}
		FileInputStream is = null;
		try{
			is = new FileInputStream(_context.getDatabasePath(_fileName));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return is;
	}
	/**
	 * @描述: 获取/data/data/<包名>/cache/的fileName文件String数据
	 * @作者：  郝炯淞
	 * @创建时间：  2014-6-3 下午6:29:11
	 * @param _context
	 * @param _fileName
	 * @return
	 * String
	 */
	public static String getStringFromCache(Context _context,String _fileName){
		if(_fileName == null || _fileName.length() <= 0){
			return null;
		}
		FileInputStream is = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try{
			is = getInputStreamFromCache(_context,_fileName);
			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {
				br.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/**
	 * @描述: 获取/data/data/<包名>/files/的fileName文件String数据
	 * @作者：  郝炯淞
	 * @创建时间：  2014-6-3 下午6:29:11
	 * @param _context
	 * @param _fileName
	 * @return
	 * String
	 */
	public static String getStringFromFiles(Context _context,String _fileName){
		if(_fileName == null || _fileName.length() <= 0){
			return null;
		}
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try{
			br = new BufferedReader(new InputStreamReader(_context.openFileInput(_fileName), "utf-8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/**
	 * @描述: 获取raw资源文件中的数据到字符串
	 * @作者：  Administrator
	 * @创建时间：  2013 上午11:26:35
	 * @param 
	 * @return String 返回json字符串
	 */
	public static String getStringFromRaw(Context _context,int _fileName){
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try{
			br = new BufferedReader(new InputStreamReader(_context.getResources().openRawResource(_fileName), "utf-8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/**
	 * @描述:  获取assets资源文件中的数据到字符串
	 * @作者：  郝炯淞
	 * @创建时间：  2014-6-3 下午6:43:45
	 * @param _context
	 * @param _filePath	assest的文件地址
	 * @return
	 * String
	 */
	public static String getStringFromAssets(Context _context,String _filePath){
		if(_filePath == null || _filePath.length() <= 0){
			return null;
		}
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try{
			br = new BufferedReader(new InputStreamReader(_context.getAssets().open(_filePath, Context.MODE_PRIVATE), "utf-8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/**
	 * @描述:  获取External外部存储目录文件中的InputStream数据
	 * @作者：  郝炯淞
	 * @创建时间：  2014-6-3 下午6:43:45
	 * @param _context
	 * @param _filePath	assest的文件地址
	 * @return
	 * String
	 */
	public static FileInputStream getInputStreamFromExternal(Context _context,String _filePath){
		if(_filePath == null || _filePath.length() <= 0){
			return null;
		}
		File file = new File(_filePath);
		if(!file.exists()){
			return null;
		}
		try{
			return new FileInputStream(file);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @描述:  获取External外部存储目录文件中的数据到字符串
	 * @作者：  郝炯淞
	 * @创建时间：  2014-6-3 下午6:43:45
	 * @param _context
	 * @param _filePath	assest的文件地址
	 * @return
	 * String
	 */
	public static String getStringFromExternal(Context _context,String _filePath){
		if(_filePath == null || _filePath.length() <= 0){
			return null;
		}
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try{
			br = new BufferedReader(new InputStreamReader(getInputStreamFromExternal(_context,_filePath), "utf-8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/**
	 * @描述: 将assets目录中的文件复制到外部存储目录中的指定目录,需要 android.permission.WRITE_EXTERNAL_STORAGE 权限
	 * @作者：  郝炯淞
	 * @创建时间：  2014-5-7 上午11:03:32
	 * @param context
	 * @param apkPath
	 * @param path
	 * @return
	 * boolean
	 */
	public static boolean copyFileFromAssets(Context context, String apkPath, String path) {
		boolean flag = false;
		int BUFFER = 10240;
		InputStream in = null;
		OutputStream out = null;
		byte b[] = null;
		try {
			in = context.getAssets().open(apkPath);
			File file = new File(path);
			if (file.exists()) {
				if (in != null && in.available() == file.length()) {
					flag = true;
				} else{
					file.delete();
				}
			}
			if (!flag) {
				boolean isOK = file.createNewFile();
				if (in != null && isOK) {
					Toast.makeText(context, "正在进行数据操作,请稍等~", Toast.LENGTH_SHORT).show();
					out = new FileOutputStream(path);
					b = new byte[BUFFER];
					int read = 0;
					while ((read = in.read(b)) > 0) {
						out.write(b, 0, read);
					}
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return flag;
	}
	
	/**
	 * @描述: 把数据复制到External外部存储目录的文件中
	 * @作者：  郝炯淞
	 * @创建时间：  2014-4-29 下午4:53:39
	 * @param _context
	 * @param _fileName
	 * @param _jsonData
	 * @return
	 * boolean
	 */
	public static boolean saveToExternal(Context _context,String _filePath,FileInputStream is){
		return saveToExternal(_context,_filePath,is);
	}
	/**
	 * @描述: 把数据复制到External外部存储目录的文件中
	 * @作者：  郝炯淞
	 * @创建时间：  2014-4-29 下午4:53:39
	 * @param _context
	 * @param _fileName
	 * @param _jsonData
	 * @return
	 * boolean
	 */
	public static boolean saveToExternal(Context _context,String _filePath,InputStream is){
		if(is == null || _filePath == null || _filePath.length() <= 0){
			return false;
		}
		boolean isSuccess = false;
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(new File(_filePath));
			byte[] b = new byte[1024*4];
			int read = 0;
			while((read = is.read(b)) > -1){
				os.write(b, 0, read);
			}
		    os.flush();
		    isSuccess = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				os.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return isSuccess;
	}
	
	/**
	 * @描述: 把数据复制到/data/data/<包名>/files/的文件中
	 * @作者：  郝炯淞
	 * @创建时间：  2014-4-29 下午4:53:39
	 * @param _context
	 * @param _fileName
	 * @param _jsonData
	 * @return
	 * boolean
	 */
	public static boolean saveToFiles(Context _context,String _fileName,InputStream is){
		if(is == null){
			return false;
		}
		boolean isSuccess = false;
		FileOutputStream os = null;
		try {
			os = _context.openFileOutput(_fileName, Context.MODE_PRIVATE);
			byte[] b = new byte[1024];
			int read = 0;
			while((read = is.read(b)) > -1){
				os.write(b, 0, read);
			}
		    os.flush();
		    isSuccess = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				os.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return isSuccess;
	}
	
	/**
	 * @描述: 把数据复制到/data/data/<包名>/files/的文件中
	 * @作者：  郝炯淞
	 * @创建时间：  2014-4-29 下午4:53:39
	 * @param _context
	 * @param _fileName
	 * @param _jsonData
	 * @return
	 * boolean
	 */
	public static boolean saveToFiles(Context _context,String _fileName,FileInputStream is){
		return saveToFiles(_context,_fileName,is);
	}
	
	/**
	 * @描述: 把数据库文件复制到/data/data/<包名>/databases/的文件中
	 * @作者：  郝炯淞
	 * @创建时间：  2014-4-29 下午4:53:39
	 * @param _context
	 * @param _fileName
	 * @param _jsonData
	 * @return
	 * boolean
	 */
	public static boolean saveDBToDatabases(Context _context,String _fileName,InputStream is){
		if(is == null){
			return false;
		}
		boolean isSuccess = false;
		FileOutputStream os = null;
		try {
			File dirFile = new File("/data/data/"+_context.getPackageName()+"/databases/");
			if(!dirFile.exists()){
				dirFile.mkdir();
			}
			File file = _context.getDatabasePath(_fileName);
			if(!file.exists()){
				file.createNewFile();
			}
			os = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int read = 0;
			while((read = is.read(b)) > -1){
				os.write(b, 0, read);
			}
		    os.flush();
		    isSuccess = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(os != null){
					os.close();
				}
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return isSuccess;
	}
	
	/**
	 * @描述: 把String数据复制到/data/data/<包名>/files/的文件中
	 * @作者：  郝炯淞
	 * @创建时间：  2014-4-29 下午4:53:39
	 * @param _context
	 * @param _fileName
	 * @param _jsonData
	 * @return
	 * boolean
	 */
	public static boolean saveStringToFiles(Context _context,String _fileName,String _jsonData){
		if(_jsonData == null || _jsonData.trim().length() <= 0){
			return false;
		}
		boolean isSuccess = false;
		FileOutputStream os = null;
		try {
			os = _context.openFileOutput(_fileName, Context.MODE_PRIVATE);
		    os.write(_jsonData.getBytes());
		    os.flush();
		    isSuccess = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return isSuccess;
	}
	
	/**
	 * @描述: 压缩图片到固定尺寸 path:图片的存放路径,需要 android.permission.WRITE_EXTERNAL_STORAGE 权限
	 * @作者：  郝炯淞
	 * @创建时间：  2014-4-30 下午3:57:57
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 * Bitmap
	 */
	public static Bitmap compressImg(String path, int width, int height) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;

			bitmap = BitmapFactory.decodeFile(path, opts);
			// 在内存中创建bitmap对象，这个对象按照缩放大小创建的
			opts.inSampleSize = calculateInSampleSize(opts, width, height);
			opts.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(path, opts);
			return bitmap;
		} catch (Exception e) {
			bitmap = null;
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
	
}
