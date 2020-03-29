package top.lcmatrix.util.codegenerator.gui.base;

import top.lcmatrix.util.codegenerator.common.plugin.ISelectableEnum;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SelectableEnumComboBox extends JComboBox<ISelectableEnum> {

    private Map<ISelectableEnum, SelectableEnumProxy> enumProxyMap = new HashMap<>();

    public SelectableEnumComboBox(){
        super();
    }

    public SelectableEnumComboBox(ISelectableEnum[] enums){
        super();
        for (ISelectableEnum anEnum : enums) {
            addItem(anEnum);
        }
    }

    @Override
    public void addItem(ISelectableEnum item) {
        SelectableEnumProxy selectableEnumProxy = new SelectableEnumProxy(item);
        super.addItem(selectableEnumProxy);
        enumProxyMap.put(item, selectableEnumProxy);
    }

    @Override
    public void insertItemAt(ISelectableEnum item, int index) {
        SelectableEnumProxy selectableEnumProxy = new SelectableEnumProxy( item);
        super.insertItemAt(selectableEnumProxy, index);
        enumProxyMap.put(item, selectableEnumProxy);
    }

    public ISelectableEnum unwrapItem(ISelectableEnum item){
        if(item instanceof SelectableEnumProxy){
            return ((SelectableEnumProxy) item).get();
        }
        return item;
    }

    public ISelectableEnum getUnwrappedSelectedItem() {
        Object selectedItem = super.getSelectedItem();
        if(selectedItem == null){
            return null;
        }
        return ((SelectableEnumProxy) selectedItem).get();
    }

    @Override
    public void removeItem(Object anObject) {
        if(anObject instanceof  SelectableEnumProxy){
            super.removeItem(anObject);
        }else{
            ISelectableEnum selectableEnum = enumProxyMap.get(anObject);
            if(selectableEnum != null){
                super.removeItem(selectableEnum);
            }
        }
    }

    @Override
    public void setSelectedItem(Object anObject) {
        if(anObject instanceof  SelectableEnumProxy){
            super.setSelectedItem(anObject);
        }else{
            ISelectableEnum selectableEnum = enumProxyMap.get(anObject);
            if(selectableEnum != null){
                super.setSelectedItem(selectableEnum);
            }
        }
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
            SelectableEnumProxy that = (SelectableEnumProxy) o;
            return selectableEnum.equals(that.selectableEnum);
        }

        @Override
        public int hashCode() {
            return Objects.hash(selectableEnum);
        }
    }
}
