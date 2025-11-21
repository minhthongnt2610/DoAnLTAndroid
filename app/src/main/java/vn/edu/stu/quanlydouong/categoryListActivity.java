package vn.edu.stu.quanlydouong;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import vn.edu.stu.quanlydouong.adapter.CategoryAdapter;
import vn.edu.stu.quanlydouong.dao.CategoryDao;
import vn.edu.stu.quanlydouong.model.Category;

public class categoryListActivity extends AppCompatActivity {

    EditText edtCatId, edtCatName, edtCatDesc;
    Button btnClear, btnSave;
    ListView lvCategory;
    ArrayList<Category> lists;
    CategoryDao dao;
    CategoryAdapter adapter;
    Category selectedCategory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.categoryList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setSupportActionBar(findViewById(R.id.toolbar));
        addControls();
        dao = new CategoryDao(this);
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
        if (itemId == R.id.mnuProduct) {
            startActivity(new Intent(this, ProductListActivity.class));
            return true;
        } else if (itemId == R.id.mnuInfo) {
            startActivity(new Intent(this, Info_Activity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void loadData() {
        lists = new ArrayList<>(dao.getAll());
        adapter = new CategoryAdapter(this, lists);
        lvCategory.setAdapter(adapter);
    }

    private void addEvents() {
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xulyClear();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xulySave();
            }
        });
        lvCategory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                return xulyXoa(position);
            }
        });
        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positon, long id) {
                selectedCategory = lists.get(positon);

                edtCatId.setText(String.valueOf(selectedCategory.getId()));
                edtCatName.setText(selectedCategory.getName());
                edtCatDesc.setText(selectedCategory.getDescription());
            }
        });
    }


    private boolean xulyXoa(int position) {
        final Category categoryCanXoa = adapter.getItem(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(categoryListActivity.this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc muốn xóa sản phẩm: " + categoryCanXoa.getName() + "?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lists.remove(categoryCanXoa);
                dao.delete(categoryCanXoa.getId());
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

    private void xulySave() {
        String name = edtCatName.getText().toString();
        String desc = edtCatDesc.getText().toString();
        Category category = new Category(name, desc);
        if(name.isEmpty()){
            edtCatName.setError("Vui lòng nhập tên danh mục");
            return;
        }else{
           if(selectedCategory == null){
               Category newCategory = new Category(name, desc);
               dao.insert(newCategory);
               Toast.makeText( categoryListActivity.this, "Thêm danh mục thành công!", Toast.LENGTH_SHORT).show();
           }else{
                selectedCategory.setName(name);
                selectedCategory.setDescription(desc);
                dao.update(selectedCategory);
                Toast.makeText( categoryListActivity.this, "Cập nhật danh mục thành công!", Toast.LENGTH_SHORT).show();
                selectedCategory = null;
              }
                loadData();
                xulyClear();
           }
        }


    private void xulyClear() {
        edtCatId.setText("");
        edtCatName.setText("");
        edtCatDesc.setText("");
    }

    private void addControls() {
        edtCatId = findViewById(R.id.edtCatId);
        edtCatName = findViewById(R.id.edtCatName);
        edtCatDesc = findViewById(R.id.edtCatDesc);
        btnClear = findViewById(R.id.btnClear);
        btnSave = findViewById(R.id.btnSave);
        lvCategory = findViewById(R.id.lvCategory);
    }
}