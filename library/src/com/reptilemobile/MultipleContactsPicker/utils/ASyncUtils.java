package com.reptilemobile.MultipleContactsPicker.utils;

import android.os.AsyncTask;
import android.os.Build;

/**
 * Author Konrad Gadzinowski
 * kgadzinowski@gmail.com
 */
public class ASyncUtils {

	public static void startMyTask(AsyncTask asyncTask, Object... params) {
	      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
	          asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
	      else
	          asyncTask.execute(params);
	  }
	
}
