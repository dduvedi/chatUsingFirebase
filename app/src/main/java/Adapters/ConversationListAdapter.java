package Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import Model.UserCollection;
import Model.UserCollectionDetails;
import Model.UserProfileModel;
import Model.UserRegisterModel;
import in.rasta.chatapp.R;
import in.rasta.chatapp.ConversationActivity;

public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ViewHolder> {

    private UserCollection details;
    private Context ctx;

    public ConversationListAdapter(Context context) {
        this.details = new UserCollection();
        this.ctx = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_conversation_list, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UserRegisterModel userRegisterModel = details.getUserCollectionDetailsList().get(position).getUserRegisterModel();

        if (userRegisterModel.getUserImage() != null && !userRegisterModel.getUserImage().isEmpty()) {
            Picasso.with(ctx).load(userRegisterModel.getUserImage()).into(holder.userImage);
        }
        holder.userName.setText(userRegisterModel.getUserName());
        holder.unreadCount.setText("0");

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, ConversationActivity.class);
                intent.putExtra("userName", userRegisterModel.getUserName());
                ctx.startActivity(intent);
                ((Activity) ctx).finish();
                ((Activity) ctx).overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (details == null || details.getUserCollectionDetailsList() == null)
            return 0;
        return details.getUserCollectionDetailsList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircularImageView userImage;
        public TextView userName;
        public TextView unreadCount;
        private LinearLayout container;

        public ViewHolder(View convertView) {
            super(convertView);
            userImage = (CircularImageView) convertView.findViewById(R.id.userImage);
            userName = (TextView) convertView.findViewById(R.id.userName);
            unreadCount = (TextView) convertView.findViewById(R.id.unreadCount);
            container = (LinearLayout) convertView.findViewById(R.id.container);
        }
    }

    public void addAll(UserCollection collection) {
        int size = 0;
        if (details.getUserCollectionDetailsList() != null) {
            size = details.getUserCollectionDetailsList().size();
            details.getUserCollectionDetailsList().clear();
            details.getUserCollectionDetailsList().addAll(collection.getUserCollectionDetailsList());
        } else {
            details.setUserCollectionDetailsList(collection.getUserCollectionDetailsList());
        }
        Gson gson = new Gson();
        String json = PreferenceManager.getDefaultSharedPreferences(ctx).getString("userDetails", "");
        UserProfileModel obj = gson.fromJson(json, UserProfileModel.class);

        for (UserCollectionDetails userCollectionDetails : details.getUserCollectionDetailsList()) {
            if (userCollectionDetails.getUserRegisterModel().getUserName().equals(obj.getUserName())) {
                details.getUserCollectionDetailsList().remove(userCollectionDetails);
                break;
            }
        }


        notifyItemRangeChanged(size, details.getUserCollectionDetailsList().size());
    }

    public String getLastItemId() {
        return details.getUserCollectionDetailsList().get(details.getUserCollectionDetailsList().size() - 1).getUserRegisterModel().getUserName();
    }
}
