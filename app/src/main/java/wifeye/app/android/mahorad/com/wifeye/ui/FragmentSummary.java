package wifeye.app.android.mahorad.com.wifeye.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import wifeye.app.android.mahorad.com.wifeye.R;

public class FragmentSummary extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        FrameLayout frame = (FrameLayout) inflater.inflate(
                R.layout.fragment_summary, container, false);

        BoxView actionBox = (BoxView) frame.findViewById(R.id.action);
        actionBox.setHeader("LAST ACTION");

        actionBox.setFact("12:35 Today");
        actionBox.setDetails("Occurrence Date");

        return frame;
    }

}
