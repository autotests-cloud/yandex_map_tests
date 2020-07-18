package cloud.autotests.tests;

import cloud.autotests.helpers.ScreenshotComparisonHelper;
import com.codeborne.selenide.Condition;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertFalse;


@Feature("Desktop/Tablet/Mobile full screenshot comparison")
class AllureReportsTests {

    @Test
    @Description("Run test on Desktop")
    void desktopTest() {
        Set<By> setIgnoredElements = new HashSet<>();
        setIgnoredElements.add(By.cssSelector(".widget__column.summary-widget__chart"));

        open("http://192.168.43.69:54986/index.html");
        $(".widget__column.summary-widget__chart").shouldBe(Condition.visible);

        Screenshot actualImage = new ScreenshotComparisonHelper()
                .takeActualScreenshot("allure/desktopTest/", setIgnoredElements);

        Screenshot expectedImage = new ScreenshotComparisonHelper()
                .getExpectedScreenshot("allure/desktopTest/");

        ImageDiff diff = new ScreenshotComparisonHelper().compareScreenshots(
                actualImage,
                expectedImage,
                10,
                "allure/desktopTest/");

        assertFalse(diff.hasDiff(), "Screenshot has difference");
    }

}