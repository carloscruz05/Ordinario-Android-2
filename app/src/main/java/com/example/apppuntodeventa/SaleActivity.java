package com.example.apppuntodeventa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SaleActivity extends AppCompatActivity {

    private ListView productListView;
    private Button finishSaleButton;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();

        productListView = findViewById(R.id.productListView);
        finishSaleButton = findViewById(R.id.finishSaleButton);

        loadProducts();

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String productId = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String productPrice = cursor.getString(cursor.getColumnIndexOrThrow("price"));

                showSaleDialog(productId, productName, productPrice);
            }
        });

        finishSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSalesSummary();
            }
        });
    }

    private void loadProducts() {
        Cursor cursor = db.rawQuery("SELECT _id, name, price, image FROM products", null);
        String[] from = {"_id", "name", "price", "image"};
        int[] to = {R.id.productId, R.id.productName, R.id.productPrice, R.id.productImage};

        adapter = new SimpleCursorAdapter(this, R.layout.product_list_item, cursor, from, to, 0);
        productListView.setAdapter(adapter);
    }

    private void showSaleDialog(final String productId, String productName, final String productPrice) {
        View view = getLayoutInflater().inflate(R.layout.dialog_sale_product, null);
        final EditText quantityEditText = view.findViewById(R.id.quantity);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vender " + productName);
        builder.setView(view);
        builder.setPositiveButton("Vender", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String quantityStr = quantityEditText.getText().toString().trim();

                if (quantityStr.isEmpty()) {
                    Toast.makeText(SaleActivity.this, "Por favor ingrese la cantidad", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        double price = Double.parseDouble(productPrice);
                        int quantity = Integer.parseInt(quantityStr);
                        double amount = price * quantity;

                        db.execSQL("INSERT INTO sales (product_id, quantity, price, amount) VALUES (?, ?, ?, ?)",
                                new Object[]{productId, quantityStr, productPrice, amount});
                        Toast.makeText(SaleActivity.this, "Producto Vendido", Toast.LENGTH_SHORT).show();
                    } catch (NumberFormatException e) {
                        Toast.makeText(SaleActivity.this, "Error al procesar la cantidad o el precio", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void showSalesSummary() {
        Cursor cursor = db.rawQuery("SELECT SUM(amount) AS total FROM sales", null);
        if (cursor.moveToFirst()) {
            double total = cursor.getDouble(cursor.getColumnIndex("total"));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Resumen de Venta");
            builder.setMessage("Total: $" + total);
            builder.setPositiveButton("Aceptar", null);
            builder.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Resumen de Venta");
            builder.setMessage("No hay ventas registradas.");
            builder.setPositiveButton("Aceptar", null);
            builder.show();
        }
        cursor.close();
    }
}
