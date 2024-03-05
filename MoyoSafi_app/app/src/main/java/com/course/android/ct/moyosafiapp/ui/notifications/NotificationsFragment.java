package com.course.android.ct.moyosafiapp.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.course.android.ct.moyosafiapp.R;
import com.course.android.ct.moyosafiapp.models.SessionManager;
import com.course.android.ct.moyosafiapp.ui.AuthentificationActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationsFragment extends Fragment {

    // VARIABLES
    private TextView actual_date_default_notification ;
    private SessionManager sessionManager;

    // DEFAULT CONSTRUCT
    public NotificationsFragment() {
        // Required empty public constructor
    }


    // FUNCTIONS
    // 1- first function
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = SessionManager.getInstance(getContext());

        // REDIRECT THE USER WHEN HI IS LOGGED IN
        if (!SessionManager.getInstance(getContext()).isLoggedIn()) {
            System.out.println("+++++++++++++++++++++++ le patient est connecté :"+sessionManager.getUser_name()+"++++++");
            Intent intent = new Intent(getActivity().getApplicationContext(), AuthentificationActivity.class); // we take the Authentification activity
            startActivity(intent); // we start it
        }
    }
    // 2- second function
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // GET VIEWS
        actual_date_default_notification = view.findViewById(R.id.actual_date_default_notification);

        // ACTIONS
        // 1- to actual_date_default_notification view
            LocalDateTime now = null; // date and actual time variable
            DateTimeFormatter formatterDate = null; // date format variable
            DateTimeFormatter formatterTime = null; // time format variable
            String actualDate = "";
            String actualTime = "";

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                now = LocalDateTime.now(); // get date and actual time
                formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy"); // format date
                formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss"); // format time
                actualDate = now.format(formatterDate); // convert date in string
                actualTime = now.format(formatterTime); // convert time in string
            }

            actual_date_default_notification.setText(actualDate +" | "+actualTime); //display the result

        return view;
    }
}