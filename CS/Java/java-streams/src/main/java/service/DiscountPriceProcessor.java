package service;

import model.Price;

public class DiscountPriceProcessor implements PriceProcessor {

    @Override
    public Price process(Price price) {
        // TODO Auto-generated method stub
        return new Price(price.getPrice() + ", then apply discount");
    }
    
}
