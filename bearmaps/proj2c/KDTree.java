package bearmaps.proj2c;

import bearmaps.proj2ab.Point;

import java.util.List;

public class KDTree {

    private static final boolean HORIZONTAL = true;
    private static final boolean VERTICAL = false;
    private Node root;
    private static final double EPSILON = 0.0000001;

    private class Node {
        private Point value;
        private Node left;
        private Node right;
        private boolean orientation;

        private Node(Point p, boolean o) {
            this.value = p;
            orientation = o;
        }
    }

    private void add(Node n, Point p, boolean o) {
        if (n == null) {
            n = new Node(p, o);
        } else if (n.orientation == VERTICAL) {
            if (p.getX() < n.value.getX()) {
                o = !o;
                if (n.left == null) {
                    n.left = new Node(p, o);
                    return;
                }
                add(n.left, p, o);
            } else if (p.getX() > n.value.getX()) {
                o = !o;
                if (n.right == null) {
                    n.right = new Node(p, o);
                    return;
                }
                add(n.right, p, o);
            } else if (p.getX() == n.value.getX()) {
                if (p.getY() == n.value.getY()) {
                    return;
                } else {
                    o = !o;
                    if (n.right == null) {
                        n.right = new Node(p, o);
                    }
                    add(n.right, p, o);
                }
            }
        } else if (n.orientation == HORIZONTAL) {
            if (p.getY() < n.value.getY()) {
                o = !o;
                if (n.left == null) {
                    n.left = new Node(p, o);
                }
                add(n.left, p, o);
            } else if (p.getY() > n.value.getY()) {
                o = !o;
                if (n.right == null) {
                    n.right = new Node(p, o);
                }
                add(n.right, p, o);
            } else if (p.getY() == n.value.getY()) {
                if (p.getX() == n.value.getX()) {
                    return;
                } else {
                    o = !o;
                    if (n.right == null) {
                        n.right = new Node(p, o);
                    }
                    add(n.right, p, o);
                }
            }
        }

    }

    public KDTree(List<Point> points) {
        int size = points.size();
        if (size == 0) {
            return;
        }
        if (size == 1) {
            root = new Node(points.get(0), VERTICAL);
            return;
        }
        root = new Node(points.get(0), VERTICAL);
        for (int x = 1; x < size; x++) {
            add(root, points.get(x), VERTICAL);
        }
    }

    public Point nearest(double x, double y) {
        Point p = new Point(x, y);
        if (root == null) {
            return null;
        }
        Node best = root;
        double pruneTemp = Math.sqrt(Point.distance(root.value, p)) - EPSILON;
        best = nearest(root, p, root, pruneTemp);
        return best.value;
    }

    private Node nearest(Node n, Point goal, Node best, double prune) {
        if (n == null) {
            return best;
        }
        if (Point.distance(goal, n.value) < Point.distance(goal, best.value)) {
            best = n;
            prune = Math.sqrt(Point.distance(best.value, goal)) - EPSILON;
        }
        if (n.orientation == VERTICAL) {
            double dist = Math.sqrt(Point.distance(goal, best.value));
            double smallerValue = distancePoints(goal.getX(), n.value.getX() - EPSILON);
            double biggerValue = distancePoints(goal.getX(), n.value.getX() + EPSILON);
            if (prune < dist) {
                if (smallerValue < biggerValue) {
                    if (distancePoints(n.value.getX(), goal.getX()) < prune) {
                        prune = distancePoints(n.value.getX(), goal.getX());
                    }
                    best = nearest(n.left, goal, best, prune);
                    if (biggerValue < dist) {
                        best = nearest(n.right, goal, best,
                                distancePoints(n.value.getX(), goal.getX()));
                    }
                } else if (biggerValue < smallerValue) {
                    if (distancePoints(n.value.getX(), goal.getX()) < prune) {
                        prune = distancePoints(n.value.getX(), goal.getX());
                    }
                    best = nearest(n.right, goal, best, prune);
                    if (smallerValue < dist) {
                        best = nearest(n.left, goal, best,
                                distancePoints(n.value.getX(), goal.getX()));
                    }
                }

            }
        }
        if (n.orientation == HORIZONTAL) {
            double dist = Math.sqrt(Point.distance(goal, best.value));
            double smallerValue = distancePoints(goal.getY(), n.value.getY() - EPSILON);
            double biggerValue = distancePoints(goal.getY(), n.value.getY() + EPSILON);
            if (prune < dist) {
                if (smallerValue < biggerValue) {
                    if (distancePoints(n.value.getY(), goal.getY()) < prune) {
                        prune = distancePoints(n.value.getY(), goal.getY());
                    }
                    best = nearest(n.left, goal, best, prune);
                    if (biggerValue < dist) {
                        best = nearest(n.right, goal, best,
                                distancePoints(n.value.getY(), goal.getY()));
                    }
                } else if (biggerValue < smallerValue) {
                    if (distancePoints(n.value.getY(), goal.getY()) < prune) {
                        prune = distancePoints(n.value.getY(), goal.getY());
                    }
                    best = nearest(n.right, goal, best, prune);
                    if (smallerValue < dist) {
                        best = nearest(n.left, goal, best,
                                distancePoints(n.value.getY(), goal.getY()));
                    }
                }
            }
        }
        return best;
    }

    private double distancePoints(double d1, double d2) {
        return Math.abs(d1 - d2);
    }

}
