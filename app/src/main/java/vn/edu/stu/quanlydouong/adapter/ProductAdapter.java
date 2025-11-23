package vn.edu.stu.quanlydouong.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import vn.edu.stu.quanlydouong.R;
import vn.edu.stu.quanlydouong.dao.CategoryDao;
import vn.edu.stu.quanlydouong.model.Category;
import vn.edu.stu.quanlydouong.model.Product;

public class ProductAdapter extends ArrayAdapter<Product> {

    private Context context;
    private List<Product> list;
    private CategoryDao categoryDao;

    public ProductAdapter(@NonNull Context context, @NonNull List<Product> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
        this.categoryDao = new CategoryDao(context);
    }

    private static class ViewHolder {
        ImageView imgProduct;
        TextView txtId, txtName, txtCategory;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_product, parent, false);

            holder = new ViewHolder();
            holder.imgProduct = convertView.findViewById(R.id.imgProduct);
            holder.txtId = convertView.findViewById(R.id.txtProductId);
            holder.txtName = convertView.findViewById(R.id.txtProductName);
            holder.txtCategory = convertView.findViewById(R.id.txtProductCategory);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product p = list.get(position);

        holder.txtId.setText("Id: " + p.getId());
        holder.txtName.setText("Name: " + p.getName());

        Category cate = categoryDao.getById(p.getCategoryId());
        if (cate != null)
            holder.txtCategory.setText("Category: " + cate.getName());
        else
            holder.txtCategory.setText("Category: Unknown");

        String img = p.getImage();

        if (img == null || img.isEmpty()) {
            holder.imgProduct.setImageResource(R.drawable.ic_app_logo);
        }
        else if (img.startsWith("content://") || img.startsWith("file://")) {
            try {
                holder.imgProduct.setImageURI(Uri.parse(img));
            } catch (Exception e) {
                holder.imgProduct.setImageResource(R.drawable.ic_app_logo);
            }
        }
        else if (img.endsWith(".png")) {
            String fileName = img.replace(".png", "");
            int resId = context.getResources()
                    .getIdentifier(fileName, "drawable", context.getPackageName());

            if (resId != 0)
                holder.imgProduct.setImageResource(resId);
            else
                holder.imgProduct.setImageResource(R.drawable.ic_app_logo);
        }
        else {
            holder.imgProduct.setImageResource(R.drawable.ic_app_logo);
        }

        return convertView;
    }
}
