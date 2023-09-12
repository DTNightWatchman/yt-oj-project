import cn.hutool.core.io.FileUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<String> strings = FileUtil.readLines("E:\\gitee\\yt-oj-project\\ytoj-code-sandbox\\src\\main\\resources\\application.yml", StandardCharsets.UTF_8);

        System.out.println(strings);
    }
}