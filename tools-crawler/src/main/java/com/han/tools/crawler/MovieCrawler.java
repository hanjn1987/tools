package com.han.tools.crawler;

/**
 * Created by hanjunnan on 2017/4/7.
 */
public class MovieCrawler {
//    public static void main(String args[]) throws IOException {
//        if (1 == 0) {
//            try {
//                testJped();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return;
//        }
//
//        String path = "C:/Users/hanjn/dev/workspace/download/";
//        mkdirs(path);
//
//        HttpClient client = HttpClientConfig.getHttpClient();
//
//        for (int i = 1; i <= 2; i++) {
//            String url = "http://1024.2048xd.pw/pw/thread.php?fid=3&page=" + i;
//            crawPageList(client, url, "http://1024.2048xd.pw/pw/", path);
//            break;
//        }
//    }
//
//
//    public static void mkdirs(String path) {
//        File file = new File(path);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//    }
//
//    public static String getPageContent(HttpClient client, String url) {
//        int retry = 0;
//        HttpGet get = new HttpGet(url);
//        get.setConfig(HttpClientConfig.getRequestConfig());
//
//        while (true) {
//            try {
//                HttpResponse response = client.execute(get);
//                if (null == response || 200 != response.getStatusLine().getStatusCode()) {
//                    if (retry++ > 3) {
//                        break;
//                    }
//
//                    System.out.println("ERROR: get Page Retry:[" + retry + "]" + url);
//                    Thread.sleep(1000 * retry);
//                    continue;
//                }
//
//                String respStr = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
//                return respStr;
//            } catch (Exception e) {
//                System.out.println("ERROR: get Page Retry:[" + retry + "]" + url);
//                if (retry++ > 3) {
//                    break;
//                }
//
//                try {
//                    Thread.sleep(1000 * retry);
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                }
//                e.printStackTrace();
//            }
//        }
//
//        return null;
//    }
//
//    public static void crawPageList(HttpClient client, String url, String base, String filepath) {
//        try {
//            String respStr = getPageContent(client, url);
//
//            Document document = Jsoup.parse(respStr);
//            Elements elements = document.select("tbody > tr.t_one");
//
//            for (Element element : elements) {
//                if (org.apache.commons.lang3.StringUtils.isNoneBlank(element.attr("onmouseover"))) {
//                    System.out.println(element.attr("onmouseover"));
//                    continue;
//                }
//
//                Element element1 = element.select("a").get(0);
//                String title = base + element1.attr("href");
//                String file = StringUtils.substringAfter(title, base).replaceAll("/", "-");
//                String content = element.text().replaceAll("\\.::", "").replaceAll(" ", "");
//                System.out.println(title + "    " + file + "  " + content);
//
//                createPage(client, title, file, filepath);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void createPage(HttpClient client, String page, String title, String filepath) {
//        try {
//            String fileDir = filepath + title + ".pdf";
//            File file = new File(fileDir);
//            if (file.exists()) {
//                System.out.println("File exist: " + fileDir);
//                return;
//            }
//
//            String respStr = getPageContent(client, page);
//
//            Document document = Jsoup.parse(respStr);
//            Element read_tpc = document.getElementById("read_tpc");
//
//            //字体
//            String font_cn = getChineseFont();
//            BaseFont bf = BaseFont.createFont(font_cn + ",1", //注意这里有一个,1
//                    BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//            Font font = new Font(bf, 12);
//
//            com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document(new Rectangle(1440, 10000));
//            PdfWriter writer = PdfWriter.getInstance(pdfDoc, new FileOutputStream(file));
//            writer.setEncryption(true, "user.",
//                    "owner.", PdfWriter.ALLOW_PRINTING);
//
//            pdfDoc.open();
//
//            BufferedReader reader = new BufferedReader(new StringReader(read_tpc.html()));
//            String line = null;
//
//            while ((line = reader.readLine()) != null) {
//                try {
//                    if (line.contains("img")) {
//                        String url = org.apache.commons.lang3.StringUtils.substringBetween(line, "<img src=\"", "\"");
//                        System.out.println(url);
//                        Image image = getImgObject(client, url);
//                        if (null == image) {
//                            pdfDoc.add(new Paragraph(new Chunk(url).setAnchor(url)));
//                        } else {
//                            pdfDoc.add(image);
//                        }
//                        continue;
//                    }
//
//                    if (line.contains("<a")) {
//                        String link = StringUtils.substringBetween(line, "<a href=\"", "\"");
//                        pdfDoc.add(new Paragraph(new Chunk("download").setAnchor(link)));
//                        continue;
//                    }
//
//                    line = line.replaceAll("<br>", "");
//                    if (StringUtils.isBlank(line)) {
//                        continue;
//                    }
//
//                    pdfDoc.add(new Paragraph(line, font));
//
//                    System.out.println(line);
//                } catch (Exception e) {
//                    System.err.println("ERROR:" + line);
//                    e.printStackTrace();
//                }
//            }
//
//            pdfDoc.close();
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 获取中文字体位置
//     *
//     * @return
//     * @author xxj 2017年4月28日
//     */
//    private static String getChineseFont() {
//        //宋体（对应css中的 属性 font-family: SimSun; /*宋体*/）
//        String font1 = "C:/Windows/Fonts/simsun.ttc";
//
//        //判断系统类型，加载字体文件
//        java.util.Properties prop = System.getProperties();
//        String osName = prop.getProperty("os.name").toLowerCase();
//        System.out.println(osName);
//        if (osName.indexOf("linux") > -1) {
//            font1 = "/usr/share/fonts/simsun.ttc";
//        }
//        if (!new File(font1).exists()) {
//            throw new RuntimeException("字体文件不存在,影响导出pdf中文显示！" + font1);
//        }
//        return font1;
//    }
//
//    public static Image getImgObject(HttpClient client, String img) {
//        HttpGet get = new HttpGet(img);
//        get.setConfig(HttpClientConfig.getRequestConfig());
//        get.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//        get.addHeader("Accept-Encoding", "gzip, deflate");
//        get.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
//        get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");
//
//        Image image = null;
//        int retry = 0;
//
//        while (true) {
//            try {
//                HttpResponse response = client.execute(get);
//                InputStream content = response.getEntity().getContent();
//                ByteOutputStream outputStream = new ByteOutputStream();
//                outputStream.write(content);
//                outputStream.close();
//                byte[] result = null;
//                result = outputStream.getBytes();
//                image = Image.getInstance(result);
//                break;
//            } catch (Exception e) {
//                System.err.println("ERROR: " + img + "[Retry:" + retry + "]");
//                e.printStackTrace();
//
//                if (retry++ > 3) {
//                    break;
//                }
//
//                try {
//                    Thread.sleep(1000 * retry);
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        }
//
//        return image;
//    }
//
//    public static byte[] getImgByte(HttpClient client, String img) {
//        HttpGet get = new HttpGet(img);
//        get.setConfig(HttpClientConfig.getRequestConfig());
//        get.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//        get.addHeader("Accept-Encoding", "gzip, deflate");
//        get.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
//        get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");
//        byte[] result = null;
//
//        int retry = 0;
//
//        while (true) {
//            try {
//                HttpResponse response = client.execute(get);
//                InputStream content = response.getEntity().getContent();
//                ByteOutputStream outputStream = new ByteOutputStream();
//                outputStream.write(content);
//                outputStream.close();
//                result = outputStream.getBytes();
//                break;
//            } catch (Exception e) {
//                System.err.println("ERROR: " + img + "[Retry:" + retry + "]");
//                e.printStackTrace();
//
//                if (retry++ > 3) {
//                    break;
//                }
//
//                try {
//                    System.out.println("[time]:" + 1000 * retry);
//                    Thread.sleep(1000 * retry);
//                    System.out.println("sleep - end");
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                }
//            }
//
//        }
//
//
//        return result;
//    }
//
//
//    private static byte[] toByteArray(InputStream in) throws IOException {
//
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024 * 4];
//        int n = 0;
//        while ((n = in.read(buffer)) != -1) {
//            out.write(buffer, 0, n);
//        }
//        return out.toByteArray();
//    }
//
//    public static void testJped() throws Exception {
//        //http://ososoo.com/files/2018/02/19/589d0c46d531331b.jpeg
//        //http://ososoo.com/files/2018/02/04/thumbs/2ce04d9df38f3cec.md.jpg
//        // http://kccdk.com/files/2018/02/05/thumbs/659281a0156f2036.md.jpg[
//        String path = "http://ososoo.com/files/2018/02/04/thumbs/2ce04d9df38f3cec.md.jpg";
//
//        //字体
//        String font_cn = getChineseFont();
//        BaseFont bf = BaseFont.createFont(font_cn + ",1", //注意这里有一个,1
//                BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//        Font font = new Font(bf, 12);
//
//        com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document(new Rectangle(1280, 10000));
//        PdfWriter writer = PdfWriter.getInstance(pdfDoc, new FileOutputStream("mm.pdf"));
//        writer.setEncryption(true, "user.",
//                "owner.", PdfWriter.ALLOW_PRINTING);
//
//        pdfDoc.open();
//
//        HttpClient client = HttpClients.createDefault();
//
//        byte[] images = getImgByte(client, path);
//
//        FileOutputStream outputStream = new FileOutputStream("temp.jpeg");
//        outputStream.write(images);
//        outputStream.close();
//
//        Image image = Image.getInstance(images);
//        pdfDoc.add(image);
//        pdfDoc.add(new Paragraph("hello", font));
//
//        pdfDoc.close();
//        writer.close();
//    }
//
//    static Map<String, NovalTemplate> templateMap = new HashMap<String, NovalTemplate>();
//
//    static {
//        templateMap.put("23us", new NovalTemplate("div#main > div.inner > dl.chapterlist", "div#main > div#BookCon > h1", "div#main > div#BookCon > div#content", "UTF-8"));
//        templateMap.put("xs222", new NovalTemplate("div#list > dl", "div.bookname > h1", "div#content", "GBK"));
//    }
}
