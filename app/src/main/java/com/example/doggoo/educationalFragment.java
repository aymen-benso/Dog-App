package com.example.doggoo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class educationalFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_educational, container, false);

        // Array of video IDs corresponding to each YouTubePlayerView
        String[] videoIds = {
                "ppkBktPF6FI", // What to Feed your Pet
                "uDP8nPELClg", // Essential Ingredients for Homemade Dog Food
                "lOY7XZvDFqA", // Dog Food Recipe For Allergies
                "j_LZmPbT_Ng"  // Dog Food: Good for Digestive Issues
        };

        // Array of YouTubePlayerView IDs
        int[] youTubePlayerViewIds = {
                R.id.youtube_player_view,
                R.id.youtube_player_view1,
                R.id.youtube_player_view2,
                R.id.youtube_player_view3
        };

        // Loop through each YouTubePlayerView and set the video
        for (int i = 0; i < youTubePlayerViewIds.length; i++) {
            YouTubePlayerView youTubePlayerView = view.findViewById(youTubePlayerViewIds[i]);
            getLifecycle().addObserver(youTubePlayerView);
            String videoId = videoIds[i];
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0);
                }
            });
        }

        return view;
    }
}
