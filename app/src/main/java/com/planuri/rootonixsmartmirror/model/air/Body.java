package com.planuri.rootonixsmartmirror.model.air;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Body {
    @SerializedName("totalCount")
    public int totalCount;
    @SerializedName("items")
    public ArrayList<AirItem> items;
    @SerializedName("pageNo")
    public int pageNo;
    @SerializedName("numOfRows")
    public int numOfRows;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ArrayList<AirItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<AirItem> items) {
        this.items = items;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    @Override
    public String toString() {
        return "Body{" +
                "totalCount=" + totalCount +
                ", items=" + items +
                ", pageNo=" + pageNo +
                ", numOfRows=" + numOfRows +
                '}';
    }
}
