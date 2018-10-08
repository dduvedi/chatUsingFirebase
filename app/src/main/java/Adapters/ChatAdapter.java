package Adapters;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Model.Chat;
import Utility.Util;
import in.rasta.chatapp.BR;
import in.rasta.chatapp.R;
import in.rasta.chatapp.databinding.ChatAdapterBinding;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.BindingHolder> {

    private ArrayList<Chat> chatList;
    private Context context;
    private String loggedInUser;
    private String conversatingWith;

    public ChatAdapter(Context context, ArrayList<Chat> chatList, String loggedInUser, String conversatingWith) {
        this.chatList = chatList;
        this.context = context;
        this.loggedInUser = loggedInUser;
        this.conversatingWith = conversatingWith;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.chat_adapter, parent, false);
        ChatAdapterBinding binding = DataBindingUtil.bind(view);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, final int position) {
        Util.removeProgressDialog();
        if (chatList.get(position).getConversationType().equalsIgnoreCase(loggedInUser.toUpperCase() + "_" + conversatingWith.toUpperCase())) {
            if (chatList.get(position).getSender().equalsIgnoreCase(loggedInUser)) {
                holder.binding.getRoot().findViewById(R.id.receiverLayout).setVisibility(View.GONE);
                if (chatList.get(position).getImageURL() != null && !chatList.get(position).getImageURL().isEmpty()) {
                    ImageView imageView = holder.binding.getRoot().findViewById(R.id.imageViewSender);
                    imageView.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(chatList.get(position).getImageURL()).into(imageView);
                } else {
                    holder.binding.getRoot().findViewById(R.id.senderMessage).setVisibility(View.VISIBLE);
                    holder.getBinding().setVariable(BR.chatMessage, chatList.get(position));
                }
            } else if (chatList.get(position).getSender().equalsIgnoreCase(conversatingWith)) {
                holder.binding.getRoot().findViewById(R.id.senderLayout).setVisibility(View.GONE);
                if (chatList.get(position).getImageURL() != null && !chatList.get(position).getImageURL().isEmpty()) {
                    ImageView imageView = holder.binding.getRoot().findViewById(R.id.imageViewReceiver);
                    imageView.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(chatList.get(position).getImageURL()).into(imageView);
                } else {
                    holder.binding.getRoot().findViewById(R.id.receiverMessage).setVisibility(View.VISIBLE);
                    holder.getBinding().setVariable(BR.chatMessage, chatList.get(position));
                }
            }
        }
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


    class BindingHolder extends RecyclerView.ViewHolder {

        private ChatAdapterBinding binding;

        BindingHolder(ChatAdapterBinding binding) {
            super(binding.getRoot());
            setBinding(binding);
        }

        public void setBinding(ChatAdapterBinding binding) {
            this.binding = binding;
        }

        public ChatAdapterBinding getBinding() {
            return binding;
        }
    }
}
