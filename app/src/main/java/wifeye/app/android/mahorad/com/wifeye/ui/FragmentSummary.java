package wifeye.app.android.mahorad.com.wifeye.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import wifeye.app.android.mahorad.com.wifeye.R;

public class FragmentSummary extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View frame = inflater.inflate(
                R.layout.fragment_summary, container, false);

        BoxView a = (BoxView) frame.findViewById(R.id.a);
        a.setHeader("LAST ACTION");
        ImageView timer = new ImageView(container.getContext());
        timer.setImageResource(R.drawable.timer);
        a.setContents(timer);
        a.setFact("1,215 cals in");
        a.setCaption("460 cals left");

        BoxView b = (BoxView) frame.findViewById(R.id.b);
        b.setHeader("APPLICATIONS");
        ImageView info = new ImageView(container.getContext());
        info.setImageResource(R.drawable.info);
        b.setContents(info);
        b.setFact("15,966 steps");
        b.setCaption("1,300 steps to go");

        return frame;
    }

}
