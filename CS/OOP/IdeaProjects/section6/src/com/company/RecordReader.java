package com.company;

public class RecordReader {
    private Record record;
    private int position = 0;

    public RecordReader(Record record){
        this.record = record;
    }

    public boolean canRead(){
        return this.position < this.record.rawData.length;
    }
    public byte readByte(){
        return this.record.rawData[this.position];
    }
    public String readSignature(){
        byte ch0 = readByte();
        byte ch1 = readByte();
        byte ch2 = readByte();
        byte ch3 = readByte();

        return String.format("%c%c%c%c", ch0, ch1, ch2, ch3);
    }
}
