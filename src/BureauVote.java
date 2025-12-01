import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BureauVote extends Remote {
        void voter(String candidate) throws RemoteException;
        void enregistrer(Electeur electeur) throws RemoteException;
}
