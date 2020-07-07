package com.example.inserttestapplication;

public class recycle_List_helper {


    public String batch; //define batch
    public String brick_type; //define type
    public String row;
    public String quantity;

    public recycle_List_helper(String brick_type, String batch, String row, String quantity)
    {
        this.brick_type = brick_type;
        this.batch = batch;
        this.row = row;
        this.quantity = quantity;
    }

    public String getBatch() {
        return batch;
    }

    public String getType() {
        return brick_type;
    }

    public String getRow() {
        return row;
    }

    public String getQuantity() {
        return quantity;
    }

}

