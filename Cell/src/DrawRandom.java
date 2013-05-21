import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javax.swing.*;

class cellPointSet {
	private int x;
	private int y;

	cellPointSet() {
		this.setX(0);
		this.setY(0);
	}

	public void setX(int num1) {
		this.x = num1;
	}

	public void setY(int num2) {
		this.y = num2;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}
}

class Cell {
	private int currentstatus;
	private int paststatus;

	Cell() {
		this.setCurrentStatus(0);
		this.setPastStatus(0);
	}

	public void setCurrentStatus(int num1) {
		this.currentstatus = num1;
	}

	public void setPastStatus(int num2) {
		this.paststatus = num2;
	}

	public int getCurrentStatus() {
		return this.currentstatus;
	}

	public int getPastStatus() {
		return this.paststatus;
	}
}

public class DrawRandom extends JFrame {
	/**
	 * 
	 */
	public static int dimension = 64;// 维度
	cellPointSet[] set = new cellPointSet[(dimension + 2) * (dimension + 2)];
	Cell[][] CAset = new Cell[(dimension + 2)][(dimension + 2)];

	// 重复检查
	public boolean checkExist(int x, int y, int pre) {
		for (int i = 0; i < pre; i++) {
			if (set[i].getX() == x && set[i].getY() == y)
				return true;
		}
		return false;
	}

	// private static final long serialVersionUID = 1L;

	public DrawRandom() {
		try {
			initialize();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}// 调用初始化方法
	}

	public void initializeStatucs(double Occupy) {
		
		int OccupyNumber = (int) (Occupy * dimension * dimension);// 初始特异点个数
		int[] buff = new int[2];
		// 随机对象
		Random r = new Random();
		for (int i = 0; i < OccupyNumber; i++) {
			for (int j = 0; j < 2; j++) {
				buff[j] = r.nextInt(dimension);// 产生0-dimension之间的随机整数
			}
			if (checkExist(buff[0], buff[1], i))// 如果这个点已经存在
			{
				i--;
			} else {
				set[i] = new cellPointSet();
				set[i].setX(buff[0] + 1);
				set[i].setY(buff[1] + 1);
				CAset[set[i].getX()][set[i].getY()].setCurrentStatus(1);
			}
		}
	}

	public void nextStatus()// 根据模型确定下一时刻状态
	{
		for (int m = 0; m < dimension + 2; m++) {
			for (int n = 0; n < dimension + 2; n++) {
				CAset[m][n].setPastStatus(CAset[m][n].getCurrentStatus());
			}
		}
		int counter = 0;
		for (int m = 1; m < dimension + 1; m++) {
			for (int n = 1; n < dimension + 1; n++) {
				counter = 0;				
				counter = CAset[m - 1][n - 1].getPastStatus()
						+ CAset[m - 1][n].getPastStatus()
						+ CAset[m - 1][n + 1].getPastStatus()
						+ CAset[m][n - 1].getPastStatus()
						+ CAset[m][n + 1].getPastStatus()
						+ CAset[m + 1][n - 1].getPastStatus()
						+ CAset[m + 1][n].getPastStatus()
						+ CAset[m + 1][n + 1].getPastStatus();
				if (CAset[m][n].getPastStatus() == 1) {
					if (counter >= 2 && counter <= 3)
						CAset[m][n].setCurrentStatus(1);
					else
						CAset[m][n].setCurrentStatus(0);
				} else if (CAset[m][n].getPastStatus() == 0) {
					if (counter == 3)
						CAset[m][n].setCurrentStatus(1);
					else
						CAset[m][n].setCurrentStatus(0);
				}
			}
		}

	}

	public void initialize() throws InterruptedException {
		this.setSize(dimension * 11, dimension * 11);
		this.setTitle("yooooooooo");
		CanvasPanel cp = new CanvasPanel();
		this.setContentPane(cp);
		// this.add(cp);
		cp.display();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new DrawRandom().setVisible(true);
	}

	class CanvasPanel extends JPanel implements Runnable {
		/**
		 * 
		 */
		double Occupy = 0.5;
		// private static final long serialVersionUID = 1L;
		Thread thread = new Thread(this);

		public void display() {
			for (int i = 0; i < dimension + 2; i++) {
				for (int j = 0; j < dimension + 2; j++) {
					CAset[i][j] = new Cell();
				}
			}

			thread.start();
		}

		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2 = (Graphics2D) g;
			Shape[][] shapes = new Shape[dimension + 2][dimension + 2];

			// 初始化网格&&初始化元胞
			for (int i = 1; i < dimension + 1; i++) {
				for (int j = 1; j < dimension + 1; j++) {
					shapes[i][j] = new Rectangle2D.Double(i * 10, j * 10, 10,
							10);
				}
			}
			for (int i = 1; i < dimension + 1; i++) {
				for (int j = 1; j < dimension + 1; j++) {
					if (CAset[i][j].getCurrentStatus() == 1) {
						g2.setColor(Color.RED);
						g2.fill(shapes[i][j]);
						g2.setColor(Color.BLACK);
						g2.draw(shapes[i][j]);
					} else {
						g2.setColor(Color.GREEN);
						g2.fill(shapes[i][j]);
						g2.setColor(Color.BLACK);
						g2.draw(shapes[i][j]);
					}
				}
			}
		}

		@Override
		public void run() {
			for (int i = 0; i < 100; i++) {
				if (i == 0) {
					initializeStatucs(Occupy);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					repaint();
				} else {
					nextStatus();
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					repaint();
				}
			}
		}
	}
}
