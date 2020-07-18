package cloud.autotests.tests;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertFalse;


@Feature("Desktop/Tablet/Mobile full screenshot comparison")
class YandexMapTests {

    String pathToResources = "src/test/resources/";

    private int DIFF_PIXELS = 20;

    private static Set<By> ignoreElements() {
        Set<By> setIgnoredElements = new HashSet<>();
//        setIgnoredElements.add(By.cssSelector(".CustomerRatings__content"))
//        setIgnoredElements.add(By.cssSelector(".LegalText__container"))
        return setIgnoredElements;
    }

    @Test
    @Description("Run test on Desktop")
    void desktopMapTest() throws IOException {
        open("https://yandex.ru/maps/");

        Screenshot actualScreenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportRetina(100, 0, 0, 2))
                .coordsProvider(new WebDriverCoordsProvider())
                .takeScreenshot(WebDriverRunner.getWebDriver());
//                .takeScreenshot(WebDriverRunner.getWebDriver(), $(".inner.steps-inner"));
        ImageIO.write(actualScreenshot.getImage(), "PNG",
                new File(pathToResources + "desktopMapTest/ActualPartOfThePageImage.png"));

        BufferedImage expectedImage = ImageIO.read(
                new File(pathToResources + "desktopMapTest/ExpectedPartOfThePageImage.png"));

        BufferedImage actualImage = actualScreenshot.getImage();
        ImageDiff diff = new ImageDiffer().makeDiff(expectedImage, actualImage).withDiffSizeTrigger(10);
        if (diff.hasDiff()) {
            ImageIO.write(diff.getMarkedImage(), "PNG", new File(pathToResources + "desktopMapTest/DifferencePartOfThePageImage.png"));
        }

        assertFalse(diff.hasDiff(), "Screenshot has difference");

        //        new HomePage()
//                .openHomePage()
//                .compareFullScreenshotHomePage(
//                        ScreenshotFileNameEnum.HomePageDesktop,
//                        ignoreElements(),
//                        DIFF_PIXELS)
    }

}