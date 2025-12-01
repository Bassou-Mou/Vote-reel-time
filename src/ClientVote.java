import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Scanner;

public class ClientVote extends UnicastRemoteObject implements Electeur {
    protected ClientVote() throws RemoteException {
        super();
    }


    @Override
    public void recevoirResultats(Map<String, Integer> scor) throws RemoteException {
        System.out.println("\n=== RÉSULTATS EN DIRECT ===");
        for(String key : scor.keySet()) {
            int numVote = scor.get(key);
            if (numVote<=1) {
                System.out.println("  * " + key + " : " + numVote + " vote");
            }else{
                System.out.println("  * " + key + " : " + numVote + " votes");
            }
        }
        System.out.println("===========================");
        System.out.print("Votez a votre candidate: ");
    }

    public static void main(String[] args){
        try{
            ClientVote clientVote = new ClientVote();
            BureauVote serveur = (BureauVote) Naming.lookup("rmi://localhost/BureauVoteService");
            serveur.enregistrer(clientVote);
            Scanner scanner = new Scanner(System.in);
            while(true){
                String saisir = scanner.nextLine();
                serveur.voter(saisir);
            }
        }catch(RemoteException | NotBoundException | MalformedURLException e){
            e.printStackTrace();
        }
    }
}
