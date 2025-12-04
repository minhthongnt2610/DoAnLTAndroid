package vn.edu.stu.quanlydouong.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import vn.edu.stu.quanlydouong.model.Category;
import vn.edu.stu.quanlydouong.sqlite.SQLiteHelper;

public class CategoryDao {
    Context context;

    public CategoryDao(Context context) {
        this.context = context;
    }

    public List<Category> getAll() {
        List<Category> lists = new ArrayList<>();
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase sql = helper.getReadableDatabase();
        Cursor cs = sql.rawQuery("SELECT * FROM Category ORDER BY id ASC", null);

        while (cs.moveToNext()) {
            int id = cs.getInt(0);
            String name = cs.getString(1);
            String description = cs.getString(2);
            Category category = new Category(id, name, description);
            lists.add(category);
        }
        cs.close();
        sql.close();
        return lists;
    }

    public void insert(Category category) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase sql = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        values.put("description", category.getDescription());
        sql.insert("Category", null, values);
        sql.close();
    }

    public void update(Category category) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase sql = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        values.put("description", category.getDescription());
        sql.update("Category", values, "id=?", new String[]{String.valueOf(category.getId())});
        sql.close();
    }

    public boolean delete(int id) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            // Bật ràng buộc khóa ngoại
            db.execSQL("PRAGMA foreign_keys = ON;");

            // Thực hiện xoá
            int rows = db.delete("Category", "id=?", new String[]{String.valueOf(id)});

            db.close();

            // Nếu rows > 0 nghĩa là xoá thành công
            return rows > 0;

        } catch (SQLiteConstraintException e) {
            // Bắt lỗi khi category có sản phẩm
            if (e.getMessage() != null && e.getMessage().contains("FOREIGN KEY constraint failed")) {
                Log.e("DB", "Không thể xóa category vì đang có sản phẩm");
            } else {
                Log.e("DB", "Lỗi khi xóa: " + e.getMessage());
            }

            db.close();
            return false;
        }
    }


    public Category getById(int categoryId) {
        Category category = null;
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase sql = helper.getReadableDatabase();
        Cursor cs = sql.rawQuery("SELECT * FROM Category WHERE id = ?", new String[]{String.valueOf(categoryId)});
        if (cs.moveToFirst()) {
            int id = cs.getInt(0);
            String name = cs.getString(1);
            String description = cs.getString(2);
            category = new Category(id, name, description);
        }
        cs.close();
        sql.close();
        return category;
    }
}
