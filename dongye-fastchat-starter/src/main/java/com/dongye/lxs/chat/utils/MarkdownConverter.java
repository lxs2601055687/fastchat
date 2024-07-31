package com.dongye.lxs.chat.utils;

import org.commonmark.node.Document;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownConverter {

    /**
     * 将 Markdown 转换为 HTML。
     *
     * @param markdown Markdown 格式的字符串
     * @return 转换后的 HTML 字符串
     */
    public static String convertMarkdownToHtml(String markdown) {
        // 创建解析器
        Parser parser = Parser.builder().build();

        // 解析 Markdown 文本
        Document document = (Document) parser.parse(markdown);

        // 创建渲染器
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        // 渲染为 HTML
        return renderer.render(document);
    }


}