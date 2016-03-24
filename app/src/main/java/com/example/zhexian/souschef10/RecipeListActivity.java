package com.example.zhexian.souschef10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.Arrays;

/**
 * Created by zhexian on 2/24/2016.
 */
public class RecipeListActivity extends AppCompatActivity implements View.OnClickListener {
    public ImageView curryChicken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);
        curryChicken = (ImageView) findViewById(R.id.imageView);

        curryChicken.setOnClickListener(this);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            switch(requestCode){
                case 100:
                    Intent intent = new Intent();
                    intent.putExtra("Recipe_Ingredients", data.getStringExtra("Recipe_Ingredients"));

                    setResult(RecipeListActivity.RESULT_OK, intent);
                    finish();
                    break;
                case 999:
                    Intent intent1 = new Intent();
                    setResult(RecipeListActivity.RESULT_CANCELED, intent1);
                    finish();
                    break;
            }
        }
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.imageView:
                Intent intent = new Intent(this, RecipeActivity.class);
                int recipeIndex = 1;
                intent.putExtra("Recipe_Index",recipeIndex);
                this.startActivityForResult(intent, 100);
                break;
        }
    }
}
