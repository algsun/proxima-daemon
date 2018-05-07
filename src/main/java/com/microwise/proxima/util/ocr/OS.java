package com.microwise.proxima.util.ocr;

/**
 * 判断操作系统工具类
 *
 * Date: 12-8-23
 * Time: 下午10:08
 * @author bastengao
 */
public class OS {
	
	/**
	 * 判断是否是windows系统
	 *
	 * @author zhang.licong
	 * @date 2012-9-4
	 * 
	 *
	 */
    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        // windows
        return (os.indexOf("win") >= 0);
    }

    /**
     * 判断是否是苹果系统
     *
     * @author zhang.licong
     * @date 2012-9-4
     * 
     *
     */
    public static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        // Mac
        return (os.indexOf("mac") >= 0);
    }

    /**
     * 判断是否是linux或unix系统
     *
     * @author zhang.licong
     * @date 2012-9-4
     * 
     *
     */
    public static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        // linux or unix
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }

    /**
     * 判断是否是Solaris系统
     * Solaris 是Sun Microsystems研发的计算机操作系统，属于UNIX系统
     *
     * @author zhang.licong
     * @date 2012-9-4
     * 
     *
     */
    public static boolean isSolaris() {
        String os = System.getProperty("os.name").toLowerCase();
        // Solaris
        return (os.indexOf("sunos") >= 0);
    }
}
