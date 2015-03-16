package tms.ubrats;

import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class FeedbackActivity extends BaseActivity {

    @InjectView(R.id.name)
    EditText mNameEt;

    @InjectView(R.id.email)
    EditText mEmailEt;

    @InjectView(R.id.comments)
    EditText mCommentsEt;

    @InjectView(R.id.sendFeedback)
    Button mSendFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.inject(this);
        setTitle(getString(R.string.feedback_txt));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mSendFeedback.setLayoutParams(new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, getValueInDp(55),
                Gravity.TOP | Gravity.RIGHT));

        setupActionBar(true, toolbar);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.sendFeedback)
    public void sendFeedback() {


        String name = mNameEt.getText().toString().trim();
        String comment = mCommentsEt.getText().toString().trim();
        String email = mEmailEt.getText().toString().trim();
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        boolean error = false;

        if (name.isEmpty()) {
            mNameEt.setError(getString(R.string.required_txt));
            error = true;
        }

        if (comment.isEmpty()) {
            mCommentsEt.setError(getString(R.string.required_txt));
            error = true;
        }

        if (email.isEmpty()) {
            mEmailEt.setError(getString(R.string.required_txt));
            error = true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailEt.setError(getString(R.string.emailRequired_txt));
            error = true;
        }

        if (android_id.isEmpty()) {
            Toast.makeText(this, getString(R.string.unableToRetrieveUDID_msg), Toast.LENGTH_SHORT).show();
            error = true;
        }


        if (!error) {

            Feedback feedback = new Feedback();
            feedback.name = name;
            feedback.email = email;
            feedback.comments = comment;
            feedback.udid = android_id;

            mSendFeedback.setEnabled(false);
            mSendFeedback.setText(getString(R.string.sending_txt));


            Networking.getRestClient().getFeedback(feedback, new Callback<Feedback.Output>() {
                @Override
                public void success(Feedback.Output output, Response response) {

                    if (output.success)
                        mSendFeedback.setText(getString(R.string.sent_txt));
                    else
                        Toast.makeText(FeedbackActivity.this, getString(R.string.yourFeedbackCouldNotBeSend_msg), Toast.LENGTH_SHORT).show();

                    enableFeedbackBtn();

                }

                @Override
                public void failure(RetrofitError error) {

                    Toast.makeText(FeedbackActivity.this, getString(R.string.yourFeedbackCouldNotBeSend_msg), Toast.LENGTH_SHORT).show();
                    enableFeedbackBtn();

                }
            });
        }
    }

    public void enableFeedbackBtn() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSendFeedback.setEnabled(true);
                mSendFeedback.setText(getString(R.string.sendIt_txt));
            }
        }, 2000);
    }


}
