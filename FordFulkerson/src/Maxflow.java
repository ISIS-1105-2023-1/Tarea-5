import java.util.LinkedList;
import java.util.Queue;

public class Maxflow {

    private final int V;        // número de vértices
    private boolean[] marcado;  // marcado[v] = true ssi existe un camino s->v en el grafo residual
    private Eje[] edgeTo;       // edgeTo[v] = el último eje en el camino más corto s->v
    private int valor;          // valor actual del flujo máximo

    // Constructor que ejecuta el algoritmo de Ford-Fulkerson
    public Maxflow(Grafo G, int s, int t) {
        V = G.getV();
        validarVertice(s);
        validarVertice(t);
        if (s == t) throw new IllegalArgumentException("Inicio es igual al sink");

        // mientras exista un camino s-t en el grafo residual
        while (tieneCamino(G, s, t)) {
            // encuentre la capacidad mínima a lo largo del camino
            double minimaCapacidad = Integer.MAX_VALUE;
            for (int v = t; v != s; v = edgeTo[v].other(v))
                minimaCapacidad = Math.min(minimaCapacidad, edgeTo[v].residualCapacityTo(v));

            // actualice el flujo a lo largo del camino
            for (int v = t; v != s; v = edgeTo[v].other(v))
                edgeTo[v].addResidualFlowTo(v, minimaCapacidad);

            // aumente el valor del flujo máximo
            valor += minimaCapacidad;
        }
    }

    // Devuelve el valor actual del flujo máximo
    public int getValor() {
        return valor;
    }

    // Comprueba si existe un camino s-t en el grafo residual
    private boolean tieneCamino(Grafo G, int s, int t) {
        marcado = new boolean[G.getV()];
        edgeTo = new Eje[G.getV()];
        Queue<Integer> queue = new LinkedList<Integer>();
        marcado[s] = true;
        queue.add(s);

        while (queue.size() > 0) {
            int v = queue.remove();
            for (Eje e : G.adj(v)) {
                int w = e.other(v);
                // si existe capacidad residual en el eje y el vértice w no ha sido marcado
                if (e.residualCapacityTo(w) > 0 && !marcado[w]) {
                    // agregue el eje a edgeTo y marque el vértice w
                    edgeTo[w] = e;
                    marcado[w] = true;
                    // agregue el vértice w a la cola
                    queue.add(w);
                }
            }
        }
        // devuelva true si el vértice t ha sido marcado (existe un camino s-t en el grafo residual)
        return marcado[t];
    }

    // Lanza una IllegalArgumentException a menos que {@code 0 <= v < V}
    private void validarVertice(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertice " + v + " no está entre 0 y " + (V-1));
    }

    public static void main(String[] args) {

        Grafo G = new Grafo(new In("grafoPeq.txt"));
        int s = 0, t = G.getV() - 1;
        Maxflow maxflow = new Maxflow(G, s, t);
        System.out.println("Max flow from " + s + " to " + t);
        for (int v = 0; v < G.getV(); v++)
            for (Eje e : G.adj(v))
                if ((v == e.from()) && e.flow() > 0)
                    System.out.println(" " + e);
        System.out.println("Max flow value = " + maxflow.getValor());
    }

}

