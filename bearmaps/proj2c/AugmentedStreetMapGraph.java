package bearmaps.proj2c;

import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.Point;
//import edu.princeton.cs.algs4.TST;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 * @author Alan Yao, Josh Hug, Martin Lee
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {

    Map<Point, Node> np;
    KDTree kd;
    Map<String, String> names;
    MyTrieSet trie;
    Map<String, List> loc;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        List<Node> nodes = this.getNodes();
        List<Point> p = new ArrayList<>();
        trie = new MyTrieSet();
        names = new HashMap<>();
        loc = new HashMap<>();
        np = new HashMap<>();

        for (Node e : nodes) {
            if (!(this.neighbors(e.id()).isEmpty())) {
                if(e.name() != null) {
                    names.put(cleanString(e.name()), e.name());
                    trie.add(cleanString(e.name()));
                    if (loc.containsKey(cleanString(e.name()))) {
                        List<Node> temp = loc.get(e.name());

                        temp.add(e);
                        loc.put(cleanString(e.name()), temp);
                    } else {
                        List<Node> n = new ArrayList<>();
                        n.add(e);
                        loc.put(cleanString(e.name()), n);
                    }
                }
                Point temp = new Point(e.lon(), e.lat());
                p.add(temp);
                np.put(temp, e);
            }
        }
        kd = new KDTree(p);
    }


    /**
     * Returns the vertex closest to the given longitude and latitude.
     *
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point temp = kd.nearest(lon, lat);
        Node n = np.get(temp);

        return n.id();
    }

    /**
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     *
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        return trie.keysWithPrefix(prefix);
    }

    /**
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     *
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        String temp = cleanString(locationName);
        List<Map<String, Object>> list = new ArrayList<>();
        List data = loc.get(cleanString(locationName));
        //get nodes that correspond to the prefix
        //add them to map.add(locationName, node)
        //add each one to list
        for (Object n : data) {
            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.put(locationName, n);
            list.add(mapTemp);
        }

        return new LinkedList<>();
    }


    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        List<String> b = new ArrayList<>();
        b.add("b");
        List<String> c = new ArrayList<>();
        c.add("b");
        c.add("c");

        map.put("a", b);
        map.put("a", c);
        System.out.println(map.get("a"));
    }

}
