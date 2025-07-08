package com.unl.laberinto.base.controller.datastruct.graphs;

public class UndirectedGraph extends DirectedGrap{
    public UndirectedGraph(Integer nro_vertex) {
        super(nro_vertex);
    }

    @Override
    public void insert(Integer o, Integer d, float weight) {
    if (o.intValue() <= nro_vertex().intValue() && d.intValue() <= nro_vertex().intValue()) {
            if (exist_edge(o, d) == null) {
                setNro_edge(nro_edge() + 1);
                Adjacency aux = new Adjacency(o, weight);
                aux.setWeight(weight);
                aux.setDestiny(d);
                getList_adjacencies()[o].add(aux);
                Adjacency auxD = new Adjacency(d, weight);
                auxD.setWeight(weight);
                auxD.setDestiny(o);
                getList_adjacencies()[d].add(auxD);
            }
        } else {
            throw new ArrayIndexOutOfBoundsException("");
        }
    }
}
