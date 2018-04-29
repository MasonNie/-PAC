import java.awt.EventQueue;

public class Graduate {


    public static void main(String[] args){
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI frame = new UI();
					frame.setVisible(true);
					//frame.setResizable(false); //窗口大小不能改变
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});//初始化界面
    	
    }
}