import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiServer {

    private static final int PORT = 23456; // Cổng mặc định của RMI Registry

    public static void main(String[] args) {
        try {
            RemoteService service = new RemoteServiceImpl();

            Registry registry;
            try {
                registry = LocateRegistry.getRegistry(PORT);
                registry.list();
                System.out.println("RMI Registry da ton tai tren cong " + PORT + ".");
            } catch (Exception e) {
                System.out.println("RMI Registry chua ton tai. Dang tao Registry moi tren cong " + PORT + "...");
                registry = LocateRegistry.createRegistry(PORT);
            }

            registry.rebind("RemoteSumService", service); // Thay đổi tên dịch vụ để phản ánh nhiệm vụ mới

            System.out.println("Server RMI da san sang. Doi tuong 'RemoteSumService' da duoc dang ky.");
            System.out.println("Server dang cho cac Client goi cac phuong thuc tu xa...");

        } catch (Exception e) {
            System.err.println("Loi Server RMI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}