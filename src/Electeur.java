import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface Electeur extends Remote {
    void recevoirResultats(Map<String,Integer> scor) throws RemoteException;
}