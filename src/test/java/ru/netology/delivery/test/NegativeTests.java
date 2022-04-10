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

public class NegativeTests {

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
    public void shouldNotValidateFieldWithEmptyCityName() {
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(closestDate);
        $("[data-test-id='name'] input").setValue(generateValidNameWithout_Yo_Letter());
        $("[data-test-id='phone'] input").setValue(generateValidPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @ParameterizedTest
    @CsvSource(value = {"latinForeignCityName,0",
            "latinRuCityName,1",
            "cyrillicRuCityNameRegCenterWithRestrictedSymbols,2",
            "restrictedSymbolsOnlyDigits,3",
            "cyrillicRuCityNameNotRegCenter,4",
            "restrictedSymbolsNoLetters,5",
            "cyrillicForeignCityName,6",
            "nonexistentCityName,7",
            "cyrillicRuCityRegCenterAllLettersLowerCase,8",
            "cyrillicRuCityRegCenterWithRoughCaseErrors,9"})
    public void shouldNotValidateFieldWithInvalidCityName(String testcase, int index) {
        $("[data-test-id='city'] input").setValue(generateInvalidCity(index));
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(closestDate);
        $("[data-test-id='name'] input").setValue(generateValidNameWithout_Yo_Letter());
        $("[data-test-id='phone'] input").setValue(generateValidPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldNotValidateFieldWithEmptyDate() {
        var user = DataGenerator.Registration.generateUserWithAllValidData();
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $(byText("Запланировать")).click();
        $("[data-test-id='date'] .input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Неверно введена дата"));
    }

    @ParameterizedTest
    @CsvSource(value = {"meetingInTwoDays,2",
            "meetingTomorrow,1",
            "meetingToday,0",
            "meetingDayAgo,-1",
            "meetingYearAgo,-365",
            "meeting_In100Days_RedundantOpportunity,100",
            "meeting_InTwoYears_RedundantOpportunity,730",
            "meeting_In100Years_RedundantOpportunity,36500",
            "meeting_In150Years_RedundantOpportunity,54750"})
    public void shouldNotValidateFieldWithRestrictedDate(String testcase, int days) {
        var user = DataGenerator.Registration.generateUserWithAllValidData();
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(generateDate(days));
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='date'] .input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @ParameterizedTest
    @CsvSource(value = {"wrongDays,0",
            "wrongMonths,1",
            "wrongLeapYearDate,2"})
    public void shouldNotValidateFieldWithNonExistentDate(String testcase, int index) {
        var user = DataGenerator.Registration.generateUserWithAllValidData();
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(generateNonExistentDate(index));
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='date'] .input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Неверно введена дата"));
    }

    @Test
    public void shouldNotValidateFieldWithEmptyName() {
        $("[data-test-id='city'] input").setValue(generateValidCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(closestDate);
        $("[data-test-id='phone'] input").setValue(generateValidPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @ParameterizedTest
    @CsvSource(value = {"fullNameLatin,0",
            "oneLetterNameCyrillic(minBoundaryTest),1",
            "extremelyLongNameCyrillic(maxBoundaryTest),2",
            "nameCyrillicWithDigits,3",
            "nameCyrillicWithSpecialSymbols,4",
            "nameCyrillicWithTwoHyphensInRow,5",
            "nameCyrillicWithHyphenAtTheBeginning,6",
            "nameCyrillicWithHyphenAtTheEnd,7",
            "nameOneHyphenOnlyWithoutLetters,8",
            "nameCyrillicAllLettersInLowerCase,9",
            "nameCyrillicWithRoughCaseErrors,10"})
    public void shouldNotValidateFieldWithInvalidName(String testcase, int index) {
        $("[data-test-id='city'] input").setValue(generateValidCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(closestDate);
        $("[data-test-id='name'] input").setValue(generateInvalidName(index));
        $("[data-test-id='phone'] input").setValue(generateValidPhone());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void shouldNotValidateFieldWithEmptyPhoneNumber() {
        $("[data-test-id='city'] input").setValue(generateValidCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(closestDate);
        $("[data-test-id='name'] input").setValue(generateValidNameWithout_Yo_Letter());
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @ParameterizedTest
    @CsvSource(value = {"onlyPlusSymbol,0",
            "tooShortNumber,1",
            "phoneNumberOfAnotherCountry,2",
            "nonExistentNumberElevenNulls,3"})
    public void shouldNotValidateFieldWithInvalidPhoneNumber(String testcase, int index) {
        $("[data-test-id='city'] input").setValue(generateValidCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(closestDate);
        $("[data-test-id='name'] input").setValue(generateValidNameWithout_Yo_Letter());
        $("[data-test-id='phone'] input").setValue(generateInvalidPhone(index));
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldBe(Condition.visible)
                .shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shouldNotSendFormWithBoxNotClicked() {
        var user = DataGenerator.Registration.generateUserWithAllValidData();
        $("[data-test-id='city'] input").setValue(user.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date'] input").setValue(closestDate);
        $("[data-test-id='name'] input").setValue(user.getName());
        $("[data-test-id='phone'] input").setValue(user.getPhone());
        $(byText("Запланировать")).click();
        $("[data-test-id='agreement'] .checkbox__text")
                .shouldHave(Condition.cssValue("color", "rgba(255, 92, 92, 1)"));
    }

    @Test
    public void shouldNotShowReplanNotificationIfUserDataAndDateRemainTheSame() {
        var user = DataGenerator.Registration.generateUserWithAllValidData();
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
        $(byText("Запланировать")).click();
        $("[data-test-id='error-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave((text("Ошибка!")));
        $("[data-test-id='error-notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(text("У вас уже запланирована встреча на указанную дату. " +
                        "Пожалуйста, измените дату для перепланирования встречи"));
    }

    @Test
    public void shouldNotReplanIfNewDataExtendsMoreThan90DaysFromNow() {
        var user = DataGenerator.Registration.generateUserWithAllValidData();
        String secondDate = generateDate(91);
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
        $("[data-test-id='error-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave((text("Ошибка!")));
        $("[data-test-id='error-notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(text("На указанную дату планирование невозможно. " +
                        "Пожалуйста, выберите дату в пределах 3 месяцев от текущей."));
    }

}
