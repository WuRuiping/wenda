package com.wrp.service;

import com.wrp.controller.HomeController;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuruiping on 2017/2/8.
 */
@Service
public class SensitiveService implements InitializingBean{

    private static  final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     *默认敏感词替换文本
     */
    private static final String DEFAULT_REPLACEMENT = "(敏感词)";

    //前缀树
    private class  TrieNode{
        /**
         * true:关键词的终结；false:继续
         */
        private boolean end = false;

        /**
         * key是下一字符，value是对应的节点树
         */
        private Map<Character,TrieNode> subNodes = new HashMap<>();

        /**
         * 向指定位置添加结点数
         * @param key
         * @param node
         */
        void addSubNode(Character key,TrieNode node){
            subNodes.put(key,node);
        }

        /**
         * 获取下个节点树
         * @param key
         * @return
         */
        TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        boolean isKeywordEnd(){
            return end;
        }

        void setKeywordEnd(boolean end){
            this.end = end;
        }

        public  int getSubNodeCount(){
            return subNodes.size();
        }


    }

    /**
     * 根节点
     */
    private TrieNode rootNode = new TrieNode();

    /**
     *
     * @param c
     * @return
     */
    private boolean isSymbol(char c){
        int ic = (int)c;
        // 0x2E80-0x9FFF东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    /**
     * 把一个敏感词串成一棵前缀树
     * @param word
     */
    public  void addWord(String lineTxt){
        TrieNode tempNode = rootNode;
        //循环每个字节
        for(int i=0;i < lineTxt.length();i++){
            Character c = lineTxt.charAt(i);
            //过滤空格
            if(isSymbol(c)){
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);
            if(node == null){
                node = new TrieNode();
                tempNode.addSubNode(c,node);
            }
            tempNode = node;
            if(i == lineTxt.length() - 1){
                //关键词结束，设置结束标志
                tempNode.setKeywordEnd(true);
            }

        }
    }

    /**
     *程序初始化属性设置之后执行这个函数
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
       try{
           //java里读取文件
           InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
           InputStreamReader read = new InputStreamReader(is);
           BufferedReader bufferedReader = new BufferedReader(read);
           String lineTxt;
           //一行一行的读
           while ((lineTxt = bufferedReader.readLine()) != null){
               lineTxt = lineTxt.trim();//去掉两边的空格
               addWord(lineTxt);
           }
           read.close();//关闭读取
       }catch (Exception e){
           logger.error("读取敏感词文件失败"+e.getMessage());
       }
    }

    /**
     * 过滤敏感词
     * @param text：一段文本
     * @return： 过滤敏感词后的文本
     */
    public String filter(String text){
       if(StringUtils.isBlank(text)){
           return text;
       }
       //敏感词替换文本
       String replacement = DEFAULT_REPLACEMENT;
       StringBuilder result = new StringBuilder();

       TrieNode tempNode = rootNode;
       int begin = 0;//敏感词的开始
       int position = 0;//text当前比较位置

        while(position < text.length()){
           char c = text.charAt(position);
           //空格和其他特殊的字符直接跳过
            if(isSymbol(c)){
                if(tempNode == rootNode){
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
           tempNode = tempNode.getSubNode(c);
           //当前位置匹配结束
           if(tempNode == null){
               //以begin开头的字符串不存在敏感词
               result.append(text.charAt(begin));
               //跳到下一字符开始测试
               position = begin + 1;
               begin = position;
               //回到敏感词树的根节点
               tempNode = rootNode;
           }else if(tempNode.isKeywordEnd()){
               result.append(replacement);
               position = position + 1;
               begin = position;
               tempNode = rootNode;
           }else{
               ++position;
           }
        }
        //把最后一次的加上：begin到结尾之前的字符串
        result.append(text.substring(begin));
        return result.toString();
    }

    public static  void main(String[] argv){
        SensitiveService s = new SensitiveService();
        s.addWord("色情");
        s.addWord("暴力");
        System.out.println(s.filter("你好X色**情XX"));
        System.out.println();
    }

}
