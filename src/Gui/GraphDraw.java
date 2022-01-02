package Gui;

import Util.Point3D;
import Util.Range;
import Util.Range2D;
import Util.Range2Range;
import classes.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Represnation of the graph using java swing.
 */
public class GraphDraw extends JPanel {
    Border border = BorderFactory.createLineBorder(Color.green);
    DirectedWeightedGraph graph;
    Range2Range range;
    ArrayList<Agent> agents;
    ArrayList<Pokemon> pokemons;
    GameManger gm;


    public GraphDraw(GameManger gm) {
        this.gm = gm;
        graph = gm.getGraph();
        this.setBounds(0, 0, 750, 600);
        this.agents = gm.getAgents();
        this.pokemons = gm.getPokemons();
        this.setBorder(border);
        importImages();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        resize();
        Graphics2D g2d = (Graphics2D) g.create();
        paintBackground(g2d);
        drawGraph(g2d);
    }

    private void drawGraph(Graphics2D g2d) {
        for (Map.Entry<Integer, HashMap<Integer, EdgeData>> entry : graph.getGraph().entrySet()) {
            int nodeKey = entry.getKey();
            Iterator<EdgeData> eItr = graph.edgeIter(nodeKey);
            while (eItr.hasNext()) {
                drawEdge(g2d, eItr.next());
            }
        }
        Iterator<NodeData> nd = graph.nodeIter();
        while (nd.hasNext()) {
            drawNode(g2d, nd.next());
        }
        for (Agent agent : gm.getAgents()) {
            drawAgent(g2d, agent);
        }
        for (Pokemon pokemon : gm.getPokemons()) {
            drawPokemon(g2d, pokemon);
        }
    }

    private void drawPokemon(Graphics2D g2d, Pokemon pokemon) {
        GeoLocation pokemonGeoLocation = pokemon.getPos();
        Point3D agentScreenLocation = (Point3D) range.world2frame(pokemonGeoLocation);
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(0, 155, 0));
        g2d.drawOval((int) agentScreenLocation.x(), (int) agentScreenLocation.y(), 15, 15);
    }

    private void drawAgent(Graphics2D g2d, Agent agent) {
        GeoLocation agentGeoLocation = agent.getPos();
        Point3D agentScreenLocation = (Point3D) range.world2frame(agentGeoLocation);
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(255, 0, 0));
        g2d.drawOval((int) agentScreenLocation.x(), (int) agentScreenLocation.y(), 15, 15);
    }

    private void drawEdge(Graphics2D g2d, EdgeData edge) {
        GeoLocation s = (GeoLocation) graph.getNodes().get(edge.getSrc()).getLocation();
        GeoLocation d = (GeoLocation) graph.getNodes().get(edge.getDest()).getLocation();
        Point3D sP = (Point3D) range.world2frame(s);
        Point3D dP = (Point3D) range.world2frame(d);
        g2d.setColor(new Color(173, 122, 68));
        drawArrow(g2d, (int) sP.x(), (int) sP.y(), (int) dP.x(), (int) dP.y());

    }

    private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g2d.create();
        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);
        int ARR_SIZE = 7;
        g.setStroke(new BasicStroke(4));
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[]{len - 10, len - ARR_SIZE - 20, len - ARR_SIZE - 10, len - 20}, new int[]{0, -ARR_SIZE, ARR_SIZE, 0}, 3);
    }


    private void drawNode(Graphics g2d, NodeData next) {
        GeoLocation pos = (GeoLocation) next.getLocation();
        Point3D fp = (Point3D) range.world2frame(pos);
        g2d.drawImage(nodePaint, (int) fp.x() - 10, (int) fp.y() - 15, null, this);
        g2d.setFont(new Font("MV Boli", Font.BOLD, 20));
        g2d.setColor(new Color(120, 35, 255));
        g2d.drawString(" " + next.getKey(), (int) fp.x() - 14, (int) fp.y() - 20);

    }

    private void resize() {
        Range rx = new Range(35, this.getWidth() - 30);
        Range ry = new Range(this.getHeight() - 20, 45);
        Range2D frame = new Range2D(rx, ry);
        range = new Range2Range(graphRange(), frame);
    }

    private Range2D graphRange() {
        double x0 = 0, x1 = 0, y0 = 0, y1 = 0;
        boolean first = true;
        for (Map.Entry<Integer, NodeData> runner : graph.getNodes().entrySet()) {
            GeoLocation p = (GeoLocation) graph.getNode(runner.getKey()).getLocation();
            if (first) {
                x0 = p.x();
                x1 = x0;
                y0 = p.y();
                y1 = y0;
                first = false;
            } else {
                if (p.x() < x0) x0 = p.x();
                if (p.x() > x1) x1 = p.x();
                if (p.y() < y0) y0 = p.y();
                if (p.y() > y1) y1 = p.y();
            }
        }
        Range xr = new Range(x0, x1);
        Range yr = new Range(y0, y1);
        return new Range2D(xr, yr);
    }

    static BufferedImage backGround = null;
    static BufferedImage nodePaint = null;


    private void paintBackground(Graphics2D g2d) {
        g2d.drawImage(backGround, 0, 0, this.getWidth(), this.getHeight(), this);

    }

    public void importImages() {
        try {
            backGround = ImageIO.read(new File("resources/Background1.jpg"));
            nodePaint = ImageIO.read(new File("resources/new.png"));
            /*
            For jar file images
             */
//            backGround = ImageIO.read(getClass().getResourceAsStream("/resources/Background1.jpg"));
//            nodePaint = ImageIO.read(getClass().getResourceAsStream("/resources/new.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}