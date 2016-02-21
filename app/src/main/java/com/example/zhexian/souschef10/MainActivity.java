package com.example.zhexian.souschef10;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    boolean firstpass = true;
    ArrayList<String> ingList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String fileName = "Ingredients.txt";

        ingList = getIngredientsList(fileName);

        //TODO: enable a change in ingredients
        //TODO: implement icons arraylist to be sent to arduino
        //TODO: use the ingredients list instead to update names
        //ingList.toString();
        if(ingList.size()<1){
            setIngredientList();
            ingList = getIngredientsList(fileName);
        }
        System.out.println(ingList.toString());
        // Initializing main screen
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Spinnaker-Regular.ttf");

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

        Button undoAllButton = (Button)findViewById(R.id.button12);

        TextView quantity1 = (TextView)findViewById(R.id.quantity1);
        TextView quantity2 = (TextView)findViewById(R.id.quantity2);
        TextView quantity3 = (TextView)findViewById(R.id.quantity3);
        TextView quantity4 = (TextView)findViewById(R.id.quantity4);
        TextView quantity5 = (TextView)findViewById(R.id.quantity5);
        TextView quantity6 = (TextView)findViewById(R.id.quantity6);
        TextView quantity7 = (TextView)findViewById(R.id.quantity7);
        TextView quantity8 = (TextView)findViewById(R.id.quantity8);
        TextView quantity9 = (TextView)findViewById(R.id.quantity9);
        TextView quantity10 = (TextView)findViewById(R.id.quantity10);
        TextView quantity11 = (TextView)findViewById(R.id.quantity11);
        TextView quantity12 = (TextView)findViewById(R.id.quantity12);

        quantity1.setVisibility(View.INVISIBLE);
        quantity2.setVisibility(View.INVISIBLE);
        quantity3.setVisibility(View.INVISIBLE);
        quantity4.setVisibility(View.INVISIBLE);
        quantity5.setVisibility(View.INVISIBLE);
        quantity6.setVisibility(View.INVISIBLE);
        quantity7.setVisibility(View.INVISIBLE);
        quantity8.setVisibility(View.INVISIBLE);
        quantity9.setVisibility(View.INVISIBLE);
        quantity10.setVisibility(View.INVISIBLE);
        quantity11.setVisibility(View.INVISIBLE);
        quantity12.setVisibility(View.INVISIBLE);

        buttonText.setTypeface(myTypeface);
        buttonText1.setTypeface(myTypeface);
        buttonText2.setTypeface(myTypeface);
        buttonText3.setTypeface(myTypeface);
        buttonText4.setTypeface(myTypeface);
        buttonText5.setTypeface(myTypeface);
        buttonText6.setTypeface(myTypeface);
        buttonText7.setTypeface(myTypeface);
        buttonText8.setTypeface(myTypeface);
        buttonText9.setTypeface(myTypeface);
        buttonText10.setTypeface(myTypeface);
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

        undoAllButton.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("entered act res");
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

        TextView quantity1 = (TextView)findViewById(R.id.quantity1);
        TextView quantity2 = (TextView)findViewById(R.id.quantity2);
        TextView quantity3 = (TextView)findViewById(R.id.quantity3);
        TextView quantity4 = (TextView)findViewById(R.id.quantity4);
        TextView quantity5 = (TextView)findViewById(R.id.quantity5);
        TextView quantity6 = (TextView)findViewById(R.id.quantity6);
        TextView quantity7 = (TextView)findViewById(R.id.quantity7);
        TextView quantity8 = (TextView)findViewById(R.id.quantity8);
        TextView quantity9 = (TextView)findViewById(R.id.quantity9);
        TextView quantity10 = (TextView)findViewById(R.id.quantity10);
        TextView quantity11 = (TextView)findViewById(R.id.quantity11);
        TextView quantity12 = (TextView)findViewById(R.id.quantity12);

        System.out.println(requestCode);
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case 1:
                    buttonText.setText(data.getStringExtra("Title"));
                    if(!(data.getStringExtra("Quantity")==null)){
                        quantity1.setVisibility(View.VISIBLE);
                        quantity1.setText(data.getStringExtra("Quantity"));
                        buttonText.setBackgroundResource(R.drawable.circle_selected);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(0)))){
                        ingList.set(0, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 2:
                    buttonText1.setText(data.getStringExtra("Title"));
                    if(!(data.getStringExtra("Quantity")==null)){
                        quantity2.setVisibility(View.VISIBLE);
                        quantity2.setText(data.getStringExtra("Quantity"));
                        buttonText1.setBackgroundResource(R.drawable.circle_selected);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(1)))){
                        ingList.set(1, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 3:
                    buttonText2.setText(data.getStringExtra("Title"));
                    if(!(data.getStringExtra("Quantity")==null)){
                        quantity3.setVisibility(View.VISIBLE);
                        quantity3.setText(data.getStringExtra("Quantity"));
                        buttonText2.setBackgroundResource(R.drawable.circle_selected);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(2)))){
                        ingList.set(2, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 4:
                    buttonText3.setText(data.getStringExtra("Title"));
                    if(!(data.getStringExtra("Quantity")==null)){
                        quantity4.setVisibility(View.VISIBLE);
                        quantity4.setText(data.getStringExtra("Quantity"));
                        buttonText3.setBackgroundResource(R.drawable.circle_selected);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(3)))){
                        ingList.set(3, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 5:
                    buttonText4.setText(data.getStringExtra("Title"));
                    if(!(data.getStringExtra("Quantity")==null)){
                        quantity5.setVisibility(View.VISIBLE);
                        quantity5.setText(data.getStringExtra("Quantity"));
                        buttonText4.setBackgroundResource(R.drawable.circle_selected);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(4)))){
                        ingList.set(4, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 6:
                    buttonText5.setText(data.getStringExtra("Title"));
                    if(!(data.getStringExtra("Quantity")==null)){
                        quantity6.setVisibility(View.VISIBLE);
                        quantity6.setText(data.getStringExtra("Quantity"));
                        buttonText5.setBackgroundResource(R.drawable.circle_selected);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(5)))){
                        ingList.set(5, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 7:
                    buttonText6.setText(data.getStringExtra("Title"));
                    if(!(data.getStringExtra("Quantity")==null)){
                        quantity7.setVisibility(View.VISIBLE);
                        quantity7.setText(data.getStringExtra("Quantity"));
                        buttonText6.setBackgroundResource(R.drawable.circle_selected);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(6)))){
                        ingList.set(6, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 8:
                    buttonText7.setText(data.getStringExtra("Title"));
                    if(!(data.getStringExtra("Quantity")==null)){
                        quantity8.setVisibility(View.VISIBLE);
                        quantity8.setText(data.getStringExtra("Quantity"));
                        buttonText7.setBackgroundResource(R.drawable.circle_selected);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(7)))){
                        ingList.set(7, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 9:
                    buttonText8.setText(data.getStringExtra("Title"));
                    if(!(data.getStringExtra("Quantity")==null)){
                        quantity9.setVisibility(View.VISIBLE);
                        quantity9.setText(data.getStringExtra("Quantity"));
                        buttonText8.setBackgroundResource(R.drawable.circle_selected);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(8)))){
                        ingList.set(8, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 10:
                    buttonText9.setText(data.getStringExtra("Title"));
                    if(!(data.getStringExtra("Quantity")==null)){
                        quantity10.setVisibility(View.VISIBLE);
                        quantity10.setText(data.getStringExtra("Quantity"));
                        buttonText9.setBackgroundResource(R.drawable.circle_selected);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(9)))){
                        ingList.set(9, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 11:
                    buttonText10.setText(data.getStringExtra("Title"));
                    if(!(data.getStringExtra("Quantity")==null)){
                        quantity11.setVisibility(View.VISIBLE);
                        quantity11.setText(data.getStringExtra("Quantity"));
                        buttonText10.setBackgroundResource(R.drawable.circle_selected);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(10)))){
                        ingList.set(10, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 12:
                    buttonText11.setText(data.getStringExtra("Title"));
                    if(!(data.getStringExtra("Quantity")==null)){
                        quantity12.setVisibility(View.VISIBLE);
                        quantity12.setText(data.getStringExtra("Quantity"));
                        buttonText11.setBackgroundResource(R.drawable.circle_selected);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(11)))){
                        ingList.set(11, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
            }
        }
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
    public void changeIngredientList(ArrayList<String> ingList){
        String fileName = "Ingredients.txt";
        try {
            File sdCard = getFilesDir();
            File dir = new File (sdCard + "/SousChef/Data");
            File file = new File(dir, fileName);
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter(file,false);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            for(int i=0;i<ingList.size();i++){
                bufferedWriter.write(ingList.get(i));
                bufferedWriter.newLine();
            }

            // Always close files.
            bufferedWriter.close();
            fileWriter.close();
        }
        catch(Exception ex) {
            Toast.makeText(MainActivity.this, "Error writing to file '" + fileName + "'",Toast.LENGTH_SHORT).show();
            // Or we could just do this:
            // ex.printStackTrace();
        }
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

        switch(v.getId()) {
            case R.id.button:
                Intent intent = new Intent(this, IngredientAmountActivity.class);
                String thisIsString = buttonText.getText().toString();
                int thisIsIndex = 1;
                intent.putExtra("Index", thisIsIndex);
                intent.putExtra("Title", thisIsString);
                this.startActivityForResult(intent, 1);

                break;
            case R.id.button1:
                Intent intent1 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString1 = buttonText1.getText().toString();
                int thisIsIndex1 = 2;
                intent1.putExtra("Index", thisIsIndex1);
                intent1.putExtra("Title", thisIsString1);
                this.startActivityForResult(intent1,2);
                break;
            case R.id.button2:
                Intent intent2 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString2 = buttonText2.getText().toString();
                int thisIsIndex2 = 3;
                intent2.putExtra("Index", thisIsIndex2);
                intent2.putExtra("Title", thisIsString2);
                this.startActivityForResult(intent2,3);
                break;
            case R.id.button3:
                Intent intent3 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString3 = buttonText3.getText().toString();
                int thisIsIndex3 = 4;
                intent3.putExtra("Index", thisIsIndex3);
                intent3.putExtra("Title", thisIsString3);
                this.startActivityForResult(intent3,4);
                break;
            case R.id.button4:
                Intent intent4 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString4 = buttonText4.getText().toString();
                int thisIsIndex4 = 5;
                intent4.putExtra("Index", thisIsIndex4);
                intent4.putExtra("Title", thisIsString4);
                this.startActivityForResult(intent4,5);
                break;
            case R.id.button5:
                Intent intent5 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString5 = buttonText5.getText().toString();
                int thisIsIndex5 = 6;
                intent5.putExtra("Index", thisIsIndex5);
                intent5.putExtra("Title", thisIsString5);
                this.startActivityForResult(intent5,6);
                break;
            case R.id.button6:
                Intent intent6 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString6 = buttonText6.getText().toString();
                int thisIsIndex6 = 7;
                intent6.putExtra("Index", thisIsIndex6);
                intent6.putExtra("Title", thisIsString6);
                this.startActivityForResult(intent6,7);
                break;
            case R.id.button7:
                Intent intent7 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString7 = buttonText7.getText().toString();
                int thisIsIndex7 = 8;
                intent7.putExtra("Index", thisIsIndex7);
                intent7.putExtra("Title", thisIsString7);
                this.startActivityForResult(intent7,8);
                break;
            case R.id.button8:
                Intent intent8 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString8 = buttonText8.getText().toString();
                int thisIsIndex8 = 9;
                intent8.putExtra("Index", thisIsIndex8);
                intent8.putExtra("Title", thisIsString8);
                this.startActivityForResult(intent8,9);
                break;
            case R.id.button9:
                Intent intent9 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString9 = buttonText9.getText().toString();
                int thisIsIndex9 = 10;
                intent9.putExtra("Index", thisIsIndex9);
                intent9.putExtra("Title", thisIsString9);
                this.startActivityForResult(intent9,10);
                break;
            case R.id.button10:
                Intent intent10 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString10 = buttonText10.getText().toString();
                int thisIsIndex10 = 11;
                intent10.putExtra("Index", thisIsIndex10);
                intent10.putExtra("Title", thisIsString10);
                this.startActivityForResult(intent10,11);
                break;
            case R.id.button11:
                Intent intent11 = new Intent(this, IngredientAmountActivity.class);
                String thisIsString11 = buttonText11.getText().toString();
                int thisIsIndex11 = 12;
                intent11.putExtra("Index", thisIsIndex11);
                intent11.putExtra("Title", thisIsString11);
                this.startActivityForResult(intent11,12);
                break;

            case R.id.button12:
                clearAll();
                break;
        }
        //finish();
    }

    public ArrayList<String> getIngredientsList(String fileName){
        //TODO: Set save data location on android
        String line;
        ArrayList<String> returnList = new ArrayList<String>();
        try {

            File sdCard = getFilesDir();
            File dir = new File (sdCard + "/SousChef/Data");
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
        catch(Exception ex) {
            Toast.makeText(MainActivity.this,"Error reading file '"+ fileName + "'",Toast.LENGTH_SHORT).show();
            // Or we could just do this:
            // ex.printStackTrace();
        }
        return returnList;
    }
    public void clearAll(){
        TextView quantity1 = (TextView)findViewById(R.id.quantity1);
        TextView quantity2 = (TextView)findViewById(R.id.quantity2);
        TextView quantity3 = (TextView)findViewById(R.id.quantity3);
        TextView quantity4 = (TextView)findViewById(R.id.quantity4);
        TextView quantity5 = (TextView)findViewById(R.id.quantity5);
        TextView quantity6 = (TextView)findViewById(R.id.quantity6);
        TextView quantity7 = (TextView)findViewById(R.id.quantity7);
        TextView quantity8 = (TextView)findViewById(R.id.quantity8);
        TextView quantity9 = (TextView)findViewById(R.id.quantity9);
        TextView quantity10 = (TextView)findViewById(R.id.quantity10);
        TextView quantity11 = (TextView)findViewById(R.id.quantity11);
        TextView quantity12 = (TextView)findViewById(R.id.quantity12);

        quantity1.setVisibility(View.INVISIBLE);
        quantity2.setVisibility(View.INVISIBLE);
        quantity3.setVisibility(View.INVISIBLE);
        quantity4.setVisibility(View.INVISIBLE);
        quantity5.setVisibility(View.INVISIBLE);
        quantity6.setVisibility(View.INVISIBLE);
        quantity7.setVisibility(View.INVISIBLE);
        quantity8.setVisibility(View.INVISIBLE);
        quantity9.setVisibility(View.INVISIBLE);
        quantity10.setVisibility(View.INVISIBLE);
        quantity11.setVisibility(View.INVISIBLE);
        quantity12.setVisibility(View.INVISIBLE);

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

        buttonText.setBackgroundResource(R.drawable.circle);
        buttonText1.setBackgroundResource(R.drawable.circle);
        buttonText2.setBackgroundResource(R.drawable.circle);
        buttonText3.setBackgroundResource(R.drawable.circle);
        buttonText4.setBackgroundResource(R.drawable.circle);
        buttonText5.setBackgroundResource(R.drawable.circle);
        buttonText6.setBackgroundResource(R.drawable.circle);
        buttonText7.setBackgroundResource(R.drawable.circle);
        buttonText8.setBackgroundResource(R.drawable.circle);
        buttonText9.setBackgroundResource(R.drawable.circle);
        buttonText10.setBackgroundResource(R.drawable.circle);
        buttonText11.setBackgroundResource(R.drawable.circle);


    }
    public void setIngredientList(){
        String fileName = "Ingredients.txt";

        try {

            File sdCard = getFilesDir();
            File dir = new File (sdCard + "/SousChef/Data");
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
        catch(Exception ex) {
            Toast.makeText(MainActivity.this, "Error writing to file '" + fileName + "'",Toast.LENGTH_SHORT).show();
            // Or we could just do this:
            // ex.printStackTrace();
        }

    }

}
