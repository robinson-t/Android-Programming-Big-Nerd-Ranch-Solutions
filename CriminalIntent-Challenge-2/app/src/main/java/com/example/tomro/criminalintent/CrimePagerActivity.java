package com.example.tomro.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

    private static final String EXTRA_CRIME_ID = "com.example.tomro.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    private Button mBeginningButton;
    private Button mEndButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);
        mCrimes = CrimeLab.get(this).getCrimes();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                updateSkipButton();
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        for (int i = 0; i < mCrimes.size(); i++){
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
        mBeginningButton = (Button) findViewById(R.id.skip_beginning_button);
        mEndButton = (Button) findViewById(R.id.skip_end_button);
        mBeginningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0, false);
            }
        });
        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mCrimes.size()-1, false);
            }
        });

    }

    private void updateSkipButton() {
        int index = mViewPager.getCurrentItem();
        if (index == mCrimes.size()-1) {
            mEndButton.setEnabled(false);
            mBeginningButton.setEnabled(true);
        } else if (index == 0) {
            mBeginningButton.setEnabled(false);
            mEndButton.setEnabled(true);
        } else {
            mBeginningButton.setEnabled(true);
            mEndButton.setEnabled(true);
        }
    }

    public static Intent newIntent(Context packageContext, UUID Id) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, Id);
        return intent;
    }
}
