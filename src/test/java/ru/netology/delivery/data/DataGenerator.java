package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }

    public static String generateValidCity() {
        String[] validCity = {"Москва", "Санкт-Петербург", "Орёл", "Петропавловск-Камчатский", "Ростов-на-Дону", "Ханты-Мансийск",
                "Горно-Алтайск", "Йошкар-Ола", "Южно-Сахалинск", "Нижний Новгород", "Нарьян-Мар", "Великий Новгород", "Улан-Удэ",
                "Южно-Сахалинск", "Тюмень", "Екатеринбург", "Челябинск", "Пермь", "Омск", "Томск", "Барнаул", "Владивосток",
                "Хабаровск", "Пенза", "Самара", "Брянск", "Тверь", "Иркутск", "Биробиджан", "Астрахань", "Краснодар", "Ставрополь",
                "Мурманск", "Архангельск", "Сыктывкар", "Тула", "МОСКВА", "САНКТ-ПЕТЕРБУРГ", "ОРЁЛ", "ПЕТРОПАВЛОВСК-КАМЧАТСКИЙ",
                "РОСТОВ-НА-ДОНУ", "ХАНТЫ-МАНСИЙСК", "ГОРНО-АЛТАЙСК", "ЙОШКАР-ОЛА", "ЮЖНО-САХАЛИНСК", "НИЖНИЙ НОВГОРОД", "НАРЬЯН-МАР",
                "ВЕЛИКИЙ НОВГОРОД", "УЛАН-УДЭ", "ЮЖНО-САХАЛИНСК", "ТЮМЕНЬ", "ЕКАТЕРИНБУРГ", "ЧЕЛЯБИНСК", "ПЕРМЬ"};
        return (validCity[new Random().nextInt(validCity.length)]);
    }

    public static String generateInvalidCity(int index) {
        Faker faker = new Faker(new Locale("en"));
        String[] invalidCityList = {faker.address().cityName(), "Moscow", "Петропавловск_К@мчатский",
                faker.numerify("######"), "Колпино", "-", "Таллинн", "rtyryiu", "тула", "моСквА"};
        return invalidCityList[index];
    }

    public static String generateDate(int shift) {
        return LocalDate.now().plusDays(shift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String generateNonExistentDate(int index) {
        String[] wrongDate = {LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("33.MM.yyyy")),
                LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("dd.15.yyyy")),
                "29.02.2026"};
        return wrongDate[index];
    }

    public static String generateValidNameWithout_Yo_Letter() {
        Faker faker = new Faker(new Locale("ru"));
        String[] validName = {faker.name().fullName().replace("ё", "е"), "Елена-Мария Иванова",
                "Светлана-Мария-Ремарк Иванова-Гурцкая", "Иван Николаевич Петров", "ИГОРЬ ПОТЕРЯЕВ", "Ия Ро"};
        return (validName[new Random().nextInt(validName.length)]);
    }

    public static String generateValidNameWith_Yo_Letter(int index) {
        String[] list = {"Алёна Плетнёва", "Ёри Иванова"};
        return list[index];
    }

    public static String generateInvalidName(int index) {
        Faker latinName = new Faker(new Locale("en"));
        Faker cyrillicName = new Faker(new Locale("ru"));
        String[] invalidNameList = {
                latinName.name().fullName(), "щ",
                cyrillicName.name().fullName().repeat(10).replace("ё", "е"),
                "Павел Иванов03", "Илья Иванов_@#$", "Анна--Мария Петрова", "-Анна Петрова", "Анна- Петрова-", "-",
                "анатолий сидоров", "иВаН ивАНов"};
        return invalidNameList[index];
    }

    public static String generateValidPhone() {
        Faker faker = new Faker();
        return faker.numerify("+7##########");
    }

    public static String generateValidPhoneWithoutPlus() {
        Faker faker = new Faker();
        return faker.numerify("7##########");
    }

    public static String generateInvalidPhone(int index) {
        Faker faker = new Faker();
        String[] invalidPhone = {"+", "+7", faker.numerify("+3##########"), "+00000000000"};
        return invalidPhone[index];
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUserWithAllValidData() {
            return new UserInfo(generateValidCity(), generateValidNameWithout_Yo_Letter(), generateValidPhone());
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}
