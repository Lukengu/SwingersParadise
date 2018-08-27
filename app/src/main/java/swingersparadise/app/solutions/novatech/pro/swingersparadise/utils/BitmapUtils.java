package swingersparadise.app.solutions.novatech.pro.swingersparadise.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class BitmapUtils {

    public static void saveBitmapToPath(Bitmap bitmap, Context context, String image_path) {

        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        storageDir.mkdirs();
        File file = new File(storageDir, image_path);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
