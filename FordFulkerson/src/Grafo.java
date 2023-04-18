import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grafo {

    private final int V; //Número de vertices del grafo
    private int E; //Numero de ejes del grafo
    private Map<Integer, List<Eje>> adj; //Lista de adyancencia de los vertices

    public Grafo(int V) {
        if (V < 0) throw new IllegalArgumentException("El numero de vertices debe ser positivo");
        this.V = V;
        this.E = 0;
        adj = new HashMap<Integer, List<Eje>>();
        for (int v = 0; v < V; v++)
            adj.put(v, new ArrayList<Eje>()) ;
    }

    public Grafo(In in) {
        this(in.readInt());
        int E = in.readInt();
        if (E < 0) throw new IllegalArgumentException("number of edges must be non-negative");
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            validarVertice(v);
            validarVertice(w);
            int capacity = in.readInt();
            addEje(new Eje(v, w, capacity));
        }
    }

    public int getV() {
        return V;
    }

    public int getE() {
        return E;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validarVertice(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertice " + v + " no esta entre 0 y " + (V-1));
    }

    /**
     * Adds the edge {@code e} to the network.
     * @param e the edge
     * @throws IllegalArgumentException unless endpoints of edge are between
     *         {@code 0} and {@code V-1}
     */
    public void addEje(Eje e) {
        int v = e.from();
        int w = e.to();
        validarVertice(v);
        validarVertice(w);
        adj.get(v).add(e);
        adj.get(w).add(e);
        E++;
    }

    /**
     * Returns the edges incident on vertex {@code v} (includes both edges pointing to
     * and from {@code v}).
     * @param v the vertex
     * @return the edges incident on vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<Eje> adj(int v) {
        validarVertice(v);
        return adj.get(v);
    }

    // return list of all edges - excludes self loops
    public Iterable<Eje> ejes() {
        ArrayList<Eje> list = new ArrayList<Eje>();
        for (int v = 0; v < V; v++)
            for (Eje e : adj(v)) {
                if (e.to() != v)
                    list.add(e);
            }
        return list;
    }


}
