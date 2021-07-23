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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.golbarg.findimagedifferences.R;
import net.golbarg.findimagedifferences.util.UtilController;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {
    public static final String TAG = HomeFragment.class.getName();
    Context context;
    AdView mAdViewHomeScreenBanner;
    ProgressBar progressLoading;
    ImageView imgPicture;
    Button btnSelectImage;
    Fragment fragment;
    Uri selectedImage;
    Drawable drawableImageView;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        context = root.getContext();
        setHasOptionsMenu(true);
        fragment = this;

        imgPicture = root.findViewById(R.id.img_picture);
        btnSelectImage = root.findViewById(R.id.btn_select_image);

        loadSavedInstance(savedInstanceState);
        LoadBannerAd(root);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ImagePicker.with(fragment).galleryOnly().crop().start();
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
            Bitmap result = UtilController.differVerticalImage(bitmap);
            drawableImageView = new BitmapDrawable(getResources(), result);
            imgPicture.setImageDrawable(drawableImageView);
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
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                Bitmap result = UtilController.differVerticalImage(bitmap);
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
            if(result != null) {
                drawableImageView = new BitmapDrawable(getResources(), result);
                imgPicture.setImageDrawable(drawableImageView);
            } else {
                UtilController.showSnackMessage(root, "Failed to Process...");
            }
        }
    }
}