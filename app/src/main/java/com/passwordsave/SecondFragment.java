package com.passwordsave;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecondFragment extends Fragment {
    private static final String TAG = "SecondFrag";
    private TextInputEditText et_password = null;
    private String s_password_global = null;
    private boolean isFABOpen = false;
    private FloatingActionButton fab_change = null;
    private FloatingActionButton fab_return = null;
    private FloatingActionButton fab_export = null;
    private FloatingActionButton fab_import = null;
    private DataBaseSql dataBaseSql = null;


    private void showFABMenu(){
        isFABOpen=true;
        fab_return.animate().translationY(-getResources().getDimension(R.dimen.standard_1));
        fab_change.animate().translationY(-getResources().getDimension(R.dimen.standard_2));
        fab_export.animate().translationY(-getResources().getDimension(R.dimen.standard_3));
        fab_import.animate().translationY(-getResources().getDimension(R.dimen.standard_4));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab_return.animate().translationY(0);
        fab_change.animate().translationY(0);
        fab_export.animate().translationY(0);
        fab_import.animate().translationY(0);
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fabMain = (FloatingActionButton) view.findViewById(R.id.fab_Main2);
        fab_return = (FloatingActionButton)  view.findViewById(R.id.fab_Return2);
        fab_change = (FloatingActionButton)  view.findViewById(R.id.fab_Change2);
        fab_export = (FloatingActionButton)  view.findViewById(R.id.fab_Export2);
        fab_import = (FloatingActionButton)  view.findViewById(R.id.fab_Import2);
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

        fab_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        fab_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_ThirdFragment);
            }
        });

    }

//    {
//        super.onViewCreated(view, savedInstanceState);
//
//        view.findViewById(R.id.fab_Return2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(SecondFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//            }
//        });
//    }
}