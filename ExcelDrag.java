import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class ExcelDrag extends JPanel implements MouseListener, MouseMotionListener, ActionListener
{
	BufferedImage image;
	boolean dragging;
	int boxWidth, boxHeight;					// in pixels
	int cellOriginX, cellOriginY;				// in pixels (i.e. (251, 323))
	int startingCellX, startingCellY;			// in actual cell coordinates (i.e. (1,1) & (2,3))
	int selectionWidth, selectionHeight;		// in actual cell coordinates (i.e. selectionWidth = 3)
	Timer timer;
	int simulationMouseX, simulationMouseY;		// in pixels
	boolean animation;
	BufferedImage mouseArrow;
	BufferedImage downArrow;
	Clip introPrompt, tryPrompt;
	JButton animationButton;
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
		drawShadedBox(g);
		drawBox(g);
		if (animation)
		{
			g.drawImage(mouseArrow, simulationMouseX, simulationMouseY, null);
		}
		else
		{
			g.drawImage(downArrow, 225, 270, null);
		}
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
		else if (animation)
		{
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
			else if (animation)
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
						if (selectionWidth < 1)
						{
							selectionWidth = 1;
						}
						break;
					}
				}
				for (int i = 1; i < 11; i++)
				{
					if (y < cellOriginY + i*boxHeight)
					{
						selectionHeight = i-startingCellY;
						if (selectionHeight < 1)
						{
							selectionHeight = 1;
						}
						break;
					}
				}
			}
			repaint();
		}
	}
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if (source == timer)
		{
			int x = simulationMouseX;
			int y = simulationMouseY;
			if (selectionWidth > 3 || selectionHeight > 8)
			{
				timer.stop();
				animation = false;
				selectionWidth = 1;
				selectionHeight = 1;
				simulationMouseX = 243;
				simulationMouseY = 329;
				tryPrompt.setFramePosition(0);
				tryPrompt.start();
				JOptionPane.showMessageDialog(this, "Now, it's your turn!");
				repaint();
				return;
			}
			for (int i = 1; i < 6; i++)
			{
				if (x < cellOriginX + i*boxWidth)
				{
					selectionWidth = i-startingCellX;
					if (selectionWidth < 1)
					{
						selectionWidth = 1;
					}
					break;
				}
			}
			for (int i = 1; i < 11; i++)
			{
				if (y < cellOriginY + i*boxHeight)
				{
					selectionHeight = i-startingCellY;
					if (selectionHeight < 1)
					{
						selectionHeight = 1;
					}
					break;
				}
			}
			repaint();
			simulationMouseX += 2;
			simulationMouseY += 3;
		}
		else if (source == animationButton)
		{
			if (animation)
			{
				return;
			}
			startingCellX = 1;
			startingCellY = 1;
			selectionWidth = 1;
			selectionHeight = 1;
			dragging = false;
			simulationMouseX = 243;
			simulationMouseY = 329;
			animation = true;
			timer.start();
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
		g.setColor(new Color(130, 130, 255, 50));
		g.fillRect(cellOriginX + startingCellX*boxWidth, cellOriginY + startingCellY*boxHeight, selectionWidth*boxWidth, selectionHeight*boxHeight);
	}
	public void loadImages()
	{
		try
		{
			image = ImageIO.read(new File("ExcelDrag.png"));
			mouseArrow = ImageIO.read(new File("mouseArrow.png"));
			downArrow = ImageIO.read(new File("downArrow.png"));
		}
		catch (IOException e) {}
	}
	public ExcelDrag()
	{
		JFrame frame = new JFrame("Excel Select/Drag Simulator");
		addMouseListener(this);
		addMouseMotionListener(this);
		loadImages();
		boxWidth = 127;
		boxHeight = 39;
		cellOriginX = 52;
		cellOriginY = 270;
		startingCellX = 1;
		startingCellY = 1;
		selectionWidth = 1;
		selectionHeight = 1;
		dragging = false;
		simulationMouseX = 243;
		simulationMouseY = 329;
		timer = new Timer(25, this);
		animationButton = new JButton("Play Animation!");
		animationButton.addActionListener(this);
		add(animationButton);
		animation = true;
		frame.setContentPane(this);
		frame.setSize(698, 698);
		frame.setLocation(200,100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File("IntroPrompt.wav"));
            introPrompt = AudioSystem.getClip();
            introPrompt.open(stream);
			stream = AudioSystem.getAudioInputStream(new File("tryPrompt.wav"));
            tryPrompt = AudioSystem.getClip();
            tryPrompt.open(stream);
        }
        catch (Exception e) {
            System.out.println("Unable to open sound file!");
        }
		introPrompt.setFramePosition(0);
        introPrompt.start();
		JOptionPane.showMessageDialog(this, "Learn how to select multiple cells by dragging.");
		timer.start();
	}
	public static void main(String[] args)
	{
		new ExcelDrag();
	}
}