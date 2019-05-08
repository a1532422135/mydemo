package com.example.test.common;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

/**
 * 提供WriteLn接口写日志数据到外部文件中,一条日志占据一行
 * 
 * <pre>
 * 支持多线程环境写入。 也可设置为不支持多线程， 这样可提高写入效能。
 * 支持文件压缩，支持文件切换，支持设定文件大小，支持设定日志存放目录
 * 
 * 经测试，每秒可以写 8000条日志数据到文件中（测试中每条日志的大小为 406个字节）
 * 
 * 注意： 对于像长期运行的系统，比如在 Tomcat中运行，<strong style='color=red'>不可以</strong>
 * 手动调用 closeLogFile去关闭日志文件
 * 
 * 但可以调用flush方法，把文件缓冲中的数据写入文件中, 一般情况下也不需要调用 flush方法.
 * 
 * 如果是只是某个时间段的运行，那么在写完文件后，必须要调用 closeLogFile 关闭文件 
 * 
 * 可以设定单个外部文件的最大容量，比如100M，超出100M后，会自动切到下一个文件。
 * 
 * 文件中每一行代表一笔日志数据的json字符串 。本功能只接受字符串数据，具体采用什么格式，由具体的业务场景决定
 * 比如，业务上需要写json格式的数据进文件，那么业务上需要提供json格式的字符串给本功能 
 * 
 * 文件的名称格式为  busLog.yyyy-mm-dd.dddd
 * 比如 busLog.2018-11-21.0001 ,表示11月21号的第一个文件.
 * 此文件达到100M后，如果还是 在 11月21号，则下面的日志会记入 
 * busLog.2018-11-21.0002 的文件中 ,序号从 0001 变为 0002,以此类推.
 * 如果文件大小超出 100M 后，同时发现系统日期发生了编号，则 新文件中的日期部分会采用新的日期，并且序号
 * 重新从 0001 开始编号,如  busLog.2018-11-22.0001
 * 
 * 如果开启了压缩，文件在关闭后，会被压缩成zip格式，比如 
 * busLog.2018-11-21.0001 会被压缩成  busLog.2018-11-21.0001.zip文件
 * 开启压缩选项后，也同时可以开启删除原文件开关，也就是在文件被压缩后，删除压缩前的原始文件
 * 
 * 最终在目录中会产生一系列文件，如下：
 * busLog.2018-11-21.0001
 * busLog.2018-11-21.0002
 * busLog.2018-11-21.0003
 * busLog.2018-11-22.0001
 * busLog.2018-11-22.0002
 * busLog.2018-11-22.0003
 * busLog.2018-11-22.0004
 * 序号最大为 9999, 一般情况，一天之内不可能达到 9999个文件，所有 四位序号足够使用,以此类推
 * 
 * 使用方式：
 * 【启动：】
 * LogBeanToFile obj = new LogBeanToFile();
 * //设定日志的存放目录,根据项目的具体情况设定,此目录如果不存在，会被自动创建起来
 * obj.setBusLogDir("d:/logDir"); 
 * //设定单个日志文件的容量,单位为 M,建议设定为 100M
 * obj.setMaxSizeMB(100);
 * //设定后台切换文件线程的延迟启动时间，单位为秒,建议设定为 60秒
 * obj.setInitialDelaySeconds(1);
 * //设定后台切换文件线程的定时执行间隔,单位为秒,建议设置为 60秒
 * obj.setDelaySeconds(60);
 * //在切换文件的瞬间，为了避免都是日志数据，后台有一个临时列表存放切换期间的接收到的日志
 * //切换完毕后，会立刻把临时列表中的日志记录到切换后的文件中
 * //这个参数表示临时列表的大小,建议设定为 50000
 * obj.setMaxTempListSize(50000); 
 * //设置不支持多线程环境，可以提升效能,不过在多线程环境下，一定要设置为true
 * obj.setMultiThreadSupport(false);
 * //是否需要压缩
 * obj.setCompress(false);
 * //压缩后，是否需要删除原始文件
 * obj.setDeleteOldFileAfterCompress(false);
 * //真正的启动                
 * obj.init();
 * 
 * 【客户端调用代码 , 写数据进日志：】
 * obj.writeLn("日志信息");
 * </pre>
 * 
 * @author carl.lu
 */
public class LogBeanToFile {

	private static final Logger logger = LoggerFactory.getLogger(LogBeanToFile.class);

	// 定时执行线程池,用于定时进行文件大小检查和文件切换
	private final ScheduledExecutorService fileSwitchScheduleService = Executors.newSingleThreadScheduledExecutor();

	// 用于执行文件压缩的单线程池
	private final ExecutorService compressExecutor = Executors.newSingleThreadExecutor();

	// 当前日期格式
	private static final String READABLE_DATE_PATTERN = "yyyy-MM-dd";

	// 1k的字节数
	private static final int ONE_K_BYTE_SIZE = 1024;

	// 表示常量0
	private static final String ZERO = "0";

	// 表示常量9
	private static final String NINE = "9";

	// 目录路径的分隔符
	private static final String FILE_PATH_SEP = File.separator;

	// 文件名各个部分的分隔符
	private static final String FILE_PART_SEP = ".";

	// 业务日志文件名的前缀
	private static final String SIMPLE_LOG_FILE_NAME = "busLog";

	// 在日志目录中查找文件名符合格式的日志文件的正则表达式
	private static final String FILE_REGEX_EXPRESSION = "^" + SIMPLE_LOG_FILE_NAME + "\\." + "\\d{4}-\\d{2}-\\d{2}.\\d{4}" + "$";

	// 查找日志的编译后的正则表达式，这样可以提升性能
	private static final Pattern FILE_REGEX_PATTERN = Pattern.compile(FILE_REGEX_EXPRESSION);

	// 一天的第一个日志的开始序号，也就是 0001
	private static final int INIT_SEQ = 1;

	// 日志序号的长度
	private static final int SEQ_LENGTH = 4;

	// 对四位来说为 0001
	private static final String INIT_SEQ_TEXT = addZero(INIT_SEQ);

	// 文件的最大序号，对4位来说，是9999, 从 0001 到 9999
	private static final int MAX_SEQ = buildMaxSeq(SEQ_LENGTH);

	// zip扩展名
	private static final String ZIP_EXT = "zip";

	// 用于切换文件期间，临时保存日志数据，切换文件完毕，会立刻把临时日志写入文件
	private BlockingQueue<String> tempLogs;

	// 控制临时列表的最大容量，防止异常情况下，撑爆内存
	private int maxTempListSize;

	// 单个日志文件的最大容量(单位 M)
	private int maxSizeMB;

	// 存放日志文件的目录,如果此目录不存在，系统会自动创建，不需要手动创建
	// 原始路径,可能是绝对路径，也可能是相对路径
	private String busLogDir;

	// 日志目录的绝对路径
	private String absBusLogDir;

	// 切换文件的定时器的初始延迟时间，可用不用延迟，主要是为了测试和系统健壮性考虑,单位 秒
	private long initialDelaySeconds;

	// 切换文件定时器的两次执行之间的间隔,单位 秒
	private long delaySeconds;

	// 是否需要多线程支持,多线程环境下，会对写操作进行排队，对效能有牺牲
	// 如果确认不需要多线程支持，请设置为 false
	private boolean multiThreadSupport;

	// 是否需要压缩
	private boolean compress;

	// 压缩后，是否需要删除原文件
	private boolean deleteOldFileAfterCompress;

	// 文件操作句柄
	private BufferedWriter bufferWriter;

	// 当前正在使用中的的日志文件名
	private String currentOpenedFileName;

	// 写保护锁,用于防止 在切换文件的询价，writeLn被调用
	private Lock lock = new ReentrantLock();

	/**
	 * <pre>
	 * 空构造函，系统启动时的主要逻辑放在了 init方法中，没有放入构造函数 原因是 
	 * 1 方便与 spring集成，注入属性
	 * 2 参数比较多，放在构造韩函数中，使用不方便，也不灵活
	 * </pre>
	 */
	public LogBeanToFile() {

	}

	/**
	 * 系统真正的启动入口,调用此方法前，请先设置 几个参数，否则不会通过完整性检查
	 */
	public void init() {
		checkParameter();
		logger.debug("参数检查完毕");
		logger.debug("busLogDir {}", busLogDir);
		logger.debug("maxSizeMB {}", maxSizeMB);
		logger.debug("initialDelaySeconds {}", initialDelaySeconds);
		logger.debug("delaySeconds {}", delaySeconds);
		logger.debug("maxTempListSize {}", maxTempListSize);
		logger.debug("multiThreadSupport {}", multiThreadSupport);
		logger.debug("compress {}", compress);
		logger.debug("deleteOldFileAfterCompress {}", deleteOldFileAfterCompress);

		this.absBusLogDir = new File(busLogDir).getAbsolutePath();
		logger.debug("绝对目录路径为 {}", absBusLogDir);

		// 初始化临时日志存放的阻塞队列
		tempLogs = new LinkedBlockingQueue<String>(maxTempListSize);

		String logFileName = initLogFile();

		logger.debug("initLogFile 执行完毕");
		openLogFile(logFileName);
		logger.debug("openLogFile 执行完毕");

		// 启动定时切换文件的线程(会根据文件当前大小判断是否需要切换)
		scheduleSwitchFileTread();
	}

	/**
	 * <pre>
	 * 启动定时切换文件的线程(会根据文件当前大小判断是否需要切换) 
	 * switchFileRunnable是一个内部类，实现了 Runnable接口，在文件的最后定义
	 * </pre>
	 */
	private void scheduleSwitchFileTread() {
		// 启动监控线程，定时监控文件文件的大小,如果文件错过设定大小，则需要切换到下一个新文件
		SwitchFileRunnable switchFileRunnable = new SwitchFileRunnable();
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
		fileSwitchScheduleService.scheduleWithFixedDelay(switchFileRunnable, initialDelaySeconds, delaySeconds, TimeUnit.SECONDS);
		logger.debug("文件切换线程 启动完毕,延迟 {} 秒开始第一次执行,执行间隔为 {} 秒", initialDelaySeconds, delaySeconds);

	}

	/**
	 * 启动参数检查，主要检查是否提供了参数
	 */
	private void checkParameter() {
		if (busLogDir == null) {
			throw new RuntimeException("请调用 setBusLogDir 方法设置日志存放目录");
		}

		if (busLogDir.trim().isEmpty()) {
			throw new RuntimeException("日志目录不能为空");
		}

		if (maxSizeMB <= 0) {
			throw new RuntimeException("请调用 setMaxSizeMB 方法设置 单个文件的最大容量");
		}

		if (initialDelaySeconds < 0) {
			throw new RuntimeException("请调用  setInitialDelaySeconds 方法设置切换文件线程的初始延迟启动时间");
		}

		if (delaySeconds <= 0) {
			throw new RuntimeException("请调用 setDelaySeconds 方法设置切换文件线程定时执行间隔");
		}

		if (maxTempListSize <= 0) {
			throw new RuntimeException("请调用 setMaxTempListSize 方法设置文件切换期间临时存放日志数据的队列的最大值");
		}
	}

	/**
	 * 文件被压缩后，是否需要删除原始文件
	 */
	public void setDeleteOldFileAfterCompress(boolean deleteOldFileAfterCompress) {
		this.deleteOldFileAfterCompress = deleteOldFileAfterCompress;
	}

	/**
	 * 是否需要压缩日志文件
	 * 
	 * @param compress
	 *            是否需要压缩，true 需要压缩，false不需要压缩
	 */
	public void setCompress(boolean compress) {
		this.compress = compress;
	}

	/**
	 * <pre>
	 * 是否需要多线程支持,多线程环境下，会对写操作进行排队，对效能有牺牲
	 * 如果确认不需要多线程支持，请设置为 false 默认是不支持多线程的, 
	 * 如果在多线程环境下写文件(也就是多个线程同时调用 同一个 LogBeanToFile的writeLn接口写)，请必须设置为 true
	 * </pre>
	 */
	public void setMultiThreadSupport(boolean multiThreadSupport) {
		this.multiThreadSupport = multiThreadSupport;
	}

	/**
	 * 设置切换文件期间，临时保存日志数据的列表长度 如果达到最大长度，则继续加入的日志会被抛弃
	 */
	public void setMaxTempListSize(int maxTempListSize) {
		this.maxTempListSize = maxTempListSize;
	}

	/**
	 * 单个日志文件的最大容量,单位 M 通常可以设置为 100
	 */
	public void setMaxSizeMB(int maxSizeMB) {
		this.maxSizeMB = maxSizeMB;
	}

	/**
	 * 切换文件的定时器的初始延迟时间，可用不用延迟，主要是为了测试和系统健壮性考虑,单位 秒
	 */
	public void setInitialDelaySeconds(long initialDelaySeconds) {
		this.initialDelaySeconds = initialDelaySeconds;
	}

	/**
	 * 切换文件定时器的两次执行之间的间隔,单位 秒
	 */
	public void setDelaySeconds(long delaySeconds) {
		this.delaySeconds = delaySeconds;
	}

	/**
	 * 存放日志文件的目录,如果此目录不存在，系统会自动创建，不需要手动创建 <br>
	 * 也支持多级目录,比如 /a/b/c , 多级目录也会自动创建<br>
	 */
	public void setBusLogDir(String busLogDir) {
		this.busLogDir = busLogDir;
	}

	/**
	 * 给客户端的主要接口,写入日志数据进日志文件<br>
	 * 写入的日志会自动换行，用户不需要提供换行符 <br>
	 * 可以支持多线程<br>
	 */
	public void writeLn(String str) {
		if (this.multiThreadSupport) {
			synchronized (this) {
				innerWriteLn(str);
			}
		} else {
			innerWriteLn(str);
		}
	}

	/**
	 * 不支持多线程的写入，内部方法
	 */
	private void innerWriteLn(String str) {
		if (lock.tryLock()) {
			try {
				bufferWriter.write(str);
				bufferWriter.newLine();
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			} finally {
				lock.unlock();
			}
		} else {
			boolean added = tempLogs.offer(str);
			if (!added) {
				logger.error("切换文件中，临时列表已满,丢弃此笔日志数据");
			} else {
				//logger.debug("切换文件中，数据写入临时列表中,列表当前大小 : {}", tempLogs.size());
			}
		}
	}

	/**
	 * 关闭日志文件,通常不需要手动关闭 <br>
	 * 日志切换线程会在切换期间关闭已经打开的文件 <br>
	 * 除非是明确知道在某个功能运行结束后，需要关闭的情况，那么请调用此方法. <br>
	 */
	public void closeLogFile() {
		try {
			bufferWriter.close();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	}

	/**
	 * 把文件缓冲区中的数据真正写到文件中
	 * 
	 * <pre>
	 * 一般情况下，<strong>不需要调用此方法</strong>，比如在 tomcat中长期运行的系统
	 * 除非是一次性运行的功能，比如做完一个功能后，就需要关闭文件的，那么可以调用此方法.
	 * 不过，调用 closeLogFile就可以清空缓存并关闭文件。
	 * 这个方法只是在没有关闭文件的情况下，临时把缓冲区清空一下.
	 * </pre>
	 */
	public void flush() {
		try {
			bufferWriter.flush();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	}

	/**
	 * 构造序号的最大值,比如4为长度的序号，返回 9999
	 */
	private static int buildMaxSeq(int seqLength) {
		StringBuilder maxSeqText = new StringBuilder();
		for (int i = 0; i < seqLength; i++) {
			maxSeqText.append(NINE);
		}
		return Integer.parseInt(maxSeqText.toString());
	}

	/**
	 * 前面补0，比如 3 补零后返回 0003 的字符串
	 */
	private static String addZero(int seq) {
		String strSeq = "" + seq;
		int addedCount = SEQ_LENGTH - strSeq.length();
		for (int i = 0; i < addedCount; i++) {
			strSeq = ZERO + strSeq;
		}
		return strSeq;
	}

	/**
	 * 判断单个日志文件是否达到设定的最大容量
	 */
	private boolean arriveMaxSize(String name) {
		String fullPath = this.getFullPath(name);
		File file = new File(fullPath);
		if (!file.exists()) {
			throw new RuntimeException("不应该出现 " + fullPath + " 不存在的情况");
		}
		if (file.length() >= getMaxSizeByteLength()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 已字节数表示的单个日志的最大容量
	 */
	private long getMaxSizeByteLength() {
		return this.maxSizeMB * ONE_K_BYTE_SIZE * ONE_K_BYTE_SIZE;
	}

	/**
	 * <pre>
	 * 初始化日志文件，主要处理了如下工作 如果日志目录不存在，则自动创建日志目录，
	 * 支持多级目录 检查是否存在可写日志文件，如果不存在则创建一个第一个日志文件,序号为0001 
	 * 如果已存在多个日志文件，则取最后一个日志文件
	 * 判断日志是否达到最大容量 如果达到最大容量，则要切换到下一个文件，
	 * 切换文件时，要考虑日期是否已经发生变更 比如最后一个日志文件的日期为 2018-11-18，
	 * 但系统日期为 2018-11-19 ,因此切换后的文件要采用最新的系统日期,序号 0001
	 * 如果系统日期没有发生变更，则文件的序号为最后一个文件的序号 加 1，
	 * 比如最后序号为 0007 ，则 新建立的日志文件的序号为 0008
	 * </pre>
	 */
	private String initLogFile() {

		if (!isLogDirExist()) {
			logger.debug("不存在日志目录");
			createLogDir();
		}

		logger.debug("已存在日志目录");

		String[] logFiles = listLogFiles();

		if (logFiles == null) {
			throw new RuntimeException("异常，可能是设置的logDir不存在");
		}

		logger.debug("已存在的日志文件数量 : {}", logFiles.length);

		String currentFileName = null;

		if (logFiles.length == 0) {
			logger.debug("不存在日志");
			// 没有文件
			currentFileName = getInitFileName();
			createLogFile(currentFileName);
		} else {
			Arrays.sort(logFiles);
			currentFileName = logFiles[logFiles.length - 1];

			logger.debug("已存在日志,最后一个日志为 : {}", currentFileName);
		}

		if (!arriveMaxSize(currentFileName)) {
			logger.debug("{} 日志还没有达到最大容量 {} M", currentFileName, this.maxSizeMB);
			return currentFileName;
		}

		logger.debug("{} 日志已达到最大容量 {} M", currentFileName, this.maxSizeMB);
		String nextFileName = getNextFileName(currentFileName);

		logger.debug("切换到下一个日志 {}", nextFileName);

		createLogFile(nextFileName);

		logger.debug("创建下一个空日志 {} 成功", nextFileName);

		return nextFileName;
	}

	/**
	 * 打开日志文件,新打开的日志文件的文件句柄设置到 对象句柄 bufferWriter<br>
	 * bufferWriter 是调用写操作时事件操作的句柄
	 */
	private void openLogFile(String logFileName) {
		String fullPath = this.getFullPath(logFileName);
		logger.debug("准备打开的日志文件的全路径为 : {}", fullPath);
		try {
			bufferWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fullPath, true), "UTF-8"));
			this.currentOpenedFileName = logFileName;
			logger.debug("打开日志 {} 成功,可以往里面写数据", fullPath);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 当前文件已经超过最大设定值,切换到一个新文件<br>
	 * 取现在日期，判断与文件的日期是否相同，如果相同说明是同一天，在原来序号的基础上增加1,日期不变<br>
	 * 如果日期不同，说明已经换日，则序号从初始需要开始<br>
	 */
	private String getNextFileName(String currentFileName) {

		String currentDate = this.getCurrentDate();
		String dateInFileName = getDateInFileName(currentFileName);

		if (currentDate.equals(dateInFileName)) {
			logger.debug("最后一个日志文件的日期与系统日期相同");
			String seqText = getSeqInFileName(currentFileName);
			int seqInt = Integer.parseInt(seqText);
			if (seqInt >= MAX_SEQ) {
				throw new RuntimeException("产生太多日志文件  , 当前序号 " + seqInt);
			}
			return compositeFileName(dateInFileName, addZero(seqInt + 1));
		} else {
			logger.debug("最后一个日志文件的日期与系统日期不同,产生下一日的日志文件");
			return compositeFileName(currentDate, INIT_SEQ_TEXT);
		}
	}

	/**
	 * 取日志文件中的序号,比如 日志文件为 busLog.2018-11-28.0007 , 返回值为 0007
	 */
	private String getSeqInFileName(String currentFileName) {
		int fileNameLength = currentFileName.length();
		return currentFileName.substring(fileNameLength - SEQ_LENGTH, fileNameLength);
	}

	/**
	 * 取日志文件中的日期,比如 日志文件为 busLog.2018-11-28.0007 , 返回值为 2018-11-28
	 */
	private String getDateInFileName(String currentFileName) {
		int length = SIMPLE_LOG_FILE_NAME.length();
		return currentFileName.substring(length + 1, length + 11);
	}

	/**
	 * 查找日志目录中，满足格式的日志文件名,满足条件的所有文件名都放入数组中返回<br>
	 * 文件格式为 busLog.yyyy-mm-dd.nnnn, 比如 busLog.2018-11-28.0004
	 */
	private String[] listLogFiles() {
		File file = new File(absBusLogDir);
		String[] fileList = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return FILE_REGEX_PATTERN.matcher(name).find();
			}
		});
		return fileList;
	}

	/**
	 * 获取系统当前日期，返回 yyyy-mm-dd格式，比如 2018-11-28
	 */
	private String getCurrentDate() {
		SimpleDateFormat ft = new SimpleDateFormat(READABLE_DATE_PATTERN);
		return ft.format(new Date());
	}

	/**
	 * 根据系统当前日期，构建该日期的第一个日志文件，比如 busLog.2018-11-28.0001 需要肯定为 0001
	 */
	private String getInitFileName() {
		return compositeFileName(getCurrentDate(), INIT_SEQ_TEXT);
	}

	/**
	 * 组装日志文件名的快捷方法 busLog.yyyy-mm-dd.nnnn <br>
	 * yyyy-mm-dd 表示日期部分<br>
	 * nnnn 表示序号部分<br>
	 * 
	 * @param date
	 *            日期部分
	 * @param seq
	 *            序号部分
	 */
	private String compositeFileName(String date, String seq) {
		return SIMPLE_LOG_FILE_NAME + FILE_PART_SEP + date + FILE_PART_SEP + seq;
	}

	/**
	 * 得到日志文件的全路径名 <br>
	 * 比如 输入 busLog.2018-11-28.0007 , 会输出 /a/b/c/busLog.2018-11-28.0007<br>
	 * 其中 /a/b/c 表示系统启动时，通过 setBusLogDir 方法设置的 日志目录路径转化成的绝对路径<br>
	 */
	private String getFullPath(String name) {
		return this.absBusLogDir + FILE_PATH_SEP + name;
	}

	/**
	 * 创建日志目录 如果目录不存在则创建，也支持多级目录创建，不需要用户提前创建好日志目录
	 */
	private void createLogDir() {
		File logDirFile = new File(this.absBusLogDir);
		boolean created = logDirFile.mkdirs();
		logger.debug("{} 目录 创建状态 : {}", this.absBusLogDir, created);
	}

	/**
	 * 判断日志目录是否存在
	 */
	private boolean isLogDirExist() {
		File logDirFile = new File(this.absBusLogDir);
		if (logDirFile.exists()) {
			logger.debug("目录已存在");
			if (logDirFile.isDirectory()) {
				logger.debug("确认是一个目录");
				return true;
			} else {
				throw new RuntimeException(absBusLogDir + " 已经存在，但并不是一个目录");
			}
		} else {
			logger.debug("目录不存在");
			return false;
		}
	}

	/**
	 * 在日志目录下，创建一个空的日志文件
	 */
	private void createLogFile(String logName) {
		// 创建空文件
		String fullPath = getFullPath(logName);
		File file = new File(fullPath);
		if (file.exists()) {
			// 文件已存在，不应该出现的情况
			throw new RuntimeException("文件  " + fullPath + " 已存在，不应该出现的情况 ");
		}
		try {
			logger.debug("文件不存在，创建新文件");
			file.createNewFile();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
			throw new RuntimeException("创建新文件失败", e);
		}
	}

	/**
	 * 文件切换<br>
	 * 内部类 定时检查文件大小，并进行日志文件切换的 线程,定时间隔通过 setDelaySeconds 设置，单位为秒 <br>
	 * 切换期间，会关闭当前正在使用的文件句柄，然后打开新的文件句柄,新的文件句柄也同样分配给 全局的 bufferWriter变量 <br>
	 * 切换期间，会通过线程同步变量 isSwitching 来通知 writeLn方法，不要把日志信息写入文件中，而是写入临时列表中<br>
	 * 切换完毕后，会通过 线程同步变量 isSwitching 通知 writeLn方法恢复写日志信息到 文件中 <br>
	 * 切换完毕后，会把切换期间写入临时列表的日志数据 写入到 切换后的日志文件中<br>
	 */
	private class SwitchFileRunnable implements Runnable {

		/**
		 * 会被定时执行的方法
		 * 
		 * 文件切换
		 */
		public void run() {
			Thread.currentThread().setName("SwitchFileRunnable");
			try {
				lock.lock();
				logger.debug("SwitchFileRunnable");
				// 将要打开的文件名
				String willOpenFileName = initLogFile();

				if (willOpenFileName.equals(currentOpenedFileName)) {

					// 检测的空闲时间，把文件缓冲中的数据写到硬盘中
					bufferWriter.flush();

					logger.debug("继续使用当前的文件，不需要切换");

				} else {
					logger.debug("切换文件,旧文件 {} ,新文件 {} ", currentOpenedFileName, willOpenFileName);
					// 关闭当前已经在用的文件句柄

					long begin = System.currentTimeMillis();

					closeLogFile();
					// 是否需要压缩
					if (compress) {
						logger.debug("开启压缩线程");
						compressExecutor.execute(new CompressRunnable(currentOpenedFileName));
					}
					// 打开新文件句柄
					openLogFile(willOpenFileName);
					long end = System.currentTimeMillis();
					logger.debug("切换文件耗时 ： {} 毫秒", (end - begin));
					
					writeTempListToFile();
					
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			} finally {
				lock.unlock();
			}
		}

		/**
		 * 压缩任务
		 */
		private class CompressRunnable implements Runnable {

			private String fileName;

			public CompressRunnable(String fileName) {
				this.fileName = fileName;
			}

			public void run() {

				Thread.currentThread().setName("CompressRunnable");

				try {
					// 压缩后的文件名会多出 zip后缀
					String oldFilePath = getFullPath(fileName);
					String zipFullPath = oldFilePath + FILE_PART_SEP + ZIP_EXT;
					long begin = System.currentTimeMillis();
					boolean success = zip(new File(oldFilePath), new File(zipFullPath));
					long end = System.currentTimeMillis();
					long span = end - begin;
					logger.debug("压缩完成  文件  " + currentOpenedFileName + " 状态 {} 耗时 {}", success, span);

					if (deleteOldFileAfterCompress) {
						// 压缩后，删除原始文件
						FileUtils.deleteQuietly(new File(oldFilePath));
						logger.debug("删除原始文件  {}", oldFilePath);
					}

				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
			}
		}

		/**
		 * 切换完毕后，把切换期间写入临时列表的日志数据 写入到 切换后的日志文件中
		 */
		private void writeTempListToFile() {
			long begin = System.currentTimeMillis();
			logger.debug("切换文件结束，开始写临时列表中的数据进文件");
			int writeCount = 0;
			while (true) {
				String str = tempLogs.poll();
				if (str == null) {
					// 队列已空
					break;
				}
				writeLn(str);
				writeCount++;
			}
			long end = System.currentTimeMillis();
			logger.debug("写 {} 条临时列表中的数据进文件,耗时 {} 毫秒", writeCount, (end - begin));
		}
	};

	/**
	 * 
	 * 压缩指定路径的文件或目录. <br>
	 * 压缩文件的路径与被压缩文件的路径和文件名都相同，但压缩文件的后缀为.zip<br>
	 * @param srcFile
     * @param destFile
	 *            要压缩的文件路径
	 * @return 压缩成功则返回true，否则返回false
	 */
	private boolean zip(File srcFile, File destFile) {
        ZipArchiveOutputStream out = null;
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(srcFile));
			out = new ZipArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(destFile)));
			ZipArchiveEntry entry = new ZipArchiveEntry(srcFile.getName());
			entry.setSize(srcFile.length());
			out.putArchiveEntry(entry);
			IOUtils.copy(is, out);
			out.closeArchiveEntry();
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(out);
		}
	}
}
