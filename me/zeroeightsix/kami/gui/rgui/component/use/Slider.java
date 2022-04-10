//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.rgui.component.use;

import me.zeroeightsix.kami.gui.rgui.component.listen.*;
import net.minecraft.util.math.*;
import me.zeroeightsix.kami.gui.rgui.poof.*;
import me.zeroeightsix.kami.gui.rgui.component.*;
import me.zeroeightsix.kami.gui.rgui.poof.use.*;

public class Slider extends AbstractComponent
{
    double value;
    double minimum;
    double maximum;
    double step;
    String text;
    boolean integer;
    
    public Slider(final double value, final double minimum, final double maximum, final double step, final String text, final boolean integer) {
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.step = step;
        this.text = text;
        this.integer = integer;
        this.addMouseListener((MouseListener)new MouseListener() {
            public void onMouseDown(final MouseListener.MouseButtonEvent event) {
                Slider.this.setValue(Slider.this.calculateValue(event.getX()));
            }
            
            public void onMouseRelease(final MouseListener.MouseButtonEvent event) {
            }
            
            public void onMouseDrag(final MouseListener.MouseButtonEvent event) {
                Slider.this.setValue(Slider.this.calculateValue(event.getX()));
            }
            
            public void onMouseMove(final MouseListener.MouseMoveEvent event) {
            }
            
            public void onScroll(final MouseListener.MouseScrollEvent event) {
            }
        });
    }
    
    public Slider(final double value, final double minimum, final double maximum, final String text) {
        this(value, minimum, maximum, getDefaultStep(minimum, maximum), text, false);
    }
    
    private double calculateValue(final double x) {
        final double d1 = x / this.getWidth();
        final double d2 = this.maximum - this.minimum;
        final double s = d1 * d2 + this.minimum;
        return MathHelper.clamp(Math.floor(Math.round(s / this.step) * this.step * 100.0) / 100.0, this.minimum, this.maximum);
    }
    
    public static double getDefaultStep(final double min, final double max) {
        double s = gcd(min, max);
        if (s == max) {
            s = max / 20.0;
        }
        if (max > 10.0) {
            s = (double)Math.round(s);
        }
        if (s == 0.0) {
            s = max;
        }
        return s;
    }
    
    public String getText() {
        return this.text;
    }
    
    public double getStep() {
        return this.step;
    }
    
    public double getValue() {
        return this.value;
    }
    
    public double getMaximum() {
        return this.maximum;
    }
    
    public double getMinimum() {
        return this.minimum;
    }
    
    public void setValue(final double value) {
        final SliderPoof.SliderPoofInfo info = new SliderPoof.SliderPoofInfo(this.value, value);
        this.callPoof((Class)SliderPoof.class, (PoofInfo)info);
        final double newValue = info.getNewValue();
        this.value = (this.integer ? ((double)(int)newValue) : newValue);
    }
    
    public static double gcd(double a, double b) {
        a = Math.floor(a);
        b = Math.floor(b);
        if (a == 0.0 || b == 0.0) {
            return a + b;
        }
        return gcd(b, a % b);
    }
    
    public abstract static class SliderPoof<T extends Component, S extends SliderPoofInfo> extends Poof<T, S>
    {
        public static class SliderPoofInfo extends PoofInfo
        {
            double oldValue;
            double newValue;
            
            public SliderPoofInfo(final double oldValue, final double newValue) {
                this.oldValue = oldValue;
                this.newValue = newValue;
            }
            
            public double getOldValue() {
                return this.oldValue;
            }
            
            public double getNewValue() {
                return this.newValue;
            }
            
            public void setNewValue(final double newValue) {
                this.newValue = newValue;
            }
        }
    }
}
