package service;

public class Service implements ServiceInterface {
    String message;
    public Service(String message) {
        this.message = message;
    }
    @Override
    public String getInfo(){
        return this.message;
    }
}


