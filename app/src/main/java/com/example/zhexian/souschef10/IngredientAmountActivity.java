package com.example.zhexian.souschef10;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DigitsKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by zhexian on 1/29/2016.
 */
public class IngredientAmountActivity extends AppCompatActivity implements View.OnClickListener{
    String title;
    int indexIng;
    String measurement = "";
    int[] quant=new int[3];
    int[] oldQuant = new int[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_amount_main);
        title = getIntent().getExtras().getString("Title");
        indexIng = getIntent().getExtras().getInt("Index");
        oldQuant = getIntent().getIntArrayExtra("Integers");
        Button b = (Button) findViewById(R.id.button15);
        b.setText(title);

        b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editIngredient();
                return true;
            }
        });
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Spinnaker-Regular.ttf");
        Button acceptButton = (Button)findViewById(R.id.button20);
        Button cancelButton = (Button)findViewById(R.id.button21);
        Button teaSpoon = (Button) findViewById(R.id.button16);
        Button halfTableSpoon = (Button) findViewById(R.id.button17);
        Button tableSpoon = (Button) findViewById(R.id.button18);


        acceptButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        teaSpoon.setOnClickListener(this);
        halfTableSpoon.setOnClickListener(this);
        tableSpoon.setOnClickListener(this);


        acceptButton.setTypeface(myTypeface);
        cancelButton.setTypeface(myTypeface);
        teaSpoon.setTypeface(myTypeface);
        halfTableSpoon.setTypeface(myTypeface);
        tableSpoon.setTypeface(myTypeface);


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
    @Override
    public void onClick(View v){
        System.out.println("running");
        Button ingName = (Button) findViewById(R.id.button15);
        title = ingName.getText().toString();
        System.out.println(title);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Quantity Selection");
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setKeyListener(DigitsKeyListener.getInstance());
        switch(v.getId())
        {
            case R.id.button21:
                title = ingName.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("Title", title);
                intent.putExtra("Measurement", measurement);
                intent.putExtra("Quant",quant);
                setResult(IngredientAmountActivity.RESULT_OK, intent);
                finish();
                break;
            case R.id.button20:
                Intent intent1 = new Intent();
                setResult(IngredientAmountActivity.RESULT_CANCELED,intent1);
                finish();
                break;


            case R.id.button16:
                input.append("0");
                alert.setView(input);

                alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String newQuantity = input.getText().toString();
                        Button teaSpoon = (Button) findViewById(R.id.button16);
                        quant[0] = Integer.parseInt(newQuantity);
                        measurement = "Xtsp";
                        teaSpoon.setText(quant[0] + "xteaspoon");
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
                break;

            case R.id.button17:
                input.append("0");
                alert.setView(input);

                alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String newQuantity = input.getText().toString();
                        Button teaSpoon = (Button) findViewById(R.id.button17);
                        quant[1] = Integer.parseInt(newQuantity);
                        measurement = "X1/2tbsp";
                        teaSpoon.setText(quant[1] + "x1/2 tbspoon");
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
                break;

            case R.id.button18:
                input.append("0");
                alert.setView(input);
                alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String newQuantity = input.getText().toString();
                        Button teaSpoon = (Button) findViewById(R.id.button18);
                        quant[2] = Integer.parseInt(newQuantity);
                        measurement = "Xtbsp";
                        teaSpoon.setText(quant[2] + "xTbspoon");
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
                break;
        }


    }
}
