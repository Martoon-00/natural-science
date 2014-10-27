package com.company;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
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
	public static final double STEP = 0.0001;
	public static int LENGTH = 800;
	public static int TOP = 600;
	public static int PADDING = 25;
	public static int LENGTH_SEPARATOR = 30;
	public static boolean isOriginal = true;
	public static boolean isClick = false;
	public static int x, y;
	public static int x0, y0;

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
			Display.setFullscreen(true);
			//Display.setDisplayMode(new DisplayMode(LENGTH, TOP));
			Display.create();
			TOP = Display.getHeight();
			LENGTH = Display.getWidth();

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
		glPointSize(1.2f);
		glLineWidth(0);

		installPoints();

		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			onDraw();

			inputMouse();
			inputKey();

			Display.update();


			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
				break;
		}
		Display.destroy();
	}

	private static void inputMouse() {
		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				if (Mouse.getEventButton() == 0) {
					x = Mouse.getX();
					y = Mouse.getY();
					isClick = true;
				}
			} else {
				if (isClick && !isOriginal) {
					x0 += Mouse.getX() - x;
					y0 += Mouse.getY() - y;
					glTranslated(Mouse.getX() - x, Mouse.getY() - y, 0);
					x = Mouse.getX();
					y = Mouse.getY();
				}
				if (Mouse.getEventButton() == 0)
					isClick = false;
			}

		}

	}


	private static void inputKey() {
		while (Keyboard.next()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				if (isOriginal) {
					TOP *= 2;
					LENGTH *= 2;
					PADDING *= 2;
					LENGTH_SEPARATOR *= 2;
					isOriginal = false;
				} else {
					TOP /= 2;
					LENGTH /= 2;
					PADDING /= 2;
					LENGTH_SEPARATOR /= 2;
					glTranslated(-x0, -y0, 0);
					x0 = 0;
					y0 = 0;
					isOriginal = true;
				}
			}


		}
	}

	private static void onDraw() {
		glColor3f(0f, 0f, 0f);
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
		glColor3f(0.7f, 0.7f, 0.7f);
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
