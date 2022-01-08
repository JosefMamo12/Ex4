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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    JButton stopButton;


    public GraphDraw(GameManger gm) {
        this.stopButton = new JButton();
        this.gm = gm;
        graph = gm.getGraph();
        this.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        this.agents = gm.getAgents();
        this.pokemons = gm.getPokemons();
        this.setBorder(border);
        stopButton.addActionListener(e -> {
            if (e.getSource() == stopButton)
                gm.stop();
        });
        importImages();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        resize();
        stopButton.setText("Stop");
        stopButton.setBackground(new Color(255, 32, 15));
        stopButton.setBounds(this.getWidth() - 150, 50, 100, 50);
        Graphics2D g2d = (Graphics2D) g.create();
        this.add(stopButton);
        paintBackground(g2d);
        drawGraph(g2d);
        if(gm.isRunning())
            drawInfo(g2d);
    }

    private void drawInfo(Graphics2D g2d) {
        g2d.drawRect(38,12 , 400,25);
        g2d.setColor(Color.white);
        g2d.fillRect(38,12 , 400,25);
        g2d.setFont(new Font("Ariel", Font.BOLD,15));
        g2d.setColor(new Color(0,0,0));
        g2d.drawString("  Case: " + gm.getGameServer().getGameLevel() + ", Grade: " + gm.getGameServer().getGrade() + ", Moves: " + gm.getGameServer().getMoves() + ", Time To END: " + TimeToSec(Integer.parseInt(gm.TimeToEnd())), 40, 30);
    }
    private static int TimeToSec(int time){
        return time / 1000;
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
        Point3D pokemonScreenLocation = (Point3D) range.world2frame(pokemonGeoLocation);
        if (pokemon.getValue() <= 5)
            g2d.drawImage(sunji, (int) pokemonScreenLocation.x() - 18, (int) pokemonScreenLocation.y() - 15, null, this);
        else if (pokemon.getValue() > 5 && pokemon.getValue() <= 10)
            g2d.drawImage(zoro, (int) pokemonScreenLocation.x() - 18, (int) pokemonScreenLocation.y() - 15, null, this);
        else
            g2d.drawImage(luffy, (int) pokemonScreenLocation.x() - 18, (int) pokemonScreenLocation.y() - 15, null, this);

    }

    private void drawAgent(Graphics2D g2d, Agent agent) {
        GeoLocation agentGeoLocation = agent.getPos();
        Point3D agentScreenLocation = (Point3D) range.world2frame(agentGeoLocation);
        g2d.setColor(new Color(0, 0, 0));
        g2d.drawRect((int) agentScreenLocation.x() + 7, (int) agentScreenLocation.y() - 78, 85, 68);
        g2d.setFont(new Font("Ariel", Font.BOLD, 14));
        g2d.drawString("Id: " + agent.getId(), (int) agentScreenLocation.x() + 10, (int) agentScreenLocation.y() - 60);
        g2d.drawString("Src: " + agent.getSrc(), (int) agentScreenLocation.x() + 10, (int) agentScreenLocation.y() - 45);
        g2d.drawString("Dest: " + agent.getDest(), (int) agentScreenLocation.x() + 10, (int) agentScreenLocation.y() - 30);
        g2d.drawString("Value: " + agent.getValue(), (int) agentScreenLocation.x() + 10, (int) agentScreenLocation.y() - 15);
        g2d.drawImage(agentImage, (int) agentScreenLocation.x() - 20, (int) agentScreenLocation.y() - 15, null, this);
    }

    private void drawEdge(Graphics2D g2d, EdgeData edge) {
        GeoLocation s = (GeoLocation) graph.getNodes().get(edge.getSrc()).getLocation();
        GeoLocation d = (GeoLocation) graph.getNodes().get(edge.getDest()).getLocation();
        Point3D sP = (Point3D) range.world2frame(s);
        Point3D dP = (Point3D) range.world2frame(d);
        g2d.setColor(new Color(0, 143, 255));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine((int)sP.x(), (int) sP.y(),(int)dP.x(),(int)dP.y() );

    }


    private void drawNode(Graphics g2d, NodeData next) {
        GeoLocation pos = (GeoLocation) next.getLocation();
        Point3D fp = (Point3D) range.world2frame(pos);
        g2d.drawImage(nodePaint, (int) fp.x() - 25, (int) fp.y() - 30, null, this);
        g2d.setFont(new Font("MV Boli", Font.BOLD, 20));
        g2d.setColor(new Color(0, 35, 0));
        g2d.drawString(" " + next.getKey(), (int) fp.x() - 14, (int) fp.y() - 20);

    }

    private void resize() {
        Range rx = new Range(50, this.getWidth() - 300);
        Range ry = new Range(this.getHeight() - 50, 100);
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
    static BufferedImage luffy = null;
    static BufferedImage zoro = null;
    static BufferedImage sunji = null;
    static BufferedImage agentImage = null;


    private void paintBackground(Graphics2D g2d) {
        g2d.drawImage(backGround, 0, 0, this.getWidth(), this.getHeight(), this);

    }

    public void importImages() {
        try {

            backGround = ImageIO.read(new File("resources/One-Piece-1000th-Episodes(2).png"));
            agentImage = ImageIO.read(new File("resources/agent.png"));
            nodePaint = ImageIO.read(new File("resources/pirateboat.png"));
            luffy = ImageIO.read(new File("resources/luffy.png"));
            zoro = ImageIO.read(new File("resources/zoro.png"));
            sunji = ImageIO.read(new File("resources/sunji.png"));

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