package cc.takacs.php_codeverage_display.display;

import cc.takacs.php_codeverage_display.clover.CloverXmlReader;
import cc.takacs.php_codeverage_display.clover.CoverageCollection;
import cc.takacs.php_codeverage_display.clover.FileCoverage;
import cc.takacs.php_codeverage_display.config.ConfigValues;
import com.intellij.openapi.editor.Editor;

import javax.swing.*;

/**
 * @author Zsolt Takacs <zsolt@takacs.cc>
 */
public class DisplayHandler {
    private FilenameDisplayMap map;

    public DisplayHandler() {
        this.map = new FilenameDisplayMap();
    }

    public void updateDisplays() {
        CloverXmlReader reader = new CloverXmlReader(getCloverXmlPath());
        CoverageCollection fileCoverages = reader.parse();

        for (String filename : fileCoverages.getKeys()) {
            FileCoverage fileCoverage = fileCoverages.get(filename);

            CoverageDisplay display = map.get(filename);

            if (display != null) {
                display.setFileCoverage(fileCoverage);

                SwingUtilities.invokeLater(new DisplayDrawerThread(display));
            }
        }
    }

    private String getCloverXmlPath() {
        return ConfigValues.getInstance().getCloverXmlPath();
    }

    public void addDisplayForEditor(Editor editor, String file) {
        CoverageDisplay display = new CoverageDisplay(editor);

        editor.getDocument().addDocumentListener(display);

        this.map.add(file, display);
    }

    public void removeDisplayForFile(String file) {
        this.map.remove(file);
    }
}
