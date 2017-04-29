package com.xdot.classroom.screens.current_schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.xdot.classroom.R;



public class CurrentScheduleDemoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_schedule_demo, container, false);

        Bundle args = getArguments();
        ((TextView) rootView.findViewById(R.id.text)).setText("Page " + args.getInt("page_position"));

        return rootView;
    }
}