import service.AnotherService;
import service.Service;

public class Main {

    public static void main(String[] args) {
        Service service = new Service("Custom Message from Service."); // Inject the service to be used by the client
        Client client = new Client(service);

        AnotherService anotherService = new AnotherService("Custom message from another service");
        Client anotherClient = new Client(anotherService);

        client.doSomething();
        anotherClient.doSomething();
    }
}