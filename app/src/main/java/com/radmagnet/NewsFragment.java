package com.radmagnet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;


public class NewsFragment extends BaseFragment {

    public static final String ARG_PARAM1 = "param1";

    private String mId;

    private OnFragmentInteractionListener mListener;

    @InjectView(com.radmagnet.R.id.headline)
    public TextView mHeadlineTv;

    @InjectView(com.radmagnet.R.id.date)
    public TextView mDateTv;

    @InjectView(com.radmagnet.R.id.creator)
    public TextView mCreatorTv;

    @InjectView(com.radmagnet.R.id.creatorDp)
    public ImageView mCreatorDpIv;

    @InjectView(com.radmagnet.R.id.newsImage)
    public ImageView mNewsIv;

    @InjectView(com.radmagnet.R.id.category)
    public TextView mCategoryTv;

    @InjectView(R.id.specific_details)
    public LinearLayout mSpecificDetailsContainer;


    @InjectView(com.radmagnet.R.id.categoryColor)
    public View mCategoryColor;


    @InjectView(com.radmagnet.R.id.webView)
    public WebView mWebView;

    @InjectView(R.id.location)
    public TextView mLocation;


    @InjectView(com.radmagnet.R.id.newsCategoryTitle)
    public TextView newsCategoryTitle;

    @InjectView(com.radmagnet.R.id.scrollView)
    public ScrollView mScrollView;

    Toolbar toolbar;

    News mNews;
    int color = 0;

    MenuItem shareMenuItem, bookmarkMenuItem;

    private ShareActionProvider mShareActionProvider;

    private String newsDetailsURL="http://www.radmagnet.com/GET/details/";


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
            mId = getArguments().getString(ARG_PARAM1);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case com.radmagnet.R.id.bookmark:
                bookMarkItem();

                break;

            case android.R.id.home:
                getBaseActivity().finish();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(com.radmagnet.R.layout.fragment_news, container, false);
        ButterKnife.inject(this, fragmentView);
        setupToolbar(fragmentView);

        mCategoryTv.setVisibility(View.GONE);
        setupAllViews();

        return fragmentView;
    }

    private void setupToolbar(View fragmentView) {

        toolbar = (Toolbar) fragmentView.findViewById(com.radmagnet.R.id.toolbar);
        toolbar.inflateMenu(com.radmagnet.R.menu.menu_details);
        shareMenuItem = toolbar.getMenu().findItem(com.radmagnet.R.id.share);
        bookmarkMenuItem = toolbar.getMenu().findItem(com.radmagnet.R.id.bookmark);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().finish();

            }
        });



    }

    @Override
    public void onResume() {

        super.onResume();



    }

    private void setupAllViews(){

        if (mId == null)
            return;

        mNews = Realm.getInstance(getActivity()).where(News.class).equalTo("id", mId, true).findFirst();
        if (mNews != null) {

            color = ((BaseActivity) getActivity()).getColorFromCategory(mNews.getCategory());
            setupTopDetails();
            setupWebView();
            updateBookmarkButton();
            updateShareIcon();
            updateBackIcon();


        } else {
            bookmarkMenuItem.setVisible(false);
        }


    }

    private void setupTopDetails() {

       //news category title in toolbar
        newsCategoryTitle.setText(getBaseActivity().getTitleFromConfig(mNews.getCategory()));

        try {
            //news image
            Picasso.with(getActivity()).load(mNews.getImageUrl()).into(mNewsIv);

            //creator display picture
            Picasso.with(getActivity()).load(mNews.getCreatorDp()).transform(new CircleTransform()).into(mCreatorDpIv);
        }catch (Exception e){
            e.printStackTrace();
        }

        //news headline
        mHeadlineTv.setText((mNews.getHeadline() != null) ? mNews.getHeadline() : getActivity().getString(com.radmagnet.R.string.missingHeadline_txt));

        //date
        mDateTv.setText((mNews.getCreatedDate() != null) ? new SimpleDateFormat(BaseApplication.DATE_FORMAT, Locale.US).format(mNews.getCreatedDate()) : "bla");

        //creator
        mCreatorTv.setText((mNews.getHeadline() != null) ? mNews.getCreator() : "");

        //category color
        mCategoryColor.setBackgroundColor(color);

        if (mNews.getStartDate() != null && mNews.getStartDate().getTime() != 0) {
            Drawable drawable = mSpecificDetailsContainer.getBackground();
            drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
            EventsView eventsView = new EventsView(getActivity());
            eventsView.setDate(mNews.getStartDate());
            if (mSpecificDetailsContainer.getChildCount() < 2)
                mSpecificDetailsContainer.addView(eventsView);

            mSpecificDetailsContainer.setVisibility(View.VISIBLE);

        }else{
            mSpecificDetailsContainer.setVisibility(View.GONE);
        }


        //location
        if (!mNews.getLocation().equals("")) {
            mLocation.setText(mNews.getLocation());
            mLocation.setVisibility(View.VISIBLE);
        } else {
            mLocation.setVisibility(View.GONE);
        }

    }


    public void setupWebView() {

        mWebView.loadUrl(newsDetailsURL+mNews.getCategory()+"/"+mNews.getPostId());

    }

    public void bookMarkItem() {

        Realm.getInstance(getActivity()).executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mNews.setBookmarked(!mNews.isBookmarked());
            }
        });
        updateBookmarkButton();


    }

    public void updateBackIcon() {

        Drawable updatedDrawable = getBaseActivity().applyColorToDrawable(com.radmagnet.R.drawable.ic_back, color);
        toolbar.setNavigationIcon(updatedDrawable);


    }

    public void updateBookmarkButton() {

        if (mNews.isBookmarked()) {

            bookmarkMenuItem.setIcon(new IconDrawable(getActivity(), Iconify.IconValue.fa_bookmark).color(color).actionBarSize());

        } else {
            bookmarkMenuItem.setIcon(new IconDrawable(getActivity(), Iconify.IconValue.fa_bookmark_o).color(color).actionBarSize());
        }

        bookmarkMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                bookMarkItem();

                return true;
            }
        });
    }

    private void updateShareIcon() {

        shareMenuItem.setIcon(new IconDrawable(getActivity(), Iconify.IconValue.fa_share_alt).color(color).actionBarSize());
        shareMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(com.radmagnet.R.string.checkOutThisRadStuff_txt));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, mNews.getHeadline());
                startActivity(sharingIntent);

                return false;
            }
        });

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
