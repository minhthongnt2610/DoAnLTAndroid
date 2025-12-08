package vn.edu.stu.NguyenMinhThong_DH52201516;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import vn.edu.stu.NguyenMinhThong_DH52201516.adapter.ProductAdapter;
import vn.edu.stu.NguyenMinhThong_DH52201516.dao.ProductDao;
import vn.edu.stu.NguyenMinhThong_DH52201516.model.Product;

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
        setSupportActionBar(findViewById(R.id.toolbar));
        dao = new ProductDao(this);
        addControls();
        addEvents();
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.mnuCategory) {
            startActivity(new Intent(this, categoryListActivity.class));
            return true;
        } else if (itemId == R.id.mnuInfo) {
            startActivity(new Intent(this, Info_Activity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        builder.setTitle(getString(R.string.confirm_delete));
        builder.setMessage(getString(R.string.delete_question) + " " + productCanXoa.getName() + "?");
        builder.setPositiveButton(getString(R.string.delete), (dialog, which) -> {
            lists.remove(productCanXoa);
            dao.deleteProduct(productCanXoa.getId());
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        });
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.create().show();
        return true;
    }

    private void xulyEditProduct(int position) {
        Product p = lists.get(position);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("id", p.getId());
        startActivity(intent);
    }

    private void xulyAddProduct() {
        startActivity(new Intent(this, DetailActivity.class));
    }

    private void addControls() {
        fabAdd = findViewById(R.id.fabAdd);
        lvProduct = findViewById(R.id.lvProduct);
    }
}
