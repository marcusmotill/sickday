package eventhorizon.sickday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by marcusmotill on 4/10/15.
 */
public class Conditions_Fragment extends Fragment {

    ExpandableListView expandableListView;
    ConditionsListViewAdapter expandableListAdapter;
    List expandableListTitle;
    TextView conditionsTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.conditions_fragment, container, false);

        HashMap expandableListDetail = getData();

        expandableListView = (ExpandableListView) rootView.findViewById(R.id.conditionsListView);
        conditionsTitle = (TextView) rootView.findViewById(R.id.tvConditionsTitle);

        expandableListTitle = new ArrayList(expandableListDetail.keySet());
        expandableListAdapter = new ConditionsListViewAdapter(getActivity(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        conditionsTitle.setTypeface(App.caecilia);

        return rootView;
    }

    public HashMap getData(){

        HashMap expandableListDetail = new HashMap();


        List items = new ArrayList();
        items.add("Ear Ache");
        items.add("Eye Infections (Conjunctivitis)");
        items.add("Headaches & Migraines");
        items.add("Sinusitis");
        items.add("Sore Throat");
        items.add("Tonsillitis");
        expandableListDetail.put("Head/Eyes/Ears/Nose/Throat", items);

        items = new ArrayList();
        items.add("Body Aches");
        items.add("Chills");
        expandableListDetail.put("Flu and Fever", items);

        items = new ArrayList();
        items.add("Asthma");
        items.add("Bronchitis");
        items.add("Pneumonia");
        expandableListDetail.put("Pulmonary", items);

        items = new ArrayList();
        items.add("Bites (Animal & Insect)");
        items.add("Burns");
        items.add("Cuts & Lacerations");
        items.add("Poison Ivy");
        items.add("Rashes");
        items.add("Suturing & Suture Removal");
        expandableListDetail.put("Skin", items);

        items = new ArrayList();
        items.add("Abdominal Pain");
        items.add("Constipation");
        items.add("Diarrhea");
        items.add("Gastroenteritis");
        items.add("Heartburn & Reflux");
        items.add("Hemorrhoids");
        items.add("Nausea");
        items.add("Vomiting");
        expandableListDetail.put("Stomach/GI", items);

        items = new ArrayList();
        items.add("Back Pain");
        items.add("Herniated Disc");
        items.add("Neck Pain");
        items.add("Sprains & Strains");
        expandableListDetail.put("Muscle", items);

        items = new ArrayList();
        items.add("Abscess");
        items.add("Allergies Allergic Reactions");
        items.add("Anxiety");
        items.add("Emergency Contraception");
        items.add("High Blood Pressure");
        items.add("Insomnia");
        items.add("Nail Infections");
        items.add("Phlebitis");
        items.add("STDs (Sexually Transmitted Diseases)");
        items.add("Swollen Glands");
        items.add("UTI (Urinary Tract Infection)");
        expandableListDetail.put("General", items);

        return expandableListDetail;
    }
}
