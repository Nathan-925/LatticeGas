import java.awt.Graphics;
import javax.swing.JFrame;

public class SquareLatticeData extends SquareLattice {

    private int scanWidth = 10, scanHeight = 10;
    
    public SquareLatticeData(int width, int height, double density) {
        super(width, height, density);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; j++) {
                double sum = 0;
                for(int ii = -scanWidth/2; ii < scanWidth/2; ii++)
                    for(int jj = -scanHeight/2; jj < scanHeight/2; jj++)
                        if(i+ii >= 0 && i+ii < arr.length && j+jj >= 0 && j+jj < arr[i].length)
                            sum += Integer.bitCount(arr[i+ii][j+jj]);
                sum /= 4*scanWidth*scanHeight;
                imgArr[i+j*arr.length] = ((int)(255*sum)<<16) | ((255-(int)(255*sum)<<8));
            }
        }
        g.drawImage(img, 0, 0, null);
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().add(new SquareLatticeData(400, 400, 1));
        frame.pack();
        frame.setVisible(true);
    }

}
