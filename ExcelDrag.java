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
	int cellOriginX, cellOriginY;				// in pixels (i.e. (251, 323))
	int startingCellX, startingCellY;			// in actual cell coordinates (i.e. (1,1) & (2,3))
	int selectionWidth, selectionHeight;		// in actual cell coordinates (i.e. selectionWidth = 3)
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
		drawShadedBox(g);
		drawBox(g);
	}
	public void mousePressed(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		if (x < cellOriginX || x > cellOriginX + 5*boxWidth || y < cellOriginY || y > cellOriginY + 10*boxHeight)
		{
			System.out.println("Can't touch here!");
			return;
		}
		else
		{
			for (int i = 1; i < 6; i++)
			{
				if (x < cellOriginX + i*boxWidth)
				{
					startingCellX = i-1;
					selectionWidth = 1;
					break;
				}
			}
			for (int i = 1; i < 11; i++)
			{
				if (y < cellOriginY + i*boxHeight)
				{
					startingCellY = i-1;
					selectionHeight = 1;
					break;
				}
			}
		}
		dragging = true;
		repaint();
	}
	public void mouseReleased(MouseEvent e)
	{
		dragging = false;
	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	public void mouseDragged(MouseEvent e)
	{
		if (dragging)
		{
			int x = e.getX();
			int y = e.getY();
			if (x < cellOriginX || x > cellOriginX + 5*boxWidth || y < cellOriginY || y > cellOriginY + 10*boxHeight)
			{
				return;
			}
			else
			{
				for (int i = 1; i < 6; i++)
				{
					if (x < cellOriginX + i*boxWidth)
					{
						selectionWidth = i-startingCellX;
						break;
					}
				}
				for (int i = 1; i < 11; i++)
				{
					if (y < cellOriginY + i*boxHeight)
					{
						selectionHeight = i-startingCellY;
						break;
					}
				}
			}
			repaint();
		}
	}
	public void drawBox(Graphics g)
	{
		g.setColor(new Color(0, 0, 0, 255));
		g.drawRect(cellOriginX + startingCellX*boxWidth, cellOriginY + startingCellY*boxHeight, selectionWidth*boxWidth, selectionHeight*boxHeight);
		g.drawRect(cellOriginX + startingCellX*boxWidth + 1, cellOriginY + startingCellY*boxHeight + 1, selectionWidth*boxWidth-2, selectionHeight*boxHeight-2);
	}
	public void drawShadedBox(Graphics g)
	{
		g.setColor(new Color(150, 150, 150, 50));
		g.fillRect(cellOriginX + startingCellX*boxWidth, cellOriginY + startingCellY*boxHeight, selectionWidth*boxWidth, selectionHeight*boxHeight);
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
		addMouseListener(this);
		addMouseMotionListener(this);
		loadImage();
		boxWidth = 127;
		boxHeight = 39;
		cellOriginX = 52;
		cellOriginY = 270;
		startingCellX = 0;
		startingCellY = 0;
		selectionWidth = 1;
		selectionHeight = 1;
		dragging = false;
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