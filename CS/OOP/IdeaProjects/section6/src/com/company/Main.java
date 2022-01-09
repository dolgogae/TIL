package com.company;

import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Math math = Math.getInstance();
        int absVal1 = Math.abs(-2);

        byte[] filedata = "qwerqwer".getBytes(StandardCharsets.UTF_8);
//
//        Record record = new Record(filedata);
//
//        RecordReader reader0 = new RecordReader(record);
//        RecordReader reader1 = new RecordReader(record);
        // 두개 reader는 독립적으로 읽는 위치가 보장된다.'

//        Record record = new Record(filedata);
//        Record.Reader reader0 = record.new Reader();
//        Record.Reader reader1 = record.new Reader();

        Record record = new Record(filedata);
        Record.Reader reader0 = new Record.Reader(record);
        Record.Reader reader1 = new Record.Reader(record);
    }
}
