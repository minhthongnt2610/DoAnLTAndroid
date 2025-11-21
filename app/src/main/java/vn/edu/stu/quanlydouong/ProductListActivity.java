package vn.edu.stu.quanlydouong;

import android.os.Bundle;
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

    private void loadData() {
        lists = new ArrayList<>(dao.getAll());
        adapter = new ProductAdapter(this, lists);
        lvProduct.setAdapter(adapter);
    }

    private void addEvents() {
    }

    private void addControls() {
        fabAdd = findViewById(R.id.fabAdd);
        lvProduct = findViewById(R.id.lvProduct);
    }
}