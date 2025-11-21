package vn.edu.stu.quanlydouong.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.edu.stu.quanlydouong.model.Product;
import vn.edu.stu.quanlydouong.sqlite.SQLiteHelper;

public class ProductDao {
    Context context;

    public ProductDao(Context context) {
        this.context = context;
    }

    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cs = db.rawQuery("SELECT * FROM Product ORDER BY id DESC", null);
        while (cs.moveToNext()) {
            int id = cs.getInt(0);
            String name = cs.getString(1);
            int cateId = cs.getInt(2);
            double price = cs.getDouble(3);
            String desc = cs.getString(4);
            String image = cs.getString(5);
            list.add(new Product(id, name, cateId, price, desc, image));
        }
        cs.close();
        db.close();
        return list;
    }

    public Product getById(int id) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cs = db.rawQuery("SELECT * FROM Product WHERE id=?", new String[]{id + ""});
        if (cs.moveToFirst()) {
            Product p = new Product(
                    cs.getInt(0),
                    cs.getString(1),
                    cs.getInt(2),
                    cs.getDouble(3),
                    cs.getString(4),
                    cs.getString(5)
            );
            cs.close();
            db.close();
            return p;
        }
        cs.close();
        db.close();
        return null;
    }


    public void insert(Product product) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase sql = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("description", product.getDescription());
        values.put("price", product.getPrice());
        values.put("categoryId", product.getCategoryId());
        values.put("image", product.getImage());
        sql.insert("Product", null, values);
        sql.close();
    }

    public void update(Product product) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase sql = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("description", product.getDescription());
        values.put("price", product.getPrice());
        values.put("categoryId", product.getCategoryId());
        values.put("image", product.getImage());
        sql.update("Product", values, "id = ?", new String[]{String.valueOf(product.getId())});
        sql.close();
    }

    public void delete(int productId) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase sql = helper.getReadableDatabase();
        sql.delete("Product", "id = ?", new String[]{String.valueOf(productId)});
        sql.close();
    }
}
