package com.mao.coding.ui.publish;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author zhangkun
 * @time 2020/12/18 3:53 PM
 * @Description
 */
public class PublishViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PublishViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("This is publish activty");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
