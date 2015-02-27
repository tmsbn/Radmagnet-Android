package tms.ubrats;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    @InjectView(R.id.newsDetailImage)
    public ImageView mNewsDetailIv;


    @InjectView(R.id.bookmark)
    public ImageButton mBookmarkIbtn;

    News mNews;

    MenuItem shareMenuItem;

    private ShareActionProvider mShareActionProvider;


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
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        shareMenuItem = menu.findItem(R.id.menu_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareMenuItem);
        setShareIntent();

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
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

        if (mPostId == null)
            return;

        mNews = Realm.getInstance(getActivity()).where(News.class).equalTo("postId", mPostId, true).findFirst();
        if (mNews != null) {
            Picasso.with(getActivity()).load(mNews.getImageUrl()).into(mNewsDetailIv);
            mHeadlineTv.setText((mNews.getHeadline() != null) ? mNews.getHeadline() : getActivity().getString(R.string.missingHeadline_txt));
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


    // Call to update the share intent
    private void setShareIntent() {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out this Rad Stuff!");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, mNews.getHeadline());

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(sharingIntent);
        }
    }

}
