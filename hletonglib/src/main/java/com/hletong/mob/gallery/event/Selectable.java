package com.hletong.mob.gallery.event;


/**
 * Created by donglua on 15/6/30.
 */
public interface Selectable {


  /**
   * Indicates if the item at position position is selected
   *
   * @param photo Photo of the item to check
   * @return true if the item is selected, false otherwise
   */
  boolean isSelected(String photo);

  /**
   * Toggle the selection status of the item at a given position
   *
   * @param photo Photo of the item to toggle the selection status for
   */
  void toggleSelection(String photo);

  /**
   * Clear the selection status for all items
   */
  void clearSelection();

  /**
   * Count the selected items
   *
   * @return Selected items count
   */
  int getSelectedItemCount();

}
