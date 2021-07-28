package net.golbarg.findimagedifferences.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import net.golbarg.findimagedifferences.R;
import net.golbarg.findimagedifferences.util.UtilController;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {
    public static final String TAG = HomeFragment.class.getName();
    Context context;
    AdView mAdViewHomeScreenBanner;
    RewardedAd mRewardedAd;

    ProgressBar progressLoading;
    ImageView imgPicture;
    Button btnSelectImage;
    SwitchCompat switchImageOrientation;
    Fragment fragment;
    Uri selectedImage;
    Drawable drawableImageView;
    View root;
    boolean isVerticalImage = true;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        context = root.getContext();
        setHasOptionsMenu(true);
        fragment = this;

        imgPicture = root.findViewById(R.id.img_picture);
        btnSelectImage = root.findViewById(R.id.btn_select_image);
        progressLoading = root.findViewById(R.id.progress_loading);
        progressLoading.setVisibility(View.GONE);
        switchImageOrientation = root.findViewById(R.id.switch_image_orientation);

        switchImageOrientation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    isVerticalImage = true;
                    switchImageOrientation.setText(R.string.vertical);
                } else {
                    isVerticalImage = false;
                    switchImageOrientation.setText(R.string.horizontal);
                }
            }
        });

        loadSavedInstance(savedInstanceState);
        LoadBannerAd(root);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ImagePicker.with(fragment).galleryOnly().crop().start();

                    /* real ad Unit: ca-app-pub-3540008829614888/4079708328 */
                    /* test ad Unit: ca-app-pub-3940256099942544/5224354917 */
                    Log.d(TAG, "clicked now load the ad");
                    AdRequest adRequest = new AdRequest.Builder().build();
                    RewardedAd.load(context, "ca-app-pub-3540008829614888/4079708328",
                            adRequest, new RewardedAdLoadCallback() {
                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    // Handle the error.
                                    Log.d(TAG, loadAdError.getMessage());
                                    mRewardedAd = null;
                                }

                                @Override
                                public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                                    mRewardedAd = rewardedAd;
                                    Log.d(TAG, "Ad was loaded.");
                                }
                            });
                    if (mRewardedAd != null) {
                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad was shown.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.d(TAG, "Ad failed to show.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad was dismissed.");
                                mRewardedAd = null;
                            }
                        });

                        Activity activityContext = getActivity();
                        mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                // Handle the reward.
                                Log.d(TAG, "The user earned the reward.");
                                int rewardAmount = rewardItem.getAmount();
                                String rewardType = rewardItem.getType();
                            }
                        });
                    } else {
                        Log.d(TAG, "The rewarded ad wasn't ready yet.");
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    private void loadSavedInstance(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            selectedImage = savedInstanceState.getParcelable("selected_image_URI");
            imgPicture.setImageURI(selectedImage);
        }
    }

    private void LoadBannerAd(View root) {
        mAdViewHomeScreenBanner = root.findViewById(R.id.adViewHomeScreenBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdViewHomeScreenBanner.loadAd(adRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            imgPicture.setImageURI(selectedImage);

            imgPicture.invalidate();
            BitmapDrawable drawable = (BitmapDrawable) imgPicture.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            new HomeFragment.ProcessImageTask(bitmap).execute();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("selected_image_URI", selectedImage);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.help_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.help:
                showHelpDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHelpDialog() {
        DialogHelpMessage helpMessage = new DialogHelpMessage();
        helpMessage.show(getChildFragmentManager(), DialogHelpMessage.TAG);
    }

    private class ProcessImageTask extends AsyncTask<String, String, Bitmap> {
        Bitmap bitmap;

        public ProcessImageTask(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLoading.setVisibility(View.VISIBLE);
            btnSelectImage.setText(R.string.processing_image);
            btnSelectImage.setEnabled(false);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                Bitmap result;
                if(isVerticalImage) {
                    result = UtilController.differVerticalImage(bitmap);
                } else {
                    result = UtilController.differHorizontalImage(bitmap);
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            progressLoading.setVisibility(View.GONE);
            btnSelectImage.setText(R.string.select_image);
            btnSelectImage.setEnabled(true);

            if(result != null) {
                drawableImageView = new BitmapDrawable(getResources(), result);
                imgPicture.setImageDrawable(drawableImageView);
            } else {
                UtilController.showSnackMessage(root, "Failed to Process...");
            }
        }
    }
}