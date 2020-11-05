package com.passwordsave;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SecondFragment extends Fragment {
    private static final String TAG = "SecondFrag";
    private static ListView _listView = null;
    private TextInputEditText et_password = null;
    private String s_password_global = null;
    private boolean isFABOpen = false;
    private FloatingActionButton fab_change = null;
    private FloatingActionButton fab_return = null;
    private FloatingActionButton fab_export = null;
    private FloatingActionButton fab_import = null;
    public static DataBaseSql dataBaseSql;
    private final String PASS_DB_NAME = "pass";
    private final String APP_DB_NAME = "app";
    private int [] g_sa_pos = null;
    private String [][] g_sa_data = null;

    private void parseXML(boolean b_mode)
    {
        String xmlRecords = "";
        String xml_data_op = "<data>";
        String xml_data_cl = "</data>";

        String xml_id_op = "<id_>";
        String xml_id_cl = "</id_>";

        String xml_app_op = "<app_>";
        String xml_app_cl = "</app_>";

        String xml_log_op = "<log_>";
        String xml_log_cl = "</log_>";

        String xml_pass_op = "<pass_>";
        String xml_pass_cl = "</pass_>";

        String xml_info_op = "<info_>";
        String xml_info_cl = "</info_>";

        if (b_mode == true)
        {
                xmlRecords = xml_data_op;
                for (int i = 0; i < g_sa_pos.length; i++)
                {
                    xmlRecords += xml_id_op;
                    xmlRecords += Integer.toString(g_sa_pos[i]);
                    xmlRecords += xml_id_cl;

                    xmlRecords += xml_app_op;
                    xmlRecords += g_sa_data[0][i];
                    xmlRecords += xml_app_cl;

                    xmlRecords += xml_log_op;
                    xmlRecords += g_sa_data[1][i];
                    xmlRecords += xml_log_cl;

                    xmlRecords += xml_pass_op;
                    xmlRecords += g_sa_data[2][i];
                    xmlRecords += xml_pass_cl;

                    xmlRecords += xml_info_op;
                    xmlRecords += g_sa_data[3][i];
                    xmlRecords += xml_info_cl;

                }
                xmlRecords += xml_data_cl;

                Bundle bundle = new Bundle();
                bundle.putString("xml",xmlRecords);
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment,bundle);

        }
        else
        {

        }
    }


    private void paintWindow()
    {
            int dSize = dataBaseSql.getSizeDB(APP_DB_NAME);
            if (dSize <= 0)
            {
                return;
            }
            g_sa_pos = new int[dSize];
            g_sa_data = new String [6][dSize];
            final String[][] sda_data = new String [6][dSize];
            if (dataBaseSql.getFromDB(APP_DB_NAME, g_sa_pos, g_sa_data) == true) {
                for (int i = 0; i < dSize; i++)
                {
                    try
                    {
                        sda_data[0][i] = CryptoLib.decrypt(g_sa_data[0][i]);
                        sda_data[1][i] = CryptoLib.decrypt(g_sa_data[1][i]);
                        sda_data[2][i] = CryptoLib.decrypt(g_sa_data[2][i]);
                        sda_data[3][i] = CryptoLib.decrypt(g_sa_data[3][i]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ListActivity listAct = new ListActivity(getActivity(), g_sa_pos, sda_data);
                _listView.setAdapter(listAct);
            }

            _listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapter, View v, int position,
                                        long arg3)
                {
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", g_sa_pos[position]);
                    bundle.putString("app",sda_data[0][position]);
                    bundle.putString("login",sda_data[1][position]);
                    bundle.putString("pass",sda_data[2][position]);
                    bundle.putString("info",sda_data[3][position]);

                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_ThirdFragment,bundle);
                    //ChangeInputBox(saPos[position],sdaApp[position],sdaLogin[position],sdaPass[position],sdaAddit[position]);
                }
            });
    }


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
        //dataBaseSql
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fabMain = (FloatingActionButton) view.findViewById(R.id.fab_Main2);
        fab_return = (FloatingActionButton)  view.findViewById(R.id.fab_Return2);
        fab_change = (FloatingActionButton)  view.findViewById(R.id.fab_Change2);
        fab_export = (FloatingActionButton)  view.findViewById(R.id.fab_Export2);
        fab_import = (FloatingActionButton)  view.findViewById(R.id.fab_Import2);
        _listView = (ListView) view.findViewById(R.id.mylistlayout);
        dataBaseSql = new DataBaseSql(getContext());
        if (dataBaseSql.checkTableExists(APP_DB_NAME) == false) {
            if (dataBaseSql.createDatabase(APP_DB_NAME) == true) {
                Toast.makeText(getContext(), "Create new database", Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(getContext(), "Can't add new database", Toast.LENGTH_LONG).show();
                return;
            }
        }
        paintWindow();
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
                paintWindow();
            }
        });

        fab_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseXML(true);
                paintWindow();
            }
        });


        fab_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseXML(false);
                paintWindow();
            }
        });

    }

}