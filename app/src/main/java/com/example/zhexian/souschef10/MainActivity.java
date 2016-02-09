package com.example.zhexian.souschef10;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String fileName = "Ingredients.txt";

        ArrayList<String> ingList = getIngredientsList(fileName);

        ingList.toString();
        if(ingList.size()<1){
            setIngredientList();
        }

        // Initializing main screen
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Spinnaker-Regular.ttf");
        Button buttonText = (Button)findViewById(R.id.button);
        buttonText.setTypeface(myTypeface);
        Button buttonText1 = (Button)findViewById(R.id.button1);
        buttonText1.setTypeface(myTypeface);
        Button buttonText2 = (Button)findViewById(R.id.button2);
        buttonText2.setTypeface(myTypeface);
        Button buttonText3 = (Button)findViewById(R.id.button3);
        buttonText3.setTypeface(myTypeface);
        Button buttonText4 = (Button)findViewById(R.id.button4);
        buttonText4.setTypeface(myTypeface);
        Button buttonText5 = (Button)findViewById(R.id.button5);
        buttonText5.setTypeface(myTypeface);
        Button buttonText6 = (Button)findViewById(R.id.button6);
        buttonText6.setTypeface(myTypeface);
        Button buttonText7 = (Button)findViewById(R.id.button7);
        buttonText7.setTypeface(myTypeface);
        Button buttonText8 = (Button)findViewById(R.id.button8);
        buttonText8.setTypeface(myTypeface);
        Button buttonText9 = (Button)findViewById(R.id.button9);
        buttonText9.setTypeface(myTypeface);
        Button buttonText10 = (Button)findViewById(R.id.button10);
        buttonText10.setTypeface(myTypeface);
        Button buttonText11 = (Button)findViewById(R.id.button11);
        buttonText11.setTypeface(myTypeface);


        buttonText.setText(ingList.get(0));
        buttonText1.setText(ingList.get(1));
        buttonText2.setText(ingList.get(2));
        buttonText3.setText(ingList.get(3));
        buttonText4.setText(ingList.get(4));
        buttonText5.setText(ingList.get(5));
        buttonText6.setText(ingList.get(6));
        buttonText7.setText(ingList.get(7));
        buttonText8.setText(ingList.get(8));
        buttonText9.setText(ingList.get(9));
        buttonText10.setText(ingList.get(10));
        buttonText11.setText(ingList.get(11));

        Button buttonText12 = (Button)findViewById(R.id.button12);
        buttonText12.setTypeface(myTypeface);
        Button buttonText13 = (Button)findViewById(R.id.button13);
        buttonText13.setTypeface(myTypeface);

        TextView weightText = (TextView) findViewById(R.id.textView2);
        weightText.setTypeface(myTypeface);

        buttonText.setOnClickListener(this);
        buttonText1.setOnClickListener(this);
        buttonText2.setOnClickListener(this);
        buttonText3.setOnClickListener(this);
        buttonText4.setOnClickListener(this);
        buttonText5.setOnClickListener(this);
        buttonText6.setOnClickListener(this);
        buttonText7.setOnClickListener(this);
        buttonText8.setOnClickListener(this);
        buttonText9.setOnClickListener(this);
        buttonText10.setOnClickListener(this);
        buttonText11.setOnClickListener(this);

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



    public void onClick(View v){
        Button buttonText = (Button)findViewById(R.id.button);
        Button buttonText1 = (Button)findViewById(R.id.button1);
        Button buttonText2 = (Button)findViewById(R.id.button2);
        Button buttonText3 = (Button)findViewById(R.id.button3);
        Button buttonText4 = (Button)findViewById(R.id.button4);
        Button buttonText5 = (Button)findViewById(R.id.button5);
        Button buttonText6 = (Button)findViewById(R.id.button6);
        Button buttonText7 = (Button)findViewById(R.id.button7);
        Button buttonText8 = (Button)findViewById(R.id.button8);
        Button buttonText9 = (Button)findViewById(R.id.button9);
        Button buttonText10 = (Button)findViewById(R.id.button10);
        Button buttonText11 = (Button)findViewById(R.id.button11);

        switch(v.getId())
        {   //TODO: add numbering system to transfer to IAA

            case R.id.button:
                Intent intent = new Intent(this, IngredientAmountActivity.class);
                String thisIsString = buttonText.getText().toString();
                intent.putExtra("Title",thisIsString);
                this.startActivity(intent);
                break;
            case R.id.button1:
                Intent intent1 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString1 = buttonText1.getText().toString();
                intent1.putExtra("Title",thisIsString1);
                this.startActivity(intent1);
                break;
            case R.id.button2:
                Intent intent2 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString2 = buttonText2.getText().toString();
                intent2.putExtra("Title",thisIsString2);
                this.startActivity(intent2);
                break;
            case R.id.button3:
                Intent intent3 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString3 = buttonText3.getText().toString();
                intent3.putExtra("Title",thisIsString3);
                this.startActivity(intent3);
                break;
            case R.id.button4:
                Intent intent4 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString4 = buttonText4.getText().toString();
                intent4.putExtra("Title",thisIsString4);
                this.startActivity(intent4);
                break;
            case R.id.button5:
                Intent intent5 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString5 = buttonText5.getText().toString();
                intent5.putExtra("Title",thisIsString5);
                this.startActivity(intent5);
                break;
            case R.id.button6:
                Intent intent6 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString6 = buttonText6.getText().toString();
                intent6.putExtra("Title",thisIsString6);
                this.startActivity(intent6);
                break;
            case R.id.button7:
                Intent intent7 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString7 = buttonText7.getText().toString();
                intent7.putExtra("Title",thisIsString7);
                this.startActivity(intent7);
                break;
            case R.id.button8:
                Intent intent8 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString8 = buttonText8.getText().toString();
                intent8.putExtra("Title",thisIsString8);
                this.startActivity(intent8);
                break;
            case R.id.button9:
                Intent intent9 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString9 = buttonText9.getText().toString();
                intent9.putExtra("Title",thisIsString9);
                this.startActivity(intent9);
                break;
            case R.id.button10:
                Intent intent10 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString10 = buttonText10.getText().toString();
                intent10.putExtra("Title",thisIsString10);
                this.startActivity(intent10);
                break;
            case R.id.button11:
                Intent intent11 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString11 = buttonText11.getText().toString();
                intent11.putExtra("Title",thisIsString11);
                this.startActivity(intent11);
                break;
            }

    }

    public ArrayList<String> getIngredientsList(String fileName){
        //TODO: USE FILEOUTPUTSTREAM!
        //TODO: Set save data location on android
        String line;
        ArrayList<String> returnList = new ArrayList<String>();
        try {

            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File (sdCard.getAbsolutePath() + "/SousChef/Data");
            File file = new File(dir, fileName);
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(file);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                returnList.add(line);
            }

            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            Toast.makeText(MainActivity.this,  "Unable to open file '" +fileName + "'"+"Creating new Ingredient List instead.", Toast.LENGTH_SHORT).show();

        }
        catch(IOException ex) {
            Toast.makeText(MainActivity.this,"Error reading file '"+ fileName + "'",Toast.LENGTH_SHORT).show();
            // Or we could just do this:
            // ex.printStackTrace();
        }
        return returnList;
    }

    public void setIngredientList(){
        String fileName = "Ingredients.txt";

        try {

            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File (sdCard.getAbsolutePath() + "/SousChef/Data");
            dir.mkdirs();
            File file = new File(dir, fileName);
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter(file);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            bufferedWriter.write("Salt");
            bufferedWriter.newLine();
            bufferedWriter.write("Pepper");
            bufferedWriter.newLine();
            bufferedWriter.write("Light Sauce");
            bufferedWriter.newLine();
            bufferedWriter.write("Dark Sauce");
            bufferedWriter.newLine();
            bufferedWriter.write("Salt");
            bufferedWriter.newLine();
            bufferedWriter.write("Pepper");
            bufferedWriter.newLine();
            bufferedWriter.write("Light Sauce");
            bufferedWriter.newLine();
            bufferedWriter.write("Dark Sauce");
            bufferedWriter.newLine();
            bufferedWriter.write("Salt");
            bufferedWriter.newLine();
            bufferedWriter.write("Pepper");
            bufferedWriter.newLine();
            bufferedWriter.write("Light Sauce");
            bufferedWriter.newLine();
            bufferedWriter.write("Dark Sauce");
            bufferedWriter.newLine();


            // Always close files.
            bufferedWriter.close();
        }
        catch(IOException ex) {
            Toast.makeText(MainActivity.this, "Error writing to file '" + fileName + "'",Toast.LENGTH_SHORT).show();
            // Or we could just do this:
            // ex.printStackTrace();
        }

    }

}
