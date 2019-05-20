package cc.hisens.hardboiled.patient.wideview.pickerview.adapter;

public interface WheelAdapter<T> {
    /**
     * Gets items count
     *
     * @return the count of wheel items
     */
    int getItemsCount();

    /**
     * Gets a wheel item_found_devices by index.
     *
     * @param index the item_found_devices index
     * @return the wheel item_found_devices text or null
     */
    T getItem(int index);

    /**
     * Gets maximum item_found_devices length. It is used to determine the wheel width.
     * If -1 is returned there will be used the default wheel width.
     *
     * @return the maximum item_found_devices length or -1
     */
    int indexOf(T o);
}
