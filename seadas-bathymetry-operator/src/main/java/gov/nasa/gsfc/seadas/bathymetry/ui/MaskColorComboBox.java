package gov.nasa.gsfc.seadas.bathymetry.ui;

import org.openide.awt.ColorComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created with IntelliJ IDEA.
 * User: knowles
 * Date: 9/4/12
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class MaskColorComboBox {
    private BathymetryData bathymetryData;

    private JLabel jLabel;
    private ColorComboBox colorExComboBox = new ColorComboBox();

    public MaskColorComboBox(BathymetryData bathymetryData) {

        this.bathymetryData = bathymetryData;

        jLabel = new JLabel("Mask Color");
        jLabel.setToolTipText("set mask color");

        colorExComboBox.setSelectedColor(new Color(225,225,225));
        colorExComboBox.setPreferredSize(colorExComboBox.getPreferredSize());
        colorExComboBox.setMinimumSize(colorExComboBox.getPreferredSize());

        colorExComboBox.setSelectedColor((bathymetryData.getMaskColor()));

        addControlListeners();
    }


    private void addControlListeners() {

        colorExComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                bathymetryData.setMaskColor(colorExComboBox.getSelectedColor());
            }
        });
    }


    public JLabel getjLabel() {
        return jLabel;
    }

    public JComboBox getColorExComboBox() {
        return colorExComboBox;
    }
}
