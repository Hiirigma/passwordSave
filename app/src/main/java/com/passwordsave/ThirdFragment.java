package com.passwordsave;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class ThirdFragment extends Fragment {
    private static final String TAG = "ThirdFrag";
    private TextInputEditText et_password = null;
    private String s_password_global = null;
    private boolean isFABOpen = false;
    private FloatingActionButton fab_add = null;
    private FloatingActionButton fab_return = null;
    private FloatingActionButton fab_replace = null;
    private FloatingActionButton fab_delete = null;
    private DataBaseSql dataBaseSql = null;


    private void showFABMenu() {
        isFABOpen = true;
        fab_return.animate().translationY(-getResources().getDimension(R.dimen.standard_1));
        fab_add.animate().translationY(-getResources().getDimension(R.dimen.standard_2));
        fab_delete.animate().translationY(-getResources().getDimension(R.dimen.standard_3));
        fab_replace.animate().translationY(-getResources().getDimension(R.dimen.standard_4));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fab_return.animate().translationY(0);
        fab_add.animate().translationY(0);
        fab_delete.animate().translationY(0);
        fab_replace.animate().translationY(0);
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fabMain = (FloatingActionButton) view.findViewById(R.id.fab_Main3);
        fab_return = (FloatingActionButton) view.findViewById(R.id.fab_Return3);
        fab_add = (FloatingActionButton) view.findViewById(R.id.fab_Add3);
        fab_delete = (FloatingActionButton) view.findViewById(R.id.fab_Del3);
        fab_replace = (FloatingActionButton) view.findViewById(R.id.fab_Replace3);
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        fab_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(R.id.action_ThirdFragment_to_SecondFragment);
            }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(R.id.action_ThirdFragment_to_SecondFragment);
            }
        });


        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(R.id.action_ThirdFragment_to_SecondFragment);
            }
        });

        fab_replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(R.id.action_ThirdFragment_to_SecondFragment);
            }
        });

    }
}