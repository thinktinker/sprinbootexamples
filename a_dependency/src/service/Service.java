package service;

public class Service implements ServiceInterface {
    String message;
    public Service(String message) {
        this.message = message;
    }

    public String getInfo(){
        return this.message;
    }
}


