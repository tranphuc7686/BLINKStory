package com.example.blinkstory.details;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.example.blinkstory.BaseActivity;
import com.example.blinkstory.Constant;
import com.example.blinkstory.R;
import com.example.blinkstory.model.entity.Element;

import java.util.Objects;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DetailElementActivity extends BaseActivity {


    private Runnable detailImageFragment = () -> {
        Element element = Objects.requireNonNull(getIntent().getExtras()).getParcelable(Constant.ELEMENT);
        DetailImageElementFragment detailImageElementFragment = DetailImageElementFragment.newInstance(element);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.element_details_container, detailImageElementFragment).commitAllowingStateLoss();


    };
    private Runnable detailVideoFragment = () -> {
        Element element = Objects.requireNonNull(getIntent().getExtras()).getParcelable(Constant.ELEMENT);
        DetailVideoElementFragment detailVideoElementFragment = DetailVideoElementFragment.newInstance(element);
        getSupportFragmentManager().beginTransaction().add(R.id.element_details_container, detailVideoElementFragment).commit();


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_element);
        //depency inject
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null && extras.containsKey(Constant.ELEMENT)) {
                Element element = extras.getParcelable(Constant.ELEMENT);
                if (element != null) {
                    if (element.getTypeData() == Constant.TYPE_IMAGE) {
                        detailImageFragment.run();
                    } else {
                        detailVideoFragment.run();
                    }
                }
            }
        }
    }
}
