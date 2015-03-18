package com.leadertun.android.multiitemsnavigationdrawer.event;

import java.util.ArrayList;

import com.leadertun.android.multiitemsnavigationdrawer.ItemWrapper;

import android.app.Fragment;

/**
 * Class grouping all events used in event bus
 * 
 * @author Slim BENHAMMOUDA
 * 
 */
public class MultiItemDrawerEvents {

    public static class MoveToFragmentEvent {
        
        private Fragment mFragment;
        private int mPosition;

        public MoveToFragmentEvent(Fragment _fragment, int _position) {
            mFragment = _fragment;
            mPosition = _position;
        }

        public Fragment getFragment() {
            return mFragment;
        }

        public int getPosition() {
            return mPosition;
        }
        
        
    }
    
    
    public static class ItemClickEvent {
        private ItemWrapper mItemWrapper;

        public ItemClickEvent(ItemWrapper _itemWrapper) {
            mItemWrapper = _itemWrapper;
        }

        public ItemWrapper getItemWrapper() {
            return mItemWrapper;
        }
    }

}
