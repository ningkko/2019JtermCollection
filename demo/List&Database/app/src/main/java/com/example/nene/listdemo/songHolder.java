package com.example.nene.listdemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class songHolder {

    public class SongHolder extends RecyclerView.ViewHolder {
        // Variables for views
        TextView artist;

        // Constructor
        public SongHolder(View itemView) {
            super(itemView);
            // (...You can add an OnClickListener to itemView here if you want...)

            // Find the views
            artist = (TextView) itemView.findViewById(R.id.artist);
        }

        // Bind method
        public void bindSong(Song song) {
            // Populate the views based on the model
            artist.setText(song.getArtist());
        }
    }

}
