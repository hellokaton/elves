package io.github.biezhi.elves.response;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import us.codecraft.xsoup.XElements;
import us.codecraft.xsoup.Xsoup;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 响应Body
 *
 * @author biezhi
 * @date 2018/1/12
 */
public class Body {

    private final InputStream inputStream;
    private final String      charset;
    private       String      bodyString;

    public Body(InputStream inputStream, String charset) {
        this.inputStream = inputStream;
        this.charset = charset;
    }

    @Override
    public String toString() {
        if (null == this.bodyString) {
            StringBuilder html = new StringBuilder(100);
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset));
                String         temp;
                while ((temp = br.readLine()) != null) {
                    html.append(temp).append("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.bodyString = html.toString();
        }
        return this.bodyString;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public Elements css(String css) {
        return Jsoup.parse(this.toString()).select(css);
    }

    public XElements xpath(String xpath) {
        return Xsoup.compile(xpath).evaluate(Jsoup.parse(this.toString()));
    }

}
