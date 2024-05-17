import service.Service;
import service.ServiceInterface;

public class Client {

    ServiceInterface service;

    public Client(ServiceInterface service) {    // constructor injection, we pass in the service to be used
        this.service = service;
    }

    public void doSomething(){
        String info = service.getInfo();
        System.out.println(info);
    }
}
