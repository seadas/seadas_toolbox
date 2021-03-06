package gov.nasa.gsfc.seadas.processing.common;

import com.bc.ceres.binding.PropertyContainer;
import com.bc.ceres.swing.TableLayout;
import gov.nasa.gsfc.seadas.processing.ocssw.OCSSW;
import gov.nasa.gsfc.seadas.processing.ocssw.OCSSWInfo;
import gov.nasa.gsfc.seadas.processing.core.ParamUtils;
import gov.nasa.gsfc.seadas.processing.core.ProcessorModel;
import org.esa.snap.runtime.Config;
import org.esa.snap.ui.AppContext;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.*;
import java.util.prefs.Preferences;

import static gov.nasa.gsfc.seadas.processing.ocssw.OCSSWConfigData.SEADAS_OCSSW_TAG_DEFAULT_VALUE;
import static gov.nasa.gsfc.seadas.processing.ocssw.OCSSWConfigData.SEADAS_OCSSW_TAG_PROPERTY;
import static gov.nasa.gsfc.seadas.processing.ocssw.OCSSWInfo.OCSSW_SRC_DIR_NAME;

/**
 * Created by IntelliJ IDEA.
 * User: Aynur Abdurazik (aabduraz)
 * Date: 3/13/13
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class OCSSWInstallerForm extends JPanel implements CloProgramUI {


    //private FileSelector ocsswDirSelector;
    JTextField fileTextField;
    //private SwingPropertyChangeSupport propertyChangeSupport = new SwingPropertyChangeSupport(this);

    ProcessorModel processorModel;
    private AppContext appContext;
    private JPanel dirPanel;
    private JPanel tagPanel;
    private JPanel missionPanel;
    private JPanel otherPanel;

    //private JPanel superParamPanel;

    public static final String INSTALL_DIR_OPTION_NAME = "--install_dir";
    public final static String VALID_TAGS_OPTION_NAME = "--tag";
    public final static String CURRENT_TAG_OPTION_NAME = "--current_tag";

    public String missionDataDir;
    public OCSSW ocssw;

    private static final Set<String> MISSIONS = new HashSet<String>(Arrays.asList(
            new String[]{"AQUARIUS",
                    "AVHRR",
                    "CZCS",
                    "GOCI",
                    "HAWKEYE",
                    "HICO",
                    "MERIS",
                    "MODISA",
                    "MODIST",
                    "MOS",
                    "OCM1",
                    "OCM2",
                    "OCRVC",
                    "OCTS",
                    "OLI",
                    "OSMI",
                    "SGLI",
                    "SEAWIFS",
                    "VIIRSN",
                    "VIIRSJ1"}
    ));
    private static final Set<String> DEFAULT_MISSIONS = new HashSet<String>(Arrays.asList(
            new String[]{
                    //"GOCI",
                    //"HICO",
                    "OCRVC"
            }
    ));


    HashMap<String, Boolean> missionDataStatus;


    public OCSSWInstallerForm(AppContext appContext, String programName, String xmlFileName, OCSSW ocssw) {
        this.appContext = appContext;
        this.ocssw = ocssw;
        processorModel = ProcessorModel.valueOf(programName, xmlFileName, ocssw);
        processorModel.setReadyToRun(true);
        setMissionDataDir(OCSSWInfo.getInstance().getOcsswDataDirPath());
        init();
        updateMissionValues();
        createUserInterface();
        processorModel.updateParamInfo(INSTALL_DIR_OPTION_NAME, getInstallDir());
        processorModel.addPropertyChangeListener(INSTALL_DIR_OPTION_NAME, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                setMissionDataDir(processorModel.getParamValue(INSTALL_DIR_OPTION_NAME) + File.separator + OCSSWInfo.OCSSW_DATA_DIR_SUFFIX);
                updateMissionStatus();
                updateMissionValues();
                createUserInterface();
            }
        });
    }

    String getMissionDataDir(){
       return missionDataDir;
    }

    void setMissionDataDir(String currentMissionDataDir) {
        missionDataDir = currentMissionDataDir;
    }

    abstract void updateMissionStatus();
    abstract void updateMissionValues();

    String getInstallDir() {
        return OCSSWInfo.getInstance().getOcsswRoot();
    }

    abstract void init();
    public ProcessorModel getProcessorModel() {
        return processorModel;
    }

    public File getSelectedSourceProduct() {
        return null;
    }

    public boolean isOpenOutputInApp() {
        return false;
    }

    public String getParamString() {
        return processorModel.getParamList().getParamString();
    }

    public void setParamString(String paramString) {
        processorModel.getParamList().setParamString(paramString);
    }

    protected void createUserInterface() {

        this.setLayout(new GridBagLayout());

        JPanel paramPanel = new ParamUIFactory(processorModel).createParamPanel();
        reorganizePanel(paramPanel);

        add(dirPanel,
                new GridBagConstraintsCustom(0, 0, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 3));
        add(tagPanel,
                new GridBagConstraintsCustom(0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 3));

        add(missionPanel,
                new GridBagConstraintsCustom(0, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 3));
        add(otherPanel,
                new GridBagConstraintsCustom(0, 3, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 3));

        setMinimumSize(getPreferredSize());
        setMaximumSize(getPreferredSize());
    }

    public JPanel getParamPanel() {
        JPanel newPanel = new JPanel(new GridBagLayout());
        newPanel.add(missionPanel,
                new GridBagConstraintsCustom(0, 0, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 3));
        newPanel.add(otherPanel,
                new GridBagConstraintsCustom(0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 2));
        return newPanel;
    }
    //ToDo: missionDataDir test should be differentiated for local and remote servers

    protected void reorganizePanel(JPanel paramPanel) {
        final Preferences preferences = Config.instance("seadas").load().preferences();
        String ocsswTagString = preferences.get(SEADAS_OCSSW_TAG_PROPERTY, SEADAS_OCSSW_TAG_DEFAULT_VALUE);

        dirPanel = new JPanel();
        tagPanel = new JPanel();
        missionPanel = new JPanel(new TableLayout(5));
        missionPanel.setBorder(BorderFactory.createTitledBorder("Mission Data"));

        otherPanel = new JPanel();
        TableLayout otherPanelLayout = new TableLayout(3);
        otherPanelLayout.setTableFill(TableLayout.Fill.HORIZONTAL);
        otherPanel.setLayout(otherPanelLayout);
        otherPanel.setBorder(BorderFactory.createTitledBorder("Others"));
        OCSSWInfo ocsswInfo = OCSSWInfo.getInstance();

        JScrollPane jsp = (JScrollPane) paramPanel.getComponent(0);
        JPanel panel = (JPanel) findJPanel(jsp, "param panel");
        Component[] options = panel.getComponents();
        String tmpString;
        for (Component option : options) {
            if (option.getName().equals("boolean field panel")) {
                Component[] bps = ((JPanel) option).getComponents();
                for (Component c : bps) {
                    tmpString = ParamUtils.removePreceedingDashes(c.getName()).toUpperCase();
                    if (MISSIONS.contains(tmpString)) {
                        if (!DEFAULT_MISSIONS.contains(tmpString)) {
                            if (ocssw.isMissionDirExist(tmpString) ||
                                    missionDataStatus.get(tmpString)) {
                                ((JPanel) c).getComponents()[0].setEnabled(false);
                            } else {
                                ((JPanel) c).getComponents()[0].setEnabled(true);
                            }
                            missionPanel.add(c);
                        }
                    } else {
                        if (tmpString.equals("SRC")) {
                            ((JLabel) ((JPanel) c).getComponent(0)).setText("Source Code");
                            if (new File(ocsswInfo.getOcsswRoot() + System.getProperty("file.separator") + OCSSW_SRC_DIR_NAME).exists()) {
                                ((JPanel) c).getComponents()[0].setEnabled(false);
                            }
                        } else if (tmpString.equals("CLEAN")) {
                            ((JLabel) ((JPanel) c).getComponent(0)).setText("Clean Install");
                            ((JPanel) c).getComponents()[0].setEnabled(true);
                        } else if (tmpString.equals("VIIRSDEM")) {
                            ((JLabel) ((JPanel) c).getComponent(0)).setText("VIIRS DEM files");
                            if (new File(ocsswInfo.getOcsswRoot() + System.getProperty("file.separator") +
                                    "share" + System.getProperty("file.separator") + "viirs" +
                                    System.getProperty("file.separator") + "dem").exists()) {
                                ((JPanel) c).getComponents()[0].setEnabled(false);
                            }
                        }
                        otherPanel.add(c);
                        otherPanel.add(new JLabel("      "));
                    }
                }
            } else if (option.getName().equals("file parameter panel")) {
                Component[] bps = ((JPanel) option).getComponents();
                for (Component c : bps) {
                    dirPanel = (JPanel) c;

                }
                if (! ocsswInfo.getOcsswLocation().equals(ocsswInfo.OCSSW_LOCATION_LOCAL)) {
                    //if ocssw is not local, then disable the button to choose ocssw installation directory
                    ((JLabel)dirPanel.getComponent(0)).setText("Remote install_dir");
                } else {
                    ((JLabel)dirPanel.getComponent(0)).setText("Local install_dir");
                }
                ((JLabel)dirPanel.getComponent(0)).setToolTipText("This directory can be set in SeaDAS-OCSSW > OCSSW Configuration");
                ((JTextField)dirPanel.getComponent(1)).setEditable(false);
                ((JTextField)dirPanel.getComponent(1)).setToolTipText("This directory can be set in SeaDAS-OCSSW > OCSSW Configuration");
                dirPanel.getComponent(2).setVisible(false);

            } else if (option.getName().equals("text field panel")) {
                Component[] bps = ((JPanel) option).getComponents();
                JPanel tempPanel1, tempPanel2;
                for (Component c : bps) {
                    if (c.getName().equals(VALID_TAGS_OPTION_NAME)) {
                        tempPanel1 = (JPanel)c;
                        ((JLabel)tempPanel1.getComponent(0)).setText("Valid OCSSW Tags:");
                        JComboBox tags = ((JComboBox)tempPanel1.getComponent(1));

                        //This segment of code is to disable tags that are not compatible with the current SeaDAS version
                        ArrayList<String> validOcsswTags = ocssw.getOcsswTagsValid4CurrentSeaDAS();
                        Font f1 = tags.getFont();
                        Font f2 = new Font("Tahoma", 0, 14);

                        if( ocsswTagString!=null) {
                            tags.setSelectedItem(ocsswTagString);
                        }

                        tags.setRenderer(new DefaultListCellRenderer() {
                            @Override
                            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                                          int index, boolean isSelected, boolean cellHasFocus) {
                                if (value instanceof JComponent)
                                    return (JComponent) value;

                                boolean itemEnabled = validOcsswTags.contains((String)value);
                                super.getListCellRendererComponent(list, value, index,
                                        isSelected && itemEnabled, cellHasFocus);

                                // Render item as disabled and with different font:
                                setEnabled(itemEnabled);
                                setFont(itemEnabled ? f1 : f2);

                                return this;
                            }
                        });
                        // code segment ends here
                        tagPanel.add( tempPanel1);
;
                    } else if(c.getName().contains(CURRENT_TAG_OPTION_NAME)  ){
                            //|| CURRENT_TAG_OPTION_NAME.contains(c.getName())) {
                        tempPanel2 = (JPanel)c;
                        ((JLabel)tempPanel2.getComponent(0)).setText("Last Installed OCSSW Tag:");
                        tagPanel.add( tempPanel2);
                    }
                }
            }
        }

    }

    private Component findJPanel(Component comp, String panelName) {
        if (comp.getClass() == JPanel.class) return comp;
        if (comp instanceof Container) {
            Component[] components = ((Container) comp).getComponents();
            for (int i = 0; i < components.length; i++) {
                Component child = findJPanel(components[i], components[i].getName());
                if (child != null) {
                    return child;
                }
            }
        }
        return null;
    }
}