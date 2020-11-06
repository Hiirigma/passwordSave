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


public class XmlFragment extends Fragment {
    private static final String TAG = "XmlFrag";
    private boolean isFABOpen = false;
    private FloatingActionButton fab_import = null;
    private FloatingActionButton fab_return = null;
    private TextInputEditText tiet_xml = null;
    private TextInputEditText tiet_pass = null;

    private void showFABMenu() {
        isFABOpen = true;
        fab_return.animate().translationY(-getResources().getDimension(R.dimen.standard_1));
        fab_import.animate().translationY(-getResources().getDimension(R.dimen.standard_2));
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fab_return.animate().translationY(0);
        fab_import.animate().translationY(0);
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_xml, container, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FloatingActionButton fabMain = (FloatingActionButton) view.findViewById(R.id.fab_main_xml);
        fab_return = (FloatingActionButton) view.findViewById(R.id.fab_ret_xml);
        fab_import = (FloatingActionButton) view.findViewById(R.id.fab_import_xml);

        tiet_xml = (TextInputEditText)view.findViewById(R.id.xml_line);
        tiet_pass = (TextInputEditText)view.findViewById(R.id.edittext_pass_xml);
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

        // check bundle
        if (getArguments() == null)
        {
            Toast.makeText(getContext(), "Nothing to export or import", Toast.LENGTH_LONG).show();
            return;
        }
        else
        {
            if (getArguments().getBoolean("mode") == true) {
                String s_xml = getArguments().getString("xml");
                tiet_xml.setText(s_xml);
            }
        }

        fab_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(XmlFragment.this)
                        .navigate(R.id.action_XmlFragment_to_SecondFragment);
            }
        });

        fab_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tiet_xml == null || tiet_pass == null) {
                    Toast.makeText(getContext(), "Trouble with tiet object", Toast.LENGTH_LONG).show();
                    return;
                }

                if (getArguments() == null) {
                    Toast.makeText(getContext(), "Nothing to export or import", Toast.LENGTH_LONG).show();
                    return;
                }


                if (getArguments().getBoolean("mode") == false) {
                    //String s_xml = getArguments().getString("xml");
                    Bundle bundle = new Bundle();
                    bundle.putString("xml", tiet_xml.getEditableText().toString());
                    bundle.putString("pass", tiet_pass.getEditableText().toString());
                    NavHostFragment.findNavController(XmlFragment.this)
                            .navigate(R.id.action_XmlFragment_to_SecondFragment, bundle);
                }
            }
        });

    }
}