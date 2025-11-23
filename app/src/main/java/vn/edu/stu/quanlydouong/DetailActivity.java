package vn.edu.stu.quanlydouong;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import vn.edu.stu.quanlydouong.dao.CategoryDao;
import vn.edu.stu.quanlydouong.dao.ProductDao;
import vn.edu.stu.quanlydouong.model.Category;
import vn.edu.stu.quanlydouong.model.Product;

public class DetailActivity extends AppCompatActivity {
    ImageView imgProduct;
    EditText edtId, edtName, edtPrice, edtDesc;
    Spinner spnCategory;
    Button btnSave, btnExit, btnChooseImage;
    String imagePath = "";
    Product product;
    CategoryDao categoryDao;
    ProductDao productDao;
    List<Category> categoryLists;
    List<String> categoryNameLists;
    List<Product> productLists;
    ArrayAdapter adapter;
    static final int REQUEST_CODE_IMAGE = 123;
    boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detailLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControls();
        addEvents();
        loadCategorySpinner();
        checkEdit();
    }

    private void loadImage(String img) {
        Log.d("LOAD_IMAGE", "imagePath = " + img);
        if (img == null || img.isEmpty()) {
            imgProduct.setImageResource(R.drawable.ic_app_logo);
            return;
        }
        if (img.startsWith("content://") || img.startsWith("file://")) {
            Log.d("LOAD_IMAGE", "-> URI → load from gallery");

            imgProduct.setImageURI(Uri.parse(img));
            return;
        }
        if (img.endsWith(".png")) {
            String fileName = img.replace(".png", "");
            fileName = fileName.toLowerCase();
            Log.d("LOAD_IMAGE", "-> PNG drawable = " + fileName);

            int resId = getResources().getIdentifier(
                    fileName, "drawable", getPackageName()
            );
            Log.d("LOAD_IMAGE", "-> resId = " + resId);
            if (resId != 0)
                imgProduct.setImageResource(resId);
            else
                imgProduct.setImageResource(R.drawable.ic_app_logo);
            return;
        }
        Log.d("LOAD_IMAGE", "-> Unrecognized format");

        imgProduct.setImageResource(R.drawable.ic_app_logo);
    }


    private void checkEdit() {
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            isEdit = true;
            int id = intent.getIntExtra("id", -1);
            product = productDao.getById(id);
            edtId.setText(String.valueOf(product.getId()));
            edtName.setText(product.getName());
            edtPrice.setText(String.valueOf(product.getPrice()));
            edtDesc.setText(product.getDescription());
            imagePath = product.getImage();
            loadImage(imagePath);
            for (int i = 0; i < categoryLists.size(); i++) {
                if (categoryLists.get(i).getId() == product.getCategoryId()) {
                    spnCategory.setSelection(i);
                    break;
                }
            }
        }
    }

    private void loadCategorySpinner() {
        categoryLists = categoryDao.getAll();
        categoryNameLists = new ArrayList<>();
        for (Category c : categoryLists) {
            categoryNameLists.add(c.getName());
        }
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, categoryNameLists);
        spnCategory.setAdapter(adapter);
    }

    private void addEvents() {
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
                adapter.notifyDataSetChanged();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xulySaveProduct();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void xulySaveProduct() {
        String name = edtName.getText().toString();
        double price = Double.parseDouble(edtPrice.getText().toString());
        String desc = edtDesc.getText().toString();
        int categoryId = categoryLists.get(spnCategory.getSelectedItemPosition()).getId();

        if (isEdit) {
            product.setName(name);
            product.setPrice(price);
            product.setDescription(desc);
            product.setCategoryId(categoryId);
            product.setImage(imagePath);
            productDao.updateProduct(product);
            Toast.makeText(this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
        } else {
            Product p = new Product(0, name, categoryId, price, desc, imagePath);
            productDao.insertProduct(p);
            Toast.makeText(this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void addControls() {
        imgProduct = findViewById(R.id.imgProduct);
        edtId = findViewById(R.id.edtId);
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtDesc = findViewById(R.id.edtDesc);
        spnCategory = findViewById(R.id.spCategory);
        btnSave = findViewById(R.id.btnSave);
        btnExit = findViewById(R.id.btnExit);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        categoryDao = new CategoryDao(this);
        productDao = new ProductDao(this);
        categoryLists = categoryDao.getAll();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();
            imagePath = uri.toString();

            Log.d("TEST_URI", "URI = " + imagePath);

            imgProduct.setImageURI(uri);
        }
    }
}