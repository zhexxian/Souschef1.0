package com.example.zhexian.souschef10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Syuqri on 2/29/2016.
 * copy pasted from RLA, TODO: will improve on this
 */
public class RecipeActivity extends AppCompatActivity implements View.OnClickListener{
    public Button selectButton;
    public Button cancelButton;
    public String recipeIngredients;
    public int recipeIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_currychicken);
        selectButton = (Button)findViewById(R.id.button22);
        cancelButton = (Button) findViewById(R.id.button23);

        recipeIndex = getIntent().getExtras().getInt("Recipe_Index");
        selectButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        switch (recipeIndex){
            case 1:
                recipeIngredients = "0 0 0:1 4 0:0 0 0:0 0 0:1 0 3:0 0 0:1 0 4:0 0 0:0 0 0:0 0 0:0 0 0:0 0 0";
                break;
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
        if (id == R.id.action_settings) {return true;}
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button22: //acceptbutton
                Intent intent = new Intent();
                intent.putExtra("Recipe_Ingredients",recipeIngredients);
                setResult(RecipeActivity.RESULT_OK,intent);
                finish();
                break;
        }
    }
}

