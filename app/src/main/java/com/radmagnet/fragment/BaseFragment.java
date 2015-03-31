package com.radmagnet.fragment;


import android.support.v4.app.Fragment;

import com.radmagnet.activities.BaseActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {


    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }


}
