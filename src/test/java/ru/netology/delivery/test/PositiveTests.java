package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.openqa.selenium.Keys.BACK_SPACE;
import static ru.netology.delivery.data.DataGenerator.*;

public class PositiveTests {

    @BeforeEach
    public void setupTest() {
        open("http://localhost:9999");
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    String closestDate = generateDate(3);

    @Test
    public void shouldPlanAndReplanMeetingSuccessfully_HappyPathTest() {
        var user = DataGenerator.Registration.generateUserWithAllValidData();
        String secondDate = generateDate(4);
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(closestDate);
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave((text("Успешно!")));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(text("Встреча успешно запланирована на " + closestDate));
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(secondDate);
        $(byText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave((text("Необходимо подтверждение")));
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $(byText("Перепланировать")).click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave((text("Успешно!")));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(text("Встреча успешно запланирована на " + secondDate));
    }

    @ParameterizedTest
    @CsvSource(value = {"letter_Yo_LowerCase,0",
            "letter_Yo_UpperCase,1"})
    public void shouldValidateNameWith_Yo_Letter(String testcase, int index) {
        $("[data-test-id='city'] input").setValue(DataGenerator.generateValidCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(closestDate);
        $("[data-test-id='name'] input").setValue(DataGenerator.generateValidNameWith_Yo_Letter(index));
        $("[data-test-id='phone'] input").setValue(DataGenerator.generateValidPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave((text("Успешно!")));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(text("Встреча успешно запланирована на " + closestDate));
    }

    @ParameterizedTest
    @CsvSource(value = {"meetingInOneWeek,7",
            "meetingIn_30_Days,30",
            "meetingIn_90_Days,90"})
    public void shouldSendFormWithValidDate(String testcase, int days) {
        var user = DataGenerator.Registration.generateUserWithAllValidData();
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(generateDate(days));
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave((text("Успешно!")));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(text("Встреча успешно запланирована на " + generateDate(days)));
    }

    @Test
    public void shouldAddPlusSymbolToPhoneNumberAutomatically() {
        $("[data-test-id='city'] input").setValue(DataGenerator.generateValidCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(closestDate);
        $("[data-test-id='name'] input").setValue(generateValidNameWithout_Yo_Letter());
        $("[data-test-id='phone'] input").setValue(generateValidPhoneWithoutPlus());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave((text("Успешно!")));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(text("Встреча успешно запланирована на " + closestDate));
    }
}
