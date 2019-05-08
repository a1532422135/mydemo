package com.example.test.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 控制内存使用的百分比<br>
 * 控制程序所用内存占分配给jvm的总内存的百分比<br>
 * 比如 new MemoryCheck(80) , 表示最多允许使用 80% 的内存<br>
 * 
 * 请在分配内存的关键点执行 hasMemory 检查，如果返回false,说明没有空闲内存空间了<br>
 * 程序中就需要暂停分配内存.
 * 
 */
public class MemoryCheck {
	private static final Logger logger = LoggerFactory.getLogger(MemoryCheck.class);
	private static final int MB_SIZE = 1024 * 1024;

	private int canUsePercent;

	/**
	 * @param canUsePercent
	 *            允许使用的最大内存百分比, 是一个整数，表示百分比 如果想控制的内存用量占总内存的 80%<br>
	 *            那么参数只要写为 80就可以
	 */
	public MemoryCheck(int canUsePercent) {
		this.canUsePercent = canUsePercent;
	}

	/**
	 * 检查是否还有可用内存空间<br>
	 * 有剩余空间可用，返回true; 否则，返回false.
	 */
	public boolean hasMemory() {
		long totalMemory = Runtime.getRuntime().totalMemory() / MB_SIZE;
		long freeMemory = Runtime.getRuntime().freeMemory() / MB_SIZE;
		long maxMemory = Runtime.getRuntime().maxMemory() / MB_SIZE;
		logger.debug(String.format("totalMemory %s MB,freeMemory %s MB,maxMemory %s MB", totalMemory, freeMemory, maxMemory));
		
		//已使用的内存总量
		long useMemory = totalMemory - freeMemory;
		if (useMemory < 0) {
			logger.error("使用的内存为负数 : ", useMemory);
			return true;
		} else {
			// 70代表 70%
			int usedPercent = (int) ((useMemory * 1.0 / maxMemory) * 100);
			if (usedPercent > canUsePercent) {
				logger.info("已使用内存百分比 : {}%  ", usedPercent);
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * 获取已使用内存的百分比<br>
	 * 返回的是一个整数，比如 65,表示已使用内存达到总内存的 65%<br>
	 */
	public int getMemoryUsedPercent() {
		long totalMemory = Runtime.getRuntime().totalMemory() / MB_SIZE;
		long freeMemory = Runtime.getRuntime().freeMemory() / MB_SIZE;
		long maxMemory = Runtime.getRuntime().maxMemory() / MB_SIZE;
		long useMemory = totalMemory - freeMemory;
		return (int) ((useMemory * 1.0 / maxMemory) * 100);
	}

	public static void main(String[] args){
		//允许使用 80%的内存
		MemoryCheck mc = new MemoryCheck(80);
		if(mc.hasMemory()){
			//有空闲内存
			//........具体业务逻辑
		}else{
			//内存用完了
			//........具体业务逻辑
		}
	}
}