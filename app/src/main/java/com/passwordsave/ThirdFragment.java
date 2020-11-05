package com.passwordsave;

import android.os.Bundle;
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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class ThirdFragment extends Fragment {
    private static final String TAG = "ThirdFrag";
    private TextInputEditText et_password = null;
    private String s_password_global = null;
    private boolean isFABOpen = false;
    private FloatingActionButton fab_add = null;
    private FloatingActionButton fab_return = null;
    private FloatingActionButton fab_delete = null;
    public static DataBaseSql dataBaseSql;
    private TextInputEditText tiet_app = null;
    private TextInputEditText tiet_login = null;
    private TextInputEditText tiet_info = null;
    private TextInputEditText tiet_pass = null;
    private final String PASS_DB_NAME = "pass";
    private final String APP_DB_NAME = "app";
    private String [] s_a_data = null;
    private String [] s_a_enc_data = null;
    private int g_d_id = -1;

    String g_s_app = null;
    String g_s_login = null;
    String g_s_pass = null;
    String g_s_info = null;

    private void showFABMenu() {
        isFABOpen = true;
        fab_return.animate().translationY(-getResources().getDimension(R.dimen.standard_1));
        fab_add.animate().translationY(-getResources().getDimension(R.dimen.standard_2));
        fab_delete.animate().translationY(-getResources().getDimension(R.dimen.standard_3));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fab_return.animate().translationY(0);
        fab_add.animate().translationY(0);
        fab_delete.animate().translationY(0);
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    private boolean checkBundle()
    {
        if (getArguments() == null)
        {
            return false;
        }
        if (getArguments().size() != 0)
        {
            g_s_app = getArguments().getString("app");
            g_s_login = getArguments().getString("login");
            g_s_pass = getArguments().getString("pass");
            g_s_info = getArguments().getString("info");
            g_d_id = getArguments().getInt("id");
            if (tiet_app == null || tiet_login == null || tiet_pass == null || tiet_info == null)
            {
                Toast.makeText(getContext(), "Trouble with tiet object", Toast.LENGTH_LONG).show();
                return false;
            }
            tiet_app.setText(g_s_app);
            tiet_login.setText(g_s_login);
            tiet_pass.setText(g_s_pass);
            tiet_info.setText(g_s_info);
            return true;
        }
        return false;
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FloatingActionButton fabMain = (FloatingActionButton) view.findViewById(R.id.fab_Main3);
        fab_return = (FloatingActionButton) view.findViewById(R.id.fab_Return3);
        fab_add = (FloatingActionButton) view.findViewById(R.id.fab_Add3);
        fab_delete = (FloatingActionButton) view.findViewById(R.id.fab_Del3);

        tiet_app = (TextInputEditText)view.findViewById(R.id.edittext_app3);
        tiet_login = (TextInputEditText)view.findViewById(R.id.edittext_login3);
        tiet_pass = (TextInputEditText)view.findViewById(R.id.edittext_pass3);
        tiet_info = (TextInputEditText)view.findViewById(R.id.edittext_info3);
        s_a_data = new String[6];
        s_a_enc_data = new String[6];
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

        checkBundle();

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
                if (tiet_app == null || tiet_login == null || tiet_pass == null || tiet_info == null)
                {
                    Toast.makeText(getContext(), "Trouble with tiet object", Toast.LENGTH_LONG).show();
                    return;
                }

                dataBaseSql = new DataBaseSql(getContext());

                // app
                s_a_data[0] = tiet_app.getEditableText().toString();
                // login
                s_a_data[1] = tiet_login.getEditableText().toString();
                // pass
                s_a_data[2] = tiet_pass.getEditableText().toString();
                // info
                s_a_data[3] = tiet_info.getEditableText().toString();

                if (s_a_data[0].equals("") || s_a_data[1].equals("") || s_a_data[2].equals(""))
                {
                    Toast.makeText(getContext(), "Please enter in the pole", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    s_a_enc_data[0] = CryptoLib.encrypt(s_a_data[0]);
                    s_a_enc_data[1] = CryptoLib.encrypt(s_a_data[1]);
                    s_a_enc_data[2] = CryptoLib.encrypt(s_a_data[2]);
                    s_a_enc_data[3] = CryptoLib.encrypt(s_a_data[3]);

                    if (g_d_id == -1) {
                        if (dataBaseSql.addToDatabase(APP_DB_NAME, s_a_enc_data) == false) {
                            Toast.makeText(getContext(), "Can't add data to database", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Successfully add data to table", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        if (dataBaseSql.addToToDatabaseById(APP_DB_NAME, s_a_enc_data,g_d_id) == false) {
                            Toast.makeText(getContext(), "Can't add data to database", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Successfully add data to table", Toast.LENGTH_SHORT).show();
                        }
                    }
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

                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(R.id.action_ThirdFragment_to_SecondFragment);
            }
        });


        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tiet_app == null || tiet_login == null || tiet_pass == null || tiet_info == null)
                {
                    Toast.makeText(getContext(), "Trouble with tiet object", Toast.LENGTH_LONG).show();
                    return;
                }

                dataBaseSql = new DataBaseSql(getContext());

                // app
                s_a_data[0] = tiet_app.getEditableText().toString();
                // login
                s_a_data[1] = tiet_login.getEditableText().toString();
                // pass
                s_a_data[2] = tiet_pass.getEditableText().toString();
                // info
                s_a_data[3] = tiet_info.getEditableText().toString();

                if (s_a_data[0].equals("") || s_a_data[1].equals("") || s_a_data[2].equals(""))
                {
                    Toast.makeText(getContext(), "Please enter in the pole", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    s_a_enc_data[0] = CryptoLib.encrypt(s_a_data[0]);
                    s_a_enc_data[1] = CryptoLib.encrypt(s_a_data[1]);
                    s_a_enc_data[2] = CryptoLib.encrypt(s_a_data[2]);
                    s_a_enc_data[3] = CryptoLib.encrypt(s_a_data[3]);

                    long d_id = dataBaseSql.verifyData(APP_DB_NAME, s_a_enc_data);
                    if (d_id != -1)
                    {
                        dataBaseSql.deleteItem(APP_DB_NAME, d_id);
                        Toast.makeText(getContext(), "Successfully add data to table", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Can't find data", Toast.LENGTH_SHORT).show();
                    }
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

                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(R.id.action_ThirdFragment_to_SecondFragment);
            }
        });

    }
}