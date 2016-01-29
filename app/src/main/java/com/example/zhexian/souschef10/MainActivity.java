package com.example.zhexian.souschef10;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Spinnaker-Regular.ttf");
        Button buttonText = (Button)findViewById(R.id.button);
        buttonText.setTypeface(myTypeface);
        Button buttonText1 = (Button)findViewById(R.id.button2);
        buttonText1.setTypeface(myTypeface);
        Button buttonText2 = (Button)findViewById(R.id.button3);
        buttonText2.setTypeface(myTypeface);
        Button buttonText3 = (Button)findViewById(R.id.button4);
        buttonText3.setTypeface(myTypeface);
        Button buttonText4 = (Button)findViewById(R.id.button5);
        buttonText4.setTypeface(myTypeface);
        Button buttonText5 = (Button)findViewById(R.id.button6);
        buttonText5.setTypeface(myTypeface);
        Button buttonText6 = (Button)findViewById(R.id.button7);
        buttonText6.setTypeface(myTypeface);
        Button buttonText7 = (Button)findViewById(R.id.button8);
        buttonText7.setTypeface(myTypeface);
        Button buttonText8 = (Button)findViewById(R.id.button9);
        buttonText8.setTypeface(myTypeface);
        Button buttonText9 = (Button)findViewById(R.id.button10);
        buttonText9.setTypeface(myTypeface);
        Button buttonText10 = (Button)findViewById(R.id.button11);
        buttonText10.setTypeface(myTypeface);
        Button buttonText11 = (Button)findViewById(R.id.button12);
        buttonText11.setTypeface(myTypeface);

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
