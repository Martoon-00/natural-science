package com.company;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;


import org.lwjgl.opengl.DisplayMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Alex on 25.10.2014.
 */
public class KCE {
	public static final double MAX_X = 4;
	public static final double STEP = 0.001;
	public static final int LENGTH = 800;
	public static final int TOP = 600;
	public static final int PADDING = 25;
	public static final int LENGTH_SEPARATOR = 30;

	private static Map<Double, ArrayList<Double>> points;

	public static void start() {
		/*Canvas canvas = new Canvas();
		canvas.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				System.out.println(e.getComponent().getX());
				super.componentResized(e);
			}
		});

		Frame frame = new Frame();
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER);*/
		try {
			/*frame.setMinimumSize(new Dimension(800, 600));
			frame.pack();
			frame.setVisible(true);*/
			/*Display.setParent(canvas);
			Display.setResizable(true);*/
			//Display.setFullscreen(true);

			Display.setDisplayMode(new DisplayMode(LENGTH, TOP));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, LENGTH, 0, TOP, 1, -1);
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glMatrixMode(GL_MODELVIEW);
		glColor3f(0f, 0f, 0f);
		glPointSize(1);
		glLineWidth(2);

		installPoints();

		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			onDraw();
			Display.update();
		}
		Display.destroy();
		//frame.dispose();
	}

	private static void onDraw() {
		//Y
		glBegin(GL_LINES);
		glVertex2f(PADDING, 0);
		glVertex2f(PADDING, TOP);
		glEnd();
		//X
		glBegin(GL_LINES);
		glVertex2f(0, PADDING);
		glVertex2f(LENGTH, PADDING);
		glEnd();

		glBegin(GL_LINES);
		glVertex2f(PADDING - (LENGTH_SEPARATOR / 2), TOP);
		glVertex2f(PADDING + (LENGTH_SEPARATOR / 2), TOP);
		glEnd();

		for (int i = 1; i <= 4; i++) {
			glBegin(GL_LINES);
			glVertex2f(
					PADDING + i * ((LENGTH - PADDING) / 4),
					PADDING - (LENGTH_SEPARATOR / 2)
			);
			glVertex2f(
					PADDING + i * ((LENGTH - PADDING) / 4),
					PADDING + (LENGTH_SEPARATOR / 2)
			);
			glEnd();
		}

		drawPoints();
	}

	private static void drawPoints() {
		glBegin(GL_POINTS);
		for (double i = 0; i < MAX_X; i += STEP) {
			final double x = i;
			points.get(i).stream().forEach(z ->
							glVertex2d(getCoordinateX(x), getCoordinateY(z))
			);
		}
		glEnd();
	}

	private static void installPoints() {
		Evaluator evaluator =
				new Evaluator(0.5, (x, r) -> x * r * (1 - x), 1e-5, 1000);
		points = new HashMap<>();
		for (double i = 0; i < MAX_X; i += STEP) {
			points.put(i, evaluator.get(i));
		}
	}

	private static double getCoordinateX(double c) {
		return PADDING + c * ((LENGTH - PADDING) / 4);
	}

	private static double getCoordinateY(double c) {
		return PADDING + c * (TOP - PADDING);
	}

	public static void main(String[] args) {
		start();
	}
}
