package com.course.android.ct.moyosafiapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.course.android.ct.moyosafiapp.R;

public class HomeFragment extends Fragment implements VitalSignDialogFragment.VitalSignDialogListener {
    // DEFAULT CONSTRUCT
    public HomeFragment() {
        // Required empty public constructor
    }

    // VARIABLES
    private TextView systol_textView;
    private TextView diastol_textView;
    private TextView glycemie_textView;
    private ImageView modify_imageView;

    // FUNCTIONS
    // 1- onCreateView function
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // INITIALISATION
        // 1- Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 2-  Views
        systol_textView = view.findViewById(R.id.systol_textView);
        diastol_textView = view.findViewById(R.id.diastol_textView);
        glycemie_textView = view.findViewById(R.id.glycemie_textView);
        modify_imageView = view.findViewById(R.id.modify_imageView);

        // ACTIONS
        modify_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("++++++++++++++++++++++++++ l'icone edit est cliquer ++++++++++++++++++++++++++");

                openDialog();
            }
        });

        return view;
    }

    // 2- Display dialog function
    private void openDialog() {
        System.out.println("++++++++++++++++++++++++++ le dialogue est apple ++++++++++++++++++++++++++");
        VitalSignDialogFragment dialogFragment = new VitalSignDialogFragment(); // Instantiation of the dialog fragment
        dialogFragment.setListener(HomeFragment.this); // écouteur (listener) pour le DialogFragment
        dialogFragment.show(getChildFragmentManager(), "VitalSignDialogFragment"); // obtenir le gestionnaire de fragments spécifique à ce fragment et affichage du dialog
    }

    // 3- we get the change in our fragment
    @Override
    public void setTextOnMainView(String new_systol_value,String new_diastol_value, String new_glycemie_vital) {
        // Mettez à jour les TextView avec les nouvelles valeurs ici
        systol_textView.setText(new_systol_value);
        diastol_textView.setText(new_diastol_value);
        glycemie_textView.setText(new_glycemie_vital);
    }
}