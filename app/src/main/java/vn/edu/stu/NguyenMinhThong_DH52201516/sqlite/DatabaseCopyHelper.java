package vn.edu.stu.NguyenMinhThong_DH52201516.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseCopyHelper {

    private static final String DB_NAME = "QUANLYDOUONG.db";
    private Context context;
    private String dbPath;

    public DatabaseCopyHelper(Context context) {
        this.context = context;
        this.dbPath = context.getDatabasePath(DB_NAME).getPath();
    }

    public void checkAndCopyDatabase() {
        SQLiteDatabase checkDB = null;

        try {
            checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception ignored) {
        }

        if (checkDB != null) {
            checkDB.close();
        } else {
            copyDatabaseFromAssets();
        }
    }

    private void copyDatabaseFromAssets() {
        try {
            InputStream inputStream = context.getAssets().open(DB_NAME);
            OutputStream outputStream = new FileOutputStream(dbPath);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
