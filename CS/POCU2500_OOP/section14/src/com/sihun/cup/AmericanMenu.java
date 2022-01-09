package com.sihun.cup;

public class AmericanMenu extends Menu{
    @Override
    public Cup createCupOrNull(Cupsize size){
        int lid = 10;

        switch (size){
            case SMALL :
                return new PaperCup(355, lid);
            case MIDIUM:
                return new PaperCup(500, lid);
            case LARGE:
                return new PaperCup(755, lid);
            default:
                assert (false) : "Unhandled CupSize: "+ size;
                return null;
        }
    }
}
