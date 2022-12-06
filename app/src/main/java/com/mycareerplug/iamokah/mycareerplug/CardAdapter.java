package com.mycareerplug.iamokah.mycareerplug;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.huxq17.swipecardsview.BaseCardAdapter;

import java.util.List;

public class CardAdapter extends BaseCardAdapter {

    private List<CardModel> modelList;
    private Context context;

    public CardAdapter(List<CardModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }



    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public int getCardLayoutId() {
        return R.layout.card_item;
    }

    @Override
    public int getVisibleCardCount() {
        return 3;
    }

    @Override
    public void onBindData(int position, View cardview) {
        if (modelList == null || modelList.size() == 0) {
            return;
        }
        TextView institution = (TextView) cardview.findViewById(R.id.institutionrealid);
        TextView programname = (TextView) cardview.findViewById(R.id.programrealid);
        TextView deadline = (TextView) cardview.findViewById(R.id.event_typez);
        TextView cost = (TextView) cardview.findViewById(R.id.city);
        TextView financialaid = (TextView) cardview.findViewById(R.id.program_id);
        TextView pay = (TextView) cardview.findViewById(R.id.descrip);
        CardModel model = modelList.get(position);

        institution.setText(model.getInstname());
        programname.setText(model.getProgramname());
        deadline.setText(model.getDeadline());
        cost.setText(model.getCost());
        financialaid.setText(model.getFinancialaid());
        pay.setText(model.getPay());

        /*
        final YouTubePlayerView mYoutubePlayerView = (YouTubePlayerView) cardview.findViewById(R.id.youtubePlay);
        final YouTubePlayer.OnInitializedListener mOnInitializeListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo("jQNa_XsFh-U&feature");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        FloatingActionButton play = (FloatingActionButton) cardview.findViewById(R.id.floatingActionButton2);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYoutubePlayerView.initialize(youtubeconfig.getApiKey(), mOnInitializeListener);
            }
        });

        */
    }
}
