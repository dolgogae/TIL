package service;

import model.Price;

public class TaxPriceProcessor implements PriceProcessor{

    @Override
    public Price process(Price price) {
        // TODO Auto-generated method stub
        return new Price(price.getPrice() + ", then applied tax");
    }
    
}
