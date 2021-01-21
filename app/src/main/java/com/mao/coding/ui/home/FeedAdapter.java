package com.mao.coding.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mao.coding.databinding.LayoutFeedTypeImageBinding;
import com.mao.coding.databinding.LayoutFeedTypeVideoBinding;
import com.mao.coding.model.Feed;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author zhangkun
 * @time 2021/1/19 10:12 AM
 * @Description
 */
public class FeedAdapter extends PagedListAdapter<Feed, FeedAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final Context mContext;
    private String mCategory;

    public FeedAdapter(Context context,String category) {
        super(new ItemCallback<Feed>() {

            // 两个item 是不是同一个
            @Override
            public boolean areItemsTheSame(@NonNull Feed oldItem, @NonNull Feed newItem) {
                return oldItem.id == newItem.id;
            }

            // 内容是否相同  areItemsTheSame 返回 true 才会执行
            @Override
            public boolean areContentsTheSame(@NonNull Feed oldItem, @NonNull Feed newItem) {
                return oldItem.equals(newItem);
            }
        });
        mContext = context;
        mCategory = category;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getItemViewType(int position) {
        Feed item = getItem(position);
        return item.itemType;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding mBinding;
        if (viewType == Feed.TYPE_IMAGE_TEXT) {
            mBinding = LayoutFeedTypeImageBinding.inflate(inflater);
        } else {
            mBinding = LayoutFeedTypeVideoBinding.inflate(inflater);
        }
        return new ViewHolder(mBinding.getRoot(), mBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bindData(getItem(position));

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding mBinding;

        public ViewHolder(@NonNull View itemView, ViewDataBinding binding) {
            super(itemView);
            this.mBinding = binding;
        }

        public void bindData(Feed item) {
            if (mBinding instanceof LayoutFeedTypeImageBinding) {
                LayoutFeedTypeImageBinding typeImageBinding = (LayoutFeedTypeImageBinding) mBinding;
                typeImageBinding.setFeed(item);
                typeImageBinding.feedImage.bindData(item.width, item.height, 16, item.cover) ;
            }else {
                LayoutFeedTypeVideoBinding typeVideoBinding = (LayoutFeedTypeVideoBinding) mBinding;
                typeVideoBinding.setFeed(item);
                typeVideoBinding.listPlayerView.bindData(mCategory,item.width,item.height,item.cover,item.url);
            }
        }
    }
}
