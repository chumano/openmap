// **********************************************************************
// 
// <copyright>
// 
//  BBN Technologies, a Verizon Company
//  10 Moulton Street
//  Cambridge, MA 02138
//  (617) 873-8000
// 
//  Copyright (C) BBNT Solutions LLC. All rights reserved.
// 
// </copyright>
// **********************************************************************
// 
// $Source: /cvs/distapps/openmap/src/openmap/com/bbn/openmap/gui/time/TimeSliderSupport.java,v $
// $RCSfile: TimeSliderSupport.java,v $
// $Revision: 1.2 $
// $Date: 2003/06/03 16:18:31 $
// $Author: dietrick $
// 
// **********************************************************************


package com.bbn.openmap.gui.time;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.bbn.openmap.util.Debug;

public class TimeSliderSupport implements TimeConstants, ChangeListener {

    protected JSlider timeSlider;
    protected RealTimeHandler rtHandler;
    protected long startTime;
    protected long endTime;

    public TimeSliderSupport(JSlider slider, RealTimeHandler rth, 
			     long startingTime, long endingTime) {
	timeSlider = slider;
	rtHandler = rth;
	startTime = startingTime;
	endTime = endingTime;

	if (Debug.debugging("timedetail")) {
	    Debug.output("TimeSliderSupport: initialized to:" + startTime +
			 ", " + endTime);
	}

	timeSlider.addChangeListener(this);
    }

    /**
     * Updates the position of the slider to a place reflective to the
     * startTime and endTime of this support object.  Only moves the
     * slider marker if the difference is greater than one.
     */
    public void update(long time) {
	if (timeSlider != null) {
	    int minimum = timeSlider.getMinimum();
	    int maximum = timeSlider.getMaximum();
	    if (time <= startTime) {
		timeSlider.setValue(minimum);
	    } else if (time >= endTime) {
		timeSlider.setValue(maximum);
	    } else {
		double val = 
		    ((double)(time - startTime)/(double)(endTime - startTime)) * (maximum-minimum);
		if (Math.abs(val - timeSlider.getValue()) > 1) {
		    if (Debug.debugging("timedetail")) {
			Debug.output("TimeSliderSupport: Setting time slider to : " + val);
		    }
		    timeSlider.setValue((int)val);
		}
	    }
	}
    }

    /**
     * ChangeListener method, sets the time on the RealTimeHandler
     * based on the current setting on the slider.
     */
    public void stateChanged(ChangeEvent ce) {
	Object obj = ce.getSource();
	if (ce.getSource() == timeSlider) {
	    rtHandler.setTime(getTime());
	}
    }

    /**
     * Get the time reflected by the current setting of the slider.
     * Assumes that the JSlider has been set in this TimeSliderSupport
     * object.
     */
    protected long getTime() {
	int maximum = timeSlider.getMaximum();
	int minimum = timeSlider.getMinimum();
	int value = timeSlider.getValue();

	try {
	    return (startTime + (long)((endTime - startTime) * value/(maximum - minimum)));
	} catch (ArithmeticException ae) {
	    Debug.error(ae.getMessage());
	    return startTime;
	}
    }

    public void setTimeSlider(JSlider jslider) {
	timeSlider = jslider;
    }

    public JSlider setTimeSlider() {
	return timeSlider;
    }

    public void setHandler(RealTimeHandler rth) {
	rtHandler = rth;
    }

    public RealTimeHandler getHandler() {
	return rtHandler;
    }

    public void setStartTime(long time) {
	startTime = time;
    }

    public long getStartTime() {
	return startTime;
    }

    public void setEndTime(long time) {
	endTime = time;
    }

    public long getEndTime() {
	return endTime;
    }
}