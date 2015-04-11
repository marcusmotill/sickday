package msqrd.sickday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by marcusmotill on 4/10/15.
 */
public class FAQ_Fragment extends Fragment {

    ExpandableListView expandableListView;
    ConditionsListViewAdapter expandableListAdapter;
    List expandableListTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.faq_fragment, container, false);

        HashMap expandableListDetail = getData();

        expandableListView = (ExpandableListView) rootView.findViewById(R.id.faqListView);
        expandableListTitle = new ArrayList(expandableListDetail.keySet());
        expandableListAdapter = new ConditionsListViewAdapter(getActivity(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        return rootView;
    }

    public HashMap getData(){

        HashMap expandableListDetail = new HashMap();


        List items = new ArrayList();
        items.add("Our flat fee is very affordable.  We do not add on any hidden fees. We take the time to provide highly personalized care.");
        items.add("According to The New York Times, the average cost of a house call in 2013 \"is around $500 a visit.\" Some services charge nearly $700. We charge just $275, and it’s reimbursed by most insurance providers.");
        items.add("Within 60–90 minutes of your call, we’re at your door.");
        items.add("The next day we check back with you to see how you’re doing and answer any additional follow-up questions you may have.");
        expandableListDetail.put("What makes Sickday different?", items);

        items = new ArrayList();
        items.add("No, but we’ll be at your door within 60–90 minutes. Just call us at \n" +
                "(212) SICKDAY about an hour before you want to see us.");
        expandableListDetail.put("Do you take appointments?", items);

        items = new ArrayList();
        items.add("Yes we prescribe medications as needed to treat your condition.");
        expandableListDetail.put("Do you prescribe medications?", items);

        items = new ArrayList();
        items.add("Absolutely. We’re an at-your-door service, so we’ll see you wherever you happen to need us.");
        expandableListDetail.put("Can you treat me in the office?", items);

        items = new ArrayList();
        items.add("In order to keep our cost affordable and our response time to a minimum, we currently visit patients in New York City.");
        expandableListDetail.put("What parts of New York do you cover?", items);

        items = new ArrayList();
        items.add("We treat patients two years old and up.");
        expandableListDetail.put("Do you treat patients of all ages?", items);

        items = new ArrayList();
        items.add("If you’re having a medical emergency, please stop reading this and go directly to the emergency room or dial 911.");
        items.add("Sickday isn’t an emergency room replacement and we don’t treat patients experiencing chest pains, shortness of breath, or head trauma—these are serious conditions that may require immediate hospitalization");
        expandableListDetail.put("Should I call Sickday in an emergency?", items);

        items = new ArrayList();
        items.add("We’re available 7 days a week; call us anytime from 6am–11pm Eastern Time");
        expandableListDetail.put("Is Sickday always available?", items);

        items = new ArrayList();
        items.add("Call Sickday at (212) 742-5329 between 6am-11pm Eastern Time");
        expandableListDetail.put("What should I do if I am having trouble using the app?", items);

        return expandableListDetail;
    }
}
