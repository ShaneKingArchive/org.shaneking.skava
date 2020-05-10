package org.shaneking.skava.io;

import org.shaneking.skava.lang.String0;

//FileTypeName
public class FTN {
  //Text
  public static final String ASP = "asp";
  public static final String CLASSPATH = "classpath";
  public static final String CONF = "conf";
  public static final String CSV = "csv";
  public static final String GITIGNORE = "gitignore";
  public static final String INI = "ini";
  public static final String JAVA = "java";
  public static final String JSON = "json";
  public static final String JSP = "jsp";
  public static final String LOG = "log";
  public static final String PHP = "php";
  public static final String PROJECT = "project";
  public static final String SQL = "sql";
  public static final String SVN = "svn";
  public static final String SYS = "sys";
  public static final String TXT = "txt";
  public static final String XML = "xml";
  //Document
  public static final String DOC = "doc";
  public static final String DOCX = "docx";
  public static final String PDF = "pdf";
  public static final String PPT = "ppt";
  public static final String PPTX = "pptx";
  public static final String XLS = "xls";
  public static final String XLSX = "xlsx";
  //Executable
  public static final String BAT = "bat";
  public static final String BIN = "bin";
  public static final String DEB = "deb";
  public static final String DMG = "dmg";
  public static final String EXE = "exe";
  public static final String MSI = "msi";
  public static final String PKG = "pkg";
  public static final String RPM = "rpm";
  public static final String SH = "sh";
  //Compiled
  public static final String A = "a";
  public static final String CLASS = "class";
  public static final String DLL = "dll";
  public static final String KO = "ko";
  public static final String SO = "so";
  //Compressed
  public static final String ARJ = "arj";
  public static final String GZ = "gz";
  public static final String RAR = "rar";
  public static final String TAR = "tar";
  public static final String ZIP = "zip";
  //Web
  public static final String CSS = "css";
  public static final String HTM = "htm";
  public static final String HTML = "html";
  public static final String JS = "js";
  //Mirror
  public static final String MDF = "mdf";
  public static final String ISO = "iso";
  //Image
  public static final String BMP = "bmp";
  public static final String GIF = "gif";
  public static final String JPG = "jpg";
  public static final String PNG = "png";
  public static final String TIF = "tif";
  public static final String SWF = "swf";
  //Audio
  public static final String MID = "mid";
  public static final String MP3 = "mp3";
  public static final String WAV = "wav";
  public static final String WMA = "wma";
  //Video
  public static final String AVI = "avi";
  public static final String MOV = "mov";
  public static final String MPEG = "mpeg";
  public static final String MPG = "mpg";
  public static final String RM = "rm";
  public static final String RMVB = "rmvb";
  public static final String WMV = "wmv";
  //Others
  public static final String ASF = "asf";
  public static final String TMP = "tmp";

  public static String P(String fileTypeName) {
    return String0.DOT + fileTypeName;
  }
}
