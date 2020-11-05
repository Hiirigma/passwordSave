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

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

public class FirstFragment extends Fragment {
    private static final String TAG = "FirstFrag";
    public static DataBaseSql dataBaseSql;
    private TextInputEditText et_password = null;
    private String s_password_global = null;
    private boolean isFABOpen = false;
    private FloatingActionButton fab_del = null;
    private FloatingActionButton fab_view = null;
    private final String PASS_DB_NAME = "pass";
    private final String APP_DB_NAME = "app";
    private long d_pass_id = -1;


    private void showFABMenu(){
        isFABOpen=true;
        fab_del.animate().translationY(-getResources().getDimension(R.dimen.standard_1));
        fab_view.animate().translationY(-getResources().getDimension(R.dimen.standard_2));
    }

    private void closeFABMenu(){
        isFABOpen=false;
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

        fab_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                s_password_global = et_password.getText().toString();
                final String s_digest = CryptoLib.md5(s_password_global);
                Log.d(TAG, s_digest);
                dataBaseSql = new DataBaseSql(getContext());


                if (dataBaseSql.checkTableExists(PASS_DB_NAME)) {
                    if (dataBaseSql.verifyPassword(PASS_DB_NAME,s_digest) == -1)
                    {
                        d_pass_id = dataBaseSql.addPasswordToDatabase(PASS_DB_NAME,s_digest);
                        if (d_pass_id == -1)
                        {
                            Toast.makeText(getContext(), "Can't add password", Toast.LENGTH_LONG).show();
                            return;
                        }
                        else {
                            Toast.makeText(getContext(), "Successfully add password", Toast.LENGTH_LONG).show();
                        }
                    }
                    try {
                        CryptoLib.setKey(s_password_global);
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                }
                else
                {
                    Toast.makeText(getContext(), "Can't verify password", Toast.LENGTH_LONG).show();
                    if (dataBaseSql.createDatabase(PASS_DB_NAME))
                    {
                        Toast.makeText(getContext(), "Successful add new table", Toast.LENGTH_LONG).show();
                        if (dataBaseSql.verifyPassword(PASS_DB_NAME,s_digest) != -1)
                        {
                            Toast.makeText(getContext(), "Password already exists", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            d_pass_id = dataBaseSql.addPasswordToDatabase(PASS_DB_NAME,s_digest);
                            if (d_pass_id == -1)
                            {
                                Toast.makeText(getContext(), "Can't add password", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getContext(), "Successfully add password", Toast.LENGTH_LONG).show();
                                try {
                                    CryptoLib.setKey(s_password_global);
                                } catch (NoSuchPaddingException e) {
                                    e.printStackTrace();
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                }
                                NavHostFragment.findNavController(FirstFragment.this)
                                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Can't create table", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        fab_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_password_global = et_password.getText().toString();
                final String s_digest = CryptoLib.md5(s_password_global);
                Log.d(TAG, s_digest);
                dataBaseSql = new DataBaseSql(getContext());

                if (dataBaseSql.deletePasswordFromDatabase(PASS_DB_NAME,s_digest) == -1)
                {
                    Toast.makeText(getContext(), "Some problem with deleting table", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Password table isn't exists from now", Toast.LENGTH_LONG).show();
                    if (dataBaseSql.deleteDatabase(APP_DB_NAME) == false)
                    {
                        Toast.makeText(getContext(), "Successfully delete applications table", Toast.LENGTH_LONG).show();
                    }else
                    {
                        Toast.makeText(getContext(), "Can't delete applications table", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }
}
