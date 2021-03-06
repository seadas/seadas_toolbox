SeaDAS Toolbox (seadas-toolbox)
==========================

A toolbox for the OBPG processing code.

[![Build Status](https://travis-ci.org/senbox-org/s3tbx.svg?branch=master)](https://travis-ci.org/senbox-org/s3tbx)
[![Coverity Scan Status](https://scan.coverity.com/projects/7247/badge.svg)](https://scan.coverity.com/projects/senbox-org-s3tbx)

Building seadas-toolbox from the source
------------------------------

Download and install the required build tools
	* Install J2SE 1.8 JDK and set JAVA_HOME accordingly. 
	* Install Maven and set MAVEN_HOME accordingly. 
	* Install git

Add $JAVA_HOME/bin, $MAVEN_HOME/bin to your PATH.

Clone the SeaDAS Toolbox source code and related repositories into a directory referred to a ${snap} from here on

    cd ${snap}
    git clone https://github.com/seadas/seadas-toolbox.git
    git clone https://github.com/senbox-org/s3tbx.git
    git clone https://github.com/senbox-org/snap-desktop.git
    git clone https://github.com/senbox-org/snap-engine.git
    
Build SNAP-Engine:

    cd ${snap}/snap-engine
    mvn install

Build SNAP-Desktop:

    cd ${snap}/snap-desktop
    mvn install

Build Sentinel-3 Toolbox:

    cd ${snap}/s3tbx
    mvn install

Build SeaDAS Toolbox:

    cd ${snap}/seadas-toolbox
    mvn install
   
If unit tests are failing, you can use the following to skip the tests
   
    mvn clean
    mvn install -Dmaven.test.skip=true
	
Setting up IntelliJ IDEA
------------------------


1. In IntelliJ IDEA, select "Import Project" and select the ${snap} directory. (Some versions: select "New -> Project From Existing Sources", then navigate upwards in the file selector to select the ${snap} directory, then select "Open")
2. Select "Import project from external model" -> "Maven"
3. Ensure the "Root directory" is ${snap}. (Note: put your actual path).
 Select "Search for projects recursively"; Ensure **not** to enable the option *Create module groups for multi-module Maven projects*. Everything can be default values.

4. Set the used SDK for the main project. A JDK 1.8 or later is needed.

5. Use the following configuration to run SNAP in the IDE: 

    **Main class:** `org.esa.snap.nbexec.Launcher`
    
    **VM parameters:** `-Dsun.awt.nopixfmt=true -Dsun.java2d.noddraw=true -Dsun.java2d.dpiaware=false`
    
    All VM parameters are optional
    
    **Program arguments:**    
       `--userdir "${snap}/seadas-toolbox/target/userdir"`
       `--clusters "${snap}/seadas-toolbox/seadas-kit/target/netbeans_clusters/seadas:${snap}/s3tbx/s3tbx-kit/target/netbeans_clusters/s3tbx"`
       `--patches "${snap}/snap-engine/$/target/classes:${snap}/seadas-toolbox/$/target/classes:${snap}/s3tbx/$/target/classes"`
    
    **Working directory:** `${snap}/snap-desktop/snap-application/target/snap/`
    
    **Use classpath of module:** `snap-main`

Enjoy!


