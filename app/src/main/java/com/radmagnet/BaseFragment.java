package com.radmagnet;


import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {


    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }


}
