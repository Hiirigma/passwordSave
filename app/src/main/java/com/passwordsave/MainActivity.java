package com.passwordsave;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private boolean isFABOpen = false;
    private FloatingActionButton fab_add = null;
    private FloatingActionButton fab_del = null;
    private FloatingActionButton fab_view = null;
    private FloatingActionButton fab_export = null;
    private FloatingActionButton fab_import = null;



    private void showFABMenu(){
        isFABOpen=true;
        fab_add.animate().translationY(-getResources().getDimension(R.dimen.standard_1));
        fab_del.animate().translationY(-getResources().getDimension(R.dimen.standard_2));
        fab_view.animate().translationY(-getResources().getDimension(R.dimen.standard_3));
        fab_export.animate().translationY(-getResources().getDimension(R.dimen.standard_4));
        fab_import.animate().translationY(-getResources().getDimension(R.dimen.standard_5));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab_add.animate().translationY(0);
        fab_del.animate().translationY(0);
        fab_view.animate().translationY(0);
        fab_export.animate().translationY(0);
        fab_import.animate().translationY(0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabMain = (FloatingActionButton) findViewById(R.id.fabMain);
        fab_add = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab_del = (FloatingActionButton) findViewById(R.id.fabDel);
        fab_view = (FloatingActionButton) findViewById(R.id.fabView);
        fab_export = (FloatingActionButton) findViewById(R.id.fabExport);
        fab_import = (FloatingActionButton) findViewById(R.id.fabImport);
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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