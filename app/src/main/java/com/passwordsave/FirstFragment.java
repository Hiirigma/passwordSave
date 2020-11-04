package com.passwordsave;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class FirstFragment extends Fragment {
    private TextInputEditText et_password = null;
    private String s_password = null;
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
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fabMain = (FloatingActionButton)  view.findViewById(R.id.fabMain);
        fab_add = (FloatingActionButton)  view.findViewById(R.id.fabAdd);
        fab_del = (FloatingActionButton)  view.findViewById(R.id.fabDel);
        fab_view = (FloatingActionButton)  view.findViewById(R.id.fabView);
        fab_export = (FloatingActionButton)  view.findViewById(R.id.fabExport);
        fab_import = (FloatingActionButton)  view.findViewById(R.id.fabImport);
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
        fab_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_password = (TextInputEditText) view.findViewById(R.id.inp_pass);
                s_password = et_password.getText().toString();
                
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }
}