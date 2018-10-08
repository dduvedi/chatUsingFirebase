package in.rasta.chatapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import Adapters.ConversationListAdapter;
import Model.UserCollection;
import Utility.Util;
import ViewModel.MainViewModel;
import in.rasta.chatapp.databinding.ActivityMainBinding;

import static Utility.Constants.THRESHOLD_TO_LOAD;

public class MainActivity extends AppCompatActivity {

    private ConversationListAdapter adapter;
    private ActivityMainBinding activityMainBinding;
    private LinearLayoutManager linearLayoutManager;
    private MainViewModel viewModel;
    private boolean isShowProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.executePendingBindings();
        activityMainBinding.setLifecycleOwner(this);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);

        adapter = new ConversationListAdapter(MainActivity.this);
        activityMainBinding.userRecyclerView.setLayoutManager(linearLayoutManager);
        activityMainBinding.userRecyclerView.setAdapter(adapter);

        setSupportActionBar((Toolbar) activityMainBinding.toolbar);

        activityMainBinding.userRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int itemCount = linearLayoutManager.getItemCount();
                    int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                    if (!isShowProgress && itemCount <= (lastVisibleItemPosition
                            + THRESHOLD_TO_LOAD)) {
                        viewModel.loadMoreData();
                    }
                }

            }
        });
        registerForLiveData();
    }

    private void registerForLiveData() {

        viewModel.fetchAllUserList().observe(this, new Observer<UserCollection>() {
            @Override
            public void onChanged(@Nullable UserCollection userCollection) {
                if (userCollection != null && userCollection.getUserCollectionDetailsList() != null
                        && userCollection.getUserCollectionDetailsList().size() > 0) {
                    adapter.addAll(userCollection);
                }
            }
        });

        viewModel.showProgressDialog().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (Util.isNetworkConnected(MainActivity.this)) {
                    if (aBoolean) {
                        Util.showProgressDialog(MainActivity.this, "Fetching Data...");
                        isShowProgress = true;
                    } else {
                        Util.removeProgressDialog();
                        isShowProgress = false;
                    }
                } else {
                    Util.showToast(MainActivity.this, "No internet connectivity.");
                }

            }
        });

        viewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                Util.showToast(MainActivity.this, error);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.profile:
                Intent profileIntent = new Intent(MainActivity.this, UserProfile.class);
                startActivity(profileIntent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                return true;


            case R.id.logout:
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putBoolean("isLoggedIn", false).commit();
                Intent loginIntent = new Intent(MainActivity.this, UserLogin.class);
                startActivity(loginIntent);
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
