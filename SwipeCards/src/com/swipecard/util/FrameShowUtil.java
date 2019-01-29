package com.swipecard.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.apache.ibatis.exceptions.ExceptionFactory;

import com.swipecard.SwipeCard;

public class FrameShowUtil {
    /** 
     * frame中的控件自适应frame大小：改变大小位置和字体 
     * @param frame 要控制的窗体 
     * @param proportion 当前和原始的比例 
     */  
    public static void modifyComponentSize(JFrame frame,double proportionW,double proportionH){  
          
        try   
        {  
        	 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  
 	        int swipeCardWidth=(int) (screenSize.width * proportionW);
 	        int swipeCardHeight=(int) (screenSize.height * proportionH);
 	        frame.setLocation((screenSize.width-swipeCardWidth)/2,(screenSize.height-swipeCardHeight)/2);
 	        frame.setSize(new Dimension(swipeCardWidth,swipeCardHeight));  
 	        
            Component[] components = frame.getContentPane().getComponents();  
            int count = 0;//计数  
            for(Component co:components)  
            {  
                String a = co.getClass().getName();//获取类型名称  
            	System.out.println(a);
               if(a.equals("javax.swing.JLabel"))  
                {  
                    count ++;  
                }  
               double locX = co.getX() * proportionW;  
               double locY = co.getY() * proportionH;  
               double width = co.getWidth() * proportionW;  
               double height = co.getHeight() * proportionH;  
                co.setLocation((int)locX, (int)locY);  
                co.setSize((int)width, (int)height);  
                int size = (int)(co.getFont().getSize() * proportionH);  
                Font font = new Font(co.getFont().getFontName(), co.getFont().getStyle(), size);  
                co.setFont(font);  
            }  
            System.out.println(count);
        }   
        catch (Exception e)   
        {  
        	throw ExceptionFactory.wrapException("ErrorCause: " + e, e);  
        }  
    }  
    
	 /** 
	    *  
	    * @param calculator 
	    * @param widthRate 宽度比例  
	    * @param heightRate 高度比例 
	    */  
	    public void sizeWindowOnScreen(JFrame frame, double widthRate, double heightRate)  
	    {  
	        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  
	        int swipeCardWidth=(int) (screenSize.width * widthRate);
	        int swipeCardHeight=(int) (screenSize.height * heightRate);
	        frame.setLocation((screenSize.width-swipeCardWidth)/2,(screenSize.height-swipeCardHeight)/2);
	        frame.setSize(new Dimension(swipeCardWidth,swipeCardHeight));  
	    }
	    
}
