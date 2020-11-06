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

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class FirstFragment extends Fragment {
    private static final String TAG = "FirstFrag";
    public static DataBaseSql dataBaseSql;
    private TextInputEditText et_password = null;
    private String s_password_global = null;
    private boolean isFABOpen = false;
    private FloatingActionButton fab_del = null;
    private FloatingActionButton fab_view = null;
    private FloatingActionButton fab_rep = null;
    private final String PASS_DB_NAME = "pass";
    private final String APP_DB_NAME = "app";
    private long d_pass_id = -1;


    private void showFABMenu(){
        isFABOpen=true;
        fab_del.animate().translationY(-getResources().getDimension(R.dimen.standard_1));
        fab_view.animate().translationY(-getResources().getDimension(R.dimen.standard_2));
        fab_rep.animate().translationY(-getResources().getDimension(R.dimen.standard_3));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab_del.animate().translationY(0);
        fab_view.animate().translationY(0);
        fab_rep.animate().translationY(0);
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
        fab_rep = (FloatingActionButton)  view.findViewById(R.id.fabRep);
        et_password = (TextInputEditText)view.findViewById(R.id.inp_pass);
        dataBaseSql = new DataBaseSql(getContext());
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
                int d_err = 0;

                if (dataBaseSql.checkTableExists(PASS_DB_NAME)) {
                    d_err = dataBaseSql.verifyPassword(PASS_DB_NAME,s_digest);
                    if (d_err == -2)
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
                    if (d_err == -1)
                    {
                        Toast.makeText(getContext(), "Password incorrect", Toast.LENGTH_LONG).show();
                        return;
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

        fab_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_ReplaceFragment);
            }
        });

       if (getArguments() != null)
       {
           String old_pass = getArguments().getString("old_pass");
           String new_pass = getArguments().getString("new_pass");

           if (!(old_pass.isEmpty() && new_pass.isEmpty()))
           {
               String s_digest_old_pass = CryptoLib.md5(old_pass);
               String s_digest_new_pass = CryptoLib.md5(new_pass);

               int d_err = dataBaseSql.verifyPassword(PASS_DB_NAME,s_digest_old_pass);

               if (d_err == -1)
               {
                   Toast.makeText(getContext(), "Password incorrect", Toast.LENGTH_LONG).show();
                   return;
               }
               if (d_err == -2)
               {
                   Toast.makeText(getContext(), "Can't verify table", Toast.LENGTH_LONG).show();
                   return;
               }
               dataBaseSql.deleteItem(PASS_DB_NAME,d_err);
               dataBaseSql.addPasswordToDatabase(PASS_DB_NAME,s_digest_new_pass);

               int d_size = dataBaseSql.getSizeDB(APP_DB_NAME);
               if (d_size == 0)
               {
                   return;
               }
               String s_enc_data[]= new String[6];
               int[] sa_pos = new int[d_size];
               String[][] sa_data = new String [6][d_size];

               try {
                   CryptoLib.setKey(new_pass);
               } catch (NoSuchPaddingException e) {
                   e.printStackTrace();
               } catch (NoSuchAlgorithmException e) {
                   e.printStackTrace();
               }
               for (int i = 0; i < d_size; i++)
               {
                   dataBaseSql.getFromDB(APP_DB_NAME,sa_pos,sa_data);
                   try {
                       s_enc_data[0] = CryptoLib.encrypt(CryptoLib.decrypt(sa_data[0][i],old_pass));
                       s_enc_data[1] = CryptoLib.encrypt(CryptoLib.decrypt(sa_data[1][i],old_pass));
                       s_enc_data[2] = CryptoLib.encrypt(CryptoLib.decrypt(sa_data[2][i],old_pass));
                       s_enc_data[3] = CryptoLib.encrypt(CryptoLib.decrypt(sa_data[3][i],old_pass));
                   } catch (InvalidKeyException e) {
                       e.printStackTrace();
                   } catch (UnsupportedEncodingException e) {
                       e.printStackTrace();
                   } catch (InvalidAlgorithmParameterException e) {
                       e.printStackTrace();
                   } catch (IllegalBlockSizeException e) {
                       e.printStackTrace();
                   } catch (BadPaddingException e) {
                       e.printStackTrace();
                   }

                   dataBaseSql.addToToDatabaseById(APP_DB_NAME,s_enc_data,sa_pos[i]);
               }

           }
       }

    }
}
