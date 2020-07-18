package cloud.autotests.helpers;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class ScreenshotComparisonHelper {
    private String pathToResources = "src/test/resources/";

    public Screenshot takeActualScreenshot(String testPath, Set<By> setIgnoreElements) {
        Screenshot takeScreenshot = new AShot()
//                .shootingStrategy(ShootingStrategies.viewportRetina(3000, 0, 0, 2))
                .coordsProvider(new WebDriverCoordsProvider())
                .ignoredElements(setIgnoreElements)
                .takeScreenshot(WebDriverRunner.getWebDriver());
        try {
            ImageIO.write(takeScreenshot.getImage(), "PNG", new File(pathToResources + testPath + "actual.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return takeScreenshot;
    }

    public Screenshot getExpectedScreenshot(String testPath) {
        Screenshot expectedScreenshot = null;
        try {
            expectedScreenshot = new Screenshot(ImageIO.read(new File(pathToResources + testPath + "expected.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return expectedScreenshot;
    }

    public ImageDiff compareScreenshots(Screenshot actual, Screenshot expected, int withDiffSizeTrigger, String testPath) {
        ImageDiff diff = new ImageDiffer().makeDiff(expected, actual).withDiffSizeTrigger(withDiffSizeTrigger);
        if (diff.hasDiff()) {
            try {
                ImageIO.write(diff.getMarkedImage(), "PNG", new File(pathToResources + testPath + "difference.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Path content = Paths.get(pathToResources + testPath + "difference.png");
            InputStream difference = null;
            try {
                difference = Files.newInputStream(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Allure.addAttachment("Difference", difference);
            Allure.addAttachment("Actual", readScreenshotFromResources(testPath + "actual.png"));
            Allure.addAttachment("Expected", readScreenshotFromResources(testPath + "expected.png"));
        } else {
            Allure.addAttachment("Actual", readScreenshotFromResources(testPath + "actual.png"));
            Allure.addAttachment("Expected", readScreenshotFromResources(testPath + "expected.png"));
        }
        return diff;
    }

    private InputStream readScreenshotFromResources(String fileName) {
        Path path = Paths.get(pathToResources + fileName);
        InputStream actualResult = null;
        try {
            actualResult = Files.newInputStream(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return actualResult;
    }
}
