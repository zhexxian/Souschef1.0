package com.example.zhexian.souschef10;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by zhexian on 1/29/2016.
 */
public class IngredientAmountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_amount_main);
        //String title = "";
        String title = getIntent().getExtras().getString("Title");
        Button b = (Button) findViewById(R.id.button15);
        b.setText(title);

        b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editIngredient();
                return true;
            }
        });


    }

    private void editIngredient() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Edit Ingredient");
        //alert.setMessage("Message");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        Button b = (Button) findViewById(R.id.button15);
        String oldIngredient = b.getText().toString();
        input.append(oldIngredient);
        alert.setView(input);

        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newIngredient = input.getText().toString();
                Button b = (Button) findViewById(R.id.button15);
                b.setText(newIngredient);
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
        //TODO: Create link back to MainActivity
        //TODO: Send data back as an intent to MainActivity, including any changes to name of ingredient, and quantity
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
