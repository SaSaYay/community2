package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sasayaya
 * @create 2022/7/10 16:43
 */
@Component
public class SensitiveFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveFilter.class);
//    替换符
    private static final String REPLACEMENT = "***";
//    根节点
    private TrieNode rootNode = new TrieNode();
    @PostConstruct
    public void init(){
        try(        InputStream IS = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(IS))
        ){
            String keyword;
            while ((keyword = reader.readLine())!=null){
//                添加到前缀树
                this.addKeyword(keyword);
            }
        }catch (IOException e){
            LOGGER.error("加载敏感词文件失败："+e.getMessage());
        }

    }
//    将一个敏感词添加到前缀树中去
    private void addKeyword(String keyword){
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if (subNode == null){
//                初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }
//            指针指向子节点，进入下一轮循环
            tempNode = subNode;
//            设置结束的标识
            if (i ==keyword.length()-1){
                tempNode.setKeywordEnd(true);
            }

        }
    }
    /*
    过滤敏感词
    返回过滤后的文本
     */
    public String filter(String text){
        if (StringUtils.isBlank(text)){
            return null;
        }
//        指针1
        TrieNode tempNode = rootNode;
//        指针2,3
        int begin = 0;
        int position = 0;
//        结果
        StringBuilder  sb =new StringBuilder();
        while (position<text.length()){
            char c = text.charAt(position);
//            跳过符号
            if(isSymbol(c)){
//                若指针1处于根节点，将此符号计入结果，让指针2向下走一步
                if (tempNode == rootNode){
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }
//            检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null){
                sb.append(text.charAt(begin));
                position =begin;
                begin++;
//                指针重新指向根节点
                tempNode =rootNode;
            }else if (tempNode.isKeywordEnd()){
//                将begin到position的字符串替换掉
                sb.append(REPLACEMENT);
//                进入下一个位置
                begin=++position;
                //                指针重新指向根节点
                tempNode =rootNode;
            }else {
//                检查下一个字符
                position++;
            }
        }
//        将最后一批字符计入结果
        sb.append(text.substring(begin,position));

        return sb.toString();
    }
    /*
    判断是否为符号
    (c <0x2E80||c>0x9FFF)东亚文字范围
     */
    private boolean isSymbol(Character c){
        return !CharUtils.isAsciiAlphanumeric(c) && (c <0x2E80||c>0x9FFF);
    }


//    前缀树
    private class TrieNode{
//        关键词结束的标志
        private boolean isKeywordEnd = false;

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

    //        子节点
        private Map<Character,TrieNode> subNodes = new HashMap<>();

//        添加子节点的方法
        public void addSubNode(Character c,TrieNode node){
            subNodes.put(c,node);
        }
//        获取子节点的方法
        public TrieNode getSubNode(Character c){
            return  subNodes.get(c);
        }
    }
}
