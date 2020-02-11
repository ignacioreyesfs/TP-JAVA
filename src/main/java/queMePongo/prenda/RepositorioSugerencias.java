package queMePongo.prenda;

import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import queMePongo.usuario.Usuario;
import java.util.List;

public class RepositorioSugerencias implements WithGlobalEntityManager {
    private static RepositorioSugerencias instancia;

    public static RepositorioSugerencias getInstance() {
        if (instancia == null) {
            instancia = new RepositorioSugerencias();
        }
        return instancia;
    }

    public List<Sugerencia> getSugerencias(String eventoId) {
        return entityManager()
                .createQuery("from Sugerencia where evento_id = :eventoId", Sugerencia.class)
                .setParameter("eventoId", eventoId)
                .getResultList();
    }

    public List<Prenda> getPrendas(String atuendoId) {
        return entityManager()
                .createNativeQuery("select * from Prenda as p, Atuendo_Prenda as ap where ap.prendas_id = p.id and ap.Atuendo_id = :atuendoId", Prenda.class)
                .setParameter("atuendoId", atuendoId)
                .getResultList();
    }
    
    public Atuendo getAtuendo(String atuendoId) {
        return entityManager()
                .createQuery("from Atuendo where id = :atuendoId", Atuendo.class)
                .setParameter("atuendoId", Integer.parseInt(atuendoId))
                .getSingleResult();
    }

    public Sugerencia getSugerencia(String id) {
        return entityManager()
                .createQuery("from Sugerencia where id = :id", Sugerencia.class)
                .setParameter("id", Integer.parseInt(id))
                .getSingleResult();
    }
}
