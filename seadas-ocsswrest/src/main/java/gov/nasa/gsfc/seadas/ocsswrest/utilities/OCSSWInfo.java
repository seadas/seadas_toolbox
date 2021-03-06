package gov.nasa.gsfc.seadas.ocsswrest.utilities;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Aynur Abdurazik (aabduraz)
 * Date: 1/9/15
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 * This class should always be synchronized with OCSSWInfo
 */
@XmlRootElement
public class OCSSWInfo {
    public static final String _OCSSWROOT_ENVVAR = "OCSSWROOT";
    public static String OCSSW_INSTALLER = "install_ocssw";
    public static String OCSSW_RUNNER = "ocssw_runner";

    public static final String SEADAS_OCSSW_VERSIONS_JSON_NAME = "seadasVersions.json";

    public static final String OCSSW_INSTALLER_URL = "https://oceandata.sci.gsfc.nasa.gov/manifest/install_ocssw";
    public static final String OCSSW_BOOTSTRAP_URL = "https://oceandata.sci.gsfc.nasa.gov/manifest/ocssw_bootstrap";
    public static final String OCSSW_MANIFEST_URL = "https://oceandata.sci.gsfc.nasa.gov/manifest/manifest.py";
    public static final String OCSSW_SEADAS_VERSIONS_URL = "https://oceandata.sci.gsfc.nasa.gov/manifest/seadasVersions.json";
    public static final String TMP_OCSSW_INSTALLER = (new File(System.getProperty("java.io.tmpdir"), "install_ocssw")).getPath();
    public static final String TMP_OCSSW_BOOTSTRAP = (new File(System.getProperty("java.io.tmpdir"), "ocssw_bootstrap")).getPath();
    public static final String TMP_OCSSW_MANIFEST = (new File(System.getProperty("java.io.tmpdir"), "manifest.py")).getPath();
    public static final String TMP_SEADAS_OCSSW_VERSIONS_FILE = (new File(System.getProperty("java.io.tmpdir"), SEADAS_OCSSW_VERSIONS_JSON_NAME)).getPath();

    public static String _OCSSW_SCRIPTS_DIR_SUFFIX =  System.getProperty("file.separator") + "bin";
    public static String _OCSSW_DATA_DIR_SUFFIX =  System.getProperty("file.separator") + "share";
    public static final String OCSSW_BIN_DIR_SUFFIX = "bin";
    public static final String OCSSW_SRC_DIR_NAME = "ocssw_src";

    public static final String OCSSW_INSTALLER_PROGRAM_NAME = "install_ocssw";
    public static final String OCSSW_RUNNER_SCRIPT = "ocssw_runner";
    public static final String OCSSW_SEADAS_INFO_PROGRAM_NAME = "seadas_info";

    private boolean installed;
    private String ocsswDir;



    public boolean isInstalled(){
        return installed;
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }

    public String getOcsswInstallDir(){
        ocsswDir = System.getenv(_OCSSWROOT_ENVVAR);
        return ocsswDir;
    }

    public String getOcsswDataDir(){
        return ocsswDir + _OCSSW_DATA_DIR_SUFFIX;
    }

    public String getOcsswScriptsDir(){
        return ocsswDir + _OCSSW_SCRIPTS_DIR_SUFFIX;
    }

}
