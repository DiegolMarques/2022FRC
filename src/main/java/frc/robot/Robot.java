// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
// import edu.wpi.first.cscore.MjpegServer;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update thebuild.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private NetworkTableEntry cameraSelection;
  private RobotContainer m_robotContainer;
  private UsbCamera usbCamera1;
  private UsbCamera usbCamera2;
  private int cameraCounter = 2;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
// Creates UsbCamera and MjpegServer [1] and connects them
usbCamera1 = new UsbCamera("USB Camera 0", 0);
usbCamera2 = new UsbCamera("USB Camera 1", 0);
cameraSelection = NetworkTableInstance.getDefault().getTable("").getEntry("CameraSelection");
// jack thinks we might need to use this in the future
//MjpegServer mjpegServer1 = new MjpegServer("serve_USB Camera 0", 1181);
//mjpegServer1.setSource(usbCamera);
Ultrasonic.setAutomaticMode(true);
}

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if(m_robotContainer.stick().getRawButtonPressed(6)){
      m_robotContainer.getGyroDrive().execute();
      System.out.println("Pressing Button 6");
    } else {
      m_robotContainer.getDefaultDrive().execute();
    }
    // this changes camera while holding the button (holding mode)
    if(m_robotContainer.stick().getRawButtonPressed(7)){
      System.out.println("Setting camera 2");
      cameraSelection.setString(usbCamera2.getName());
    } else if (m_robotContainer.stick().getRawButtonReleased(7)){
      System.out.println("Setting camera 1");
      cameraSelection.setString(usbCamera1.getName());
    }
    // this changes camera if button is pressed. (switching mode)
    if(m_robotContainer.stick().getRawButtonPressed(7)){
      if ( cameraCounter % 2 == 0 ){
        System.out.println("Setting camera 2");
        cameraSelection.setString(usbCamera2.getName());
      } else {
        System.out.println("Setting camera 1");
        cameraSelection.setString(usbCamera1.getName());
      }
      cameraCounter++;
    }
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
