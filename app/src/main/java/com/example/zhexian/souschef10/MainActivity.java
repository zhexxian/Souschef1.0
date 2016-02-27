package com.example.zhexian.souschef10;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

//TODO: implement dispense to arduino
//TODO: add quantity left for each ingredient

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<String> ingList;
    ArrayList<Integer> ingSelected = new ArrayList<Integer>();
    int[][] dataToArduino = new int[12][4];
    int[][][] recipeList = new int[2][12][4];
    public Button buttonText;
    public Button buttonText1;
    public Button buttonText2;
    public Button buttonText3;
    public Button buttonText4;
    public Button buttonText5;
    public Button buttonText6;
    public Button buttonText7;
    public Button buttonText8;
    public Button buttonText9;
    public Button buttonText10;
    public Button buttonText11;

    public TextView quantity1;
    public TextView quantity2;
    public TextView quantity3;
    public TextView quantity4;
    public TextView quantity5;
    public TextView quantity6;
    public TextView quantity7;
    public TextView quantity8;
    public TextView quantity9;
    public TextView quantity10;
    public TextView quantity11;
    public TextView quantity12;
    public Button undoAllButton;
    public Button undoButton;
    public Button dispenseButton;
    public Button recipeButton;
    public TextView weightText;


    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothAdapter btAdapter;
    Set<BluetoothDevice> devicesArray;
    ArrayList<String> pairedDevices;

    public static final int REQUEST_ENABLE_BT = 1;
    static final int SUCCESS_CONNECT = 2;
    static final int DISPENSE = 3;

    Handler mmHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SUCCESS_CONNECT:
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
                    break;
                case DISPENSE:
                    Toast.makeText(getApplicationContext(), "dispense clicked", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        if(btAdapter==null){
            Toast.makeText(this,"Device doesn't support bluetooth. Closing application", Toast.LENGTH_LONG).show();
        }
        if(!btAdapter.isEnabled()){
            turnOnBT();
        }

        getPairedDevices();

        String fileName = "Ingredients.txt";

        ingList = getIngredientsList(fileName);

        //ingList.toString();
        if(ingList.size()<1){
            setIngredientList();
            ingList = getIngredientsList(fileName);
        }
        System.out.println(ingList.toString());
        // Initializing main screen
        runOnUiThread(buttonInitialization);
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
        undoButton.setOnClickListener(this);
        recipeButton.setOnClickListener(this);

    }

    private void turnOnBT(){
        Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(btIntent, REQUEST_ENABLE_BT);
    }

    private void getPairedDevices(){
        devicesArray = btAdapter.getBondedDevices();
        BluetoothDevice x = null;
        if(devicesArray.size()>0){
            for(BluetoothDevice device: devicesArray){
                Log.e("GET PAIRED DEVICE", device.getName());
//                pairedDevices.add(device.getName());
                x = device;

            }
        }
        ConnectThread connect = new ConnectThread(x);
        connect.start();
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
    /******************************** onActivityResult ***********************************
     *  this method receives data from IAA after this activity has called IAA via the startActivityForResult() method
     *
     * @param requestCode - is the integer value which is sent to IAA as reference (the integer corresponds to the ingredient slot number)
     * @param resultCode - checks if the result from IAA is need or not. RESULT_OK means needed, else not needed
     * @param data - the intent object that is received from IAA, containing measurement, quanityt and name of the ingredient
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //System.out.println("entered act res");
        //String[] measurementIndex = {"Xtsp","X1/2tbsp","Xtbsp"};
        //System.out.println(requestCode);
        if (resultCode==RESULT_OK) {
            switch (requestCode) {
                case 1:
                    buttonText.setText(data.getStringExtra("Title"));
                    if (!(data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[1] && data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[2])){
                        quantity1.setVisibility(View.VISIBLE);
                        int qty = forArduino(data.getIntArrayExtra("Quant"),0);
                        quantity1.setText(qty + data.getStringExtra("Measurement"));
                        buttonText.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(1);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(0)))){
                        ingList.set(0, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 2:
                    buttonText1.setText(data.getStringExtra("Title"));
                    if (!(data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[1] && data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[2])){
                        quantity2.setVisibility(View.VISIBLE);
                        int qty = forArduino(data.getIntArrayExtra("Quant"), 1);
                        quantity2.setText(qty + data.getStringExtra("Measurement"));
                        buttonText1.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(2);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(1)))){
                        ingList.set(1, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 3:
                    buttonText2.setText(data.getStringExtra("Title"));
                    if (!(data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[1] && data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[2])){
                        quantity3.setVisibility(View.VISIBLE);
                        int qty = forArduino(data.getIntArrayExtra("Quant"), 2);
                        quantity3.setText(qty + data.getStringExtra("Measurement"));
                        buttonText2.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(3);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(2)))){
                        ingList.set(2, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 4:
                    buttonText3.setText(data.getStringExtra("Title"));
                    if (!(data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[1] && data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[2])) {
                        quantity4.setVisibility(View.VISIBLE);
                        int qty = forArduino(data.getIntArrayExtra("Quant"), 3);
                        quantity4.setText(qty + data.getStringExtra("Measurement"));
                        buttonText3.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(4);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(3)))){
                        ingList.set(3, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 5:
                    buttonText4.setText(data.getStringExtra("Title"));
                    if (!(data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[1] && data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[2])) {
                        quantity5.setVisibility(View.VISIBLE);
                        int qty = forArduino(data.getIntArrayExtra("Quant"), 4);
                        quantity5.setText(qty + data.getStringExtra("Measurement"));
                        buttonText4.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(5);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(4)))){
                        ingList.set(4, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 6:
                    buttonText5.setText(data.getStringExtra("Title"));
                    if (!(data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[1] && data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[2])) {
                        quantity6.setVisibility(View.VISIBLE);
                        int qty = forArduino(data.getIntArrayExtra("Quant"), 5);
                        quantity6.setText(qty + data.getStringExtra("Measurement"));
                        buttonText5.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(6);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(5)))){
                        ingList.set(5, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 7:
                    buttonText6.setText(data.getStringExtra("Title"));
                    if (!(data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[1] && data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[2])) {
                        quantity7.setVisibility(View.VISIBLE);
                        int qty = forArduino(data.getIntArrayExtra("Quant"), 6);
                        quantity7.setText(qty + data.getStringExtra("Measurement"));
                        buttonText6.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(7);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(6)))){
                        ingList.set(6, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 8:
                    buttonText7.setText(data.getStringExtra("Title"));
                    if (!(data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[1] && data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[2])) {
                        quantity8.setVisibility(View.VISIBLE);
                        int qty = forArduino(data.getIntArrayExtra("Quant"), 7);
                        quantity8.setText(qty + data.getStringExtra("Measurement"));
                        buttonText7.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(8);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(7)))){
                        ingList.set(7, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 9:
                    buttonText8.setText(data.getStringExtra("Title"));
                    if (!(data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[1] && data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[2])) {
                        quantity9.setVisibility(View.VISIBLE);
                        int qty = forArduino(data.getIntArrayExtra("Quant"), 8);
                        quantity9.setText(qty + data.getStringExtra("Measurement"));
                        buttonText8.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(9);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(8)))){
                        ingList.set(8, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 10:
                    buttonText9.setText(data.getStringExtra("Title"));
                    if (!(data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[1] && data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[2])) {
                        quantity10.setVisibility(View.VISIBLE);
                        int qty = forArduino(data.getIntArrayExtra("Quant"), 9);
                        quantity10.setText(qty + data.getStringExtra("Measurement"));
                        buttonText9.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(10);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(9)))){
                        ingList.set(9, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 11:
                    buttonText10.setText(data.getStringExtra("Title"));
                    if (!(data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[1] && data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[2])) {
                        quantity11.setVisibility(View.VISIBLE);
                        int qty = forArduino(data.getIntArrayExtra("Quant"), 10);
                        quantity11.setText(qty + data.getStringExtra("Measurement"));
                        buttonText10.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(11);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(10)))){
                        ingList.set(10, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;
                case 12:
                    buttonText11.setText(data.getStringExtra("Title"));
                    if (!(data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[1] && data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[2])) {
                        quantity12.setVisibility(View.VISIBLE);
                        int qty = forArduino(data.getIntArrayExtra("Quant"), 11);
                        quantity12.setText(qty + data.getStringExtra("Measurement"));
                        buttonText11.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(12);
                    }
                    if(!(data.getStringExtra("Title").equals(ingList.get(11)))){
                        ingList.set(11, data.getStringExtra("Title"));
                        changeIngredientList(ingList);
                    }
                    break;

                case 19:
                    runOnUiThread(undoAll);
                    String receive = data.getStringExtra("Recipe");
                    recipeConverter(receive);
                    runOnUiThread(recipeResult);
                    break;
                    //TODO: continue recipe
            }
            System.out.println(Arrays.deepToString(dataToArduino));
        }
    }

    /*************************** RUNNABLES *******************************
     * buttonInitialization - initializes the buttons by finding in layout  and assigning to Button
     */
    private Runnable buttonInitialization = new Runnable() {
        @Override
        public void run() {
            Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Spinnaker-Regular.ttf");

            buttonText = (Button)findViewById(R.id.button);
            buttonText1 = (Button)findViewById(R.id.button1);
            buttonText2 = (Button)findViewById(R.id.button2);
            buttonText3 = (Button)findViewById(R.id.button3);
            buttonText4 = (Button)findViewById(R.id.button4);
            buttonText5 = (Button)findViewById(R.id.button5);
            buttonText6 = (Button)findViewById(R.id.button6);
            buttonText7 = (Button)findViewById(R.id.button7);
            buttonText8 = (Button)findViewById(R.id.button8);
            buttonText9 = (Button)findViewById(R.id.button9);
            buttonText10 = (Button)findViewById(R.id.button10);
            buttonText11 = (Button)findViewById(R.id.button11);

            undoAllButton = (Button)findViewById(R.id.button12);
            undoButton = (Button) findViewById(R.id.button14);
            dispenseButton = (Button) findViewById(R.id.button13);
            recipeButton = (Button) findViewById(R.id.button19);

            quantity1 = (TextView)findViewById(R.id.quantity1);
            quantity2 = (TextView)findViewById(R.id.quantity2);
            quantity3 = (TextView)findViewById(R.id.quantity3);
            quantity4 = (TextView)findViewById(R.id.quantity4);
            quantity5 = (TextView)findViewById(R.id.quantity5);
            quantity6 = (TextView)findViewById(R.id.quantity6);
            quantity7 = (TextView)findViewById(R.id.quantity7);
            quantity8 = (TextView)findViewById(R.id.quantity8);
            quantity9 = (TextView)findViewById(R.id.quantity9);
            quantity10 = (TextView)findViewById(R.id.quantity10);
            quantity11 = (TextView)findViewById(R.id.quantity11);
            quantity12 = (TextView)findViewById(R.id.quantity12);

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

            undoButton.setTypeface(myTypeface);
            undoAllButton.setTypeface(myTypeface);

            dispenseButton.setTypeface(myTypeface);

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

            weightText = (TextView) findViewById(R.id.textView2);
            weightText.setTypeface(myTypeface);
        }
    };
    /***
     * undoAll - returns the layout to original state (no shaded or quantity shown)
     */
    private Runnable undoAll = new Runnable() {
        @Override
        public void run() {
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
    };
    /***
     * recipeResult - gets the dataToArduino array and converts it to visible form ie show in the
     * layout ie shows what is selected and which quantity is choses
     */
    public Runnable recipeResult = new Runnable() {
        @Override
        public void run() {
            String[] measurementIndex = {"Xtsp","X1/2tbsp","Xtbsp"};
            String meas = "";
            int amount = 0;
            for(int i=0;i<12;i++){
                if(dataToArduino[i][0]==0){
                    continue;
                }
                System.out.println(i);
                for(int j=1;j<4;j++){
                    if(dataToArduino[i][j]!=0){
                        meas= measurementIndex[j-1];
                        amount = dataToArduino[i][j];
                    }
                }
                switch (i) {
                    case 0:
                        quantity1.setVisibility(View.VISIBLE);
                        quantity1.setText(amount + meas);
                        buttonText.setBackgroundResource(R.drawable.circle_selected);
                        break;
                    case 1:
                        quantity2.setVisibility(View.VISIBLE);
                        quantity2.setText(amount + meas);
                        buttonText1.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(2);

                        break;
                    case 2:
                        quantity3.setVisibility(View.VISIBLE);
                        quantity3.setText(amount + meas);
                        buttonText2.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(3);
                        break;
                    case 3:
                        quantity4.setVisibility(View.VISIBLE);
                        quantity4.setText(amount + meas);
                        buttonText3.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(4);
                        break;
                    case 4:
                        quantity5.setVisibility(View.VISIBLE);
                        quantity5.setText(amount + meas);
                        buttonText4.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(5);
                        break;
                    case 5:
                        quantity6.setVisibility(View.VISIBLE);
                        quantity6.setText(amount + meas);
                        buttonText5.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(6);
                        break;
                    case 6:
                        quantity7.setVisibility(View.VISIBLE);
                        quantity7.setText(amount + meas);
                        buttonText6.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(7);
                        break;
                    case 7:
                        quantity8.setVisibility(View.VISIBLE);
                        quantity8.setText(amount + meas);
                        buttonText7.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(8);
                        break;
                    case 8:
                        quantity9.setVisibility(View.VISIBLE);
                        quantity9.setText(amount + meas);
                        buttonText8.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(9);
                        break;
                    case 9:
                        quantity10.setVisibility(View.VISIBLE);
                        quantity10.setText(amount + meas);
                        buttonText9.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(10);
                        break;
                    case 10:
                        quantity11.setVisibility(View.VISIBLE);
                        quantity11.setText(amount + meas);
                        buttonText10.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(11);
                        break;
                    case 11:
                        quantity12.setVisibility(View.VISIBLE);
                        quantity12.setText(amount + meas);
                        buttonText11.setBackgroundResource(R.drawable.circle_selected);
                        ingSelected.add(12);
                        break;
                }
            }
            ingSelected.add(99);
        }
    };

    /**************************** METHODS ****************************
     * recipeConverter - receives a string containing data to be translated into an array from the recipeList activity
     *                 - this is done by splitting the string into 12 at the ":" and then splitting the 12 strings further into four at " "
     * @param receive - String receive is a string containing data to be translated into an array from the recipeList activity
     */
    public void recipeConverter(String receive){
        String[] receiveSplit = receive.split(":");
        for(int i=0;i<12;i++){
            String[] receiveSplitSplit = receiveSplit[i].split(" ");
            if(Integer.parseInt(receiveSplitSplit[0])==0){
                continue;
            }
            for(int j=0;j<4;j++){
                dataToArduino[i][j]=Integer.parseInt(receiveSplitSplit[j]);
            }
        }
    }

    /***
     * forArduino - this method is used to change the subarray from dataToArduino such that it contains the quantity to be displayed in the layout
     * @param getIntArray - this array contains just quantity values. for example [1,0,0] means 1 times teaspoon
     * @param ingNumber - this integer value is the ingredient slot number
     * @return - returns an integer value for the caller to receive
     */
    public int forArduino(int[] getIntArray, int ingNumber){
        int qty=0;
        dataToArduino[ingNumber][0]=1;
        for(int i=0;i<3;i++){
            dataToArduino[ingNumber][i+1]=getIntArray[i];
            if(getIntArray[i]!=0){
                qty=getIntArray[i];
            }
        }
        return qty;
    }

    /***
     * changeIngredientList - this method changes existing txt file to contain the latest change of the names of the ingredients
     * @param ingList - this array contains the all the current names of the ingredients
     */
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

    /***
     * onClick - this method  allows the user to click. this in turn call the IAA while passing data to the IAA such as ingredient and the ingredient slot number
     * @param v - the layout view
     */
    public void onClick(View v){

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
                System.out.println(ingSelected.size());
                if(ingSelected.size()>0){
                    runOnUiThread(undoAll);
                }
                else{
                    Toast.makeText(MainActivity.this,"There is nothing to undo!",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.button13:
                Log.e("Button13", "Button pressed");
                dispense();
                break;
            case R.id.button14:
                if(ingSelected.size()>0){
                    undo(ingSelected.get(ingSelected.size()-1));
                }
                else{
                    Toast.makeText(MainActivity.this,"There is nothing to undo!",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.button19:
                Intent intent19 = new Intent(this, RecipeListActivity.class);
                this.startActivityForResult(intent19,19);
                break;


        }
    }


    public void dispense(){
        mmHandler.obtainMessage(DISPENSE).sendToTarget();
        Log.e("Inside Dispense", "Dispense pressed");
        Toast.makeText(getApplicationContext(),"Dispensed",Toast.LENGTH_LONG).show();
    }
    /***
     * undo - undo the latest change (excluding name change) that is read from the ingSelected.
     * @param caseNumber - this integer value is the ingredient slot number
     */
    public void undo(int caseNumber){
        int[] zeroes = {0,0,0,0};
        switch(caseNumber){
            case 1:
                quantity1.setVisibility(View.INVISIBLE);
                buttonText.setBackgroundResource(R.drawable.circle);
                ingSelected.remove(ingSelected.size() - 1);
                dataToArduino[0]=zeroes;
                break;
            case 2:
                quantity2.setVisibility(View.INVISIBLE);
                buttonText1.setBackgroundResource(R.drawable.circle);
                ingSelected.remove(ingSelected.size() - 1);
                dataToArduino[1]=zeroes;
                break;
            case 3:
                quantity3.setVisibility(View.INVISIBLE);
                buttonText2.setBackgroundResource(R.drawable.circle);
                ingSelected.remove(ingSelected.size() - 1);
                dataToArduino[2]=zeroes;
                break;
            case 4:
                quantity4.setVisibility(View.INVISIBLE);
                buttonText3.setBackgroundResource(R.drawable.circle);
                ingSelected.remove(ingSelected.size() - 1);
                dataToArduino[3]=zeroes;
                break;
            case 5:
                quantity5.setVisibility(View.INVISIBLE);
                buttonText4.setBackgroundResource(R.drawable.circle);
                ingSelected.remove(ingSelected.size() - 1);
                dataToArduino[4]=zeroes;
                break;
            case 6:
                quantity6.setVisibility(View.INVISIBLE);
                buttonText5.setBackgroundResource(R.drawable.circle);
                ingSelected.remove(ingSelected.size() - 1);
                dataToArduino[5]=zeroes;
                break;
            case 7:
                quantity7.setVisibility(View.INVISIBLE);
                buttonText6.setBackgroundResource(R.drawable.circle);
                ingSelected.remove(ingSelected.size() - 1);
                dataToArduino[6]=zeroes;
                break;
            case 8:
                quantity8.setVisibility(View.INVISIBLE);
                buttonText7.setBackgroundResource(R.drawable.circle);
                ingSelected.remove(ingSelected.size() - 1);
                dataToArduino[7]=zeroes;
                break;
            case 9:
                quantity9.setVisibility(View.INVISIBLE);
                buttonText8.setBackgroundResource(R.drawable.circle);
                ingSelected.remove(ingSelected.size() - 1);
                dataToArduino[8]=zeroes;
                break;
            case 10:
                quantity10.setVisibility(View.INVISIBLE);
                buttonText9.setBackgroundResource(R.drawable.circle);
                ingSelected.remove(ingSelected.size() - 1);
                dataToArduino[9]=zeroes;
                break;
            case 11:
                quantity11.setVisibility(View.INVISIBLE);
                buttonText10.setBackgroundResource(R.drawable.circle);
                ingSelected.remove(ingSelected.size() - 1);
                dataToArduino[10]=zeroes;
                break;
            case 12:
                quantity12.setVisibility(View.INVISIBLE);
                buttonText11.setBackgroundResource(R.drawable.circle);
                ingSelected.remove(ingSelected.size() - 1);
                dataToArduino[11]=zeroes;
                break;
            case 99:
                runOnUiThread(undoAll);
                break;
        }
    }

    /***
     * getIngredientList - this method searches the file directory for the txt file containing all the ingredient names
     * @param fileName - the file name
     * @return - returns an arraylist containing all the ingredients from the txt file
     */
    public ArrayList<String> getIngredientsList(String fileName){
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
            Toast.makeText(MainActivity.this,  "Welcome first time user!", Toast.LENGTH_SHORT).show();

        }
        catch(Exception ex) {
            Toast.makeText(MainActivity.this,"Error reading file '"+ fileName + "'",Toast.LENGTH_SHORT).show();
            // Or we could just do this:
            // ex.printStackTrace();
        }
        return returnList;
    }

    /***
     * setIngredientList - this method is for first time users. initializes a txt file in the file directory containing random ingredient names
     */
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
            fileWriter.close();
        }
        catch(Exception ex) {
            Toast.makeText(MainActivity.this, "Error writing to file '" + fileName + "'",Toast.LENGTH_SHORT).show();
        }

    }



    private class ConnectThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device){
            BluetoothSocket tmp = null;
            mmDevice = device;

            try{
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            }catch(IOException e){
                e.printStackTrace();
            }
            mmSocket = tmp;

        }

        public void run(){
            try{
                mmSocket.connect();
            }catch (IOException connectException){
                connectException.printStackTrace();
                try{
                    mmSocket.close();
                }catch (IOException e){}

            }

            mmHandler.obtainMessage(SUCCESS_CONNECT,mmSocket).sendToTarget();
        }
    }

/*    private void CheckBt() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "Bluetooth Disabled !",
                    Toast.LENGTH_SHORT).show();
                   *//* It tests if the bluetooth is enabled or not, if not the app will show a message. *//*
        }

        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),
                    "Bluetooth null !", Toast.LENGTH_SHORT)
                    .show();
        }
    }*/

}
