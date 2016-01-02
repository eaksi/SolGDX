package com.github.eaksi.stactics.engine;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class Camera extends OrthographicCamera{
	
	private boolean zoomingIn = false;	 
	private boolean zoomingOut = false;
	private int zoomFrame = 0;				// current frame of zoom
	private int zoomLevel = 0;				// current zoom level
	private final int maxZoom = 10;			// how close you can zoom in
	private final int minZoom = -20;		// how far can you zoom out		// TODO: fix tearing when below 0 coordinates
	private final int moveIncrement = 10;	// how many units the camera moves at once
	
	// TODO: vertical and horizontal moveIncrement differs? test
	
	// TODO: mouse movement testing, smoothing

	// TODO: reimplement camera following character movement 
	
	public Camera(int width, int height) {
		super();
		this.setToOrtho(false, width, height);
	}

	public int getZoomLevel() {
		return zoomLevel;
	}
	
	// This method is called from keyboard controls, sets the zoom going
	public void setZoom(boolean in) {
		if (zoomingIn || zoomingOut) return;
		
		if (in && zoomLevel < maxZoom) {
			zoomingIn = true;
			zoomFrame = 10;
		}
		if (!in && zoomLevel > minZoom) {
			zoomingOut = true;
			zoomFrame = 10;
		}
	}
	
	// This is called every update, zooms a small amount
	public void updateZoom() {
		if (zoomingIn) {
			zoomFrame--;
			zoomLevel++;
			this.zoom -= 0.05;
			if (zoomFrame <= 0) {
				zoomingIn = false;
			}
		}
		if (zoomingOut) {
			zoomFrame--;
			zoomLevel--;
			this.zoom += 0.05;
			if (zoomFrame <= 0) {
				zoomingOut = false;
			}
		}
	}
	
	public void moveLeft(int amount)	{ translate(-amount, 0, 0); }
	public void moveRight(int amount)	{ translate(amount, 0, 0);  }
	public void moveDown(int amount)	{ translate(0, -amount, 0); }
	public void moveUp(int amount) 		{ translate(0, amount, 0);  }
	
	// TODO: refactor
	public void moveHorizontal(int amount)	{ translate(amount, 0, 0);  }
	public void moveVertical(int amount)	{ translate(0, amount, 0);  }
	
	
	// keyboard-controlled camera movement
	public void moveLeft()	{ translate(-moveIncrement, 0, 0); }
	public void moveRight() { translate(moveIncrement, 0, 0);  }
	public void moveDown()	{ translate(0, -moveIncrement, 0); }
	public void moveUp() 	{ translate(0, moveIncrement, 0);  }
	
	
}
