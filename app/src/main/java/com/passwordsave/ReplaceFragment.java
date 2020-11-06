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

public class ReplaceFragment extends Fragment {
    private FloatingActionButton fab_rep = null;
    private FloatingActionButton fab_ret = null;
    private final String PASS_DB_NAME = "pass";
    private final String APP_DB_NAME = "app";
    private TextInputEditText et_old_password = null;
    private TextInputEditText et_new_password = null;
    private boolean isFABOpen = false;

    private void showFABMenu() {
        isFABOpen = true;
        fab_ret.animate().translationY(-getResources().getDimension(R.dimen.standard_1));
        fab_rep.animate().translationY(-getResources().getDimension(R.dimen.standard_2));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fab_ret.animate().translationY(0);
        fab_rep.animate().translationY(0);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_replace, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fabMain = (FloatingActionButton) view.findViewById(R.id.fabMainRep);
        fab_rep = (FloatingActionButton) view.findViewById(R.id.fab_pass_rep);
        fab_ret = (FloatingActionButton) view.findViewById(R.id.fab_pass_ret);
        et_old_password = (TextInputEditText) view.findViewById(R.id.old_change);
        et_new_password = (TextInputEditText) view.findViewById(R.id.new_change);

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

        fab_ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(ReplaceFragment.this)
                        .navigate(R.id.action_ReplaceFragment_to_FirstFragment);
            }
        });

        fab_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String s_old_pass = et_old_password.getText().toString();
                String s_new_pass = et_new_password.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("old_pass",s_old_pass);
                bundle.putString("new_pass",s_new_pass);
                NavHostFragment.findNavController(ReplaceFragment.this)
                        .navigate(R.id.action_ReplaceFragment_to_FirstFragment,bundle);
            }
        });

    }
}
