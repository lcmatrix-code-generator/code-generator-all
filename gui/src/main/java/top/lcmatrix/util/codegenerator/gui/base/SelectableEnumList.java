package top.lcmatrix.util.codegenerator.gui.base;

import top.lcmatrix.util.codegenerator.common.plugin.ISelectableEnum;

import javax.swing.*;
import java.util.*;

public class SelectableEnumList extends JList<ISelectableEnum> {

    private Map<ISelectableEnum, SelectableEnumProxy> enumProxyMap = new HashMap<>();

    public SelectableEnumList(){
        super();
    }

    public SelectableEnumList(ISelectableEnum[] listData) {
        super();
        setListData(listData);
    }

    @Override
    public void setListData(ISelectableEnum[] listData) {
        SelectableEnumProxy[] dataProxys = new SelectableEnumProxy[listData.length];
        for (int i = 0; i < listData.length; i++) {
            dataProxys[i] = new SelectableEnumProxy(listData[i]);
            enumProxyMap.put(listData[i], dataProxys[i]);
        }
        super.setListData(dataProxys);
    }

    @Override
    public List<ISelectableEnum> getSelectedValuesList() {
        List<ISelectableEnum> selectedValuesProxyList = super.getSelectedValuesList();
        if(selectedValuesProxyList == null){
            return null;
        }
        List<ISelectableEnum> itemList = new ArrayList<>();
        for (ISelectableEnum selectableEnumProxy : selectedValuesProxyList) {
            itemList.add(((SelectableEnumProxy) selectableEnumProxy).get());
        }
        return itemList;
    }

    @Override
    public void setSelectedValue(Object anObject, boolean shouldScroll) {
        if(anObject instanceof SelectableEnumProxy){
            super.setSelectedValue(anObject, shouldScroll);
        } else {
            SelectableEnumProxy selectableEnumProxy = enumProxyMap.get(anObject);
            if(selectableEnumProxy != null){
                super.setSelectedValue(selectableEnumProxy, shouldScroll);
            }
        }
    }

    public void setSelectedValue(List<ISelectableEnum> enums) {
        if(enums.isEmpty()){
            return;
        }
        List<Integer> selectIndexes = new ArrayList<>();
        for (ISelectableEnum anEnum : enums) {
            int index = indexOfItem(anEnum);
            if(index >= 0){
                selectIndexes.add(index);
            }
        }
        if(selectIndexes.isEmpty()){
            return;
        }
        int[] selectedIndexesInt= new int[selectIndexes.size()];
        for (int i = 0; i < selectIndexes.size(); i++) {
            selectedIndexesInt[i] = selectIndexes.get(i);
        }
        setSelectedIndices(selectedIndexesInt);
    }

    public int indexOfItem(ISelectableEnum item){
        SelectableEnumProxy itemProxy = null;
        if(item instanceof SelectableEnumProxy){
            itemProxy = (SelectableEnumProxy) item;
        } else {
            itemProxy = enumProxyMap.get(item);
        }
        if(itemProxy == null){
            return -1;
        }
        ListModel<ISelectableEnum> model = getModel();
        for (int i = 0; i < model.getSize(); i++) {
            ISelectableEnum proxy = model.getElementAt(i);
            if(proxy.equals(itemProxy)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public ISelectableEnum getSelectedValue() {
        ISelectableEnum selectedValue = super.getSelectedValue();
        return ((SelectableEnumProxy) selectedValue).get();
    }

    public ISelectableEnum unwrapItem(ISelectableEnum item){
        if(item instanceof SelectableEnumProxy){
            return ((SelectableEnumProxy) item).get();
        }
        return item;
    }

    @Override
    public void setListData(Vector<? extends ISelectableEnum> listData) {
        throw new UnsupportedOperationException();
    }

    private static class SelectableEnumProxy implements ISelectableEnum {
        private ISelectableEnum selectableEnum;
        SelectableEnumProxy(ISelectableEnum textValueEnum){
            this.selectableEnum = textValueEnum;
        }

        public ISelectableEnum get() {
            return selectableEnum;
        }

        @Override
        public String getDisplayText() {
            return selectableEnum.getDisplayText();
        }

        @Override
        public String toString() {
            return selectableEnum.getDisplayText();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SelectableEnumList.SelectableEnumProxy that = (SelectableEnumList.SelectableEnumProxy) o;
            return selectableEnum.equals(that.selectableEnum);
        }

        @Override
        public int hashCode() {
            return Objects.hash(selectableEnum);
        }
    }
}
