import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

/**
 * 
 * @author Huang Shih-Hao
 * @class SiButton
 * @properties some of Color object
 *
 */
public class SiButton extends JButton {
	private static final long serialVersionUID = 3L;
	private Color OriginBg;
	private Color OriginFg;
	private Color enteredBg;
	private Color enteredFg;
	private Color pressedBg;
	private Color pressedFg;

	public MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			super.mouseEntered(e);
			setBackground(enteredBg);
			setForeground(enteredFg);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			super.mouseExited(e);
			setBackground(OriginBg);
			setForeground(OriginFg);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);
			setBackground(enteredBg);
			setForeground(enteredFg);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			super.mousePressed(e);
			setBackground(pressedBg);
			setForeground(pressedFg);
		}
	};

	public SiButton(String s, Color OriginBg, Color OriginFg, Color enteredBg, Color enteredFg, Color pressedBg, Color pressedFg) {
		super(s);
		this.OriginBg = OriginBg;
		this.OriginFg = OriginFg;
		this.enteredBg = enteredBg;
		this.enteredFg = enteredFg;
		this.pressedBg = pressedBg;
		this.pressedFg = pressedFg;
		setBackground(OriginBg);
		setForeground(OriginFg);
		addMouseListener(mouseAdapter);
		setBorder(new LineBorder(Color.decode("#B6B6B6"), 1));
		setFocusPainted(false);

		// remove a weird blue color of a button
		setModel(new DefaultButtonModel() {
			private static final long serialVersionUID = 222L;

			@Override
			public boolean isPressed() {
				return false;
			}

			@Override
			public boolean isRollover() {
				return false;
			}

			@Override
			public void setRollover(boolean b) {
			}
		});
	}

	public Color getOriginBg() {
		return OriginBg;
	}

	public Color getOriginFg() {
		return OriginFg;
	}

	public Color getEnteredBg() {
		return enteredBg;
	}

	public Color getEnteredFg() {
		return enteredFg;
	}

	public Color getPressedBg() {
		return pressedBg;
	}

	public Color getPressedFg() {
		return pressedFg;
	}
}