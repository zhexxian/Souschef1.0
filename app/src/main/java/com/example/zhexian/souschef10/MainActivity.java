package com.example.zhexian.souschef10;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

/*************************** TODO LIST **********************************
 * Main page:
 - DONE [UI] tare button for weight
 -[Function] after pressing tare button, weight displayed will be actual weight data received minus the weight of container
 - DONE [UI] make weight and tare button fixed in position (dont allow the position of tare depend on the weight variable as this will move tare button when weight changes)

 * Ingredient amount page:
 - DONE [UI] change 'select amount' to 'quantity'
 - [Function] autocomplete for ingredient name

 * Recipe page:

 * RecipeList page:
 - DONE [UI] add a button to add new recipes
 - [Function] add function for above button
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<String> ingList;
    ArrayList<Integer> ingSelected = new ArrayList<Integer>();
    ArrayList<Button> ingredientButtons = new ArrayList<Button>();
    ArrayList<TextView> quantityText = new ArrayList<TextView>();

    int[][] dataToArduino = new int[12][3];

    public Button undoAllButton;
    public Button undoButton;
    public Button dispenseButton;
    public Button recipeButton;
    public TextView weightText;
    public TextView weightValue;
/***
     * BLUETOOTH PORTION
     *
     */
public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final String TAG = "Main Activity";
    BluetoothAdapter btAdapter;
    Set<BluetoothDevice> devicesArray;
    ArrayList<String> pairedDevices;
    ConnectThread connect;
    ConnectedThread connected;
    BufferedReader mBufferedReader;

    public static final int REQUEST_ENABLE_BT = 1;
    static final int SUCCESS_CONNECT = 2;
    static final int DISPENSE = 3;
    static final int MESSAGE_READ = 4;

    Handler mmHandler;

    /***
     * END BLUETOOTH PORTION
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        for(Button i : ingredientButtons){
            i.setOnClickListener(this);
        }

        undoAllButton.setOnClickListener(this);
        undoButton.setOnClickListener(this);
        recipeButton.setOnClickListener(this);
        dispenseButton.setOnClickListener(this);

        //Get a handle to the default local Bluetooth adapter.
        btAdapter = BluetoothAdapter.getDefaultAdapter();

        if(btAdapter==null){
            Toast.makeText(this,"Device doesn't support bluetooth. Closing application", Toast.LENGTH_LONG).show();
            finish();
            System.exit(0);
        }
        if(!btAdapter.isEnabled()){
            turnOnBT();
        }

        getPairedDevices();

    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        if(connect!=null){
            connect.cancel();
            connect = null;
        }
        if(connected!=null){
            connected.cancel();
            connected = null;
        }
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
        System.out.println("REQUESTCODE " + requestCode);
        if (resultCode==RESULT_OK) {
            if(requestCode == 19){
                runOnUiThread(undoAll);
                String receive = data.getStringExtra("Recipe_Ingredients");
                recipeConverter(receive);
                runOnUiThread(recipeResult);
            }
            else{
                ingredientButtons.get(requestCode-1).setText(data.getStringExtra("Title"));
                if (!(data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[1])){
                    quantityText.get(requestCode-1).setVisibility(View.VISIBLE);
                    int qty = forArduino(data.getIntArrayExtra("Quant"), 0);
                    if(data.getStringExtra("Measurement").equals("Xtsp")){
                        quantityText.get(requestCode-1).setText(qty/2.0 + data.getStringExtra("Measurement"));
                    }
                    else{
                        quantityText.get(requestCode-1).setText(qty + data.getStringExtra("Measurement"));
                    }
                    ingredientButtons.get(requestCode-1).setBackgroundResource(R.drawable.circle_selected);
                    ingSelected.add(1);
                }
                else if((data.getIntArrayExtra("Quant")[0] == data.getIntArrayExtra("Quant")[1])){
                    undo(requestCode);
                }
                else if(!(data.getStringExtra("Title").equals(ingList.get(requestCode-1)))){
                    ingList.set(requestCode-1, data.getStringExtra("Title"));
                    changeIngredientList(ingList);
                }
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
            ingredientButtons.add((Button)findViewById(R.id.button));
            ingredientButtons.add((Button)findViewById(R.id.button1));
            ingredientButtons.add((Button)findViewById(R.id.button2));
            ingredientButtons.add((Button)findViewById(R.id.button3));
            ingredientButtons.add((Button)findViewById(R.id.button4));
            ingredientButtons.add((Button)findViewById(R.id.button5));
            ingredientButtons.add((Button)findViewById(R.id.button6));
            ingredientButtons.add((Button)findViewById(R.id.button7));
            ingredientButtons.add((Button)findViewById(R.id.button8));
            ingredientButtons.add((Button)findViewById(R.id.button9));
            ingredientButtons.add((Button)findViewById(R.id.button10));
            ingredientButtons.add((Button)findViewById(R.id.button11));

            undoAllButton = (Button)findViewById(R.id.button12);
            undoButton = (Button) findViewById(R.id.button14);
            dispenseButton = (Button) findViewById(R.id.button13);
            recipeButton = (Button) findViewById(R.id.button19);

            quantityText.add((TextView)findViewById(R.id.quantity1));
            quantityText.add((TextView)findViewById(R.id.quantity2));
            quantityText.add((TextView)findViewById(R.id.quantity3));
            quantityText.add((TextView)findViewById(R.id.quantity4));
            quantityText.add((TextView)findViewById(R.id.quantity5));
            quantityText.add((TextView)findViewById(R.id.quantity6));
            quantityText.add((TextView)findViewById(R.id.quantity7));
            quantityText.add((TextView)findViewById(R.id.quantity8));
            quantityText.add((TextView)findViewById(R.id.quantity9));
            quantityText.add((TextView)findViewById(R.id.quantity10));
            quantityText.add((TextView)findViewById(R.id.quantity11));
            quantityText.add((TextView)findViewById(R.id.quantity12));

            for(TextView i : quantityText){
                i.setVisibility(View.INVISIBLE);
            }

            for(Button i : ingredientButtons){
                i.setTypeface(myTypeface);
            }

            undoButton.setTypeface(myTypeface);
            undoAllButton.setTypeface(myTypeface);

            dispenseButton.setTypeface(myTypeface);

            for(int i=0;i<12;i++){
                ingredientButtons.get(i).setText(ingList.get(i));
            }

            weightText = (TextView) findViewById(R.id.textView2);
            weightText.setTypeface(myTypeface);

            weightValue = (TextView) findViewById(R.id.textView3);
            weightValue.setTypeface(myTypeface);

        }
    };
    /***
     * undoAll - returns the layout to original state (no shaded or quantity shown)
     */
    private Runnable undoAll = new Runnable() {
        @Override
        public void run() {

            for(TextView i : quantityText){
                i.setVisibility(View.INVISIBLE);
            }
            for(Button i : ingredientButtons){
                i.setBackgroundResource(R.drawable.circle);
            }

            int[][] newdata = new int[12][3];
            dataToArduino = newdata;
            while(ingSelected.size()>0){
                ingSelected.remove(ingSelected.size()-1);
            }
        }
    };
    /***
     * recipeResult - gets the dataToArduino array and converts it to visible form ie show in the
     * layout ie shows what is selected and which quantity is choses
     */
    public Runnable recipeResult = new Runnable() {
        @Override
        public void run() {
            String[] measurementIndex = {"Xtsp","Xtbsp"};
            String meas = "";
            String newamount;
            int amount = 0;
            for(int i=0;i<12;i++){
                if(dataToArduino[i][0]==0){
                    continue;
                }
                System.out.println(i);
                for(int j=1;j<3;j++){
                    if(dataToArduino[i][j]!=0){
                        meas= measurementIndex[j-1];
                        amount = dataToArduino[i][j];
                    }
                }
                if(meas.equals("Xtsp")){
                    newamount = ""+amount/2.0;
                }
                else{
                    newamount =""+ amount;
                }
                quantityText.get(i).setVisibility(View.VISIBLE);
                quantityText.get(i).setText(newamount + meas);
                ingredientButtons.get(i).setBackgroundResource(R.drawable.circle_selected);
            }
            ingSelected.add(99);
            System.out.println(ingSelected.toString());

        }
    };

    /**************************** METHODS ****************************
     * recipeConverter - receives a string containing data to be translated into an array from the recipeList activity
     *                 - this is done by splitting the string into 12 at the ":" and then splitting the 12 strings further into four at " "
     * @param receive - String receive is a string containing data to be translated into an array from the recipeList activity
     */
    public void recipeConverter(String receive){
        String[] receiveSplit = receive.split(":");
        System.out.println(receive);
        int[][] newData = new int[12][3];
        for(int i=0;i<12;i++){
            String[] receiveSplitSplit = receiveSplit[i].split(" ");
/*            if(Integer.parseInt(receiveSplitSplit[0])==0){
                continue;
            }*/
            for(int j=0;j<3;j++){
                //System.out.println(receiveSplitSplit[j]);
                int value = Integer.parseInt(receiveSplitSplit[j]);
                newData[i][j]=value;
            }
        }
        dataToArduino=newData;
        System.out.println(Arrays.deepToString(newData));
        System.out.println("from recipe: "+Arrays.deepToString(dataToArduino));
    }

    /***
     * forArduino - this method is used to change the subarray from dataToArduino such that it contains the quantity to be displayed in the layout
     * @param getIntArray - this array contains just quantity values. for example [1,1,0] means 1 times teaspoon
     * @param ingNumber - this integer value is the ingredient slot number
     * @return - returns an integer value for the caller to receive
     */
    public int forArduino(int[] getIntArray, int ingNumber){
        int qty=0;
        dataToArduino[ingNumber][0]=1;
        dataToArduino[ingNumber][1]=getIntArray[0];
        dataToArduino[ingNumber][2]=getIntArray[1];
        if(getIntArray[0]!=0){
            qty = getIntArray[0];
        }
        else if(getIntArray[1]!=0){
            qty = getIntArray[1];
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

        Intent intent = new Intent(this, IngredientAmountActivity.class);
        String ingName = "";
        int ingIndex = 0;
        boolean isIngButton = false;
        switch(v.getId()) {
            case R.id.button:
                ingIndex = 1;
                ingName = ingredientButtons.get(0).getText().toString();
                isIngButton = true;
                break;

            case R.id.button1:
                ingName = ingredientButtons.get(1).getText().toString();
                ingIndex = 2;
                isIngButton = true;
                break;

            case R.id.button2:
                ingName = ingredientButtons.get(2).getText().toString();
                ingIndex = 3;
                isIngButton = true;
                break;

            case R.id.button3:
                ingName = ingredientButtons.get(3).getText().toString();
                ingIndex = 4;
                isIngButton = true;
                break;

            case R.id.button4:
                ingName = ingredientButtons.get(4).getText().toString();
                ingIndex = 5;
                isIngButton = true;
                break;

            case R.id.button5:
                ingName = ingredientButtons.get(5).getText().toString();
                ingIndex = 6;
                isIngButton = true;
                break;

            case R.id.button6:
                ingName = ingredientButtons.get(6).getText().toString();
                ingIndex = 7;
                isIngButton = true;
                break;

            case R.id.button7:
                ingName = ingredientButtons.get(7).getText().toString();
                ingIndex = 8;
                isIngButton = true;
                break;

            case R.id.button8:
                ingName = ingredientButtons.get(8).getText().toString();
                ingIndex = 9;
                isIngButton = true;
                break;

            case R.id.button9:
                ingName = ingredientButtons.get(9).getText().toString();
                ingIndex = 10;
                isIngButton = true;
                break;

            case R.id.button10:
                ingName = ingredientButtons.get(10).getText().toString();
                ingIndex = 11;
                isIngButton = true;
                break;

            case R.id.button11:
                ingName = ingredientButtons.get(11).getText().toString();
                ingIndex = 12;
                isIngButton = true;
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
//                Toast.makeText(MainActivity.this, "Dispensing", Toast.LENGTH_SHORT).show();
                //TODO: ADD METHOD CALL HERE, PREFERABLY CREATE A METHOD OUTSIDE OF THIS SWITCH/CASE STATEMENT
                /***
                 * INCLUDE METHOD CALL HERE
                 */
                mmHandler.obtainMessage(DISPENSE).sendToTarget();
//                dispense();

                break;
            case R.id.button14:
                if(ingSelected.size()>0){
                    undo(ingSelected.get(ingSelected.size()-1));
                    if(ingSelected.size()>0) {
                        ingSelected.remove(ingSelected.size() - 1);
                    }
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
        if(isIngButton){
            intent.putExtra("Index", ingIndex);
            intent.putExtra("Title", ingName);
            intent.putExtra("Integers",dataToArduino[ingIndex-1]);
            this.startActivityForResult(intent,ingIndex);
        }
    }
    public String dispense(int[][] array){
        String data = "";
        for (int i =0; i<array.length;i++){
            if (array[i][0]!=1){
                continue;
            }
            else{
/*                if(i<10)
                    data+=0;
                    data+=i;*/
                switch (i+1){
                    case 1:
                        data+=1;
                        data+=3;
                        break;
                    case 2:
                        data+=2;
                        data+=3;
                        break;
                    case 3:
                        data+=3;
                        data+=3;
                        break;
                    case 4:
                        data+=4;
                        data+=3;
                        break;
                    case 5:
                        data+=1;
                        data+=2;
                        break;
                    case 6:
                        data+=2;
                        data+=2;
                        break;
                    case 7:
                        data+=3;
                        data+=2;
                        break;
                    case 8:
                        data+=4;
                        data+=2;
                        break;
                    case 9:
                        data+=1;
                        data+=1;
                        break;
                    case 10:
                        data+=2;
                        data+=1;
                        break;
                    case 11:
                        data+=3;
                        data+=1;
                        break;

                    case 12:
                        data+=4;
                        data+=1;
                        break;

                }
                for (int j=1; j<array[i].length; j++){
                    data+=array[i][j];
                }
               // data+='\n';
            }
        }

        Log.e(TAG, "Dispense pressed");
        return data;

    }
    /***
     * undo - undo the latest change (excluding name change) that is read from the ingSelected.
     * @param caseNumber - this integer value is the ingredient slot number
     */
    public void undo(int caseNumber){
        System.out.println(ingSelected.toString());
        System.out.println(ingSelected.size());
        int[] zeroes = {0, 0, 0};
        if(caseNumber==99){
            runOnUiThread(undoAll);
        }
        else{
            quantityText.get(caseNumber-1).setVisibility(View.INVISIBLE);
            ingredientButtons.get(caseNumber-1).setBackgroundResource(R.drawable.circle);
            dataToArduino[caseNumber-1] = zeroes;
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
            bufferedWriter.write("Cumin Seeds");
            bufferedWriter.newLine();
            bufferedWriter.write("Ground Pepper");
            bufferedWriter.newLine();
            bufferedWriter.write("Kosher Salt");
            bufferedWriter.newLine();
            bufferedWriter.write("Fennel Seeds");
            bufferedWriter.newLine();
            bufferedWriter.write("Cinnamon");
            bufferedWriter.newLine();
            bufferedWriter.write("Chili Powder");
            bufferedWriter.newLine();
            bufferedWriter.write("Sugar");
            bufferedWriter.newLine();
            bufferedWriter.write("Coriander Powder");
            bufferedWriter.newLine();
            bufferedWriter.write("Turmeric Powder");
            bufferedWriter.newLine();
            bufferedWriter.write("Poppy Seeds");
            bufferedWriter.newLine();
            // Always close files.
            bufferedWriter.close();
            fileWriter.close();
        }
        catch(Exception ex) {
            Toast.makeText(MainActivity.this, "Error writing to file '" + fileName + "'",Toast.LENGTH_SHORT).show();
        }

    }

/*    private void CheckBt() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "Bluetooth Disabled !",
                    Toast.LENGTH_SHORT).show();
                   *//**//* It tests if the bluetooth is enabled or not, if not the app will show a message. *//**//*
        }

        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),
                    "Bluetooth null !", Toast.LENGTH_SHORT)
                    .show();
        }
    }*/


    private class ConnectThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device){
            BluetoothSocket tmp = null;
            mmDevice = device;

            try{
                tmp = mmDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            }catch(Exception e){
                e.printStackTrace();
                Log.e(TAG, e.getMessage().toString());

            }
            mmSocket = tmp;

        }

        public void run(){


            try{
                mmSocket.connect();
                connected = new ConnectedThread(mmSocket);
                connected.start();
                Log.e(TAG, "Connected");
                mmHandler.obtainMessage(SUCCESS_CONNECT,mmSocket).sendToTarget();

            }catch (IOException connectException){
                Log.e(TAG, "cannot connect");
                Log.e(TAG, connectException.getMessage());
                try{
                    runOnUiThread(toasterFailed);
                    mmSocket.close();
                    Log.e(TAG, "closed the socket");
                }catch (IOException e){
                    Log.e(TAG, "can't close the socket");

                }

            }


        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "closing connect thread");
            }
        }
    }
    public Runnable toasterSuccess = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(MainActivity.this, "Successfully connected to Souschef device!", Toast.LENGTH_SHORT).show();
        }
    };

    public Runnable toasterFailed = new Runnable() {
        @Override
        public void run() {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Souschef Device Not Found");
            alertDialog.setMessage("Please ensure that the Souschef device has been turned on. Proceed to restart this application once the Souschef device has been turned on.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
            alertDialog.show();
        }
    };
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            InputStreamReader mInputStreamReader = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            mInputStreamReader = new InputStreamReader(mmInStream);
            mBufferedReader = new BufferedReader(mInputStreamReader);
        }

        public void run() {
            Log.e(TAG, "inside connected run");
            runOnUiThread(toasterSuccess);
            String inpdata = null;
            while (true) {
                try {
                    inpdata = mBufferedReader.readLine();
                    Log.e(TAG, inpdata);
                    mmHandler.obtainMessage(MESSAGE_READ, 1, 1,
                            inpdata).sendToTarget();



                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                    Log.e(TAG, "run exception in connected thread");
                    break;
                }
            }
        }

        public void write(String data) {
            try {
                PrintStream printStream = new PrintStream(mmOutStream);
                printStream.print(data);
            } catch (Exception e) {
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }
    private void turnOnBT(){
        Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(btIntent, REQUEST_ENABLE_BT);
    }

    private void getPairedDevices(){
        devicesArray = btAdapter.getBondedDevices(); //gets the devices paired to local device
        BluetoothDevice x = null;
        if(devicesArray.size()>0){
            for(BluetoothDevice device: devicesArray){
                Log.e(TAG, device.getName());
                if(device.getName().equals("HC-05")) {
                    x = device;
                }
            }
        }
        if(x!=null){
            connect = new ConnectThread(x);
            connect.start();
        }
        else{
            Toast.makeText(this,"Unable to connect to Souschef device",Toast.LENGTH_SHORT).show();
        }
    }
    {
        mmHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case SUCCESS_CONNECT:
                        Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                        send_message(connected, "connected to android");
                        break;
                    case DISPENSE:
                        try {
                            if(!ingSelected.isEmpty()) {
                                String message = "$";
                                message += dispense(dataToArduino);
                                message += "#";
                                Log.e(TAG, message);
                                send_message(connected, message);
                                Toast.makeText(getApplicationContext(), "dispense clicked", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Message sent");
                            }else{
                                Toast.makeText(getApplicationContext(),"Please select ingredients to dispense", Toast.LENGTH_SHORT).show();
                            }
                        }catch (NullPointerException e){
                            Toast.makeText(getApplicationContext(), "Bluetooth not connected", Toast.LENGTH_SHORT).show();

                        }

                        break;
                    case MESSAGE_READ:
                        String inpData = (String) msg.obj;
                        weightValue.setText(inpData);
                        break;
                }
            }
        };
    }
    private void send_message(ConnectedThread thread, String data){
        thread.write(data);
    }
}
