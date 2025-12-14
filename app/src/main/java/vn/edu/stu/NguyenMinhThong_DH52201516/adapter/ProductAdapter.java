package vn.edu.stu.NguyenMinhThong_DH52201516.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import vn.edu.stu.NguyenMinhThong_DH52201516.R;
import vn.edu.stu.NguyenMinhThong_DH52201516.dao.CategoryDao;
import vn.edu.stu.NguyenMinhThong_DH52201516.model.Category;
import vn.edu.stu.NguyenMinhThong_DH52201516.model.Product;

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
        TextView txtId, txtName, txtCategory, txtDesc;
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
            holder.txtDesc = convertView.findViewById(R.id.txtDesc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Product p = list.get(position);
        holder.txtId.setText(context.getString(R.string.product_id) + " " + String.format("SP%03d", p.getId()));
        holder.txtName.setText(context.getString(R.string.product_name) + " " + p.getName());
        holder.txtDesc.setText(context.getString(R.string.product_desc) + " " + p.getDescription());

        Category cate = categoryDao.getById(p.getCategoryId());
        if (cate != null)
            holder.txtCategory.setText(context.getString(R.string.product_category) + " " + cate.getName());
        else
            holder.txtCategory.setText(context.getString(R.string.product_category) + " " + context.getString(R.string.unknown));
        byte[] imgBlob = p.getImage();
        if (imgBlob != null && imgBlob.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgBlob, 0, imgBlob.length);
            holder.imgProduct.setImageBitmap(bitmap);
        } else {
            holder.imgProduct.setImageResource(R.drawable.ic_app_logo);
        }
        return convertView;
    }
}
