package io.github.biezhi.elves.response;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import us.codecraft.xsoup.XElements;
import us.codecraft.xsoup.Xsoup;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author biezhi
 * @date 2018/1/12
 */
public class Body {

    private final InputStream inputStream;
    private final String      bodyString;

    public Body(InputStream inputStream, Charset charset) {
        this.inputStream = inputStream;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int                   nRead;
        byte[]                data   = new byte[1024];
        try {
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] byteArray = buffer.toByteArray();
        this.bodyString = new String(byteArray, charset);
    }

    @Override
    public String toString() {
        return this.bodyString;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public Elements css(String css) {
        return Jsoup.parse(this.bodyString).select(css);
    }

    public XElements xpath(String xpath) {
        return Xsoup.compile(xpath).evaluate(Jsoup.parse(this.bodyString));
    }

}
