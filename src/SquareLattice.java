import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SquareLattice extends JPanel implements Runnable {

	protected byte[][] arr;
	
	protected BufferedImage img;
	protected int[] imgArr;
	
	public SquareLattice(int width, int height, double density) {
	    img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    imgArr = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
	    
		arr = new byte[width][height];
		for(int i = 0; i < width; i++)
			for(int j = 0; j < height; j++) {
				for(int l = 0; l < 4; l++)
					if(Math.random() < density)
						arr[i][j] |= (byte)(1<<l);
			}
		
		for(int i = width/4; i < width; i++)
			for(int j = height/4; j < height; j++)
				arr[i][j] = 0;
		
		new Thread(this).start();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(arr.length, arr[0].length);
	}
	
	public void tick() {
		for(int i = 0; i < arr.length; i++) {
			if((arr[i][0] & 0b0010) == 0b0010)
				arr[i][0] ^= 0b1010;
			if((arr[i][arr[0].length-1] & 0b1000) == 0b1000)
				arr[i][arr[0].length-1] ^= 0b1010;
		}
		for(int i = 0; i < arr[0].length; i++) {
			if((arr[0][i] & 0b0100) == 0b0100)
				arr[0][i] ^= 0b0101;
			if((arr[arr.length-1][i] & 0b0001) == 0b0001)
				arr[arr.length-1][i] ^= 0b0101;
		}
		
		byte newArr[][] = new byte[arr.length][arr[0].length];
		
		for(int i = 0; i < arr.length; i++)
			for(int j = 0; j < arr[0].length; j++) {
				if(i < arr.length-1)
					newArr[i+1][j] |= (arr[i][j] & 0b0001);
				if(j > 0)
					newArr[i][j-1] |= (arr[i][j] & 0b0010);
				if(i > 0)
					newArr[i-1][j] |= (arr[i][j] & 0b0100);
				if(j < arr[0].length-1)
					newArr[i][j+1] |= (arr[i][j] & 0b1000);
			}
		
		for(int i = 0; i < arr.length; i++)
			for(int j = 0; j < arr[0].length; j++)
				if(Integer.bitCount(newArr[i][j]) == 2)
					newArr[i][j] ^= 0b1111;
		
		arr = newArr;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for(int i = 0; i < arr.length; i++)
			for(int j = 0; j < arr[0].length; j++) {
				int n = (int)(255*(Integer.bitCount(arr[i][j])/4.0));
				n += (n<<16)+(n<<8);
				imgArr[i+j*arr.length] = n;
			}
		
		g.drawImage(img, 0, 0, null);
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				Thread.sleep(5);
				tick();
				repaint();
			}
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().add(new SquareLattice(400, 400, 0.7));
		frame.pack();
		frame.setVisible(true);
	}
	
}