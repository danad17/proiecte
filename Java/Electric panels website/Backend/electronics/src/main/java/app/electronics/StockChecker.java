package app.electronics;

public class StockChecker {

    public  String checkStock(Long number){
        if(number > 0 && number <= 10)
            return "Stock low";
        if(number > 10 && number <= 20)
            return "Stock sufficient";
        if(number > 20)
            return "Stock overflow";
        return "Stock empty";
    }

}