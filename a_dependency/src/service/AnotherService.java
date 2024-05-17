package service;

public class AnotherService implements ServiceInterface{
    String anotherMessage;

    public AnotherService(String anotherMessage) {
        this.anotherMessage = anotherMessage;
    }

    public String getInfo(){
        return "Another " + this.anotherMessage;
    }
}
