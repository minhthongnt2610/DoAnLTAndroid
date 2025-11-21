package vn.edu.stu.quanlydouong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import vn.edu.stu.quanlydouong.R;
import vn.edu.stu.quanlydouong.model.Category;

import java.util.List;

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

        holder.txtCatId.setText("ID: " + c.getId());
        holder.txtCatName.setText("Name: " + c.getName());
        holder.txtCatDesc.setText("Desc: " + c.getDescription());

        return convertView;
    }
}
