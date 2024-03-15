package com.course.android.ct.moyosafiapp.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.course.android.ct.moyosafiapp.R;
import com.course.android.ct.moyosafiapp.models.SessionManager;
import com.course.android.ct.moyosafiapp.models.entity.Notifications;
import com.course.android.ct.moyosafiapp.ui.AuthentificationActivity;
import com.course.android.ct.moyosafiapp.viewModel.PatientViewModel;
import com.course.android.ct.moyosafiapp.viewModel.injections.ViewModelFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotificationsFragment extends Fragment {

    // VARIABLES
    private TextView actual_date_default_notification ;
    private SessionManager sessionManager;
    private PatientViewModel patientViewModel;
    private ListView listView;
    private LinearLayout default_display_layout ;
    private ArrayAdapter<Notifications> adapter; // Déclaration de l'adaptateur ici
    private Context context;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        patientViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance(requireActivity())).get(PatientViewModel.class);

        // GET VIEWS
        actual_date_default_notification = view.findViewById(R.id.actual_date_default_notification);
        default_display_layout = view.findViewById(R.id.default_display_layout);

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

        actual_date_default_notification.setText(actualDate +" | "+actualTime); // display the result

        listView = view.findViewById(R.id.listViewNotifications);

        patientViewModel.getAllNotifications().observe(getActivity(), new Observer<List<Notifications>>() {
            @Override
            public void onChanged(List<Notifications> notifications) {
                if (isAdded()) { // Vérifiez si le fragment est attaché à son contexte
                    if (notifications != null && !notifications.isEmpty()) {
                        // Mettre à jour l'adaptateur avec les nouvelles données
                        NotificationsAdapter adapter = new NotificationsAdapter(requireContext().getApplicationContext(), notifications);
                        listView.setAdapter(adapter);
                    } else {
                        System.out.println("+++++++++++++++++++++++++++++ le listeView est vide ++++++++++++++++++++++++++++++++++++");
                        default_display_layout.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                }
            }
        });

        return view;
    }


    // Classe interne privée pour l'adaptateur
    private class NotificationsAdapter extends ArrayAdapter<Notifications> {

        public NotificationsAdapter(Context context, List<Notifications> notifications) {
            super(context, 0, notifications);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.notification_item, parent, false);
            }

            Notifications notification = getItem(position); // Récupérer la notification à cette position

            // Récupérer les références des TextViews dans la vue
            TextView dateTextView = view.findViewById(R.id.date_of_notification);
            TextView messageTextView = view.findViewById(R.id.content_notification);

            // Affecter les données de la notification aux TextViews correspondants
            if (notification != null) {
                dateTextView.setText(notification.getNotification_date()+" | "+notification.getNotification_time());
                messageTextView.setText(notification.getNotification_content());
            }

            return view;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }
}