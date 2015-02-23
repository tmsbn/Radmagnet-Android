package tms.ubrats;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;


public class NewsFragment extends Fragment {

    public static final String ARG_PARAM1 = "param1";

    private String mPostId;

    private OnFragmentInteractionListener mListener;

    @InjectView(R.id.headline)
    public TextView mHeadlineTv;

    @InjectView(R.id.date)
    public TextView mDateTv;


    @InjectView(R.id.bookmark)
    public ImageButton mBookmarkIbtn;

    News mNews;


    public static NewsFragment newInstance(String postId) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, postId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPostId = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.inject(this, fragmentView);


        return fragmentView;
    }

    @Override
    public void onResume() {

        super.onResume();


        mNews = Realm.getInstance(getActivity()).where(News.class).equalTo("postId", mPostId, true).findFirst();
        if (mNews != null) {

            mHeadlineTv.setText((mNews.getHeadline() != null) ? mNews.getHeadline() : "glue");
            mDateTv.setText((mNews.getCreatedDate() != null) ? new SimpleDateFormat(BaseApplication.DATE_FORMAT, Locale.US).format(mNews.getCreatedDate()) : "bla");
            updateBookmarkButton();

        } else {
            mBookmarkIbtn.setVisibility(View.GONE);
        }

        Log.v("onResume", "onResume of Fragment was called");

    }

    public void updateBookmarkButton() {

        if (mNews.isBookmarked()) {

            mBookmarkIbtn.setActivated(true);
        } else {
            mBookmarkIbtn.setActivated(false);
        }

    }

    @OnClick(R.id.bookmark)
    public void bookMarkItem() {

        Realm.getInstance(getActivity()).executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mNews.setBookmarked(!mNews.isBookmarked());
            }
        });

        updateBookmarkButton();


    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(Uri uri);
    }

}
