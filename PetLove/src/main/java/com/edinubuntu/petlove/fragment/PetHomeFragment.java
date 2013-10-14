package com.edinubuntu.petlove.fragment;

import android.content.Intent;
import android.graphics.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.activity.MarketActivity;
import com.edinubuntu.petlove.object.Pet;
import com.edinubuntu.petlove.object.User;
import com.edinubuntu.petlove.util.manager.UserManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

/**
 * Created by edward_chiang on 13/10/4.
 */
public class PetHomeFragment extends SherlockFragment {

    public static final long MINUTES_TO_VISIT_AGAIN = 60L;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pet_home, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        User currentPlayer = UserManager.getCurrentPlayer();
        if (currentPlayer != null) {

            List<Pet> petList = currentPlayer.getActivePets();
            if (petList != null && !petList.isEmpty()) {

                // TODO so far we only support one
                Pet pet = petList.get(0);

                DisplayMetrics metrics = getResources().getDisplayMetrics();

                final ImageView petImageView = (ImageView)getView().findViewById(R.id.pet_image_view);

                AsyncTask croppedImageTask = new AsyncTask<Pet, Void, Bitmap>() {
                    /**
                     * Override this method to perform a computation on a background thread. The
                     * specified parameters are the parameters passed to {@link #execute}
                     * by the caller of this task.
                     * <p/>
                     * This method can call {@link #publishProgress} to publish updates
                     * on the UI thread.
                     *
                     * @param params The parameters of the task.
                     * @return A result, defined by the subclass of this task.
                     * @see #onPreExecute()
                     * @see #onPostExecute
                     * @see #publishProgress
                     */
                    @Override
                    protected Bitmap doInBackground(Pet... params) {

                        if (params.length > 0) {
                            Pet pet = params[0];

                            try {
                                return Picasso.with(getSherlockActivity())
                                        .load(pet.getProfile().getImageName()).get();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        super.onPostExecute(bitmap);

                        Bitmap croppedBitmap = getCroppedBitmap(bitmap);

                        petImageView.setImageBitmap(croppedBitmap);
                    }
                };
                croppedImageTask.execute(new Pet[]{pet});

                RelativeLayout.LayoutParams petImageViewLayoutParams = (RelativeLayout.LayoutParams) petImageView.getLayoutParams();
                int longestPixels = (metrics.widthPixels < metrics.heightPixels) ? metrics.widthPixels: metrics.heightPixels;
                petImageViewLayoutParams.width = (int)(longestPixels / 2);
                petImageViewLayoutParams.height = (int)(longestPixels / 2);
                petImageView.requestLayout();
            } else {
                // Replace by Empty fragment
                PetEmptyFragment petEmptyFragment = new PetEmptyFragment();
                FragmentManager fragmentManager = getSherlockActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, petEmptyFragment).commit();
            }

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pet, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_adapt_pet:
                Intent marketIntent = new Intent(getSherlockActivity(), MarketActivity.class);
                startActivity(marketIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
}
