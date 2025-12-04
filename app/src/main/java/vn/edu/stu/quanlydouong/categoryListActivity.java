package vn.edu.stu.quanlydouong;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        lists = new ArrayList<>(dao.getAll());
        adapter = new CategoryAdapter(this, lists);
        lvCategory.setAdapter(adapter);
    }

    private void addEvents() {
        btnClear.setOnClickListener(v -> xulyClear());

        btnSave.setOnClickListener(v -> xulySave());

        lvCategory.setOnItemLongClickListener((adapterView, view, position, id) -> xulyXoa(position));

        lvCategory.setOnItemClickListener((adapterView, view, position, id) -> {
            selectedCategory = lists.get(position);

            edtCatId.setText(String.valueOf(selectedCategory.getId()));
            edtCatName.setText(selectedCategory.getName());
            edtCatDesc.setText(selectedCategory.getDescription());
        });
    }

    private boolean xulyXoa(int position) {
        final Category categoryCanXoa = adapter.getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(categoryListActivity.this);
        builder.setTitle(getString(R.string.confirm_delete));
        builder.setMessage(getString(R.string.delete_question) + " " + categoryCanXoa.getName() + "?");

        builder.setPositiveButton(getString(R.string.delete), (dialog, which) -> {

            boolean ketQua = dao.delete(categoryCanXoa.getId());

            if (ketQua) {
                lists.remove(categoryCanXoa);
                adapter.notifyDataSetChanged();

                Toast.makeText(categoryListActivity.this,
                        getString(R.string.delete_success),
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(categoryListActivity.this,
                        getString(R.string.delete_fail_has_products),
                        Toast.LENGTH_LONG).show();
            }

            dialog.dismiss();
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());

        builder.create().show();
        return true;
    }


    private void xulySave() {
        String name = edtCatName.getText().toString();
        String desc = edtCatDesc.getText().toString();

        if (name.isEmpty()) {
            edtCatName.setError(getString(R.string.error_empty_name));
            return;
        }

        if (selectedCategory == null) {
            Category newCategory = new Category(name, desc);
            dao.insert(newCategory);
            Toast.makeText(this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
        } else {
            selectedCategory.setName(name);
            selectedCategory.setDescription(desc);
            dao.update(selectedCategory);
            Toast.makeText(this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
            selectedCategory = null;
        }

        loadData();
        xulyClear();
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
