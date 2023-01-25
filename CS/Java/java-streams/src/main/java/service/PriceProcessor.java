package service;

import model.Price;

@FunctionalInterface
public interface PriceProcessor {
    Price process(Price price);

    /**
     * PriceProcessor는 구현해야할 함수가 없어 다음처럼 람다를 이용해서 구현이 가능하다.
     * 그렇게 되면 process가 호출되는 구조이다.
     * @param next
     * @return
     */
    default PriceProcessor andThen(PriceProcessor next){
        return price -> next.process(process(price));
    }
}
