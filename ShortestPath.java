package busInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.NullPointerException;
import java.util.Scanner;

class shortestpathbetweenstops {

    
    ArrayList<Stop> listOfBusStops;
    Graph mg;
    String transfersInfo, stopTimesInfo, stopsFileInfo;
    double[] dists;
    int maximumId = 0;

    public shortestpathbetweenstops(String tFile, String sTFile, String sFile) throws FileNotFoundException {
        this.transfersInfo = tFile;
        this.stopTimesInfo = sTFile;
        this.stopsFileInfo = sFile;
        listOfBusStops = new ArrayList<Stop>();

        gettingStops();
        settingupEdges();
    }

    public void gettingStops() throws FileNotFoundException {
        File stops = new File(stopsFileInfo);
        Scanner stopsScanner = new Scanner(stops);
        stopsScanner.nextLine();

        String stop;
        String[] stopInfo;
        int stopId;

        while (stopsScanner.hasNextLine()) {
            stop = stopsScanner.nextLine();
            stopInfo = stop.split(",");
            stopId = Integer.parseInt(stopInfo[0]);
            listOfBusStops.add(new Stop(stopId, stopInfo[2]));
            maximumId = Math.max(stopId, maximumId);
        }
        maximumId++;
        stopsScanner.close();
    }

    void settingupEdges() throws FileNotFoundException {
        File stopTimes = new File(stopTimesInfo);
        File transfers = new File(transfersInfo);

        Scanner info = new Scanner(transfers);
        info.nextLine();

        mg = new Graph(maximumId);

        String transfer;
        String[] transferValues;

        while (info.hasNextLine()) {
            transfer = info.nextLine();
            transferValues = transfer.split(",");

            int from = Integer.parseInt(transferValues[0]);
            int to = Integer.parseInt(transferValues[1]);
            int type = Integer.parseInt(transferValues[2]);
            int cost = (type == 0) ? 2 : (Integer.parseInt(transferValues[3]) / 100);

            mg.addEdge(from, to, cost);
        }
        info.close();

        info = new Scanner(stopTimes);
        info.nextLine();

        String trip;
        String[] tripValues;
        int from = 1;
        int to, sequenceValue;
        boolean skipEdge;

        while (info.hasNextLine()) {
            trip = info.nextLine();
            tripValues = trip.split(",");
            sequenceValue = Integer.parseInt(tripValues[4]);

            skipEdge = false;

            if (sequenceValue != 1) {
                to = Integer.parseInt(tripValues[3]);
                for (Edge ed : mg.adj(from)) {
                    if (ed.to == to) {
                        skipEdge = true;
                        break;
                    }
                }
                if (!skipEdge) {
                    mg.addEdge(from, to, 1);
                }
            }

            from = Integer.parseInt(tripValues[3]);
        }
        info.close();
    }

    public void relaxing(double[] distTo, Edge[] edgeTo, int v) {
        for (Edge e : mg.adj(v)) {
            int w = e.to;
            if (distTo[w] > distTo[v] + e.weight) {
                distTo[w] = distTo[v] + e.weight;
                edgeTo[w] = e;
            }
        }
    }

    public Edge[] runDijkstraAlgo(int start) {
        double[] distTo = new double[maximumId];
        boolean[] relaxed = new boolean[maximumId];
        Edge[] edgeTo = new Edge[maximumId];

        for (Stop stop : listOfBusStops) {
            distTo[stop.getId()] = Double.POSITIVE_INFINITY;
            relaxed[stop.getId()] = false;
            edgeTo[stop.getId()] = null;
        }
        distTo[start] = 0;

        int current = start;

        for (int i = 0; i < listOfBusStops.size(); i++) {
            relaxing(distTo, edgeTo, current);
            relaxed[current] = true;
            double min = Double.POSITIVE_INFINITY;
            for (Stop stop : listOfBusStops) {
                if (distTo[stop.getId()] < min && !relaxed[stop.getId()]) {
                    min = distTo[stop.getId()];
                    current = stop.getId();
                }
            }
        }
        dists = distTo;
        return edgeTo;
    }

    public String gettingPathSequence(int startingstop, int destinationstop) throws FileNotFoundException {
        settingupEdges();

        if (getStopById(startingstop) == null) {
            return "Departure stop does not exist";
        }
        if (getStopById(destinationstop) == null) {
            return "Destination stop does not exist";
        }

        Edge[] edgeTo = runDijkstraAlgo(startingstop);

        ArrayList<String> sequence = new ArrayList<String>();

        int currentStop = destinationstop;

        sequence.add(getStopById(destinationstop));
        do {
            try {
                currentStop = edgeTo[currentStop].from;
            } catch (NullPointerException e) {
                return "There is no path !";
            }
            sequence.add(getStopById(currentStop));
        } while (currentStop != startingstop);
        Collections.reverse(sequence);

        sequence.add(0, dists[destinationstop] + "");

        String returnString = "";
        for (String string : sequence) {
            returnString += string + "\n";
        }
        return returnString;
    }

    public String getStopById(int id) {
        for (Stop s : listOfBusStops) {
            if (s.getId() == id) {
                return s.getName();
            }
        }
        return "Faulty Stop!";
    }

    class Edge {

        int from, to;
        double weight;

        Edge(int from, int to, double weight) {
            this.to = to;
            this.from = from;
            this.weight = weight;
        }

    }

    class Graph {

        ArrayList<ArrayList<Edge>> adjL = new ArrayList<ArrayList<Edge>>();

        Graph(int edges) {
            for (int i = 0; i < edges; i++) {
                adjL.add(new ArrayList<Edge>());
            }
        }

        ArrayList<Edge> adj(int vertex) {
            return adjL.get(vertex);
        }

        void addEdge(int from, int to, int weight) {
            adjL.get(from).add(new Edge(from, to, weight));
        }
    }

    class Stop {

        int id;
        String name;

        Stop(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }
}