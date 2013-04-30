import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ExcelDrag extends JPanel implements MouseListener, MouseMotionListener
{
	BufferedImage image;
	boolean dragging;
	int boxWidth, boxHeight;
	int cellOriginX, cellOriginY;				// in pixels (i.e. (250, 323))
	int startingCellX, startingCellY;			// in actual cell coordinates (i.e. (1,1) & (2,3))
	int endingCellX, endingCellY;				// in actual cell coordinates (i.e. (1,1) & (2,3))
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
		drawShadedBox(g);
		drawBox(g);
	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void drawBox(Graphics g)
	{
		g.setColor(new Color(0, 0, 0, 255));
		g.drawRect(x, y, regionWidth*boxWidth, regionHeight*boxHeight);
		g.drawRect(x+1, y+1, boxWidth-2, boxHeight-2);
	}
	public void drawShadedBox(Graphics g)
	{
		g.setColor(new Color(150, 150, 150, 50));
		g.fillRect(x, y, boxWidth, boxHeight);
	}
	public void loadImage()
	{
		try
		{
			image = ImageIO.read(new File("ExcelDrag.png"));
		}
		catch (IOException e) {}
	}
	public ExcelDrag()
	{
		JFrame frame = new JFrame("Excel Select/Drag Simulator");
		loadImage();
		boxWidth = 127;
		boxHeight = 39;
		cellOriginX = 52;
		cellOriginY = 270;
		startingCellX = 0;
		startingCellY = 0;
		endingCellX = 0;
		endingCellY = 0;
		frame.setContentPane(this);
		frame.setSize(698, 698);
		frame.setLocation(200,200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	public static void main(String[] args)
	{
		new ExcelDrag();
	}
}