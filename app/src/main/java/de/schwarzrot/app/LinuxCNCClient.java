package de.schwarzrot.app;
/*
 * **************************************************************************
 *
 *  file:       LinuxCNCClient.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    4.9.2019 by Django Reinhard
 *  copyright:  all rights reserved
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * **************************************************************************
 */


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import com.jme3.util.JmeFormatter;

import com.mindovercnc.linuxcnc.Initializer;
import com.mindovercnc.linuxcnc.SystemMessage;
import de.schwarzrot.bean.AppSetup;
import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.bean.themes.UITheme;
import de.schwarzrot.database.DBToolLibrary;
import de.schwarzrot.database.ToolCatDBTable;
import de.schwarzrot.database.ToolDBTable;
import de.schwarzrot.gui.MainPaneHorizontal;
import de.schwarzrot.gui.MainPaneVertical;
import de.schwarzrot.gui.PaneStack;
import de.schwarzrot.jar.JarInfo;
import de.schwarzrot.model.GCodeInfo;
import de.schwarzrot.model.ValueModel;
import com.mindovercnc.linuxcnc.nml.BufferDescriptor;
import de.schwarzrot.system.*;
import de.schwarzrot.util.DatabaseUtils;
import de.schwarzrot.util.Preview3DCreator;

import ca.odell.glazedlists.BasicEventList;


public class LinuxCNCClient extends JFrame implements Runnable {
   class WindowEventListener extends WindowAdapter {
      public WindowEventListener(ConfigHolder configHolder) {
         this.configHolder = configHolder;
      }


      @Override
      public void windowClosing(WindowEvent e) {
         configHolder.saveConfigs();
         try {
            jme3App.stop();
         } catch (Throwable t) {
            t.printStackTrace();
         }
         super.windowClosing(e);
      }


      private final ConfigHolder configHolder;
   };


   @SuppressWarnings("unchecked")
   public LinuxCNCClient(String[] args) {
      super(LCStatus.getStatus().lm("AppTitle"));
      Initializer.INSTANCE.loadLibraries();
      this.iniFile      = determineIniFile(args);
      this.errorLog     = new BasicEventList<SystemMessage>();
      this.errorReader  = new CncErrorReader(errorLog);
      this.statusReader = new CncStatusReader1(errorReader, new BufferDescriptor());
      this.cmdWriter    = new CncCommandWriter(errorLog, statusReader);
      this.errorActive  = LCStatus.getStatus().getModel("errorActive");
      this.errorReader.setErrorSignal(errorActive);
      dialogParent = this;
      LCStatus.getStatus().setApp(this);
   }


   public final CncCommandWriter getCommandWriter() {
      return cmdWriter;
   }


   public Map<String, JarInfo> getExportHandlers() {
      return exportHandlers;
   }


   public Preview3DCreator getJME3App() {
      return jme3App;
   }


   public String inputDialog(String title, String prompt) {
      return inputDialog(title, prompt, null);
   }


   public String inputDialog(String title, String prompt, String defaultValue) {
      if (device.getFullScreenWindow() == null) {
         return (String) JOptionPane.showInputDialog(dialogParent, prompt, title, JOptionPane.PLAIN_MESSAGE,
               null, null, defaultValue);
      } else {
         return (String) JOptionPane.showInternalInputDialog(dialogParent, prompt, title,
               JOptionPane.PLAIN_MESSAGE, null, null, defaultValue);
      }
   }


   public void loadFile(File gcodeFile) {
      try {
         paneStack.getGcodeLister().loadFile(gcodeFile.getAbsolutePath());
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
   }


   @Override
   public void run() {
      while (!statusReader.isInitializationCompleted()) {
         try {
            Thread.sleep(10);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
      // process inifile from linuxcnc and pass axis limits to preview creator
      jme3App = new Preview3DCreator(readSetup());
      // tell RS274 that we do axis mapping for jme
      // jme will read that property from Reader
      jme3App.setGeoParser(new RS274Reader(true));
      exportHandlers = findExportHandlers(exportHandlerDir);
      paneStack      = PaneStack.getInstance(cmdWriter, errorReader);
      ConfigHolder ch = new ConfigHolder(getClass().getSimpleName(), errorLog);

      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      JPopupMenu.setDefaultLightWeightPopupEnabled(false);
      addWindowListener(new WindowEventListener(ch));
      ToolTipManager ttm = ToolTipManager.sharedInstance();

      ttm.setDismissDelay(10000);
      ttm.setInitialDelay(100);
      ttm.setReshowDelay(100);
      this.setLayout(new BorderLayout());
      if (portraitMode)
         desktop = new MainPaneVertical(cmdWriter, errorReader);
      else
         desktop = new MainPaneHorizontal(cmdWriter, errorReader);

      this.add(desktop);
      this.pack();
      if (portraitMode) {
         this.setSize(1207, 1900);

         // TODO: test phase - remove it!
         this.setLocation(1200, 30);
      } else {
         this.setSize(1900, 1180);

         // TODO: test phase - remove it!
         this.setLocation(560, 30);
      }
      final SplashScreen splash    = SplashScreen.getSplashScreen();

      GCodeInfo gCodeInfo = LCStatus.getStatus().getGCodeInfo();
      if(gCodeInfo != null && gCodeInfo.getFileName() != null){
         File               gcodeFile = new File(gCodeInfo.getFileName());
         // TODO:
         // initializeLC();
         if (gcodeFile.exists() && gcodeFile.canRead()) {
            loadFile(gcodeFile);
         }
      }

      ISysTickStarter stStarter = new ISysTickStarter() {
         @Override
         public void start() {
            sysTick = new SysTick(new SysUpdater(statusReader), 100l);
         }
      };
      stStarter.start();
      if (fullScreen)
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               toFullScreen();
            }
         });
      if (splash != null)
         splash.close();
      this.setVisible(true);
   }


   public void toFullScreen() {
      if (isDisplayable()) {
         setVisible(false);
         dispose();
      }
      setUndecorated(true);
      if (!isVisible())
         setVisible(true);
      device.setFullScreenWindow(this);
      dialogParent = desktop;
   }


   public void toWindow() {
      device.setFullScreenWindow(null);
      setVisible(false);
      dispose();
      setUndecorated(false);
      setVisible(true);
      dialogParent = this;
   }


   public int xtendedInputDialog(String title, JComponent dialogPane) {
      if (device.getFullScreenWindow() == null) {
         return JOptionPane.showConfirmDialog(dialogParent, dialogPane, title, JOptionPane.OK_CANCEL_OPTION);
      } else {
         return JOptionPane.showInternalConfirmDialog(dialogParent, dialogPane, title,
               JOptionPane.OK_CANCEL_OPTION);
      }
   }


   // browse process list for server instance and get inifile of its runtime arguments
   protected File determineIniFile(String[] args) {
      File file = null;

      if (args.length > 0) {
         file = new File(args[0]);

         if (!file.exists() || !file.canRead())
            file = null;
      }
      if (file == null) {
         try {
            Process        p    = Runtime.getRuntime().exec("ps -ef");
            BufferedReader br   = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String         line = null;

            while ((line = br.readLine()) != null) {
               if (line.contains("linuxcncsvr")) {
                  String[] parts = line.split("\\s+");

                  if (parts.length > 2 && parts[parts.length - 2].compareTo("-ini") == 0) {
                     file = new File(parts[parts.length - 1]);
                     break;
                  }
                  System.err.println("could not process ps-result: " + line);
                  break;
               }
            }
            br.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      return file;
   }


   protected Map<String, JarInfo> findExportHandlers(String baseDir) {
      File base = new File(baseDir);

      if (!base.isDirectory()) {
         throw new IllegalArgumentException("baseDir [" + base.getAbsolutePath() + "] must be the path to "
               + "the directory, where the jar of the exporthandlers are " + "located");
      }
      Map<String, JarInfo> m  = new HashMap<String, JarInfo>();
      JarInfo              ji = null;

      for (File f : base.listFiles()) {
         if (f.getName().startsWith("."))
            continue;
         if (f.getName().endsWith(".jar")) {
            // System.out.println("check file: " + f.getAbsolutePath());
            if ((ji = inspectArchive(f)) != null) {
               ji.setName(f.getName().substring(0, f.getName().length() - 4));
               m.put(ji.getName(), ji);
            }
         }
      }
      return m;
   }


   // TODO: do we need to set max velocity on startup?
   // MAX_VELOCITY: 53.333333
   // message #207 (EMC_TRAJ_SET_MAX_VELOCITY) of size 32
   //
   protected void initializeLC() {
      cmdWriter.enableOptionalStop();
      cmdWriter.skipCComment();
   }


   protected JarInfo inspectArchive(File f) {
      JarFile jf      = null;
      JarInfo jarInfo = null;

      try {
         jf = new JarFile(f);
         Manifest   m     = jf.getManifest();
         Attributes attrs = m.getMainAttributes();

         for (Object k : attrs.keySet()) {
            if (ImpType.compareTo(k.toString()) == 0) {
               if (ExHdr.compareTo(attrs.get(k).toString()) == 0) {
                  jarInfo = new JarInfo();
                  jarInfo.setPath(f);
               }
               break;
            }
         }

         if (jarInfo != null) {
            Enumeration<JarEntry> jarEntries = jf.entries();

            while (jarEntries.hasMoreElements()) {
               JarEntry je    = jarEntries.nextElement();
               String[] parts = je.getName().split("/");
               String   name  = parts[parts.length - 1];

               // skip inner classes
               if (name.contains("$"))
                  continue;

               if (!je.isDirectory() && name.endsWith(".class")) {
                  // System.out.println("\tfound potential handler: " + name);
                  name = je.getName().replace('/', '.');

                  jarInfo.setClassName(name.substring(0, name.length() - 6));
               }
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         if (jf != null) {
            try {
               jf.close();
            } catch (IOException e) {
            }
         }
      }
      return jarInfo;
   }


   protected float[] readSetup() {
      AppSetup setup  = LCStatus.getStatus().getSetup();
      float[]  limits = new float[6];

      try {
         setup.parseIniFile(iniFile.getAbsolutePath());

         String tmp = setup.getProperty("AXIS_X", "MIN_LIMIT");

         try {
            limits[0] = Float.parseFloat(tmp);
         } catch (Throwable t) {
            t.printStackTrace();
         }
         tmp = setup.getProperty("AXIS_X", "MAX_LIMIT");

         try {
            limits[1] = Float.parseFloat(tmp);
         } catch (Throwable t) {
            t.printStackTrace();
         }
         tmp = setup.getProperty("AXIS_Y", "MIN_LIMIT");

         try {
            limits[2] = Float.parseFloat(tmp);
         } catch (Throwable t) {
            t.printStackTrace();
         }
         tmp = setup.getProperty("AXIS_Y", "MAX_LIMIT");

         try {
            limits[3] = Float.parseFloat(tmp);
         } catch (Throwable t) {
            t.printStackTrace();
         }
         tmp = setup.getProperty("AXIS_Z", "MIN_LIMIT");

         try {
            limits[4] = Float.parseFloat(tmp);
         } catch (Throwable t) {
            t.printStackTrace();
         }
         tmp = setup.getProperty("AXIS_Z", "MAX_LIMIT");

         try {
            limits[5] = Float.parseFloat(tmp);
         } catch (Throwable t) {
            t.printStackTrace();
         }
      } catch (Throwable t) {
         throw new RuntimeException("no Config! - Is linuxcnc running?", t);
      }
      return limits;
   }


   public static void main(String[] args) {
      JmeFormatter formatter      = new JmeFormatter();
      Handler      consoleHandler = new ConsoleHandler();

      consoleHandler.setFormatter(formatter);

      Logger.getLogger("").removeHandler(Logger.getLogger("").getHandlers()[0]);
      Logger.getLogger("").addHandler(consoleHandler);

      checkAppArgs(args);
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      device = ge.getDefaultScreenDevice();
      UITheme.setupDefaults(themeName);
      String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
      File   cd          = new File(".");

      try {
         javax.swing.UIManager.setLookAndFeel(lookAndFeel);
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      } catch (InstantiationException e) {
         e.printStackTrace();
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      } catch (javax.swing.UnsupportedLookAndFeelException e) {
         e.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
      LCStatus.getStatus().checkLocale();
      DatabaseUtils dbUtils = new DatabaseUtils();

      System.out.println("current directory: " + cd.getAbsolutePath());
      dbUtils.addTable(new ToolCatDBTable(true));
      dbUtils.addTable(new ToolDBTable());
      LCStatus.getStatus().setToolLibrary(new DBToolLibrary(dbUtils));
      SwingUtilities.invokeLater(new LinuxCNCClient(args));
   }


   protected static void checkAppArgs(String[] args) {
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      double    width      = screenSize.getWidth();
      double    height     = screenSize.getHeight();

      exportHandlerDir = "../exHdr";
      themeName        = "Gtk";
      portraitMode     = height > width;
      for (int i = 0; i < args.length; ++i) {
         String arg = args[i];

         if (arg.startsWith("-") || arg.startsWith("/"))
            arg = args[i].substring(1);

//         if ("checkBuffer".compareToIgnoreCase(arg) == 0) {
//            String cbDir = ".";
//
//            if (args.length > i + 1)
//               cbDir = args[i + 1];
//            new com.mindovercnc.linuxcnc.nml.CheckBufferDescriptor(cbDir).run();
//            System.exit(0);
//         }
         if ("portrait".compareToIgnoreCase(arg) == 0) {
            portraitMode = true;
         }
         if ("base".compareToIgnoreCase(arg) == 0) {
            exportHandlerDir = args[i + 1];
         }
         if ("full".compareToIgnoreCase(arg) == 0) {
            fullScreen = true;
         }
         if ("theme".compareToIgnoreCase(arg) == 0) {
            themeName = args[i + 1];
         }
      }
      UITheme.put(UITheme.Application_mode_portrait, portraitMode);
   }


   private final CncStatusReader1 statusReader;
   @SuppressWarnings("unused")
   private SysTick               sysTick;
   private final CncCommandWriter cmdWriter;
   private final CncErrorReader errorReader;
   private ValueModel<Boolean>   errorActive;
   private PaneStack             paneStack;
   private JDesktopPane          desktop;
   private Preview3DCreator      jme3App;
   private List<SystemMessage>   errorLog;
   private Map<String, JarInfo>  exportHandlers;
   private final File            iniFile;
   private static boolean        portraitMode;
   private static boolean        fullScreen;
   private static String         exportHandlerDir;
   private static String         themeName;
   private static Component      dialogParent;
   private static GraphicsDevice device;
   private static final String   ExHdr            = "ExportHandler";
   private static final String   ImpType          = "Implementation-Type";
   private static final long     serialVersionUID = 1L;
}
