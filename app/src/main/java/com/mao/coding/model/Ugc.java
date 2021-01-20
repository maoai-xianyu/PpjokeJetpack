package com.mao.coding.model;

import java.io.Serializable;

import androidx.annotation.Nullable;

/**
 * @author zhangkun
 * @time 2021/1/6 9:51 AM
 * @Description
 */
public class Ugc implements Serializable {


    /**
     * likeCount : 153
     * shareCount : 0
     * commentCount : 4454
     * hasFavorite : false
     * hasLiked : true
     * hasdiss:false
     */

    public int likeCount;
    public int shareCount;
    public int commentCount;
    public boolean hasFavorite;
    public boolean hasdiss;
    public boolean hasLiked;

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || !(obj instanceof Ugc)) {
            return false;
        }
        Ugc newUgc = (Ugc) obj;
        return likeCount == newUgc.likeCount
            && shareCount == newUgc.shareCount
            && commentCount == newUgc.commentCount
            && hasFavorite == newUgc.hasFavorite
            && hasLiked == newUgc.hasLiked
            && hasdiss == newUgc.hasdiss;
    }

}
