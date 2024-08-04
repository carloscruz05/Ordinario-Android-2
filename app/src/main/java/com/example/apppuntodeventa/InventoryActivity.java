package com.example.apppuntodeventa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class InventoryActivity extends AppCompatActivity {

    private ImageView imageViewRefrescos, imageViewPizzas, imageViewAbarrotes;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        databaseHelper = new DatabaseHelper(this);

        imageViewRefrescos = findViewById(R.id.imageViewRefrescos);
        imageViewPizzas = findViewById(R.id.imageViewPizzas);
        imageViewAbarrotes = findViewById(R.id.imageViewAbarrotes);

        imageViewRefrescos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInventoryOptions("Refrescos");
            }
        });

        imageViewPizzas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInventoryOptions("Pizzas");
            }
        });

        imageViewAbarrotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInventoryOptions("Abarrotes");
            }
        });
    }

    private void showInventoryOptions(final String category) {
        final CharSequence[] options = {"Insertar Producto", "Eliminar Producto", "Actualizar Producto"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(category);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showInsertProductDialog(category);
                        break;
                    case 1:
                        showDeleteProductDialog();
                        break;
                    case 2:
                        showUpdateProductDialog();
                        break;
                }
            }
        });
        builder.show();
    }

    private void showInsertProductDialog(final String category) {
        View view = getLayoutInflater().inflate(R.layout.dialog_insert_product, null);
        final EditText nameEditText = view.findViewById(R.id.name);
        final EditText priceEditText = view.findViewById(R.id.price);
        final EditText quantityEditText = view.findViewById(R.id.quantity);
        final EditText imageEditText = view.findViewById(R.id.image);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insertar Producto");
        builder.setView(view);
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString().trim();
                String price = priceEditText.getText().toString().trim();
                String quantity = quantityEditText.getText().toString().trim();
                String image = imageEditText.getText().toString().trim();

                if (name.isEmpty() || price.isEmpty() || quantity.isEmpty() || image.isEmpty()) {
                    Toast.makeText(InventoryActivity.this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                    db.execSQL("INSERT INTO products (name, price, quantity, image, date) VALUES (?, ?, ?, ?, ?)",
                            new String[]{name, price, quantity, image, "2024-08-04"});
                    Toast.makeText(InventoryActivity.this, "Producto Insertado", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void showDeleteProductDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_delete_product, null);
        final EditText idEditText = view.findViewById(R.id.product_id);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Producto");
        builder.setView(view);
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String productId = idEditText.getText().toString().trim();

                if (productId.isEmpty()) {
                    Toast.makeText(InventoryActivity.this, "Por favor ingrese el ID del producto", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                    db.execSQL("DELETE FROM products WHERE id=?", new String[]{productId});
                    Toast.makeText(InventoryActivity.this, "Producto Eliminado", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void showUpdateProductDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_update_product, null);
        final EditText idEditText = view.findViewById(R.id.product_id);
        final EditText priceEditText = view.findViewById(R.id.price);
        final EditText quantityEditText = view.findViewById(R.id.quantity);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar Producto");
        builder.setView(view);
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String productId = idEditText.getText().toString().trim();
                String price = priceEditText.getText().toString().trim();
                String quantity = quantityEditText.getText().toString().trim();

                if (productId.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
                    Toast.makeText(InventoryActivity.this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                    db.execSQL("UPDATE products SET price=?, quantity=? WHERE id=?", new String[]{price, quantity, productId});
                    Toast.makeText(InventoryActivity.this, "Producto Actualizado", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}
