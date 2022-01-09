package com.sihun.cup;

public class Cup {
    private int sizeMl;

    protected Cup(int sizeMl){
        this.sizeMl = sizeMl;
    }

    public int getSize(){
        return this.sizeMl;
    }

//    public static Cup createOrNull(Cupsize size){
//        switch (size){
//            case SMALL :
//                return new Cup(355);
//            case MIDIUM:
//                return new Cup(500);
//            case LARGE:
//                return new Cup(755);
//            default:
//                assert (false) : "Unhandled CupSize: "+ size;
//                return null;
//        }
//    }
}
