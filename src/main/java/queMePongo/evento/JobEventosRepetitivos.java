package queMePongo.evento;

public class JobEventosRepetitivos {

	public static void main(String[] args) {
		System.out.println("Job inicializado.");
		RepositorioEventos.getInstance().ejecutarEventosRepetitivos();
		System.out.println("Job ejecutado correctamente.");
	}

}
