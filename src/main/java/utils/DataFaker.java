package utils;
import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import java.util.Date;
import java.util.Locale;

public class DataFaker {
     Faker faker = new Faker( new Locale("es-MX"));

    public String getRandomFullName(){
       return faker.name().fullName();
    }
    public String getRandomName(){
        return faker.name().firstName();
    }
    public String getRandomLastName(){
        return faker.name().lastName();
    }
    public String getRandomFullAddress(){
        return faker.address().fullAddress();
    }
    public String getRandomCity(){
        return faker.address().cityName();
    }
    public String getRandomZipCode(){
        return faker.address().zipCode().toLowerCase(Locale.ROOT);
    }
    public String getRandomEmail(){
        return faker.internet().emailAddress();
    }
    public String getRandomJobTitle(){
        return faker.job().title();
    }
    public String getRandomJobPosition(){
        return faker.job().position();
    }
    public String getRandomPassword(int min, int max){
        return faker.internet().password(min, max);
    }
    public String getRandomNumber(int size){

        return faker.number().digits(size);
    }
    public long getRandomRangeNumber(long min, long max){

        return faker.number().numberBetween(min,max);
    }
    public String getCreditCard(){
        return faker.finance().creditCard();
    }
    public String getRandomCreditCardMastercard(){
        return faker.finance().creditCard(CreditCardType.MASTERCARD);
    }
    public String getRandomCreditCardVisa(){
        return faker.finance().creditCard(CreditCardType.VISA);
    }
    public Date getRandomDateBetween(Date min, Date max){
        return faker.date().between(min, max);
    }
    public String getRandomText(int size){
        return faker.lorem().characters(size);
    }
    public String getRandomCompanyName(){
        return faker.company().name();
    }
}
