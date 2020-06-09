import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Prism extends JPanel {

	private int width, height;
	private int x1, x2, x3, y1, y2, y3;
	JRadioButton fristRadioB;
	JRadioButton secondRadioB;
	Color color = new Color(0x9500597B, true);
	Color lineColor = Color.BLACK;
	double finalX, finalY;
	double finalX2, finalY2;
    double beamAngle = 43;
    double beamAngle2;
    double beamAngle3;
    double coef1 = 1.5;
    double coef2;
    int startX, startY;
    boolean whiteLight = false;


	@Override
	protected void paintComponent(Graphics gg) {
		super.paintComponent(gg);
		
        Graphics2D g = (Graphics2D) gg;
		width = getWidth();
		height = getHeight();

		g.setStroke(new BasicStroke(2)); // grubość lini promienia odbitego 


		g.setColor(color);
		g.fillPolygon(new int[] {x1, x2, x3}, new int[] {y1, y2, y3}, 3);
		g.setColor(lineColor);

        // rysowanie pryzmatu 
		drawLine(x1 - 300, startX, (int) (Math.tan(Math.toRadians(beamAngle - 45)) * (x1 - 300)) + startY, startY, g);
		g.setStroke(new BasicStroke(1));
		drawLine(x1 - 300, startX, (int) (Math.tan(Math.toRadians( - beamAngle)) * (x1 - 300)) + startY, startY, g);
		g.setStroke(new BasicStroke(2));
		
// rozszczepienie światła białego 
		rysuj(g, beamAngle, lineColor);
		if(whiteLight) {
			rysuj(g, beamAngle + 0.5, new Color(90, 0, 90));
			rysuj(g, beamAngle + 1, new Color(75, 0, 130));
			rysuj(g, beamAngle + 1.5, new Color(0, 0, 255));
			rysuj(g, beamAngle + 2, new Color(0, 255, 255));
			rysuj(g, beamAngle + 2.5, new Color(0, 255, 0));
			rysuj(g, beamAngle + 3, new Color(252, 233, 3));
			rysuj(g, beamAngle + 3.5, new Color(252, 147, 3));
			rysuj(g, beamAngle + 3.5, new Color(255, 0, 0));
		}
		repaint();
		
// ustawienie wartości wierzchołków 60-60-60 
		fristRadioB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				x1 = width/2;
				x2 = x1 + 100;
				x3 = x1 - 100;
				y1 = height/2-70;
				y2 = y1 + 170;
				y3 = y1 + 170;
				startX = x1 - 30;
				startY = y1 + 50;
				repaint();
			}
		});

// ustawienie wartości wierzchołków 45-90-45  
		secondRadioB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				x1 = width/2;
				x2 = x1 + 100;
				x3 = x1 - 100;
				y1 = height/2;
				y2 = y1 + 100;
				y3 = y1 + 100;
				startX = x1 - 30;
				startY = y1 + 30;
				repaint();
			}
		});

	}


	public void setFristRadioB(JRadioButton fristRadioB) {
		this.fristRadioB = fristRadioB;
	}

	public void setSecondRadioB(JRadioButton secondRadioB) {
		this.secondRadioB = secondRadioB;
	}

	public void setColor(Color color) {
		this.color = color;
		repaint();
	}

	public void drawLine(int x1, int x2, int y1, int y2, Graphics2D g) {
        g.drawLine(x1, y1, x2, y2);
    }


    public double findLineA(int x1, int x2, int y1, int y2) { // współczynnik a w ax+b
	    double a = (double)( y2-y1)/(x2 - x1);
	    return a;
    }

    public double findLineB(int x1, int x2, int y1, int y2) { // współczynnik b w ax+b 
        double a = (double)( y2-y1)/(x2 - x1);
        double b = (double) y2 - (a * x2);
        return b;
    }

    public void setBeamAngle(int beamAngle) {
        this.beamAngle = beamAngle - 45;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

	public void setCoef2(double coef2) {
		this.coef2 = coef2;
		repaint();
	}

	// obliczenia związane z załamaniem i odbiciem wiązki w pryzmacie 
	public void rysuj(Graphics2D g, double beamAngle, Color color) {
		g.setColor(color);
		beamAngle2 = Math.asin((coef2/coef1)*Math.sin(Math.toRadians(beamAngle)));
		beamAngle3 = Math.asin((coef1/coef2)*Math.sin(beamAngle2));

		finalX = (startY - findLineB(x1,x2,y1,y2)) / (findLineA(x1,x2,y1,y2) + Math.tan(beamAngle2));
		finalY = (-finalX * Math.tan(beamAngle2) + startY);


		if(finalX > x1 && finalX < x2) { // ograniczenie biegu wiązki przez wierzchłoki pryzmatu 

			finalX2 = 800;
			finalY2 = (-finalX2 * Math.tan(beamAngle3) + startY);
			drawLine(startX, (int) finalX, startY, (int) finalY, g);
			drawLine((int) finalX,(int) finalX2, (int) finalY , (int) finalY2, g);

		}
	}

	public void setWhiteLight(boolean whiteLight) {
		this.whiteLight = whiteLight;
	}
}
