package io.dianto.baking.app.events;


public class Recipe_Step_Event {

    private int selectedPosition;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
