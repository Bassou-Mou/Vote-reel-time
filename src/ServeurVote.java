import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServeurVote extends UnicastRemoteObject implements BureauVote {

    private Map<String,Integer> scor;
    private List<Electeur> electeursConnectes;

    protected ServeurVote() throws RemoteException {
        super();
        scor = new HashMap<>();
        electeursConnectes = new ArrayList<>();
        scor.put("Bassou Mouacha", 0);
        scor.put("El-Mehdi Boulalaam", 0);
        scor.put("Redouane Amouri", 0);
        scor.put("Jamal Amouri", 0);
    }

    @Override
    public synchronized void voter(String candidate) throws RemoteException {
        System.out.println("Vote reçu pour : " + candidate);
        if (scor.containsKey(candidate)) {
            scor.put(candidate, scor.get(candidate) + 1);
            notifierToutLeMonde();
        }else{
            System.out.println("-> Candidat inconnu.");        }
    }

    private void notifierToutLeMonde() {
        System.out.println("Diffusion des nouveaux résultats...");
        List<Electeur> electeurs = new ArrayList<>(electeursConnectes);
        for (Electeur electeur : electeurs) {
            try{
                electeur.recevoirResultats(scor);
            }catch(RemoteException e){
                System.out.println("Client injoignable détecté, suppression de la liste.");
                electeursConnectes.remove(e);
            }
        }
    }

    @Override
    public synchronized void enregistrer(Electeur electeur) throws RemoteException {
        electeursConnectes.add(electeur);
        System.out.println("Nouveau client connecté. Total: " + electeursConnectes.size());
        electeur.recevoirResultats(scor);
    }
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        try{
            LocateRegistry.createRegistry(1099);
            ServeurVote serveur = new ServeurVote();
            Naming.rebind("BureauVoteService", serveur);
            System.out.println("Bureau de Vote OUVERT (Candidats: Bassou Mouacha, El-Mehdi Boulalaam,Redouane Amouri,Jamal Amouri) )");
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
