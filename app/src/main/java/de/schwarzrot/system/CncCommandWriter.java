package de.schwarzrot.system;
/*
 * **************************************************************************
 *
 *  file:       CommandWriter.java
 *  project:    GUI for linuxcnc
 *  subproject: graphical application frontend
 *  purpose:    create a smart application, that assists in managing
 *              control of cnc-machines
 *  created:    15.10.2019 by Django Reinhard
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


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.Timer;

import com.mindovercnc.linuxcnc.CommandWriter;
import com.mindovercnc.linuxcnc.SystemMessage;
import de.schwarzrot.app.ApplicationMode;
import de.schwarzrot.bean.LCStatus;
import de.schwarzrot.model.ValueModel;
import com.mindovercnc.linuxcnc.nml.InterpState;
import com.mindovercnc.linuxcnc.nml.SpindleDirection;
import com.mindovercnc.linuxcnc.nml.TaskAutoMode;
import com.mindovercnc.linuxcnc.nml.TaskMode;
import com.mindovercnc.linuxcnc.nml.TaskState;


public class CncCommandWriter {
   class JogInfo {
      public int axis;
      public int direction;
   }


   public CncCommandWriter(List<SystemMessage> log, CncStatusReader1 statusReader) {
      this.log          = log;
      this.statusReader = statusReader;
      this.nativeCommandWriter = new CommandWriter();
   }


   public void clearEStop() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdClearEStop")));
      nativeCommandWriter.setTaskState(TaskState.EStopReset.getStateNum());
   }


   public void enableFlood(boolean enable) {
      StringBuilder sb = new StringBuilder(LCStatus.getStatus().lm("cmdFloodEnable"));

      sb.append(" ");
      if (enable)
         sb.append(LCStatus.getStatus().lm("enable"));
      else
         sb.append(LCStatus.getStatus().lm("disable"));
      log.add(new SystemMessage(sb.toString()));
      nativeCommandWriter.setFlood(enable);
   }


   public void enableMist(boolean enable) {
      StringBuilder sb = new StringBuilder(LCStatus.getStatus().lm("cmdMistEnable"));

      sb.append(" ");
      if (enable)
         sb.append(LCStatus.getStatus().lm("enable"));
      else
         sb.append(LCStatus.getStatus().lm("disable"));
      log.add(new SystemMessage(sb.toString()));
      nativeCommandWriter.setMist(enable);
   }


   public void enableOptionalStop() {
      log.add(new SystemMessage("cmdOptonalStop"));
      nativeCommandWriter.setOptionalStop(true);
   }


   public void enableOptionalStop(boolean enable) {
      if (enable)
         log.add(new SystemMessage(LCStatus.getStatus().lm("cmdOptionalStopEnable")));
      else
         log.add(new SystemMessage(LCStatus.getStatus().lm("cmdOptionalStopDisable")));
      nativeCommandWriter.setOptionalStop(enable);
   }


   public void enableSpindle(boolean enable, int speed, SpindleDirection direction) {
      System.out
            .println("COMMAND: enable spindle with parameters enable == " + (enable ? "enable" : "disable")
                  + " - speed == " + speed + " - direction == " + direction.getDirection());
      log.add(new SystemMessage(String.format(LCStatus.getStatus().lm("cmdSpindle"),
            LCStatus.getStatus().lm(enable ? "enable" : "disable"), speed, direction.getDirection())));
      nativeCommandWriter.setSpindle(enable, speed, direction.getDirection());
   }


   public void execGCode() {
      if (((Boolean) LCStatus.getStatus().getModel("singleStep").getValue())) {
         log.add(new SystemMessage(LCStatus.getStatus().lm("cmdExecSingleStep")));
         setAuto(TaskAutoMode.AutoStep);
      } else {
         if (LCStatus.getStatus().getModel("interpState").getValue()
               .equals(InterpState.Paused.getStateNum())) {
            resumeGCodeExecution();
         } else {
            log.add(new SystemMessage(LCStatus.getStatus().lm("cmdExecAutoRun")));
            setAuto(TaskAutoMode.AutoRun);
         }
      }
   }


   public void execMDI(String command) {
      if (command.length() > 254) {
         log.add(new SystemMessage(LCStatus.getStatus().lm("errMDI2Long"), SystemMessage.MessageType.OperatorError));
         throw new IllegalArgumentException("command length must not exceed 255 characters!");
      }
      log.add(new SystemMessage(String.format(LCStatus.getStatus().lm("cmdExecMDI"), command)));
      nativeCommandWriter.sendMDICommand(command);
   }


   public void homeAll() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdHomeAll")));
      ValueModel<TaskMode> tm = LCStatus.getStatus().getModel("taskMode");
      System.err.println("execute home all axis - task mode is: " + tm.getValue());
      nativeCommandWriter.homeAxis(-1);
   }


   public void jogStep(String what, double stepSize, double speed) {
      System.out.println("jogStep: " + what + " - stepSize: " + stepSize + " - speed: " + speed);
      JogInfo ji = new JogInfo();

      determineJogInfo(ji, what);

      System.err.println("from what: " + what + "\tgot axis: " + ji.axis + " and dir: " + ji.direction);
      nativeCommandWriter.jogStep(ji.axis, stepSize * ji.direction, speed);
   }


   public void loadGCodeFile(File gcodeFile) {
      if (gcodeFile.exists() && gcodeFile.canRead()) {
         log.add(new SystemMessage(
               String.format(LCStatus.getStatus().lm("cmdLoadGCodeFile"), gcodeFile.getAbsolutePath())));
         nativeCommandWriter.loadTaskPlan(gcodeFile.getAbsolutePath());
      }
   }


   public void loadToolsDefinition(File toolsFile) {
      if (toolsFile != null && toolsFile.exists() && toolsFile.canRead()) {
         log.add(new SystemMessage(
               String.format(LCStatus.getStatus().lm("cmdLoadToolTable"), toolsFile.getAbsolutePath())));
         nativeCommandWriter.loadToolTable(toolsFile.getAbsolutePath());
      } else {
         log.add(new SystemMessage(LCStatus.getStatus().lm("cmdReloadToolTable")));
         nativeCommandWriter.loadToolTable(null);
      }
      ActionListener al = new ActionListener() {
                           @Override
                           public void actionPerformed(ActionEvent e) {
                              statusReader.readToolsDefinitions();
                           }
                        };
      // TODO: may be we have to adjust delay
      Timer          tm = new Timer(1000, al);

      tm.setRepeats(false);
      tm.start();
   }


   public void machinePowerOff() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdPowerOff")));
      nativeCommandWriter.setTaskState(TaskState.MachineOff.getStateNum());
   }


   public void machinePowerON() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdPowerOn")));
      nativeCommandWriter.setTaskState(TaskState.MachineOn.getStateNum());
   }


   public void pauseGCodeExecution() {
      setAuto(TaskAutoMode.AutoPause);
   }


   public void resumeGCodeExecution() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdExecAutoResume")));
      setAuto(TaskAutoMode.AutoResume);
   }

   protected final void setAuto(TaskAutoMode mode) {
      nativeCommandWriter.setAuto(mode.ordinal(), 0);
   }

   public void runFromLine(int line) {
      LCStatus status = LCStatus.getStatus();

      if (((Boolean) status.getModel("singleStep").getValue())) {
         log.add(new SystemMessage(String.format(LCStatus.getStatus().lm("cmdStepFromLine"), line)));
         // NOTE: as linuxcnc does not support singlestep from given line,
         // we need a sequence like this:
         // 1. remember feed factor and fast feed factor
         // 2. set feed factor and fast feed factor to 0
         // 3. execute "run from line"
         // 4. issue "pause"
         // 5. restore feed factor and fast feed factor
         // 6. execute "step"
         double feedFactor  = status.getSpeedInfo().getFeedFactor();
         double rapidFactor = status.getSpeedInfo().getRapidFactor();

         nativeCommandWriter.setFeedOverride(0);
         nativeCommandWriter.setRapidOverride(0);
         nativeCommandWriter.setAuto(TaskAutoMode.AutoRun.ordinal(), line);
         setAuto(TaskAutoMode.AutoPause);
         nativeCommandWriter.setFeedOverride(feedFactor);
         nativeCommandWriter.setRapidOverride(rapidFactor);
         setAuto(TaskAutoMode.AutoStep);
      } else {
         log.add(new SystemMessage(String.format("cmdRunFromLine", line)));
         nativeCommandWriter.setAuto(TaskAutoMode.AutoRun.ordinal(), line);
      }
   }


   public void setFeedRate(double rate) {
      log.add(new SystemMessage(String.format(LCStatus.getStatus().lm("cmdFeedRateOverride"), rate)));
      System.err.println(String.format(LCStatus.getStatus().lm("cmdFeedRateOverride"), rate));
      nativeCommandWriter.setFeedOverride(rate);
   }


   /*
    * this command will be issued from panel in manual editing mode,
    * so we have to fake mdi-mode and switch back again
    */
   public void setFixture(String command) {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdSetFixture")));
      sendHiddenMDI(command);
   }


   public void setRapidRate(double rate) {
      log.add(new SystemMessage(String.format(LCStatus.getStatus().lm("cmdRapidRateOverride"), rate)));
      System.err.println(String.format(LCStatus.getStatus().lm("cmdRapidRateOverride"), rate));
      nativeCommandWriter.setRapidOverride(rate);
   }


   public void setSpindleSpeedFactor(double rate) {
      log.add(new SystemMessage(String.format(LCStatus.getStatus().lm("cmdSpindleSpeedOverride"), rate)));
      nativeCommandWriter.setSpindleOverride(rate);
   }


   public void setTaskAbort() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdAbortTask")));
      nativeCommandWriter.taskAbort();
   }


   public void setTaskModeAuto() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdTaskModeAuto")));
      nativeCommandWriter.setTaskMode(TaskMode.TaskModeAuto.getMode());
   }


   public void setTaskModeManual() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdTaskModeManual")));
      nativeCommandWriter.setTaskMode(TaskMode.TaskModeManual.getMode());
   }


   public void setTaskModeMDI() {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdTaskModeMDI")));
      nativeCommandWriter.setTaskMode(TaskMode.TaskModeMDI.getMode());
   }


   public void setToolProperties(String toolChangeCmd) {
      log.add(new SystemMessage(LCStatus.getStatus().lm("cmdSetToolProperties")));
      sendHiddenMDI(toolChangeCmd);
   }


   public void skipCComment() {
      skipCComment(true);
   }


   public void skipCComment(boolean enable) {
      nativeCommandWriter.setBlockDelete(enable);
   }


   public void startJogging(String what, double speed) {
      System.out.println("jogStart: " + what + " - speed: " + speed);
      JogInfo ji = new JogInfo();

      determineJogInfo(ji, what);

      System.err.println("from what: " + what + "\tgot axis: " + ji.axis + " and dir: " + ji.direction);
      nativeCommandWriter.jogStart(ji.axis, ji.direction * speed);
   }


   public void startSpindleCCW(int speed) {
      this.nativeCommandWriter.setSpindle(true, speed, -1);
   }


   public void startSpindleCW(int speed) {
      this.nativeCommandWriter.setSpindle(true, speed, 1);
   }


   public void stopJogging(String what) {
      System.out.println("jogStop: " + what);
      JogInfo ji = new JogInfo();

      determineJogInfo(ji, what);

      System.err.println("from what: " + what + "\tgot axis: " + ji.axis + " and dir: " + ji.direction);
      nativeCommandWriter.jogStop(ji.axis);
   }


   public void stopSpindle() {
      this.nativeCommandWriter.setSpindle(false, 0, 0);
   }


   protected void determineJogInfo(JogInfo ji, String what) {
      switch (what.charAt(0)) {
         case 'X':
         case 'x':
            ji.axis = 0;
            break;
         case 'Y':
         case 'y':
            ji.axis = 1;
            break;
         case 'Z':
         case 'z':
            ji.axis = 2;
            break;
         case 'A':
         case 'a':
            ji.axis = 3;
            break;
         case 'B':
         case 'b':
            ji.axis = 4;
            break;
         case 'C':
         case 'c':
            ji.axis = 5;
            break;
         case 'U':
         case 'u':
            ji.axis = 6;
            break;
         case 'V':
         case 'v':
            ji.axis = 7;
            break;
         case 'W':
         case 'w':
            ji.axis = 8;
            break;
      }
      if (what.charAt(1) == '-')
         ji.direction = -1;
      else
         ji.direction = 1;
   }

   protected void sendHiddenMDI(String command) {
      ValueModel<ApplicationMode> v  = LCStatus.getStatus().getModel("applicationMode");
      ApplicationMode             am = v.getValue();

      setTaskModeMDI();
      execMDI(command);
      setTaskModeManual();
      v.setValue(am);
   }


   private final CommandWriter nativeCommandWriter;
   private final List<SystemMessage> log;
   private final CncStatusReader1 statusReader;
   private static final String       OffsetMASK  = "G92 X%.3f Y%.3f Z%.3f A%.3f B%.3f C%.3f U%.3f V%.3f W%.3f";
   private static final String       FixtureMASK = "G10 L2 P%d X%.3f Y%.3f Z%.3f A%.3f B%.3f C%.3f U%.3f V%.3f W%.3f";
}
