package vn.edu.stu.NguyenMinhThong_DH52201516.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import vn.edu.stu.NguyenMinhThong_DH52201516.R;
import vn.edu.stu.NguyenMinhThong_DH52201516.model.Category;

public class CategoryAdapter extends ArrayAdapter<Category> {

    Context context;
    List<Category> list;

    public CategoryAdapter(Context context, List<Category> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        TextView txtCatId, txtCatName, txtCatDesc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_category, parent, false);

            holder = new ViewHolder();
            holder.txtCatId = convertView.findViewById(R.id.txtCatId);
            holder.txtCatName = convertView.findViewById(R.id.txtCatName);
            holder.txtCatDesc = convertView.findViewById(R.id.txtCatDesc);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Category c = list.get(position);

        holder.txtCatId.setText(context.getString(R.string.type_id) + " " + String.format("TL%03d", c.getId()));
        holder.txtCatName.setText(context.getString(R.string.type_name) + " " + c.getName());
        holder.txtCatDesc.setText(context.getString(R.string.type_description) + " " + c.getDescription());

        return convertView;
    }
}
