package com.passwordsave;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FirstFragment extends Fragment {
    private static final String TAG = "FirstFrag";
    private TextInputEditText et_password = null;
    private String s_password_global = null;
    private boolean isFABOpen = false;
    private FloatingActionButton fab_add = null;
    private FloatingActionButton fab_del = null;
    private FloatingActionButton fab_view = null;
    private DataBaseSql dataBaseSql = null;


    private void showFABMenu(){
        isFABOpen=true;
        fab_add.animate().translationY(-getResources().getDimension(R.dimen.standard_1));
        fab_del.animate().translationY(-getResources().getDimension(R.dimen.standard_2));
        fab_view.animate().translationY(-getResources().getDimension(R.dimen.standard_3));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab_add.animate().translationY(0);
        fab_del.animate().translationY(0);
        fab_view.animate().translationY(0);
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
        FloatingActionButton fabMain = (FloatingActionButton) view.findViewById(R.id.fabMain);
        fab_add = (FloatingActionButton)  view.findViewById(R.id.fabAdd);
        fab_del = (FloatingActionButton)  view.findViewById(R.id.fabDel);
        fab_view = (FloatingActionButton)  view.findViewById(R.id.fabView);
        et_password = (TextInputEditText)view.findViewById(R.id.inp_pass);
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

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        s_password_global = et_password.getText().toString();
        final String s_digest = messageDigest.digest(s_password_global.getBytes()).toString();
        Log.d(TAG, s_digest);
        dataBaseSql = new DataBaseSql(getContext(),s_digest);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataBaseSql.createDatabase())
                {
                    Toast.makeText(getContext(), "Successful add new table", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Can't create table", Toast.LENGTH_LONG).show();
                }
            }
        });

        fab_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (dataBaseSql.checkTableExists(s_digest)) {
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                }
                else
                {
                    Toast.makeText(getContext(), "Can't verify password", Toast.LENGTH_LONG).show();
                }
            }
        });

        fab_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataBaseSql.deleteDatabase(s_digest))
                {
                    Toast.makeText(getContext(), "Some problem with deleting table", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Table isn't exists from now", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
