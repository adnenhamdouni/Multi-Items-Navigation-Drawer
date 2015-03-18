package com.leadertun.android.multiitemsnavigationdrawer;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leadertun.android.multiitemsnavigationdrawer.event.MultiItemDrawerEvents;

import de.greenrobot.event.EventBus;

public class customMultiItemsAdapter extends BaseAdapter {

    private static final int TYPE_SEPARATOR = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_MAX_COUNT = TYPE_ITEM + 1;

    ItemWrapper mItemWrapper;
    private ArrayList<ItemWrapper> mListItemWrapper = new ArrayList<ItemWrapper>();

    public static ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();

    private ViewHolder holder = null;

    private ArrayList<String> mData = new ArrayList<String>();
    private LayoutInflater mInflater;

    private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();

    public customMultiItemsAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void addItem(final String item) {
        mData.add(item);
        mListItemWrapper.add(new ItemWrapper(item));
        notifyDataSetChanged();
    }

    public void addSeparatorItem(final String item) {
        mData.add(item);
        mSeparatorsSet.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final int type = getItemViewType(position);
        mData.get(position);

        if (convertView == null) {
            holder = new ViewHolder();

            switch (type) {
            case TYPE_SEPARATOR:
                convertView = mInflater.inflate(R.layout.list_item_layout_1,
                        null);
                holder.textView = (TextView) convertView
                        .findViewById(R.id.title_separator);
                break;
            case TYPE_ITEM:

                convertView = mInflater.inflate(R.layout.liste_item_chekbox,
                        null);

                RelativeLayout mRelativeLayout = (RelativeLayout) convertView
                        .findViewById(R.id.relative_layout);

                holder.textView = (TextView) convertView
                        .findViewById(R.id.title);
                holder.checkbox = (CheckBox) convertView
                        .findViewById(R.id.check);

                holder.checkbox.setOnCheckedChangeListener(null);

                mRelativeLayout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        mItemWrapper = mListItemWrapper.get(position - 1);

                        Log.v("adnen", "box " + mItemWrapper.getName()
                                + " has been clicked ");

                        EventBus.getDefault().post(
                                new MultiItemDrawerEvents.ItemClickEvent(
                                        mItemWrapper));

                        if (mItemWrapper.isSelected() == true) {

                            mItemWrapper.setSelected(false);
                            holder.checkbox.setChecked(mItemWrapper
                                    .isSelected());
                            Log.v("adnen", "box " + mItemWrapper.getName()
                                    + " has been checked ");
                        } else if (mItemWrapper.isSelected() == false) {

                            mItemWrapper.setSelected(true);

                            holder.checkbox.setChecked(mItemWrapper
                                    .isSelected());
                            Log.v("adnen", "box " + mItemWrapper.getName()
                                    + " has been inchecked ");
                        }

                        // holder.checkbox.setChecked(!mListItemWrapper.get(position).isSelected());

                    }
                });

                // holder.checkbox
                // .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                //
                // @Override
                // public void onCheckedChanged(
                // CompoundButton buttonView, boolean isChecked) {
                // // TODO Auto-generated method stub
                //
                // if (buttonView.isChecked()) {
                //
                // Log.v("adnen", "box "
                // + mListItemWrapper.get(position)
                // .getName()
                // + " has been checked ");
                // mListItemWrapper.get(position).setSelected(
                // true);
                //
                // EventBus.getDefault().post(
                // new MultiItemDrawerEvents.MoveToFragmentEvent(
                // new MyFragment()));
                //
                // } else {
                // Log.v("adnen", "box "
                // + mListItemWrapper.get(position)
                // .getName()
                // + " has been Inchecked");
                // mListItemWrapper.get(position).setSelected(
                // false);
                // }
                // }
                // });

                break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(mData.get(position));

        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
        protected CheckBox checkbox;
    }

}