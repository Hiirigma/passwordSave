package com.passwordsave;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ListActivity extends ArrayAdapter {
    private final Activity context;

    private int[] d_pos = {0};
    private String[][] ss_data;

    public ListActivity(Activity context, int[] func_d_pos, String[][] func_ss_data) {
        super(context, R.layout.activity_list, func_ss_data[0]);
        this.context = context;
        this.d_pos = func_d_pos;
        ss_data = func_ss_data;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_list, null, true);
        TextView textview_app = (TextView) rowView.findViewById(R.id.textview_app_al);
        TextView textview_login = (TextView) rowView.findViewById(R.id.textview_login_al);
        TextView sID = (TextView) rowView.findViewById(R.id.textview_id_al);
        sID.setText(Integer.toString(d_pos[position]));
        textview_app.setText("App: " + ss_data[0][position]);
        textview_login.setText("Login: " + ss_data[1][position]);
        return rowView;
    }
}