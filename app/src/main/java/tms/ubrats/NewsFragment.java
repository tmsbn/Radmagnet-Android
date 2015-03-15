package tms.ubrats;

import android.app.Activity;
import android.content.Intent;
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

    private String mPostId;

    private OnFragmentInteractionListener mListener;

    @InjectView(R.id.headline)
    public TextView mHeadlineTv;

    @InjectView(R.id.date)
    public TextView mDateTv;

    @InjectView(R.id.creator)
    public TextView mCreatorTv;

    @InjectView(R.id.creatorDp)
    public ImageView mCreatorDpIv;

    @InjectView(R.id.newsImage)
    public ImageView mNewsIv;

    @InjectView(R.id.category)
    public View mCategory;


    @InjectView(R.id.categoryColor)
    public View mCategoryColor;


    @InjectView(R.id.webView)
    public WebView mWebView;


    @InjectView(R.id.scrollView)
    public ScrollView mScrollView;

    Toolbar toolbar;

    News mNews;
    int color = 0;

    MenuItem shareMenuItem, bookmarkMenuItem;

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
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.bookmark:
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

        View fragmentView = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.inject(this, fragmentView);
        setupToolbar(fragmentView);

        mCategory.setVisibility(View.GONE);
        setupAllViews();

        return fragmentView;
    }

    private void setupToolbar(View fragmentView) {

        toolbar = (Toolbar) fragmentView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_details);
        shareMenuItem = toolbar.getMenu().findItem(R.id.share);
        bookmarkMenuItem = toolbar.getMenu().findItem(R.id.bookmark);
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

        if (mPostId == null)
            return;

        mNews = Realm.getInstance(getActivity()).where(News.class).equalTo("postId", mPostId, true).findFirst();
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

        toolbar.setTitle(getBaseActivity().getStyledActionTitle(mNews.getCategory()));
        Picasso.with(getActivity()).load(mNews.getImageUrl()).into(mNewsIv);
        Picasso.with(getActivity()).load(mNews.getCreatorDp()).transform(new CircleTransform()).into(mCreatorDpIv);
        mHeadlineTv.setText((mNews.getHeadline() != null) ? mNews.getHeadline() : getActivity().getString(R.string.missingHeadline_txt));
        mDateTv.setText((mNews.getCreatedDate() != null) ? new SimpleDateFormat(BaseApplication.DATE_FORMAT, Locale.US).format(mNews.getCreatedDate()) : "bla");
        mCreatorTv.setText((mNews.getHeadline() != null) ? mNews.getCreator() : "");
        mCategoryColor.setBackgroundColor(color);

    }


    public void setupWebView() {

        mWebView.loadUrl("file:///android_asset/web/universitynews.php");

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

        Drawable updatedDrawable = getBaseActivity().applyColorToDrawable(R.drawable.ic_back, color);
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

        shareMenuItem.setIcon(new IconDrawable(getActivity(), Iconify.IconValue.fa_share).color(color).actionBarSize());

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
