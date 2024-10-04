package ru.skypro.homework.utility.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.utility.FileUtilityService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

@Service
public class FileUtilityServiceImpl implements FileUtilityService {

    /**
     * Сохраняет загруженный файл на диск.
     * <p>
     * Если родительский каталог пути назначения не существует, метод создаст его.
     * Если по указанному пути файл уже существует, он будет удален перед сохранением нового файла.
     * Затем метод сохраняет содержимое загруженного файла в новый файл по заданному пути.
     *
     * @param formFile Загружаемый файл, полученный от пользователя.
     * @param filePath Путь, по которому файл будет сохранен на диске.
     * @throws IOException если в процессе работы метода произошла ошибка ввода-вывода.
     */
    @Override
    public void saveFile(MultipartFile formFile, Path filePath) throws IOException {

        if (!Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }

        Files.deleteIfExists(filePath);

        try (
                InputStream inputStream = formFile.getInputStream();
                OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 16384);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 16384)
        ) {
            bufferedInputStream.transferTo(bufferedOutputStream);
        }
    }

    /**
     * Возвращает расширение файла из его полного имени.
     * <p>
     * Этот метод ищет последнюю точку в имени файла и возвращает всё, что следует за ней.
     * Это считается расширением файла. Если точка в имени файла отсутствует, результат будет пустой строкой.
     *
     * @param fileName Имя файла, для которого необходимо найти расширение.
     * @return Расширение файла или пустую строку, если расширение не найдено.
     * @throws NullPointerException если передан null в качестве имени файла.
     */
    @Override
    public String getExtensions(String fileName) {
        return Objects.requireNonNull(fileName.substring(fileName.lastIndexOf(".") + 1));
    }

    /**
     * Генерирует изображение определенного размера для хранения в базе данных.
     * <p>
     * Этот метод считывает изображение с диска, масштабирует его до заданной ширины,
     * сохраняя пропорции, и возвращает масштабированное изображение в виде массива байтов.
     *
     * @param filePath путь к файлу изображения на диске.
     * @param width    желаемая ширина конечного изображения.
     * @return массив байтов масштабированного изображения.
     * @throws IOException если возникают ошибки при чтении файла или при его масштабировании.
     */
    @Override
    public byte[] generateFileForDataBase(Path filePath, int width) throws IOException {

        try (InputStream inputStream = new FileInputStream(filePath.toFile())) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 16384);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedImage image = ImageIO.read(bufferedInputStream);

            // Если width равен нулю, сохраняем исходное разрешение
            if (width == 0) {
                ImageIO.write(image, getExtensions(filePath.getFileName().toString()), byteArrayOutputStream);
            } else {
                // Вычисляем высоту и ширину, проверяем их на допустимость
                int newWidth = Math.max(1, width); // ширина не может быть меньше 1
                int newHeight = Math.max(1, image.getHeight() * newWidth / image.getWidth()); // высота не может быть меньше 1

                // Создание уменьшенного изображения
                int height = image.getHeight() * width / image.getWidth();
                BufferedImage previewForm = new BufferedImage(width, height, image.getType());
                Graphics2D drawable = previewForm.createGraphics();
                drawable.drawImage(image, 0, 0, width, height, null);
                drawable.dispose();

                ImageIO.write(previewForm, getExtensions(filePath.getFileName().toString()), byteArrayOutputStream);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }
}
