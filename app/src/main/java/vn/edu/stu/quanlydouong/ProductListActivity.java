package vn.edu.stu.quanlydouong;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import vn.edu.stu.quanlydouong.adapter.ProductAdapter;
import vn.edu.stu.quanlydouong.dao.ProductDao;
import vn.edu.stu.quanlydouong.model.Category;
import vn.edu.stu.quanlydouong.model.Product;

public class ProductListActivity extends AppCompatActivity {
    FloatingActionButton fabAdd;
    ListView lvProduct;
    ProductDao dao;
    ProductAdapter adapter;
    ArrayList<Product> lists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.productId), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dao = new ProductDao(this);
        addControls();
        addEvents();
        loadData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        lists = new ArrayList<>(dao.getAllProduct());
        adapter = new ProductAdapter(this, lists);
        lvProduct.setAdapter(adapter);
    }

    private void addEvents() {
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xulyAddProduct();
            }
        });
        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                xulyEditProduct(position);
            }
        });
        lvProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                return xulyDeleteProduct(position);
            }
        });

    }

    private boolean xulyDeleteProduct(int position) {

        final Product productCanXoa = adapter.getItem(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductListActivity.this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc muốn xóa sản phẩm: " + productCanXoa.getName() + "?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lists.remove(productCanXoa);
                dao.deleteProduct(productCanXoa.getId());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    private void xulyEditProduct(int position) {
        Product p = lists.get(position);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("id", p.getId());
        startActivity(intent);
    }
    private void xulyAddProduct() {
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }

    private void addControls() {
        fabAdd = findViewById(R.id.fabAdd);
        lvProduct = findViewById(R.id.lvProduct);
    }
}