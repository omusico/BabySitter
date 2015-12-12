package com.ohad.babysitter.utility;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class RealPathUtil {

	@SuppressLint("NewApi")
	public static String getRealPathFromURI_API19(Context context, Uri uri){
		    // DocumentProvider
		    if (DocumentsContract.isDocumentUri(context, uri)) {
		        // ExternalStorageProvider
		        if (isExternalStorageDocument(uri)) {
		            final String docId = DocumentsContract.getDocumentId(uri);
		            final String[] split = docId.split(":");
		            final String type = split[0];

		            if ("primary".equalsIgnoreCase(type)) {
		                return Environment.getExternalStorageDirectory() + "/"
		                        + split[1];
		            }

		            // TODO handle non-primary volumes
		        }
		        // DownloadsProvider
		        else if (isDownloadsDocument(uri)) {

		            final String id = DocumentsContract.getDocumentId(uri);
		            final Uri contentUri = ContentUris.withAppendedId(
		                    Uri.parse("content://downloads/public_downloads"),
		                    Long.valueOf(id));

		            return getDataColumn(context, contentUri, null, null);
		        }
		        // MediaProvider
		        else if (isMediaDocument(uri)) {
		            final String docId = DocumentsContract.getDocumentId(uri);
		            final String[] split = docId.split(":");
		            final String type = split[0];

		            Uri contentUri = null;
		            if ("image".equals(type)) {
		                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		            } else if ("video".equals(type)) {
		                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		            } else if ("audio".equals(type)) {
		                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		            }

		            final String selection = "_id=?";
		            final String[] selectionArgs = new String[] { split[1] };

		            return getDataColumn(context, contentUri, selection,
		                    selectionArgs);
		        }
		    }
		    // MediaStore (and general)
		    else if ("content".equalsIgnoreCase(uri.getScheme())) {
		        return getDataColumn(context, uri, null, null);
		    }
		    // File
		    else if ("file".equalsIgnoreCase(uri.getScheme())) {
		        return uri.getPath();
		    }
		    return null;

		}


	@SuppressLint("NewApi")
	public static String getRealPathFromURI_API11to18(Context context, Uri uri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		String result = null;

		CursorLoader cursorLoader = new CursorLoader(
				context, 
				uri, proj, null, null, null);        
		Cursor cursor = cursorLoader.loadInBackground();

		if(cursor != null){
			int column_index = 
					cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			result = cursor.getString(column_index);
		}
		return result;  
	}

	
	public static String getRealPathFromURI_BelowAPI11(Context context, Uri uri){
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
		assert cursor != null;
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String ans = cursor.getString(column_index);
		cursor.close();
		return ans;
	}


	public static String getRealPathFromURI(Context context, Uri uri){
		String path;

		if(Build.VERSION.SDK_INT > 18)
			path = getRealPathFromURI_API19(context, uri);
		else if(Build.VERSION.SDK_INT > 10)
			path = getRealPathFromURI_API11to18(context, uri);
		else
			path = getRealPathFromURI_BelowAPI11(context, uri);

		if (path == null) {
			path = getRealPathFromURI2(context, uri);
		}

		if (path == null){
			path = getRealPathFromSdPathUri(context, uri);
		}

		return path;
	}
	
	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The mContext.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
	        String selection, String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = { column };

	    try {
	        cursor = context.getContentResolver().query(uri, projection,
	                selection, selectionArgs, null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int column_index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(column_index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri
	            .getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri
	            .getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri
	            .getAuthority());
	}

	private static String getRealPathFromURI2(Context c, Uri contentUri) {

		Cursor cursor = null;
		try {
			String[] projection = { MediaStore.Images.Media.DATA };
			cursor = c.getContentResolver().query(contentUri, projection, null, null, null);
			assert cursor != null;
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		catch (Exception e) {
			return null;
		}
		finally {
			if (cursor != null) {
				cursor.close();
			}
		}

	}

	public static String getRealPathFromSdPathUri(Context c, Uri contentUri) {
		try {
			// Will return "image:x*"
			String wholeID = DocumentsContract.getDocumentId(contentUri);

			// Split at colon, use second item in the array
			String id = wholeID.split(":")[1];

			// which information to take from the image
			String[] column = { MediaStore.Images.Media.DATA };

			// where id is equal to
			String sel = MediaStore.Images.Media._ID + "=?";

			Cursor cursor = c.getContentResolver().
					query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							column, sel, new String[]{ id }, null);

			String filePath = "";

			assert cursor != null;
			int columnIndex = cursor.getColumnIndex(column[0]);

			if (cursor.moveToFirst()) {
				filePath = cursor.getString(columnIndex);
			}

			cursor.close();
			return filePath;
		} catch (Exception e) {
			return null;
		}
	}


}