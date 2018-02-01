package com.hletong.mob.gallery.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hletong.mob.base.BaseRecyclerAdapter;
import com.hletong.mob.gallery.builder.PickerBuilder;
import com.hletong.mob.gallery.event.Selectable;

import java.util.ArrayList;
import java.util.List;


public abstract class SelectableAdapter
        extends BaseRecyclerAdapter<String> implements Selectable {

    private static final String TAG = SelectableAdapter.class.getSimpleName();
    private final List<String> selectedPhotos;
    private int currentDirIndex = PickerBuilder.INDEX_ALL_PHOTOS;

    public SelectableAdapter(Context context, List<String> data) {
        super(context, data);
        selectedPhotos = new ArrayList<>();
    }

    public void setSelectedPhotos(@NonNull List<String> selectedPhotos) {
        clearSelection();
        this.selectedPhotos.addAll(selectedPhotos);
    }

    /**
     * from copy
     *
     * @return 当前选中的图片
     */
    @NonNull
    public ArrayList<String> getSelectedPhotos() {
        return new ArrayList<>(selectedPhotos);
    }

    /**
     * Indicates if the item at position where is selected
     *
     * @param photoPath Photo of the item to check
     * @return true if the item is selected, false otherwise
     */
    @Override
    public boolean isSelected(String photoPath) {
        return selectedPhotos.contains(photoPath);
    }


    /**
     * Toggle the selection status of the item at a given position
     *
     * @param photoPath Photo of the item to toggle the selection status for
     */
    @Override
    public void toggleSelection(String photoPath) {
        if (isSelected(photoPath)) {
            selectedPhotos.remove(photoPath);
        } else {
            selectedPhotos.add(photoPath);
        }
    }


    /**
     * Clear the selection status for all items
     */
    @Override
    public void clearSelection() {
        selectedPhotos.clear();
    }


    /**
     * Count the selected items
     *
     * @return Selected items count
     */
    @Override
    public int getSelectedItemCount() {
        return selectedPhotos.size();
    }

    public void setCurrentDirIndex(int currentDirIndex) {
        this.currentDirIndex = currentDirIndex;
    }

    public int getCurrentDirIndex() {
        return currentDirIndex;
    }
}