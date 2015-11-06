package com.cooee.hybridlocker;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
	public static void copyAssetDirToFiles(Context remoteContext,
			Context context, String dirname) throws IOException {
		File dir = new File(remoteContext.getFilesDir() + "/" + dirname);
		dir.mkdir();

		AssetManager assetManager = context.getAssets();
		String[] children = assetManager.list(dirname);
		for (String child : children) {
			child = dirname + '/' + child;
			String[] grandChildren = assetManager.list(child);
			if (0 == grandChildren.length)
				copyAssetFileToFiles(remoteContext, context, child);
			else
				copyAssetDirToFiles(remoteContext, context, child);
		}
	}

	public static void copyAssetFileToFiles(Context remoteContext,
			Context context, String filename) throws IOException {
		InputStream is = context.getAssets().open(filename);
		byte[] buffer = new byte[is.available()];
		is.read(buffer);
		is.close();

		File of = new File(remoteContext.getFilesDir() + "/" + filename);
		of.createNewFile();
		FileOutputStream os = new FileOutputStream(of);
		os.write(buffer);
		os.close();
	}
}
