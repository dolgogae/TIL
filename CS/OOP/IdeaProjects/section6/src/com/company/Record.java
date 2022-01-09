package com.company;

public class Record {
//    final byte[] rawData;  //비정적일때
    private final byte[] rawData;

    public Record(byte[] rawData){
        this.rawData = rawData;
    }

    // 비정적 내포 클래스 사용한 버전전
    // 패키지 접근제어자보다도 강한 캡슐화
//    public class Reader {
//        private int position = 0;
//
//        public boolean canRead(){
//            return this.position < rawData.length;
//        }
//        public byte readByte(){
//            return rawData[this.position];
//        }
//        public String readSignature(){
//            byte ch0 = readByte();
//            byte ch1 = readByte();
//            byte ch2 = readByte();
//            byte ch3 = readByte();
//
//            return String.format("%c%c%c%c", ch0, ch1, ch2, ch3);
//        }
//    }

    public static class Reader {
        private final Record record;
        private int position = 0;

        public Reader(Record record){
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
}